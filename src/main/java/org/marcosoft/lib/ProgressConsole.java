package org.marcosoft.lib;

public class ProgressConsole implements Progress {

	public void setTitle(String title) {
		System.out.println(title);
	}

	public void setProgress(String template, Object... objects) {
		System.out.println(String.format(template, objects));
	}

	public void setProgress(int value) {
		System.out.println(value + "%");
	}

	public void finished() {
		System.out.println("fim");
	}
}
