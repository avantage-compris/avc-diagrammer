package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.BOTTOM;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.RIGHT;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.TOP;

import java.io.File;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

public class AppCenters extends AvcDiagrammer {

	public static void main(final String... args) throws Exception {

		new AppCenters() // .border(1)
				.addOutputFile(new File("target/AppCenters.svg")) //
				.run(600, 450);
	}

	@Override
	protected void body() {

		inside_of_box(10, 10, 250, 170, "#69f");

		final Node webapp1 = box(30, 50, 80, "#6cf", "webapp1");

		final Node app_data1 = database(155, 50, 70, "#6cf", "app", "data1");

		final Node b_converter1 = box(140, 120, 80, "#6cf", "b_converter1");

		outside_of_box("AppCenter1");

		inside_of_box(10, 230, 250, 170, "#69f");

		final Node webapp2 = box(30, 270, 80, "#6cf", "webapp2");

		final Node app_data2 = database(155, 270, 70, "#6cf", "app", "data2");

		final Node b_converter2 = box(140, 340, 80, "#6cf", "b_converter2");

		outside_of_box("AppCenter2");

		inside_of_box(320, 10, 250, 400, "#f66");

		final Node workers = box(400, 260, 80, "#f96", "workers");

		final Node b_data = database(400, 50, 100, "#f96", "business", "data");

		outside_of_box("BizMaster");

		arrow(webapp1, app_data1);
		arrow(b_converter1, app_data1);
		arrow(workers, TOP, b_converter1, RIGHT);
		arrow(webapp2, app_data2);
		arrow(b_converter2, app_data2);
		arrow(workers, BOTTOM, b_converter2, RIGHT);
		arrow(workers, b_data);
	}
}
