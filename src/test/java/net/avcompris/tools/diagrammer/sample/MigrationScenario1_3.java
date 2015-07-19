package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.BOTTOM;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.LEFT;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.TOP;

import java.io.File;

public class MigrationScenario1_3 extends MigrationScenario1_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario1_3() // .border(1)
				.addOutputFile(new File("target/MigrationScenario1_3.svg")) //
				.run(600, 420);
	}

	@Override
	protected void body() {

		webapps();
		
		arrow_gray(webapp0, app_data0);
		arrow_gray(webapp0, BOTTOM, b_commands, LEFT);
		arrow_gray(webapp0, BOTTOM, queue, LEFT);

		arrows();

		arrow(webapp1, TOP, app_data0, LEFT);
		arrow(webapp1, TOP, b_commands, LEFT);
		arrow(webapp1, queue);

		final Node user = hidden(10, webapp1.y() - 5, 0,
				webapp1.total_height() + 10);

		arrow(user, webapp1);
	}
}
