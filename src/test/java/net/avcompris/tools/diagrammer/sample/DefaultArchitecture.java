package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.BOTTOM;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.LEFT;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.RIGHT;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.TOP;

import java.io.File;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

public class DefaultArchitecture extends AvcDiagrammer {

	public static void main(final String... args) throws Exception {

		new DefaultArchitecture()
				// .border(1)
				.printToSystemOut(false)
				.addOutputFile(new File("target/DefaultArchitecture.svg")) //
				.run(600, 450);
	}

	@Override
	protected void body() {

		inside_of_box(10, 10, 250, 400, "#69f");

		final Node webapp = box(_ + 20, _ + 40, 80, "#3cf", "webapp");

		final Node app_data = database(155, _, 70, ____, "app", "data");

		final Node b_converter = box(_ - 15, _ + 70, 80, ____, "b_converter");

		final Node b_commands = database(_ + 15, _ + 70, 70, "#ccc",
				"business", "commands");

		final Node command_mgr = box(_ - 15, _ + 70, 80, ____, "cmd_mgr");

		final Node queue = queue(_, _ + 50, _, ____, "queue");

		outside_of_box("AppCenter");

		inside_of_box(320, 10, 250, 400, "#f63");

		final Node worker = box(_ + 80, queue.y(), 80, "#f93", "worker <1>");

		box(_, _ + LINE_HEIGHT, _, ____, "worker <2>");

		box(_, _ + LINE_HEIGHT, _, ____, "worker <3>");

		final Node b_data = database(_, 50, _ + 20, ____, "business", "data");

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
