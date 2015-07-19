package net.avcompris.tools.diagrammer;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.avcompris.tools.diagrammer.sample.MyFirstDiagram;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AllSamplesTest {

	@Parameters(name = "{0}")
	public static Collection<Object[]> parameters() throws Exception {

		final List<Object[]> parameters = new ArrayList<Object[]>();

		final String packageName = MyFirstDiagram.class.getPackage().getName();

		for (final File file : new File("src/test/java/"
				+ packageName.replace('.', File.separatorChar)).listFiles()) {

			if (!file.isFile()) {
				continue;
			}

			final String filename = file.getName();

			if (!filename.endsWith(".java")) {
				continue;
			}

			final String sampleDiagrammerClassName = packageName + "."
					+ substringBeforeLast(filename, ".java");

			@SuppressWarnings("unchecked")
			final Class<? extends AvcDiagrammer> sampleDiagrammerClass = //
			(Class<? extends AvcDiagrammer>) Class
					.forName(sampleDiagrammerClassName);

			parameters.add(new Object[]{
					filename, sampleDiagrammerClass
			});
		}

		return parameters;
	}

	public AllSamplesTest(
			final String classSimpleName,
			final Class<? extends AvcDiagrammer> sampleDiagrammerClass) {

		this.sampleDiagrammerClass = checkNotNull(sampleDiagrammerClass,
				"sampleDiagrammerClass");
	}

	private final Class<? extends AvcDiagrammer> sampleDiagrammerClass;

	@Test
	public void test() throws Exception {

		sampleDiagrammerClass.getMethod("main", String[].class).invoke(null,
				new Object[]{
					new String[0]
				});
	}
}
