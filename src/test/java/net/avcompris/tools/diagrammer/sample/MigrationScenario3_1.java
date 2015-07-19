package net.avcompris.tools.diagrammer.sample;


public class MigrationScenario3_1 extends MigrationScenario3_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario3_1();
	}
	
	@Override
	protected void body() {

		v0("state=1205001");

		arrow(webapp0,app_data0);
		arrow(b_converter0, app_data0);
		arrow(workers, b_converter0);
		
		user0();
	}
}
