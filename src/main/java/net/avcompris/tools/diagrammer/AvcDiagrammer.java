package net.avcompris.tools.diagrammer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

				final Shape marker = marker().property("id", "arrow")
						.property("markerWidth", 6).property("markerHeight", 6)
						.property("viewBox", "-3 -3 6 6").property("refX", 2)
						.property("refY", 0)
						.property("markerUnits", "strokeWidth")
						.property("orient", "auto");

				polygon().points("-1,0 -3,3 3,0 -3,-3").fill("#000").close();

				marker.close();

				AvcDiagrammer.this.body();
			}
		};

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

	private static final int DEPTH_DX = 20;
	private static final int DEPTH_DY = 10;
	protected static final int BOX_OFFSET = 10;
	protected static final int LINE_HEIGHT = 20;

	protected final Node box(final double x, final double y,
			final double width, final double height, final String fillColor,
			final String... labels) {

		diagrammer().path().moveTo(x, y + height).h(width)
				.l(DEPTH_DX, DEPTH_DY).h(-width).l(-DEPTH_DX, -DEPTH_DY)
				.fill(darkenColor(fillColor)).stroke("#000").close();

		diagrammer().path().moveTo(x + width, y + height).v(-height)
				.l(DEPTH_DX, DEPTH_DY).v(height).l(-DEPTH_DX, -DEPTH_DY)
				.fill(lightenColor(lightenColor(fillColor))).stroke("#000")
				.close();

		diagrammer().rect()
				.fill(lightenColor(lightenColor(lightenColor(fillColor))))
				.stroke("#000").x(x).y(y).width(width).height(height).close();

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

			text.x(x + width / 2).y(y + 14 + i * LINE_HEIGHT).fontSize(14)
					.fill("#000").textAnchor("middle").close();

			++i;
		}

		return new NodeBox(x, y, width, height);
	}

	protected final Node box(final double x, final double y,
			final double width, final String fillColor, final String... labels) {

		return box(x, y, width, labels.length * LINE_HEIGHT + 2, fillColor,
				labels);
	}

	protected final Node database(final double x, final double y,
			final double width, final double height, final String fillColor,
			final String... labels) {

		final double rx = width / 2;
		final double ry = 1.3 * DEPTH_DY / 2;

		diagrammer().path().moveTo(x, y + height).v(-height)
				.arc(rx, ry, 0, true, true, width, 0).v(height)
				.fill(lightenColor(lightenColor(lightenColor(fillColor))))
				.stroke("#000").close();

		diagrammer().ellipse().cx(x + width / 2).cy(y + height).rx(rx).ry(ry)
				.fill(darkenColor(fillColor)).stroke("#000").close();

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

			text.x(x + width / 2 - 1).y(y + 8 + i * LINE_HEIGHT).fontSize(14)
					.fill("#000").textAnchor("middle").close();

			++i;
		}

		return new NodeDatabase(x, y, width, height);
	}

	protected final Node database(final double x, final double y,
			final double width, final String fillColor, final String... labels) {

		return database(x, y, width, labels.length * LINE_HEIGHT + 2,
				fillColor, labels);
	}

	protected final Node queue(final double x, final double y,
			final double width, final double height, final String fillColor,
			final String... labels) {

		final double rx = DEPTH_DX / 2 / 1.3;
		final double ry = height / 2;

		diagrammer().path().moveTo(x + width + rx, y).h(-width)
				.arc(rx, ry, 0, false, false, 0, height).h(width)
				.fill(lightenColor(lightenColor(lightenColor(fillColor))))
				.stroke("#000").close();

		diagrammer().ellipse().cx(x + width + rx).cy(y + height / 2).rx(rx)
				.ry(ry).fill(lightenColor(lightenColor(fillColor)))
				.stroke("#000").close();

		final double wi = 5;
		final double rxi = 5;
		final double ryi = ry * rxi / rx;
		final double hi = height * rxi / rx;
		final double yi = y + height / 2 - 7;

		for (int i = 0; i < 4; ++i) {

			final double xi = x + width / 2 + (i - 1.5) * 12;

			diagrammer().path().moveTo(xi + wi + rxi, yi).h(-wi)
					.arc(rxi, ryi, 0, false, false, 0, hi).h(wi)
					.fill(lightenColor(fillColor)).stroke("#000").close();

			diagrammer().ellipse().cx(xi + wi + rxi).cy(yi + hi / 2).rx(rxi)
					.ry(ryi).fill(darkenColor(fillColor)).stroke("#000")
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

			text.x(x + width / 2 + 10).y(y + 38 + i * LINE_HEIGHT).fontSize(14)
					.fill("#000").textAnchor("middle").close();

			++i;
		}

		return new NodeQueue(x, y, width, height);
	}

	protected final Node queue(final double x, final double y,
			final double width, final String fillColor, final String... labels) {

		return queue(x, y, width, LINE_HEIGHT + 2, fillColor, labels);
	}

	private static String darkenColor(final String color) {

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

	private static String lightenColor(final String color) {

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

		final double stroke_opacity = 0.3;

		diagrammer().path().fillOpacity(0.2).stroke_opacity(stroke_opacity)
				.moveTo(x + BOX_OFFSET, y + BOX_OFFSET)
				.h(width - BOX_OFFSET - BOX_OFFSET).l(DEPTH_DX, DEPTH_DY)
				.h(-width + BOX_OFFSET + BOX_OFFSET).l(-DEPTH_DX, -DEPTH_DY)
				.fill(darkenColor(fillColor)).stroke("#000").close();

		diagrammer().path().fillOpacity(0.2).stroke_opacity(stroke_opacity)
				.moveTo(x + BOX_OFFSET, y + BOX_OFFSET).l(DEPTH_DX, DEPTH_DY)
				.v(height - BOX_OFFSET - BOX_OFFSET).l(-DEPTH_DX, -DEPTH_DY)
				.v(-height + BOX_OFFSET + BOX_OFFSET)
				.fill(lightenColor(lightenColor(fillColor))).stroke("#000")
				.close();

		diagrammer().path().fillOpacity(0.2).stroke_opacity(stroke_opacity)
				.moveTo(x + BOX_OFFSET + DEPTH_DX, y + BOX_OFFSET + DEPTH_DY)
				.h(width - BOX_OFFSET - BOX_OFFSET)
				.v(height - BOX_OFFSET - BOX_OFFSET)
				.h(-width + BOX_OFFSET + BOX_OFFSET)
				.v(-height + BOX_OFFSET + BOX_OFFSET)
				.fill(lightenColor(lightenColor(lightenColor(fillColor))))
				.stroke("#000").close();

		boxStack.push(new Box(x, y, width, height, fillColor));
	}

	protected final void outside_of_box(final String... labels) {

		final Box box = boxStack.pop();

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
	}

	private static int getTextWidth(final String text) {

		return 14 + 7 * text.length();
	}

	protected static abstract class Node {

		public abstract double top();

		public abstract double right();

		public abstract double bottom();

		public abstract double left();

		public abstract double middle_x_top();

		public abstract double middle_x_bottom();

		public abstract double middle_y_left();

		public abstract double middle_y_right();
	}

	private static class NodeBox extends Node {

		public NodeBox(
				final double x,
				final double y,
				final double width,
				final double height) {

			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		private final double x;
		private final double y;
		private final double width;
		private final double height;

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

	private static class NodeDatabase extends Node {

		public NodeDatabase(
				final double x,
				final double y,
				final double width,
				final double height) {

			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		private final double x;
		private final double y;
		private final double width;
		private final double height;

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

			return y + height + 2;
		}

		@Override
		public double left() {

			return x;
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

	private static class NodeQueue extends Node {

		public NodeQueue(
				final double x,
				final double y,
				final double width,
				final double height) {

			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		private final double x;
		private final double y;
		private final double width;
		private final double height;

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

		final double x1;
		final double y1;
		final double x2;
		final double y2;

		if (from.right() < to.left()) {

			x1 = from.right();
			x2 = to.left() - 2.5;

			if (from.top() >= to.top() && from.bottom() <= to.bottom()) {

				y1 = from.middle_y_right();

			} else if (from.top() <= to.top() && from.bottom() >= to.bottom()) {

				y1 = to.middle_y_left();

			} else {

				throw new NotImplementedException();
			}

			y2 = y1;

		} else if (from.left() > to.right()) {

			throw new NotImplementedException();

		} else if (from.top() > to.bottom()) {

			y1 = from.top();
			y2 = to.bottom();

			if (from.left() <= to.left() && from.right() >= from.right()) {

				x1 = to.middle_x_bottom();

			} else if (from.left() >= to.left() && from.right() <= to.right()) {

				x1 = from.middle_x_top();

			} else {

				throw new NotImplementedException();
			}

			x2 = x1;

		} else {

			throw new NotImplementedException();
		}

		final Shape shape = diagrammer().path().stroke("#000").opacity(0.8)
				.strokeWidth(3).fill("none");

		if ("dashed".equals(style)) {
			shape.property("stroke-dasharray", "6,4");
		}

		shape.moveTo(x1, y1).l(x2 - x1, y2 - y1).leaveOpen()
				.property("marker-end", "url(#arrow)").close();
	}

	protected final void arrow(final Node from, final NodeSide fromSide,
			final Node to, final NodeSide toSide) {

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

			qx = x1;

			if ((y2 - y1) > 3 * (x2 - x1)) {

				qy = y1 + 3 * (x2 - x1);

			} else {

				qy = y2;
			}

		} else if (fromSide == NodeSide.TOP && toSide == NodeSide.RIGHT) {

			x1 = from.middle_x_top();
			y1 = from.top();

			x2 = to.right() + 2.5;
			y2 = to.middle_y_right();

			qx = x1;

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

		} else {

			throw new NotImplementedException();
		}

		diagrammer().path().stroke("#000").opacity(0.8).strokeWidth(3)
				.fill("none").moveTo(x1, y1)
				.q(qx - x1, qy - y1, x2 - x1, y2 - y1).leaveOpen()
				.property("marker-end", "url(#arrow)").close();
	}
}
