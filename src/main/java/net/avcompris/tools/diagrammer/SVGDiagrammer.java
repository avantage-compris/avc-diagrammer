package net.avcompris.tools.diagrammer;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.CharEncoding.UTF_8;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

/**
 * Raw methods to generate SVG directly.
 * 
 * @author David Andrianavalontsalama
 */
public abstract class SVGDiagrammer {

	/**
	 * Override this method and add directives to output the SVG.
	 */
	protected abstract void body() throws Exception;

	//private final PrintStream out = System.out;

	/**
	 * The method responsible to output the generated SVG.
	 */
	public final void run(final int width, final int height) {

		println("<svg xmlns='http://www.w3.org/2000/svg'"
				+ " xmlns:xlink='http://www.w3.org/1999/xlink'" + " width='"
				+ width + "' height='" + height + "'>");

		try {

			body();

		} catch (final RuntimeException e) {

			throw e;

		} catch (final Exception e) {

			throw new RuntimeException(e);
		}

		println("</svg>");

		flush();
	}

	protected Shape rect() {

		return new Shape("rect");
	}

	protected Shape path() {

		return new Shape("path");
	}

	protected Shape text(final String label) {

		return new Shape("text") {

			@Override
			public void close() {

				println(">" + label + "</text>");
			}
		};
	}

	@Nullable
	private Shape currentShape = null;

	protected class Shape {

		public Shape(final String elementName) {

			print("<" + elementName);
		}

		public Shape property(final String name, final String value) {

			print(" " + name + "='" + value + "'");

			return this;
		}

		public Shape fontSize(final double value) {

			print(" font-size" + "='" + value + "'");

			return this;
		}

		public Shape fontWeight(final String value) {

			print(" font-weight" + "='" + value + "'");

			return this;
		}

		public Shape fontFamily(final String value) {

			print(" font-family" + "='" + value + "'");

			return this;
		}

		public Shape textAnchor(final String value) {

			print(" text-anchor" + "='" + value + "'");

			return this;
		}

		public Shape opacity(final double opacity) {

			print(" opacity" + "='" + opacity + "'");

			return this;
		}

		public Shape fillOpacity(final double opacity) {

			print(" fill-opacity" + "='" + opacity + "'");

			return this;
		}

		public Shape stroke_opacity(final double opacity) {

			print(" stroke-opacity" + "='" + opacity + "'");

			return this;
		}

		public Path moveTo(final double x, final double y) {

			print(" d='");

			println("M" + x + "," + y);

			return new Path(this);
		}

		public Shape x(final double x) {

			print(" x='" + x + "'");

			return this;
		}

		public Shape y(final double y) {

			print(" y='" + y + "'");

			return this;
		}

		public Shape rx(final double rx) {

			print(" rx='" + rx + "'");

			return this;
		}

		public Shape ry(final double ry) {

			print(" ry='" + ry + "'");

			return this;
		}

		public Shape width(final double width) {

			print(" width='" + width + "'");

			return this;
		}

		public Shape height(final double height) {

			print(" height='" + height + "'");

			return this;
		}

		public Shape stroke(final String stroke) {

			print(" stroke='" + stroke + "'");

			return this;
		}

		public Shape strokeWidth(final double strokeWidth) {

			print(" stroke-width='" + strokeWidth + "'");

			return this;
		}

		public Shape fill(final String fill) {

			print(" fill='" + fill + "'");

			return this;
		}

		public void close() {

			println("/>");

			SVGDiagrammer.this.currentShape = null;
		}
	}

	protected class Path {

		public Path(final Shape parentShape) {

			this.parentShape = parentShape;
		}

		private final Shape parentShape;

		public Path h(final double h) {

			print(" h" + h);

			return this;
		}

		public Path v(final double v) {

			print(" v" + v);

			return this;
		}

		public Path move(final double x, final double y) {

			print(" m" + x + "," + y);

			return this;
		}

		public Path l(final double x, final double y) {

			print(" l" + x + "," + y);

			return this;
		}

		private Shape close() {

			println(" z'");

			return parentShape;
		}

		private Shape leaveOpen() {

			println("'");

			return parentShape;
		}

		private Path cut() {

			println(" z");

			return this;
		}

		public Shape fill(final String fill) {

			close();

			return parentShape.fill(fill);
		}

		public Shape stroke(final String stroke) {

			close();

			return parentShape.stroke(stroke);
		}
	}

	private final List<File> outputFiles = new ArrayList<File>();

	protected final SVGDiagrammer addOutputFile(final File outputFile) throws IOException {

		checkNotNull(outputFile, "outputFile");

		outputFiles.add(outputFile);

		FileUtils.write(outputFile, "", UTF_8);

		return this;
	}

	private void println(final String s) {

		System.out.println(s);

		for (final File outputFile : outputFiles) {

			final boolean APPEND = true;

			try {

				FileUtils.write(outputFile, s + "\n", UTF_8, APPEND);

			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void print(final String s) {

		System.out.print(s);

		for (final File outputFile : outputFiles) {

			final boolean APPEND = true;

			try {

				FileUtils.write(outputFile, s, UTF_8, APPEND);

			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void flush() {

		System.out.flush();
	}
}
