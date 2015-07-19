package net.avcompris.tools.diagrammer.sample;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

abstract class MigrationScenario1_0 extends AvcDiagrammer {

	protected Node webapp0;
	protected Node app_data0;
	protected Node b_converter0;
	protected Node b_commands;
	protected Node command_mgr;
	protected Node queue;
	protected Node webapp1;

	protected void webapps() {

		inside_of_box(100, 10, 460, 370, "#69f");

		webapp0 = box(_ + 50, _ + 40, 80, "#3cf", "webapp v0");

		app_data0 = database(415, _, 70, ____, "app", "data v0");

		b_converter0 = box(_ - 25, _ + 70, 100, ____, "b_converter v0");

		b_commands = database(_ + 25, _ + 70, 70, "#ccc", "business",
				"commands");

		command_mgr = box(_ - 15, _ + 70, 80, ____, "cmd_mgr");

		queue = queue(_, _ + 50, _, ____, "queue");

		outside_of_box("AppCenter");

		webapp1 = box(webapp0.x(), queue.y() - 4, webapp0.width(), "#fc3",
				"webapp v1");
	}
	
	protected void arrows() {
		
		arrow(b_converter0, app_data0);
		arrow(command_mgr, b_commands);
	}
}
