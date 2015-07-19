package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.*;
import java.io.File;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

public class DefaultArchitecture extends AvcDiagrammer {

	public static void main(final String... args) throws Exception {

		new DefaultArchitecture() // .border(1)
				.addOutputFile(new File("target/DefaultArchitecture.svg")) //
				.run(600, 450);
	}

	@Override
	protected void body() {

		inside_of_box(10, 10, 250, 400, "#69f");

		final Node webapp = box(30, 50, 80, "#6cf", "webapp");

		final Node app_data = database(155, 50, 70, "#6cf", "app", "data");

		final Node b_converter = box(140, 120, 80, "#6cf", "b_converter");

		final Node b_commands = database(155, 190, 70, "#ccc", "business",
				"commands");

		final Node command_mgr = box(140, 260, 80, "#ccc", "cmd_mgr");

		final Node queue = queue(140, 310, 80, "#ccc", "queue");

		outside_of_box("AppCenter");

		inside_of_box(320, 10, 250, 400, "#f66");

		final Node worker = box(400, 310, 80, "#f96", "worker <1>");
		box(400, 310 + LINE_HEIGHT, 80, "#f96", "worker <2>");
		box(400, 310 + 2 * LINE_HEIGHT, 80, "#f96", "worker <3>");

		final Node b_data = database(400, 50, 100, "#f96", "business", "data");

		outside_of_box("BizMaster");

		arrow(webapp, app_data);
		arrow(webapp, BOTTOM, b_commands, LEFT);
		arrow(b_converter, app_data);
		arrow(command_mgr, b_commands);
		arrow(webapp, BOTTOM, queue, LEFT);
		arrow(queue, worker, "dashed");
		arrow(worker, TOP, b_converter, RIGHT);
		arrow(worker, TOP, command_mgr, RIGHT);
		arrow(worker, b_data);
	}
}
