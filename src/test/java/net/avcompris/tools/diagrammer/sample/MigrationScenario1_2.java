package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.BOTTOM;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.LEFT;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.TOP;

import java.io.File;

public class MigrationScenario1_2 extends MigrationScenario1_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario1_2() // .border(1)
				.addOutputFile(new File("target/MigrationScenario1_2.svg")) //
				.run(600, 420);
	}

	@Override
	protected void body() {

		webapps();

		arrow_yellow(webapp1, TOP, app_data0, LEFT);
		arrow_yellow(webapp1, TOP, b_commands, LEFT);
		arrow_yellow(webapp1, queue);

		arrows();
		
		arrow(webapp0, app_data0);
		arrow(webapp0, BOTTOM, b_commands, LEFT);
		arrow(webapp0, BOTTOM, queue, LEFT);

		final Node user = hidden(10, webapp0.y() - 5, 0,
				webapp0.total_height() + 10);

		arrow(user, webapp0);
	}
}
