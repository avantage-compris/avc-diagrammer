package net.avcompris.tools.diagrammer.sample;


public class MigrationScenario5_2 extends MigrationScenario5_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario5_2();
	}

	@Override
	protected void body() {

		components();

		arrow(b_converter0, app_data);
		arrow(workers0, b_data);
		arrow(workers0, b_converter0);
		
		arrow_yellow(workers1,b_data);
		arrow_yellow(b_converter1,app_data);
	}
}
