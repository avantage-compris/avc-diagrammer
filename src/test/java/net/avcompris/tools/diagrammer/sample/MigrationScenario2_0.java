package net.avcompris.tools.diagrammer.sample;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

abstract class MigrationScenario2_0 extends AvcDiagrammer {

	protected Node app_data0;
	protected Node b_converter0;
	protected Node b_converter1;
	protected Node workers;

	protected void components() {

		inside_of_box(10, 10, 350, 320, "#69f");

		app_data0 = database(90, 50, 210, "#3cf", "app", "data v0");

		b_converter0 = box(_ + 110, _ + 90, 100, ____, "b_converter v0");

		outside_of_box("AppCenter");

		inside_of_box(410, _, 160, _, "#f63");

		workers = box(_ + 40, 260, 80, "#f93", "workers");

		final Node b_data = database(_ + 10, 50, 70, ____, "business", "data");

		outside_of_box("BizMaster");

		b_converter1 = box(80, 200, 100, "#fc3", "b_converter v1");

		arrow(workers, b_data);
	}
}
