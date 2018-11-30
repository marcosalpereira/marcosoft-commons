package org.marcosoft.lib;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class SoftwareUpdate {
	private static final String LAST_VERSION = "last-version";

	private static final String LAST_VERSION_WARN_MESSAGE = "last-version-warn-message";

	private final Progress progress;
	private final App app;

	private SoftwareUpdate(App app, Progress progress) {
		this.app = app;
		this.progress = progress;
	}

	public static void main(String[] args) throws IOException {
	}

	public static void update(App app) {
		final ProgressBar progress = new ProgressBar();
		update(app, progress);
		progress.dispose();
	}

	public static void update(App app, Progress progress) {
		final SoftwareUpdate softwareUpdate = new SoftwareUpdate(app, progress);
		softwareUpdate.checkReleaseNotes();
		try {
			final File newJar = softwareUpdate.doIt();
			if (newJar != null) {
				softwareUpdate.runNewVersion(newJar);
			}
		} catch (final IOException localIOException) {
		}
	}

	private void checkReleaseNotes() {
		showReleaseNotes();
		showWarnMessage();
	}

	private void runNewVersion(File newVersion) {
		Exec.jar(newVersion.getAbsolutePath(),
				Arrays.asList("-Dupdate.disabled=YES"),
				Arrays.asList(app.getArgs()));
		System.exit(0);
	}

	private boolean isUpdateDisabled() {
		return System.getProperty("update.disabled", "NO").toUpperCase().matches("YES|SIM|1");
	}

	private File doIt() throws IOException {
		if (isUpdateDisabled()) {
			return null;
		}

		final Version newVersion = checkForNewVersion();
		if (newVersion == null) {
			return null;
		}
		final String url = String.format(this.app.getUpdateHost() + "/%s/download/v%s/binario.zip",
				new Object[] { getReleasesPage(), newVersion });

		progressString("%s v%s - Atualizando para v%s ...",
				new Object[] { this.app.getName(), this.app.getVersion(), newVersion });

		final String zipFile = this.app.getRunningFolder() + File.separator + "binario.zip";
		WebUtils.downloadFile(url, zipFile, this.progress);
		return unzip(newVersion, zipFile);
	}

	private File unzip(final Version newVersion, final String zipFile) throws IOException {
		final List<File> jar = new ArrayList<File>();

		new ZipUtils() {
			@Override
			protected void unziped(File file) {
				if (file.getName().endsWith(newVersion.get() + ".jar")) {
					File jarFile = file;
					if (!app.runningJarHasVersionOnName()) {
						if (file.renameTo(app.getRunningJar())) {
							jarFile = app.getRunningJar();
						}
					}
					jar.add(jarFile);
				}
			}
		}.unZip(zipFile, this.app.getRunningFolder(), this.progress);
		if (jar.isEmpty()) {
			progressString("Arquivo de distribuição corrompido!");
			return null;
		}
		return jar.get(0);

	}

	private void progressString(String template, Object... objects) {
		this.progress.setProgress(String.format(template, objects));
	}

	public Version checkForNewVersion() {
		progressString("%s v%s - Verificando se existe versão nova",
				new Object[] { this.app.getName(), this.app.getVersion() });

		final String latestVersionStr = getLatestVersion();
		if (latestVersionStr == null) {
			return null;
		}

		final Version latestVersion = new Version(latestVersionStr);
		final boolean existsNewVersion = latestVersion.compareTo(this.app.getVersion()) > 0;
		if (existsNewVersion) {
			return latestVersion;
		}
		return null;
	}

	public String getReleasesPage() {
		return this.app.getUpdateFolder()+ "/" + this.app.getName() + "/releases";
	}

	private String getLatestVersion() {
		final String fileUrl = this.app.getUpdateHost() + "/" + getReleasesPage() + "/" + getVersionType();
		final String latestVersionPage = WebUtils.download(fileUrl, this.progress);
		if (latestVersionPage == null) {
			return null;
		}

		final Pattern pattern = Pattern.compile("^[0-9]+(\\.[0-9]+)*$");
		final Matcher matcher = pattern.matcher(latestVersionPage);
		if (matcher.matches()) {
			return matcher.group(1);
		}

		final String searchStr = "/" + getReleasesPage() + "/download/";
		final int ini = latestVersionPage.indexOf(searchStr) + searchStr.length();
		final int fim = latestVersionPage.indexOf('/', ini);
		if (fim <= ini) {
			return null;
		}
		return latestVersionPage.substring(ini + 1, fim);
	}

	private String getVersionType() {
		return System.getProperty("ms.update.versionType", "latest");
	}

	private void showReleaseNotes() {
		final Version lastVersion = getLastExecutedVersion(LAST_VERSION);
		this.app.getApplicationProperties().setProperty(LAST_VERSION, this.app.getVersion().get());
		if (!this.app.getVersion().gt(lastVersion)) {
			return;
		}
		final InputStream stream = getClass().getClassLoader().getResourceAsStream("releaseNotes.txt");
		if (stream != null) {
			final String conteudo = IOUtils.readContent(stream, "ISO-8859-1");
			showInfoMessage(conteudo);
		}
	}

	private Version getLastExecutedVersion(String releaseNotesKey) {
		final String property = this.app.getApplicationProperties().getProperty(releaseNotesKey);
		try {
			return new Version(property);
		} catch (final IllegalArgumentException e) {
		}
		return null;
	}

	private void showWarnMessage() {
		final Version lastVersion = getLastExecutedVersion(LAST_VERSION_WARN_MESSAGE);
		this.app.getApplicationProperties().setProperty(LAST_VERSION_WARN_MESSAGE, this.app.getVersion().get());

		if (!this.app.getVersion().gt(lastVersion)) {
			return;
		}

		final String fileName = String.format("mensagemImportante-%s.txt", new Object[] { this.app.getVersion().get() });
		final InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
		if (stream != null) {
			final String conteudo = IOUtils.readContent(stream, "ISO-8859-1");
			showWarnMessage(conteudo);
		}
	}

	private void showInfoMessage(String conteudo) {
		final Component c = new JScrollPane(new JTextArea(conteudo, 10, 70));
		JOptionPane.showMessageDialog(null, c, this.app.getTitle(), 1);
	}

	private void showWarnMessage(String conteudo) {
		final JTextPane textArea = new JTextPane();
		textArea.setContentType("text/html");
		textArea.setText(conteudo);
		textArea.setPreferredSize(new Dimension(650, 300));
		final Component c = new JScrollPane(textArea);
		JOptionPane.showMessageDialog(null, c, "Mensagem Importante", 2);
	}
}
