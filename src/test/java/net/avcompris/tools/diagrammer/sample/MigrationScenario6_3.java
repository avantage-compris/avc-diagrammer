package net.avcompris.tools.diagrammer.sample;

public class MigrationScenario6_3 extends MigrationScenario6_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario6_3();
	}

	@Override
	protected void body() {

		v0("state=9315489");

		arrow(workers0, b_data0);
		arrow(workers0, b_converter);

		b_data1("state=9315001");
		workers1();
		batch();

		arrow_yellow(workers1, b_data1);

		arrow(batch, b_data1);
	}
}
