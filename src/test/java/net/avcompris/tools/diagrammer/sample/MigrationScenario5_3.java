package net.avcompris.tools.diagrammer.sample;


public class MigrationScenario5_3 extends MigrationScenario5_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario5_3();
	}

	@Override
	protected void body() {

		components();

		arrow_gray(b_converter0, app_data);
		// arrow(workers0, b_data);
		// arrow(workers0, b_converter0);
		
		arrow(workers1,b_data);
		arrow(b_converter1,app_data);
	}
}
