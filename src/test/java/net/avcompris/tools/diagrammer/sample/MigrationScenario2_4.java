package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.RIGHT;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.TOP;

import java.io.File;

public class MigrationScenario2_4 extends MigrationScenario2_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario2_4()
				// .border(1)
				.printToSystemOut(false)
				.addOutputFile(new File("target/MigrationScenario2_4.svg")) //
				.run(600, 370);
	}

	@Override
	protected void body() {

		components();

		arrow(b_converter1, app_data0);
		arrow(workers, TOP, b_converter1, RIGHT);
		obsolete(b_converter0);
	}
}
