package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.BOTTOM;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.RIGHT;

public class MigrationScenario3_5 extends MigrationScenario3_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario3_5();
	}

	@Override
	protected void body() {

		v0("state=1205506");

		obsolete(webapp0);
		obsolete(app_data0);
		obsolete(b_converter0);

		webapp1();
		app_data1("state=1205506");
		b_converter1();
		batch();

		arrow(webapp1, app_data1);
		arrow(b_converter1, app_data1);
		arrow(workers, BOTTOM, b_converter1, RIGHT);

		obsolete(batch);

		user1();
	}
}
