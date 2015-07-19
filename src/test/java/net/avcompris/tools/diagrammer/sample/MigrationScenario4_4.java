package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.BOTTOM;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.LEFT;

public class MigrationScenario4_4 extends MigrationScenario4_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario4_4();
	}

	@Override
	protected void body() {

		components();

		arrow(workers1, b_data);
		arrow(workers1, LEFT, b_converter, BOTTOM);

		obsolete(workers0);
	}
}
