package net.avcompris.tools.diagrammer;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.CharEncoding.UTF_8;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.io.FileUtils;

import com.avcompris.util.XMLUtils;

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

	protected Shape marker() {

		return new Shape("marker");
	}

	protected Shape rect() {

		return new Shape("rect");
	}

	protected Shape ellipse() {

		return new Shape("ellipse");
	}

	protected Shape polygon() {

		return new Shape("polygon");
	}

	protected Shape path() {

		return new Shape("path");
	}

	protected Shape text(final String label) {

		return new Shape("text") {

			@Override
			public void close() {

				println(">" + XMLUtils.xmlEncode(label) + "</text>");

				shapes.pop();
			}
		};
	}

	private final Stack<Shape> shapes = new Stack<Shape>();

	protected class Shape {

		private final String elementName;

		private boolean hasChildren = false;

		public Shape(final String elementName) {

			this.elementName = checkNotNull(elementName, "elementName");

			if (!shapes.isEmpty()) {

				println(">");

				shapes.peek().hasChildren = true;
			}

			print("<" + elementName);

			shapes.push(this);
		}

		public Shape property(final String name, final String value) {

			print(" " + name + "='" + value + "'");

			return this;
		}

		public Shape property(final String name, final int value) {

			return property(name, Integer.toString(value));
		}

		public Shape property(final String name, final double value) {

			return property(name, Double.toString(value));
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

		public Shape points(final String points) {

			print(" points" + "='" + points + "'");

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

		public Shape cx(final double cx) {

			print(" cx='" + cx + "'");

			return this;
		}

		public Shape cy(final double cy) {

			print(" cy='" + cy + "'");

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

			if (hasChildren) {

				println("</" + elementName + ">");

			} else {

				println("/>");
			}

			shapes.pop();
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

		public Path arc(final double xRadius,
				final double yRadius, //
				final double xAxisRotation, final boolean largeArcFlag,
				final boolean sweepFlag, //
				final double endX, final double endY) {

			print(" a" + xRadius + "," + yRadius + "," + xAxisRotation + ","
					+ (largeArcFlag ? 1 : 0) + "," + (sweepFlag ? 1 : 0) + ","
					+ endX + "," + endY);

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

		public Path q(final double qx, final double qy, double x, final double y) {

			print(" q" + qx + "," + qy + " " + x + "," + y);

			return this;
		}

		public Shape close() {

			println(" z'");

			return parentShape;
		}

		public Shape leaveOpen() {

			println("'");

			return parentShape;
		}

		public Path cut() {

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

	private boolean printToSystemOut = true;

	public final void printToSystemOut(final boolean printToSystemOut) {

		this.printToSystemOut = printToSystemOut;
	}

	private final List<File> outputFiles = new ArrayList<File>();

	protected final SVGDiagrammer addOutputFile(final File outputFile) throws IOException {

		checkNotNull(outputFile, "outputFile");

		outputFiles.add(outputFile);

		FileUtils.write(outputFile, "", UTF_8);

		return this;
	}

	private void println(final String s) {

		if (printToSystemOut) {

			System.out.println(s);
		}

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

		if (printToSystemOut) {

			System.out.print(s);
		}

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
