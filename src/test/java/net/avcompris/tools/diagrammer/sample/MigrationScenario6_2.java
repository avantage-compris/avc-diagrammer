package net.avcompris.tools.diagrammer.sample;

public class MigrationScenario6_2 extends MigrationScenario6_0 {

	public static void main(final String... args) throws Exception {

		new MigrationScenario6_2();
	}

	@Override
	protected void body() {

		v0("state=9315489");

		arrow(workers0, b_data0);
		arrow(workers0, b_converter);

		b_data1("state=9315001");
	}
}
