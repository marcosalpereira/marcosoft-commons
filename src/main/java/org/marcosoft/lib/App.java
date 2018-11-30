package org.marcosoft.lib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class App {
	private final Version version;
	private String title;
	private String jarName;
	private String name;
	private ApplicationProperties applicationProperties;
	private String updateHost;
	private String runningFolder;
	private String[] args;
	private File runningJar;
    private String updateFolder;

	public App(String name, String defaultTitle, Version version) {
		this.name = name;
		this.title = defaultTitle;
		this.version = version;
	}

	public App(String[] args, String defaultTitle) {
		this.args = args;
		String versionStr = null;
		try {
			final InputStream inputStream = App.class.getResourceAsStream("/META-INF/MANIFEST.MF");
			final Manifest manifest = new Manifest(inputStream);
			if (manifest != null) {
				final Attributes attributes = manifest.getMainAttributes();
				versionStr = (String) attributes.get(Attributes.Name.IMPLEMENTATION_VERSION);
				this.title = ((String) attributes.get(Attributes.Name.IMPLEMENTATION_TITLE));
				this.name = ((String) attributes.get(new Attributes.Name("App-Name")));
				this.jarName = ((String) attributes.get(new Attributes.Name("Jar-Name")));
				this.updateHost = ((String) attributes.get(new Attributes.Name("Update-Host")));
				this.updateFolder = ((String) attributes.get(new Attributes.Name("Update-Folder")));
			}
		} catch (final IOException localIOException) {
		}
		if (versionStr == null) {
			versionStr = "99";
		}
		this.version = new Version(versionStr);
		if (this.title == null) {
			this.title = defaultTitle;
		}
	}

	public String getUpdateFolder() {
        return updateFolder;
    }

	public String[] getArgs() {
		return args;
	}

	public String getUpdateHost() {
		return this.updateHost;
	}

	public String getName() {
		return this.name;
	}

	public Version getVersion() {
		return this.version;
	}

	public String getTitle() {
		return this.title;
	}

	public String getRunningFolder() {
		if (this.runningFolder == null) {
			try {
				final URI uri = App.class.getProtectionDomain().getCodeSource().getLocation().toURI();
				this.runningFolder = new File(uri).getParent();
			} catch (final URISyntaxException localURISyntaxException) {
			}
		}
		return this.runningFolder;
	}

	public File getRunningJar() {
		if (this.runningJar == null) {
			try {
				final URI uri = App.class.getProtectionDomain().getCodeSource().getLocation().toURI();
				this.runningJar = new File(uri);
			} catch (final URISyntaxException localURISyntaxException) {
			}
		}
		return this.runningJar;
	}

	public static void main(String[] args) {
		final File f = new File(
				"/home/54706424372/dev/java/src/lib-tools/src/main/java/org/marcosoft/lib/SoftwareUpdate.java");
		System.out.println(f.getParentFile());
	}

	public String getJarName() {
		return this.jarName;
	}

	public ApplicationProperties getApplicationProperties() {
		if (this.applicationProperties == null) {
			this.applicationProperties = new ApplicationProperties(this.name);
		}
		return this.applicationProperties;
	}

	public boolean runningJarHasVersionOnName() {
		final File jar = getRunningJar();
		if (jar == null) return false;
		return jar.getName().endsWith(getVersion() + ".jar");
	}
}