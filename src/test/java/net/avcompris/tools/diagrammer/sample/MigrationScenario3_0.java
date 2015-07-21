package net.avcompris.tools.diagrammer.sample;

import java.io.File;
import java.io.IOException;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

abstract class MigrationScenario3_0 extends AvcDiagrammer {

	protected Node webapp0;
	protected Node app_data0;
	protected Node b_converter0;
	protected Node webapp1;
	protected Node app_data1;
	protected Node b_converter1;
	protected Node workers;
	protected Node batch;

	protected MigrationScenario3_0() {

		try {

			// border(1).
			printToSystemOut(false).addOutputFile(
					new File("target/" + getClass().getSimpleName() + ".svg")) //
					.run(600, 370);

		} catch (final IOException e) {

			throw new RuntimeException(e);
		}
	}

	protected void v0(final String state) {

		inside_of_box(30, 10, 320, 320, "#69f");

		webapp0 = box(70, 50, 80, "#3cf", "webapp v0");

		app_data0 = database(210, 50, 90, ____, "app data v0", state);

		b_converter0 = box(_ - 10, _ + 70, 100, ____, "b_converter v0");

		outside_of_box("AppCenter");

		inside_of_box(410, _, 160, _, "#f63");

		workers = box(_ + 40, b_converter0.y(), 80, "#f93", "workers");

		final Node b_data = database(_ + 10, 50, 70, ____, "business", "data");

		outside_of_box("BizMaster");

		arrow(workers, b_data);
	}

	protected void webapp1() {

		webapp1 = box(webapp0.x(), webapp0.y() + 140, webapp0.width(), "#fc3",
				"webapp v1");
	}

	protected void app_data1(final String state) {

		app_data1 = database(app_data0.x(), app_data0.y() + 140,
				app_data0.width(), app_data0.height(), "#fc3", "app data v1",
				state);
	}

	protected void b_converter1() {

		b_converter1 = box(b_converter0.x(), b_converter0.y() + 140,
				b_converter0.width(), "#fc3", "b_converter v1");
	}

	protected void batch() {

		batch = box(workers.x(), b_converter1.y(), workers.width(), "#f93",
				"batch");
	}

	protected void user0() {

		final Node user = hidden(-10, webapp0.y() - 5, 0,
				webapp0.total_height() + 10);

		arrow(user, webapp0);
	}

	protected void user1() {

		final Node user = hidden(-10, webapp1.y() - 5, 0,
				webapp1.total_height() + 10);

		arrow(user, webapp1);
	}
}
