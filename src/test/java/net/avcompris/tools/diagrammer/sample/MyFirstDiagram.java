package net.avcompris.tools.diagrammer.sample;

import java.io.File;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

public class MyFirstDiagram extends AvcDiagrammer {

	public static void main(final String... args) throws Exception {

		new MyFirstDiagram().border(1)
				.addOutputFile(new File("target/MyFirstDiagram.svg"))
				.run(160, 120);
	}

	@Override
	protected void body() {

		box(10, 10, 100, "#69f", "Hello", "*World!*");
	}
}
