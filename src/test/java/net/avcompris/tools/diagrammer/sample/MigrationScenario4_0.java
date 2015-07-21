package net.avcompris.tools.diagrammer.sample;

import java.io.File;
import java.io.IOException;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

abstract class MigrationScenario4_0 extends AvcDiagrammer {

	protected Node b_converter;
	protected Node workers0;
	protected Node workers1;
	protected Node b_data;

	protected MigrationScenario4_0() {

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

		inside_of_box(10, 10, 200, 270, "#69f");

		b_converter = box(_ + 70, 150, 80, "#3cf", "b_converter");

		final Node app_data = database(_, 50, 80, ____, "app", "data");

		arrow(b_converter, app_data);

		outside_of_box("AppCenter");

		inside_of_box(270, _, 300, _, "#f63");

		workers0 = box(_ + 60, 150, 80, "#f93", "workers v0");

		b_data = database(320, 50, 220, ____, "business", "data");

		workers1 = box(_ + 100, 220, 80, "#fc3", "workers v1");

		outside_of_box("BizMaster");
	}
}
