package net.avcompris.tools.diagrammer.sample;


public class MigrationScenario5_1 extends MigrationScenario5_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario5_1();
	}

	@Override
	protected void body() {

		components();

		arrow(b_converter0, app_data);
		arrow(workers0, b_data);
		arrow(workers0, b_converter0);
	}
}
