package org.marcosoft.lib;

/**
 * Progress.
 */
public interface Progress {
	void setProgress(String template, Object... objects);

	void setProgress(int progress);

	void finished();
}
