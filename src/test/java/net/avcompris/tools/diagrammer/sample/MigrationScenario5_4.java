package net.avcompris.tools.diagrammer.sample;


public class MigrationScenario5_4 extends MigrationScenario5_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario5_4();
	}

	@Override
	protected void body() {

		components();

		obsolete(b_converter0);
		obsolete(workers0);
		arrow(workers1, b_converter1);
		
		arrow(workers1,b_data);
		arrow(b_converter1,app_data);
	}
}
