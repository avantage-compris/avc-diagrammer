package net.avcompris.tools.diagrammer.sample;

import java.io.File;
import java.io.IOException;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

abstract class MigrationScenario6_0 extends AvcDiagrammer {

	protected Node b_converter;
	protected Node workers0;
	protected Node workers1;
	protected Node b_data0;
	protected Node b_data1;
	protected Node batch;

	protected MigrationScenario6_0() {

		try {

			// border(1).
			printToSystemOut(false).addOutputFile(
					new File("target/" + getClass().getSimpleName() + ".svg")) //
					.run(600, 390);

		} catch (final IOException e) {

			throw new RuntimeException(e);
		}
	}

	protected void v0(final String state) {

		inside_of_box(10, 10, 160, 340, "#69f");

		final Node app_data = database(_ + 45, 50, 60, "#3cf", "app", "data");

		b_converter = box(_ - 10, 130, 80, ____, "b_converter");

		arrow(b_converter, app_data);

		outside_of_box("AppCenter");

		inside_of_box(220, _, 350, _, "#f63");

		b_data0 = database(_ + 55, 50, 120, "#f93", "business data v0", state);

		workers0 = box(_ + 20, b_converter.y(), 80, ____, "workers v0");

		outside_of_box("BizMaster");
	}

	protected void b_data1(final String state) {

		b_data1 = database(b_data0.x(), b_data0.y() + 160, b_data0.width(),
				"#fc3", "business data v1", state);
	}

	protected void workers1() {

		workers1 = box(workers0.x(), b_data1.y() - b_data0.y() + workers0.y(),
				workers0.width(), "#fc3", "workers v1");
	}

	protected void batch() {

		batch = box(b_data1.x() + 160, b_data1.y() + 10, 80,
				workers0.fillColor(), "batch");
	}
}
