package net.avcompris.tools.diagrammer;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

abstract class AppInfo {

	public final String artifactId;
	public final String version;
	public final String url;

	private AppInfo(
			final String artifactId,
			final String version,
			final String url) {

		this.artifactId = validate(artifactId, "project.artifactId");
		this.version = validate(version, "project.version");
		this.url = validate(url, "project.url");
	}

	private static String validate(final String value, final String label) {

		if (value == null) {
			throw new IllegalArgumentException(
					"Illegal null value for property \"" + label + "\"");
		}

		if (isBlank(value)) {
			throw new IllegalArgumentException(
					"Illegal empty value for property \"" + label + "\"");
		}

		if (value.contains("$")) {
			throw new IllegalArgumentException("Illegal value for property \""

			+ label + "\": " + value + "."

			+ " It seems the value in app.properties has not been processed."

			+ " Make sure you've run \"mvn process-resources\".");
		}

		return value;
	}

	private static AppInfo appInfo = null;

	public static AppInfo get() {

		if (appInfo != null) {
			return appInfo;
		}

		final InputStream is = AppInfo.class.getClassLoader()
				.getResourceAsStream("app.properties");

		if (is == null) {
			throw new RuntimeException("Cannot find app.properties");
		}

		final Properties properties = new Properties();

		try {

			try {

				properties.load(is);

			} finally {
				is.close();
			}

		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		appInfo = new AppInfo(properties.getProperty("project.artifactId"),
				properties.getProperty("project.version"),
				properties.getProperty("project.url")) {

		};

		return appInfo;
	}
}
