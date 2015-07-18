package net.avcompris.tools.diagrammer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.avcompris.tools.diagrammer.SVGDiagrammer.Shape;

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
	//protected static final int BOX_OFFSET = 10;
	protected static final int LINE_HEIGHT = 20;

	protected final void box(final double x, final double y,
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
	}

	protected final void box(final double x, final double y,
			final double width, final String fillColor, final String... labels) {

		box(x, y, width, labels.length * LINE_HEIGHT + 2, fillColor, labels);
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
}
