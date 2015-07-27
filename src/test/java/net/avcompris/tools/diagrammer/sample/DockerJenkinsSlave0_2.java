package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.BOTTOM;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.RIGHT;

public class DockerJenkinsSlave0_2 extends DockerJenkinsSlave0_0 {

	public static void main(final String... args) throws Exception {

		new DockerJenkinsSlave0_2();
	}

	@Override
	protected void body() {

		pre();

		jenkins_slave0();

		arrow(jenkins, git);

		arrow(jenkins, BOTTOM, docker, RIGHT);

		arrow(jenkins, BOTTOM, docker_registry, RIGHT);

		arrow(jenkins, BOTTOM, jenkins_slave0, RIGHT);

		arrow(docker_registry, shared_folder);
	}
}
