package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.BOTTOM;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.LEFT;

public class MigrationScenario6_5 extends MigrationScenario6_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario6_5();
	}

	@Override
	protected void body() {

		v0("state=9315506");

		obsolete(workers0);
		obsolete(b_data0);

		b_data1("state=9315506");
		workers1();
		batch();
		obsolete(batch);

		arrow(workers1, b_data1);
		arrow(workers1, LEFT, b_converter, BOTTOM);
	}
}
