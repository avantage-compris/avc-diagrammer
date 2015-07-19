package net.avcompris.tools.diagrammer.sample;


public class MigrationScenario3_4 extends MigrationScenario3_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario3_4();
	}

	@Override
	protected void body() {

		v0("state=1205506");

		arrow(webapp0, app_data0);
		arrow(b_converter0, app_data0);

		webapp1();
		app_data1("state=1205489");
		b_converter1();
		batch();

		arrow(webapp1, app_data1);
		arrow(b_converter1, app_data1);
		//arrow(workers, BOTTOM,b_converter1, RIGHT);

		arrow(batch, b_converter1);

		user0();
	}
}
