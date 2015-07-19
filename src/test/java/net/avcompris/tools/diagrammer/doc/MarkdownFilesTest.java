package net.avcompris.tools.diagrammer.doc;

import static org.apache.commons.lang3.CharEncoding.UTF_8;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.avcompris.tools.diagrammer.sample.MyFirstDiagram;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MarkdownFilesTest {

	@Parameters(name = "{0}")
	public static Collection<Object[]> parameters() throws Exception {

		final List<Object[]> parameters = new ArrayList<Object[]>();

		for (final File file : new File(".").listFiles()) {

			if (!file.isFile()) {
				continue;
			}

			final String filename = file.getName();

			if (!filename.endsWith(".md")) {
				continue;
			}

			parameters.add(new Object[]{
					filename, file
			});
		}

		return parameters;
	}

	public MarkdownFilesTest(
			final String markdownFilename,
			final File markdownFile) {

		this.markdownFile = markdownFile;
	}

	private final File markdownFile;

	@Test
	public void allEmbeddedSvgAreCorrect() throws Exception {

		for (final String line : FileUtils.readLines(markdownFile, UTF_8)) {

			if (!line.trim().startsWith("![")) {
				continue;
			}

			if (!line.contains(".svg:")) {
				continue;
			}

			if (!line.contains("(data:image/svg+xml")) {
				continue;
			}

			// e.g. "MyFirstDiagram"
			final String namePrefix = substringBetween(line, "![", ".svg");

			final String data = substringBetween(line,
					"](data:image/svg+xml;base64,", ")");

			// e.g. "net.avcompris.tools.diagrammer.sample.MyFirstDiagram"
			final String diagrammerClassName = MyFirstDiagram.class
					.getPackage().getName() + "." + namePrefix;

			Class.forName(diagrammerClassName)
					.getMethod("main", String[].class)
					.invoke(null, new Object[]{
						new String[0]
					});

			final String refData = Base64
					.encodeBase64String(FileUtils.readFileToByteArray(new File(
							"target", namePrefix + ".svg")));

			assertEquals(namePrefix + ".svg data", refData, data);
		}
	}
}
