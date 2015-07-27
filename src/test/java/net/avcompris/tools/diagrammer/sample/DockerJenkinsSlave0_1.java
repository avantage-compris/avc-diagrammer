package net.avcompris.tools.diagrammer.sample;

import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.RIGHT;
import static net.avcompris.tools.diagrammer.AvcDiagrammer.NodeSide.TOP;

public class DockerJenkinsSlave0_1 extends DockerJenkinsSlave0_0 {

	public static void main(final String... args) throws Exception {

		new DockerJenkinsSlave0_1();
	}

	@Override
	protected void body() {

		pre();

		arrow(terminal, hg);

		arrow(terminal, TOP, docker, RIGHT);

		arrow(terminal, TOP, docker_registry, RIGHT);

		arrow(docker_registry, shared_folder);
	}
}
