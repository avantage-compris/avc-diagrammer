package net.avcompris.tools.diagrammer.sample;

import java.io.File;
import java.io.IOException;

import net.avcompris.tools.diagrammer.AvcDiagrammer;

public class DockerJenkinsSlave0_0 extends AvcDiagrammer {

	protected DockerJenkinsSlave0_0() {

		try {

			// border(1).
			printToSystemOut(false).addOutputFile(
					new File("target/" + getClass().getSimpleName() + ".svg")) //
					.run(640, 470);

		} catch (final IOException e) {

			throw new RuntimeException(e);
		}
	}

	public static void main(final String... args) throws Exception {

		new DockerJenkinsSlave0_0();
	}

	protected Node git;
	protected Node hg;
	protected Node docker;
	protected Node docker_registry;
	protected Node jenkins;
	protected Node jenkins_slave0;
	protected Node shared_folder;
	protected Node terminal;

	@Override
	protected void body() {

		pre();

		jenkins_slave0();
	}

	protected final void pre() {

		inside_of_box(10, 10, 250, 400, "#69f");

		installed_packages("#36c", "virtualbox");

		inside_of_box(_ + 10, _ + 40, 180, 220, "#6f6");

		installed_packages("#3c3", "docker");

		docker_registry = docker_image(_ + 10, _ + 150, 130, 30,
				"docker-registry");

		docker = outside_of_box("CentOS 7", "VM-with-docker");

		shared_folder = directory(_ + 80, _ + 290, "#69f",
				"/var/docker-registry");

		outside_of_box("Debian", "Host 2");

		inside_of_box(300, 10, 310, 210, "#66f");

		installed_packages("#33c", "virtualbox");

		inside_of_box(_ + 170, _ + 40, 110, 80, "#66f");

		final Node[] git_hg = installed_packages(50, "#33c", "git", "hg");

		git = git_hg[0];
		hg = git_hg[1];

		outside_of_box("Debian", "VM-repo");

		inside_of_box(_ - 140, _, 110, _, "#66f");

		jenkins = installed_packages(60, "#33c", "jenkins")[0];

		outside_of_box("Debian", "VM-ci");

		outside_of_box("Debian", "Host 1");

		inside_of_box(460, 280, 130, 60, "#66f");

		terminal = installed_packages(80, "#33c", "Terminal.app")[0];

		outside_of_box("Mac OS X", "my laptop");
	}

	protected final void jenkins_slave0() {

		jenkins_slave0 = docker_image(50, 115, 130, 80, "jenkins-slave0");

		docker_packages("ssh", "JDK + Maven");
	}
}
