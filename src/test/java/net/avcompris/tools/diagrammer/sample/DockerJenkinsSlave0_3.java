package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.BOTTOM;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.RIGHT;

public class DockerJenkinsSlave0_3 extends DockerJenkinsSlave0_0 {

	public static void main(final String... args) throws Exception {

		new DockerJenkinsSlave0_3();
	}

	@Override
	protected void body() {

		pre();

		jenkins_slave0();

		arrow(jenkins, git);

		final Arrow arrow_jenkins_docker = arrow(jenkins, BOTTOM, docker, RIGHT);

		arrow(jenkins, BOTTOM, docker_registry, RIGHT);

		final Arrow arrow_jenkins_container = arrow(jenkins, BOTTOM,
				jenkins_slave0, RIGHT);

		arrow(docker_registry, shared_folder);

		bullet("1", docker);

		bullet("2", shared_folder);

		bullet("3", docker_package);

		bullet("4", arrow_jenkins_docker);

		bullet("5", 0.8, arrow_jenkins_container);

		bullet("6", docker_registry);
	}
}
