package net.avcompris.tools.diagrammer.sample;

import java.io.File;
import java.io.IOException;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

abstract class MigrationScenario5_0 extends AvcDiagrammer {

	protected Node b_converter0;
	protected Node b_converter1;
	protected Node app_data;
	protected Node workers0;
	protected Node workers1;
	protected Node b_data;

	protected MigrationScenario5_0() {

		try {

			// border(1).
			printToSystemOut(false).addOutputFile(
					new File("target/" + getClass().getSimpleName() + ".svg")) //
					.run(600, 320);

		} catch (final IOException e) {

			throw new RuntimeException(e);
		}
	}

	protected void components() {

		inside_of_box(10, 10, 250, 270, "#69f");

		app_data = database(_ + 65, 50, 140, "#3cf", "app", "data");

		b_converter1 = box(_ - 25, 220, 100, "#fc3", "b_converter v1");

		b_converter0 = box(_ + 70, 150, _, "#3cf", "b_converter v0");

		outside_of_box("AppCenter");

		inside_of_box(320, _, 250, _, "#f63");

		b_data = database(_ + 65, 50, 140, "#f93", "business", "data");

		workers0 = box(_ - 15, 150, 80, ____, "workers v0");

		workers1 = box(_ + 70, 220, _, "#fc3", "workers v1");

		outside_of_box("BizMaster");
	}
}
