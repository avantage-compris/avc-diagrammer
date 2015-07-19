package net.avcompris.tools.diagrammer.sample;


public class MigrationScenario4_2 extends MigrationScenario4_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario4_2();
	}

	@Override
	protected void body() {

		components();

		arrow(workers0, b_data);
		arrow(workers0, b_converter);
		arrow_yellow(workers1, b_data);
	}
}
