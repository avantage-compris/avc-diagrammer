package net.avcompris.tools.diagrammer.sample;

public class MigrationScenario4_3 extends MigrationScenario4_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario4_3();
	}

	@Override
	protected void body() {

		components();

		arrow(workers1, b_data);

		arrow_gray(workers0, b_data);
	}
}
