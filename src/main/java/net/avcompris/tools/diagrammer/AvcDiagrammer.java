package net.avcompris.tools.diagrammer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.annotation.Nullable;

import net.avcompris.tools.diagrammer.SVGDiagrammer.Shape;

import com.avcompris.lang.NotImplementedException;

/**
 * Utility base class for diagrammers. This class draws high-level items
 * such as boxes, but doesn’t
 * give access to SVG primitives such as rects, lines, etc.
 * Use {@link SVGDiagrammer} for that.
 * 
 * @author David Andrianavalontsalama
 */
public abstract class AvcDiagrammer {

	/**
	 * Use this for numeric values (x, y, width…)
	 * relative to the preceding node you declared.
	 */
	protected static final int _ = 20000;

	/**
	 * Use this for String values (fillColor…) identical to the ones of the
	 * preceding node you declared.
	 */
	protected static final String ____ = "SAME AS PREVIOUS";

	/**
	 * Override this method and add directives to output the SVG.
	 */
	protected abstract void body() throws Exception;

	private int borderWidth = 0;

	/**
	 * Set a border. You may call this method prior to call 
	 * {@link run(int, int)}.
	 */
	protected final AvcDiagrammer border(final int borderWidth) {

		this.borderWidth = borderWidth;

		return this;
	}

	private boolean printToSystemOut = true;

	public final AvcDiagrammer printToSystemOut(final boolean printToSystemOut) {

		this.printToSystemOut = printToSystemOut;

		return this;
	}

	private final List<File> outputFiles = new ArrayList<File>();

	public final AvcDiagrammer addOutputFile(final File outputFile) throws IOException {

		checkNotNull(outputFile, "outputFile");

		outputFiles.add(outputFile);

		return this;
	}

	private SVGDiagrammer diagrammer = null;

	private SVGDiagrammer diagrammer() {

		final SVGDiagrammer d = diagrammer;

		if (d == null) {
			throw new IllegalStateException(
					"diagrammer (SVGDiagrammer) has not been initialized.");
		}

		return d;
	}

	private enum ArrowColor {

		BLACK("#000"), YELLOW("#ff0"), RED("#f00"), GRAY("#ccc");

		private ArrowColor(final String color) {

			this.color = color;
		}

		public final String color;
	}

	/**
	 * The method responsible to output the generated SVG.
	 * If you want to add a border, call {@link border(int)} before calling
	 * this methid.
	 */
	public final void run(final int width, final int height) {

		if (diagrammer != null) {
			throw new IllegalStateException(
					"diagrammer (SVGDiagrammer) has already been initialized.");
		}

		diagrammer = new SVGDiagrammer() {

			@Override
			protected void body() throws Exception {

				if (borderWidth != 0) {
					rect().x(0).y(0).width(width).height(height).stroke("#000")
							.fill("none").close();
				}

				AvcDiagrammer.this.body();
			}
		};

		diagrammer.printToSystemOut(printToSystemOut);

		for (final File outputFile : outputFiles) {

			try {

				diagrammer.addOutputFile(outputFile);

			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		diagrammer().run(width, height);

		diagrammer = null;
	}

	private boolean hasFilter1Def = false;

	private void ensureFilter1Def() {

		if (hasFilter1Def) {
			return;
		}

		hasFilter1Def = true;

		diagrammer().println("<defs>");
		diagrammer().println("<filter id='filter-shadow1'>");
		diagrammer().println(
				"<feGaussianBlur in='SourceGraphic' stdDeviation='5'/>");
		diagrammer().println("</filter>");
		diagrammer().println("</defs>");
	}

	private final Set<ArrowColor> arrowDefsAlreadyEmbedded = new HashSet<ArrowColor>();

	private void ensureArrowDef(final ArrowColor color) {

		if (arrowDefsAlreadyEmbedded.contains(color)) {
			return;
		}

		arrowDefsAlreadyEmbedded.add(color);

		final Shape marker = diagrammer().marker()
				.property("id", "arrow_" + color.name())
				.property("markerWidth", 6).property("markerHeight", 6)
				.property("viewBox", "-3 -3 6 6").property("refX", 2)
				.property("refY", 0).property("markerUnits", "strokeWidth")
				.property("orient", "auto");

		diagrammer().polygon().points("-1,0 -3,3 3,0 -3,-3").fill(color.color)
				.close();

		marker.close();
	}

	private static final int DEPTH_DX = 20;
	private static final int DEPTH_DY = 10;
	protected static final int BOX_OFFSET = 10;
	protected static final int LINE_HEIGHT = 20;

	protected final Node hidden(final int x, final int y, final int width,
			final int height) {

		final double x_ = handleUnderscore(previousNode().x, x);
		final double y_ = handleUnderscore(previousNode().y, y);
		final double w_ = handleUnderscore(previousNode().width, width);
		final double h_ = handleUnderscore(previousNode().height, height);

		return new NodeBox(x_, y_, w_, h_, "transparent");
	}

	protected final Node box(final int x, final int y, final int width,
			final int height, final String fillColor, final String... labels) {

		final double x_ = handleUnderscore(previousNode().x, x);
		final double y_ = handleUnderscore(previousNode().y, y);
		final double w_ = handleUnderscore(previousNode().width, width);
		final double h_ = handleUnderscore(previousNode().height, height);
		final String fillColor_ = handleUnderscore(previousNode().fillColor,
				fillColor);

		diagrammer().path().moveTo(x_, y_ + h_).h(w_).l(DEPTH_DX, DEPTH_DY)
				.h(-w_).l(-DEPTH_DX, -DEPTH_DY).fill(darkenColor(fillColor_))
				.stroke("#000").close();

		diagrammer().path().moveTo(x_ + w_, y_ + h_).v(-h_)
				.l(DEPTH_DX, DEPTH_DY).v(h_).l(-DEPTH_DX, -DEPTH_DY)
				.fill(lightenColor(lightenColor(fillColor_))).stroke("#000")
				.close();

		diagrammer().rect()
				.fill(lightenColor(lightenColor(lightenColor(fillColor_))))
				.stroke("#000").x(x_).y(y_).width(w_).height(h_).close();

		int i = 0;

		for (final String label : labels) {

			final Shape text;

			if (label.startsWith("*") && label.endsWith("*")
					&& label.length() >= 2) {

				text = diagrammer()
						.text(label.substring(1, label.length() - 1))
						.fontWeight("bold");

			} else {

				text = diagrammer().text(label);
			}

			text.x(x_ + w_ / 2).y(y_ + 14 + i * LINE_HEIGHT).fontSize(14)
					.fill("#000").textAnchor("middle").close();

			++i;
		}

		return new NodeBox(x_, y_, w_, h_, fillColor_);
	}

	protected final void obsolete(final Node node) {

		final int S = 6;
		final int L = 14;

		diagrammer().path().moveTo(node.x + node.width / 2 - L - S, node.y)
				.l(S, -S).l(L, L).l(L, -L).l(S, S).l(-L, L).l(L, L).l(-S, S)
				.l(-L, -L).l(-L, L).l(-S, -S).l(L, -L).l(-L, -L).stroke("none")
				.fill("#f00").opacity(0.6).close();
	}

	protected final Node box(final int x, final int y, final int width,
			final String fillColor, final String... labels) {

		return box(x, y, width, labels.length * LINE_HEIGHT + 2, fillColor,
				labels);
	}

	protected final Node database(final int x, final int y, final int width,
			final int height, final String fillColor, final String... labels) {

		final double x_ = handleUnderscore(previousNode().x, x);
		final double y_ = handleUnderscore(previousNode().y, y);
		final double w_ = handleUnderscore(previousNode().width, width);
		final double h_ = handleUnderscore(previousNode().height, height);
		final String fillColor_ = handleUnderscore(previousNode().fillColor,
				fillColor);

		final double rx = w_ / 2;
		final double ry = 1.3 * DEPTH_DY / 2;

		diagrammer().path().moveTo(x_, y_ + h_).v(-h_)
				.arc(rx, ry, 0, true, true, w_, 0).v(h_)
				.fill(lightenColor(lightenColor(lightenColor(fillColor_))))
				.stroke("#000").close();

		diagrammer().ellipse().cx(x_ + w_ / 2).cy(y_ + h_).rx(rx).ry(ry)
				.fill(darkenColor(fillColor_)).stroke("#000").close();

		int i = 0;

		for (final String label : labels) {

			final Shape text;

			if (label.startsWith("*") && label.endsWith("*")
					&& label.length() >= 2) {

				text = diagrammer()
						.text(label.substring(1, label.length() - 1))
						.fontWeight("bold");

			} else {

				text = diagrammer().text(label);
			}

			text.x(x_ + w_ / 2 - 1).y(y_ + 8 + i * LINE_HEIGHT).fontSize(14)
					.fill("#000").textAnchor("middle").close();

			++i;
		}

		return new NodeDatabase(x_, y_, w_, h_, fillColor_);
	}

	protected final Node database(final int x, final int y, final int width,
			final String fillColor, final String... labels) {

		return database(x, y, width, labels.length * LINE_HEIGHT + 2,
				fillColor, labels);
	}

	protected final Node queue(final int x, final int y, final int width,
			final int height, final String fillColor, final String... labels) {

		final double x_ = handleUnderscore(previousNode().x, x);
		final double y_ = handleUnderscore(previousNode().y, y);
		final double w_ = handleUnderscore(previousNode().width, width);
		final double h_ = handleUnderscore(previousNode().height, height);
		final String fillColor_ = handleUnderscore(previousNode().fillColor,
				fillColor);

		final double rx = DEPTH_DX / 2 / 1.3;
		final double ry = height / 2;

		diagrammer().path().moveTo(x_ + w_ + rx, y_).h(-w_)
				.arc(rx, ry, 0, false, false, 0, h_).h(w_)
				.fill(lightenColor(lightenColor(lightenColor(fillColor_))))
				.stroke("#000").close();

		diagrammer().ellipse().cx(x_ + w_ + rx).cy(y_ + h_ / 2).rx(rx).ry(ry)
				.fill(lightenColor(lightenColor(fillColor_))).stroke("#000")
				.close();

		final double wi = 5;
		final double rxi = 5;
		final double ryi = ry * rxi / rx;
		final double hi = height * rxi / rx;
		final double yi = y_ + height / 2 - 7;

		for (int i = 0; i < 4; ++i) {

			final double xi = x_ + w_ / 2 + (i - 1.5) * 12;

			diagrammer().path().moveTo(xi + wi + rxi, yi).h(-wi)
					.arc(rxi, ryi, 0, false, false, 0, hi).h(wi)
					.fill(lightenColor(fillColor_)).stroke("#000").close();

			diagrammer().ellipse().cx(xi + wi + rxi).cy(yi + hi / 2).rx(rxi)
					.ry(ryi).fill(darkenColor(fillColor_)).stroke("#000")
					.close();
		}

		int i = 0;

		for (final String label : labels) {

			final Shape text;

			if (label.startsWith("*") && label.endsWith("*")
					&& label.length() >= 2) {

				text = diagrammer()
						.text(label.substring(1, label.length() - 1))
						.fontWeight("bold");

			} else {

				text = diagrammer().text(label);
			}

			text.x(x_ + w_ / 2 + 10).y(y_ + 38 + i * LINE_HEIGHT).fontSize(14)
					.fill("#000").textAnchor("middle").close();

			++i;
		}

		return new NodeQueue(x_, y_, w_, h_, fillColor_);
	}

	protected final Node queue(final int x, final int y, final int width,
			final String fillColor, final String... labels) {

		return queue(x, y, width, LINE_HEIGHT + 2, fillColor, labels);
	}

	public static String darkenColor(final String color) {

		if (!color.startsWith("#")) {
			return color;
		}

		final StringBuilder sb = new StringBuilder("#");

		for (final char c : color.substring(1).toCharArray()) {

			final char c2;

			if (c > '0' && c <= '9') {
				c2 = (char) ((int) c - 1);
			} else if (c == 'a') {
				c2 = '9';
			} else if (c > 'a' && c <= 'f') {
				c2 = (char) ((int) c - 1);
			} else {
				c2 = c;
			}

			sb.append(c2);
		}

		return sb.toString();
	}

	public static String lightenColor(final String color) {

		if (!color.startsWith("#")) {
			return color;
		}

		final StringBuilder sb = new StringBuilder("#");

		for (final char c : color.substring(1).toCharArray()) {

			final char c2;

			if (c >= '0' && c < '9') {
				c2 = (char) ((int) c + 1);
			} else if (c == '9') {
				c2 = 'a';
			} else if (c >= 'a' && c < 'f') {
				c2 = (char) ((int) c + 1);
			} else {
				c2 = c;
			}

			sb.append(c2);
		}

		return sb.toString();
	}

	private final Stack<Box> boxStack = new Stack<Box>();

	private static class Box {

		public final double x;
		public final double y;
		public final double width;
		public final double height;
		public String fillColor;

		public Box(
				final double x,
				final double y,
				final double width,
				final double height,
				final String fillColor) {

			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.fillColor = fillColor;
		}
	}

	protected final void inside_of_box(final int x, final int y,
			final int width, final int height, final String fillColor) {

		final double x_ = handleUnderscore(previousNode().x, x);
		final double y_ = handleUnderscore(previousNode().y, y);
		final double w_ = handleUnderscore(previousNode().width, width);
		final double h_ = handleUnderscore(previousNode().height, height);
		final String fillColor_ = handleUnderscore(previousNode().fillColor,
				fillColor);

		final double stroke_opacity = 0.3;

		diagrammer().path().fillOpacity(0.2).stroke_opacity(stroke_opacity)
				.moveTo(x_ + BOX_OFFSET, y_ + BOX_OFFSET)
				.h(w_ - BOX_OFFSET - BOX_OFFSET).l(DEPTH_DX, DEPTH_DY)
				.h(-w_ + BOX_OFFSET + BOX_OFFSET).l(-DEPTH_DX, -DEPTH_DY)
				.fill(darkenColor(fillColor_)).stroke("#000").close();

		diagrammer().path().fillOpacity(0.2).stroke_opacity(stroke_opacity)
				.moveTo(x_ + BOX_OFFSET, y_ + BOX_OFFSET).l(DEPTH_DX, DEPTH_DY)
				.v(h_ - BOX_OFFSET - BOX_OFFSET).l(-DEPTH_DX, -DEPTH_DY)
				.v(-h_ + BOX_OFFSET + BOX_OFFSET)
				.fill(lightenColor(lightenColor(fillColor_))).stroke("#000")
				.close();

		diagrammer().path().fillOpacity(0.2).stroke_opacity(stroke_opacity)
				.moveTo(x_ + BOX_OFFSET + DEPTH_DX, y_ + BOX_OFFSET + DEPTH_DY)
				.h(w_ - BOX_OFFSET - BOX_OFFSET)
				.v(h_ - BOX_OFFSET - BOX_OFFSET)
				.h(-w_ + BOX_OFFSET + BOX_OFFSET)
				.v(-h_ + BOX_OFFSET + BOX_OFFSET)
				.fill(lightenColor(lightenColor(lightenColor(fillColor_))))
				.stroke("#000").close();

		final Box box = new Box(x_, y_, w_, h_, fillColor_);

		boxStack.push(box);

		currentNode = new NodeBox(box);
	}

	protected final Node outside_of_box(final String... labels) {

		final Box box = boxStack.pop();

		currentNode = new NodeBox(box);

		int labelWidth = 0;

		for (final String label : labels) {

			final int textWidth = getTextWidth(label);

			if (textWidth > labelWidth) {

				labelWidth = textWidth;
			}
		}

		final int metaHeight = LINE_HEIGHT * labels.length;

		diagrammer().path().moveTo(box.x, box.y + box.height + metaHeight)
				.h(labelWidth).l(DEPTH_DX, DEPTH_DY).h(-labelWidth)
				.l(-DEPTH_DX, -DEPTH_DY).fill(darkenColor(box.fillColor))
				.stroke("#000").close();

		diagrammer().path()
				.moveTo(box.x + labelWidth, box.y + box.height + metaHeight)
				.v(-metaHeight).l(DEPTH_DX, DEPTH_DY).v(metaHeight)
				.l(-DEPTH_DX, -DEPTH_DY)
				.fill(lightenColor(lightenColor(box.fillColor))).stroke("#000")
				.close();

		diagrammer().path().moveTo(box.x + labelWidth, box.y + box.height)
				.h(box.width - labelWidth).l(DEPTH_DX, DEPTH_DY)
				.h(-box.width + labelWidth).l(-DEPTH_DX, -DEPTH_DY)
				.fill(darkenColor(box.fillColor)).stroke("#000").close();

		diagrammer().path().moveTo(box.x + box.width, box.y + box.height)
				.v(-box.height).l(DEPTH_DX, DEPTH_DY).v(box.height)
				.l(-DEPTH_DX, -DEPTH_DY)
				.fill(lightenColor(lightenColor(box.fillColor))).stroke("#000")
				.close();

		diagrammer().path().property("fill-rule", "evenodd")
				.moveTo(box.x, box.y).h(box.width).v(box.height)
				.h(-box.width + labelWidth).v(metaHeight).h(-labelWidth)
				.v(-box.height - metaHeight).cut()

				.move(BOX_OFFSET, BOX_OFFSET)
				.v(box.height - BOX_OFFSET - BOX_OFFSET)
				.h(box.width - BOX_OFFSET - BOX_OFFSET)
				.v(-box.height + BOX_OFFSET + BOX_OFFSET)
				.h(-box.width + BOX_OFFSET + BOX_OFFSET).close()
				.fill(lightenColor(lightenColor(lightenColor(box.fillColor))))
				.stroke("#000").close();

		int i = 0;

		for (final String label : labels) {

			++i;

			diagrammer().text(label).x(box.x + labelWidth / 2)
					.y(box.y + box.height + i * LINE_HEIGHT - 12).fontSize(14)
					.fill("#000").textAnchor("middle").close();
		}

		return new NodeBox(box.x, box.y, box.width, box.height, box.fillColor);
	}

	private static int getTextWidth(final String text) {

		return 14 + 7 * text.length();
	}

	@Nullable
	private Node currentNode = null;

	private Node previousNode() {

		if (currentNode != null) {

			return currentNode;
		}

		return NO_PREVIOUS_NODE;
	}

	private final Node NO_PREVIOUS_NODE = new Node(_, _, _, _, ____) {

		@Override
		public double top() {

			throw new IllegalStateException();
		}

		@Override
		public double right() {

			throw new IllegalStateException();
		}

		@Override
		public double bottom() {

			throw new IllegalStateException();
		}

		@Override
		public double left() {

			throw new IllegalStateException();
		}

		@Override
		public int total_height() {

			throw new IllegalStateException();
		}

		@Override
		public double middle_x_top() {

			throw new IllegalStateException();
		}

		@Override
		public double middle_x_bottom() {

			throw new IllegalStateException();
		}

		@Override
		public double middle_y_left() {

			throw new IllegalStateException();
		}

		@Override
		public double middle_y_right() {

			throw new IllegalStateException();
		}
	};

	private static double handleUnderscore(final double previousValue,
			final double value) {

		if (value < _ / 2) {
			return value;
		}

		if (previousValue > _ / 2) {
			throw new IllegalArgumentException("Cannot compute relative value "
					+ value + ": previousValue " + previousValue
					+ " is invalid.");
		}

		return previousValue + (value - _);
	}

	private static String handleUnderscore(final String previousValue,
			final String value) {

		if (!____.equals(value)) {
			return value;
		}

		if (____.equalsIgnoreCase(previousValue)) {
			throw new IllegalArgumentException("Cannot compute relative value "
					+ value + ": previousValue " + previousValue
					+ " is invalid.");
		}

		return previousValue;
	}

	protected abstract class Node {

		public Node(
				final double x,
				final double y,
				final double width,
				final double height,
				final String fillColor) {

			if ((int) x == _ && (int) y == _ //
					&& (int) width == _ && (int) height == _ //
					&& ____.equals(fillColor)) { // NO_PREVIOUS_NODE
				// OK
			} else if (x > _ / 2) {
				throw new IllegalArgumentException("x: " + x);
			} else if (y > _ / 2) {
				throw new IllegalArgumentException("y: " + y);
			} else if (width > _ / 2) {
				throw new IllegalArgumentException("width: " + width);
			} else if (height > _ / 2) {
				throw new IllegalArgumentException("height: " + height);
			} else if (____.equals(fillColor)) {
				throw new IllegalArgumentException("fillColor: " + fillColor);
			}

			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.fillColor = fillColor;

			AvcDiagrammer.this.currentNode = this;
		}

		protected final double x;
		protected final double y;
		protected final double width;
		protected final double height;
		protected final String fillColor;

		public final int x() {

			return (int) x;
		}

		public final int y() {

			return (int) y;
		}

		public final int width() {

			return (int) width;
		}

		public final int height() {

			return (int) height;
		}

		public final String fillColor() {

			return fillColor;
		}

		public abstract double top();

		public abstract double right();

		public abstract double bottom();

		public abstract double left();

		public abstract int total_height();

		public abstract double middle_x_top();

		public abstract double middle_x_bottom();

		public abstract double middle_y_left();

		public abstract double middle_y_right();
	}

	private class NodePlain extends Node {

		public NodePlain(
				final double x,
				final double y,
				final double width,
				final double height,
				final String fillColor) {

			super(x, y, width, height, fillColor);
		}

		@Override
		public double top() {

			return y;
		}

		@Override
		public double right() {

			return x + width;
		}

		@Override
		public double bottom() {

			return y + height;
		}

		@Override
		public double left() {

			return x;
		}

		@Override
		public int total_height() {

			return (int) height;
		}

		@Override
		public double middle_x_top() {

			return x + width / 2;
		}

		@Override
		public double middle_x_bottom() {

			return x + width / 2;
		}

		@Override
		public double middle_y_left() {

			return y + height / 2;
		}

		@Override
		public double middle_y_right() {

			return y + height / 2;
		}
	}

	private class NodeDirectory extends Node {

		public NodeDirectory(
				final double x,
				final double y,
				final String fillColor) {

			super(x - 8, y, 30, 30, fillColor);
		}

		@Override
		public double top() {

			return y - 1;
		}

		@Override
		public double right() {

			return x + width;
		}

		@Override
		public double bottom() {

			return y + height;
		}

		@Override
		public double left() {

			return x;
		}

		@Override
		public int total_height() {

			return (int) height;
		}

		@Override
		public double middle_x_top() {

			return x + 6;
		}

		@Override
		public double middle_x_bottom() {

			return x + width / 2;
		}

		@Override
		public double middle_y_left() {

			return y + height / 2;
		}

		@Override
		public double middle_y_right() {

			return y + height / 2;
		}
	}

	private class NodeBox extends Node {

		public NodeBox(
				final double x,
				final double y,
				final double width,
				final double height,
				final String fillColor) {

			super(x, y, width, height, fillColor);
		}

		public NodeBox(final Box box) {

			this(box.x, box.y, box.width, box.height, box.fillColor);
		}

		@Override
		public double top() {

			return y;
		}

		@Override
		public double right() {

			return x + width + DEPTH_DX / 2;
		}

		@Override
		public double bottom() {

			return y + height + DEPTH_DY / 2;
		}

		@Override
		public double left() {

			return x;
		}

		@Override
		public int total_height() {

			return (int) height + DEPTH_DY;
		}

		@Override
		public double middle_x_top() {

			return x + width / 2;
		}

		@Override
		public double middle_x_bottom() {

			return x + width / 2 + DEPTH_DX / 2;
		}

		@Override
		public double middle_y_left() {

			return y + height / 2 + DEPTH_DY / 2;
		}

		@Override
		public double middle_y_right() {

			return y + height / 2 + DEPTH_DY / 2;
		}
	}

	private class NodeDatabase extends Node {

		public NodeDatabase(
				final double x,
				final double y,
				final double width,
				final double height,
				final String fillColor) {

			super(x, y, width, height, fillColor);
		}

		@Override
		public double top() {

			return y;
		}

		@Override
		public double right() {

			return x + width;
		}

		@Override
		public double bottom() {

			return y + height + 2;
		}

		@Override
		public double left() {

			return x;
		}

		@Override
		public int total_height() {

			return (int) height + DEPTH_DY / 2;
		}

		@Override
		public double middle_x_top() {

			return x + width / 2;
		}

		@Override
		public double middle_x_bottom() {

			return x + width / 2;
		}

		@Override
		public double middle_y_left() {

			return y + height / 2;
		}

		@Override
		public double middle_y_right() {

			return y + height / 2;
		}
	}

	private class NodeQueue extends Node {

		public NodeQueue(
				final double x,
				final double y,
				final double width,
				final double height,
				final String fillColor) {

			super(x, y, width, height, fillColor);
		}

		@Override
		public double top() {

			return y;
		}

		@Override
		public double right() {

			return x + width + DEPTH_DX / 2 / 1.3;
		}

		@Override
		public double bottom() {

			return y + height;
		}

		@Override
		public double left() {

			return x;
		}

		@Override
		public int total_height() {

			return (int) height;
		}

		@Override
		public double middle_x_top() {

			return x + width / 2;
		}

		@Override
		public double middle_x_bottom() {

			return x + width / 2;
		}

		@Override
		public double middle_y_left() {

			return y + height / 2;
		}

		@Override
		public double middle_y_right() {

			return y + height / 2;
		}
	}

	public enum NodeSide {

		TOP, RIGHT, BOTTOM, LEFT;
	}

	protected final void arrow(final Node from, final Node to) {

		arrow(from, to, "solid");
	}

	protected final void arrow(final Node from, final Node to,
			final String style) {

		arrow(ArrowColor.BLACK, from, to, style);
	}

	protected final void arrow_yellow(final Node from, final Node to) {

		arrow_yellow(from, to, "solid");
	}

	protected final void arrow_yellow(final Node from, final Node to,
			final String style) {

		arrow(ArrowColor.YELLOW, from, to, style);
	}

	protected final void arrow_red(final Node from, final Node to) {

		arrow_red(from, to, "solid");
	}

	protected final void arrow_red(final Node from, final Node to,
			final String style) {

		arrow(ArrowColor.RED, from, to, style);
	}

	protected final void arrow_gray(final Node from, final Node to) {

		arrow_gray(from, to, "solid");
	}

	protected final void arrow_gray(final Node from, final Node to,
			final String style) {

		arrow(ArrowColor.GRAY, from, to, style);
	}

	private final void arrow(final ArrowColor color, final Node from,
			final Node to, final String style) {

		ensureArrowDef(color);

		final double x1;
		final double y1;
		final double x2;
		final double y2;

		if (from.right() < to.left()) {

			x1 = from.right();
			x2 = to.left() - 3;

			if (from.top() >= to.top() && from.bottom() <= to.bottom()) {

				y1 = from.middle_y_right();

			} else if (from.top() <= to.top() && from.bottom() >= to.bottom()) {

				y1 = to.middle_y_left();

			} else {

				System.err.println("from.right: " + from.right() //
						+ " < to.left: " + to.left());
				System.err.println("from.top: " + from.top() //
						+ ", to.top: " + to.top());
				System.err.println("from.bottom: " + from.bottom() //
						+ ", to.bottom: " + to.bottom());
				throw new NotImplementedException();
			}

			y2 = y1;

		} else if (from.left() > to.right()) {

			x1 = from.left();
			x2 = to.right() + 2.5;

			if (from.top() >= to.top() && from.bottom() <= to.bottom()) {

				y1 = from.middle_y_left();

			} else if (from.top() <= to.top() && from.bottom() >= to.bottom()) {

				y1 = to.middle_y_right();

			} else {

				System.err.println("from.left: " + from.left() //
						+ " < to.right: " + to.right());
				System.err.println("from.top: " + from.top() //
						+ ", to.top: " + to.top());
				System.err.println("from.bottom: " + from.bottom() //
						+ ", to.bottom: " + to.bottom());
				throw new NotImplementedException();
			}

			y2 = y1;

		} else if (from.top() > to.bottom()) {

			y1 = from.top() - 0.5;
			y2 = to.bottom();

			if (from.left() <= to.left() && from.right() >= to.right()) {

				x1 = to.middle_x_bottom();

			} else if (from.left() >= to.left() && from.right() <= to.right()) {

				x1 = from.middle_x_top();

			} else if (from.middle_x_top() >= to.left()
					&& from.middle_x_top() <= to.right()) {

				x1 = from.middle_x_top();

			} else {

				System.err.println("from.top: " + from.top() //
						+ " > to.bottom: " + to.bottom());
				System.err.println("from.left: " + from.left() //
						+ ", to.left: " + to.left());
				System.err.println("from.right: " + from.right() //
						+ ", to.right: " + to.right());
				throw new NotImplementedException();
			}

			x2 = x1;

		} else if (from.bottom() < to.top()) {

			y1 = from.bottom() - 0.5;
			y2 = to.top() - 2.5;

			if (from.left() <= to.left() && from.right() >= to.right()) {

				x1 = to.middle_x_top();

			} else if (from.left() >= to.left() && from.right() <= to.right()) {

				x1 = from.middle_x_bottom();

			} else if (from.middle_x_bottom() >= to.left()
					&& from.middle_x_bottom() <= to.right()) {

				x1 = from.middle_x_bottom();

			} else {

				System.err.println("from.top: " + from.top() //
						+ " > to.bottom: " + to.bottom());
				System.err.println("from.left: " + from.left() //
						+ ", to.left: " + to.left());
				System.err.println("from.right: " + from.right() //
						+ ", to.right: " + to.right());
				throw new NotImplementedException();
			}

			x2 = x1;

		} else {

			throw new NotImplementedException();
		}

		final Shape shape = diagrammer().path().stroke(color.color)
				.opacity(0.8).strokeWidth(3).fill("none");

		if ("dashed".equals(style)) {
			shape.property("stroke-dasharray", "6,4");
		}

		shape.moveTo(x1, y1).l(x2 - x1, y2 - y1).leaveOpen()
				.property("marker-end", "url(#arrow_" + color.name() + ")")
				.close();
	}

	protected final void arrow(final Node from, final NodeSide fromSide,
			final Node to, final NodeSide toSide) {

		arrow(ArrowColor.BLACK, from, fromSide, to, toSide);
	}

	protected final void arrow_yellow(final Node from, final NodeSide fromSide,
			final Node to, final NodeSide toSide) {

		arrow(ArrowColor.YELLOW, from, fromSide, to, toSide);
	}

	protected final void arrow_gray(final Node from, final NodeSide fromSide,
			final Node to, final NodeSide toSide) {

		arrow(ArrowColor.GRAY, from, fromSide, to, toSide);
	}

	private final void arrow(final ArrowColor color, final Node from,
			final NodeSide fromSide, final Node to, final NodeSide toSide) {

		ensureArrowDef(color);

		final double x1;
		final double y1;
		final double qx;
		final double qy;
		final double x2;
		final double y2;

		if (fromSide == NodeSide.BOTTOM && toSide == NodeSide.LEFT) {

			x1 = from.middle_x_bottom();
			y1 = from.bottom();

			x2 = to.left() - 2.5;
			y2 = to.middle_y_left();

			// if ((x2 - x1) > 3 * (y2 - y1)) {

			// qx = x2 - 3 * (y2 - y1);

			// } else {

			qx = x1;

			// }

			if ((y2 - y1) > 3 * (x2 - x1)) {

				qy = y1 + 3 * (x2 - x1);

			} else {

				qy = y2;
			}

		} else if (fromSide == NodeSide.TOP && toSide == NodeSide.RIGHT) {

			x1 = from.middle_x_top();
			y1 = from.top() - 0.5;

			x2 = to.right() + 2.5;
			y2 = to.middle_y_right();

			// if ((x1 - x2) > 3 * (y1 - y2)) {

			// qx = x2 + 3 * (y1 - y2);

			// } else {

			qx = x1;

			// }

			if ((y1 - y2) > 3 * (x1 - x2)) {

				qy = y1 - 3 * (x1 - x2);

			} else {

				qy = y2;
			}

		} else if (fromSide == NodeSide.BOTTOM && toSide == NodeSide.RIGHT) {

			x1 = from.middle_x_bottom();
			y1 = from.bottom();

			x2 = to.right() + 2.5;
			y2 = to.middle_y_right();

			qx = x1;

			if ((y2 - y1) > 3 * (x1 - x2)) {

				qy = y1 + 3 * (x1 - x2);

			} else {

				qy = y2;
			}

		} else if (fromSide == NodeSide.TOP && toSide == NodeSide.LEFT) {

			x1 = from.middle_x_top();
			y1 = from.top() - 0.5;

			x2 = to.left() - 2.5;
			y2 = to.middle_y_left();

			// if ((x1 - x2) > 3 * (y1 - y2)) {

			// qx = x2 + 3 * (y1 - y2);

			// } else {

			qx = x1;

			// }

			qy = y2;

		} else if (fromSide == NodeSide.LEFT && toSide == NodeSide.BOTTOM) {

			x1 = from.left();
			y1 = from.middle_y_left();

			x2 = to.middle_x_bottom();
			y2 = to.bottom();

			if ((x1 - x2) > 3 * (y1 - y2)) {

				qx = x1 - 3 * (y1 - y2);

			} else {

				qx = x2;

			}

			qy = y1;

		} else {

			throw new NotImplementedException();
		}

		diagrammer().path().stroke(color.color).opacity(0.8).strokeWidth(3)
				.fill("none").moveTo(x1, y1)
				.q(qx - x1, qy - y1, x2 - x1, y2 - y1).leaveOpen()
				.property("marker-end", "url(#arrow_" + color.name() + ")")
				.close();
	}

	protected final Node[] installed_packages(final String fillColor,
			final String... packageNames) {

		return installed_packages(130, fillColor, packageNames);
	}

	protected final Node[] installed_packages(final int width,
			final String fillColor, final String... packageNames) {

		final Box currentBox = boxStack.peek();

		final Node[] nodes = new Node[packageNames.length];

		for (int i = 0; i < packageNames.length; ++i) {

			final int dy = i * LINE_HEIGHT;

			final Node node = box((int) currentBox.x + BOX_OFFSET,
					(int) currentBox.y + BOX_OFFSET + dy, width, LINE_HEIGHT,
					fillColor, packageNames[i]);

			nodes[i] = node;
		}

		return nodes;
	}

	private final List<Box> dockerImages = new ArrayList<Box>();

	protected final Node docker_image(final int x, final int y,
			final int width, final int height, final String label) {

		final double x_ = handleUnderscore(previousNode().x, x);
		final double y_ = handleUnderscore(previousNode().y, y);
		final double w_ = handleUnderscore(previousNode().width, width);
		final double h_ = handleUnderscore(previousNode().height, height);

		final String fillColor = "#668";

		dockerImages.add(new Box(x_, y_, w_, h_, fillColor));

		ensureFilter1Def();

		diagrammer().rect().x(x_ + DEPTH_DX).y(y_ + DEPTH_DY).rx(20).ry(20)
				.width(w_).height(h_).stroke("none").strokeWidth(5)
				.fill("#668").opacity(0.3)
				.property("filter", "url(#filter-shadow1)").close();

		diagrammer().rect().x(x_).y(y_).rx(20).ry(20).width(w_).height(h_)
				.stroke("#f60").strokeWidth(5).fill("#ccd").close();

		diagrammer().text(label).x(x_ + w_ / 2).y(y_ + h_ - 12).fontSize(14)
				.fill("#000").textAnchor("middle").close();

		return new NodePlain(x_ - 2, y_ - 2, w_ + 4, h_ + 4, fillColor);
	}

	protected final void docker_packages(final String... packageNames) {

		final Box box = dockerImages.get(dockerImages.size() - 1);

		final int width = (int) box.width - 40;

		int i = 0;

		for (final String packageName : packageNames) {

			++i;

			diagrammer().rect().x(box.x + BOX_OFFSET + 10)
					.y(box.y + BOX_OFFSET + i * LINE_HEIGHT - 20).width(width)
					.height(LINE_HEIGHT - 2).stroke("none").fill("#999")
					.close();

			diagrammer().text(packageName)
					.x(box.x + BOX_OFFSET + width / 2 + 10)
					.y(box.y + BOX_OFFSET + i * LINE_HEIGHT - 7).fontSize(14)
					.fill("#000").textAnchor("middle").close();
		}
	}

	private final Set<String> directoryColors = new HashSet<String>();

	private void ensureDirectory(final String fillColor) {

		if (directoryColors.contains(fillColor)) {
			return;
		}

		directoryColors.add(fillColor);

		diagrammer().println("<defs>");
		diagrammer().println("<g id='directory'>");
		diagrammer().println(
				"<path d='m38,44 v-20 h12 l2,4 h14 v16 h-28'"
						+ " opacity='0.3' fill='" + fillColor
						+ "' filter='url(#filter-shadow1)'/>");
		diagrammer().println(
				"<path d='m30,40 v-20 h12 l2,4 h14 v16 h-28'"
						+ " stroke='#000' stroke-width='1' fill='#fc9'/>");
		diagrammer().println("</g>");
		diagrammer().println("</defs>");
	}

	protected final Node directory(final int x, final int y,
			final String fillColor, final String path) {

		return directory("#directory", x, y, fillColor, path);
	}

	private final Node directory(final String id, final int x, final int y,
			final String fillColor, final String path) {

		final double x_ = handleUnderscore(previousNode().x, x);
		final double y_ = handleUnderscore(previousNode().y, y);

		ensureDirectory(fillColor);

		diagrammer().text(path).x(x_).y(y_ + 36).fontSize(10)
				.fontFamily("monospace").fill("#000").textAnchor("middle")
				.close();

		// diagrammer().text(path).x(x_ + 8).y(y_ + 40).fontSize(10)
		// 		.fontFamily("monospace").fill("#000").textAnchor("middle")
		// 		.property("filter", "url(#filter-shadow1)").close();

		diagrammer().println(
				"<use xlink:href='" + id + "' x='" + (x_ - 38) + "' y='"
						+ (y_ - 20) + "'/>");

		return new NodeDirectory(x_, y_, fillColor);
	}
}
