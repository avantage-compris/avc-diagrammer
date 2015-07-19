package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.LEFT;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.TOP;

import java.io.File;

public class MigrationScenario1_4 extends MigrationScenario1_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario1_4() // .border(1)
				.addOutputFile(new File("target/MigrationScenario1_4.svg")) //
				.run(600, 420);
	}

	@Override
	protected void body() {

		webapps();

		arrows();

		arrow(webapp1, TOP, app_data0, LEFT);
		arrow(webapp1, TOP, b_commands, LEFT);
		arrow(webapp1, queue);

		final Node user = hidden(10, webapp1.y() - 5, 0,
				webapp1.total_height() + 10);

		arrow(user, webapp1);
		
		obsolete(webapp0);
	}
}
