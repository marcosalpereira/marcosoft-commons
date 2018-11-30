package org.marcosoft.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Exec {
	public static Process jar(String jarFile, List<String> vmArgs, List<String> jarArgs) {
		try {
			final List<String> cmd = new ArrayList<String>(2 + vmArgs.size() + jarArgs.size());
			cmd.add("java");
			cmd.addAll(vmArgs);
			cmd.add("-jar");
			cmd.add(jarFile);
			cmd.addAll(jarArgs);

			final String[] cmdArray = cmd.toArray(new String[0]);

			System.out.println(java.util.Arrays.asList(cmdArray));

			return Runtime.getRuntime().exec(cmdArray);

		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getProcessErrorOutput(Process process) throws IOException {
		final BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		final StringBuilder sb = new StringBuilder();
		String line;
		while ((line = stdError.readLine()) != null) {
			sb.append(line); sb.append('\n');
		}
		sb.append("Exit Value:" + process.exitValue());
		return sb.toString();
	}

	public static String getProcessOutput(Process process) throws IOException {
		final BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		final StringBuilder sb = new StringBuilder();
		String line;
		while ((line = stdError.readLine()) != null) {
			sb.append(line); sb.append('\n');
		}
		sb.append("Exit Value:" + process.exitValue());
		return sb.toString();
	}

	public static Process exe(String exe, List<String> args) {
		try {
			final List<String> cmd = new ArrayList<String>(1 + args.size());
			cmd.add(exe);
			cmd.addAll(args);

			final String[] cmdArray = cmd.toArray(new String[0]);

			System.out.println(java.util.Arrays.asList(cmdArray));

			return Runtime.getRuntime().exec(cmdArray);

		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
