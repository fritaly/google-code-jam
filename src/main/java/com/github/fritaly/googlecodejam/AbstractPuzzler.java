package com.github.fritaly.googlecodejam;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractPuzzler {

	public AbstractPuzzler() {
	}

	protected boolean isLogEnabled() {
		// Log disabled by default, override if necessary
		return false;
	}

	protected final void log(String message) {
		if (isLogEnabled()) {
			System.out.println(message);
		}
	}

	protected final void log(Object object) {
		if (isLogEnabled()) {
			System.out.println(object.toString());
		}
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	protected List<String> getInputs() {
		// Provide a default implementation which works most of the time
		return Arrays.asList(String.format("%s-sample.in", getName()), String.format("%s-small-practice.in", getName()),
				String.format("%s-large-practice.in", getName()));
	}

	public final void run() throws Exception {
		for (String resource : getInputs()) {
			InputStream inputStream = getClass().getResourceAsStream(resource);
			InputStreamReader inputStreamReader = null;
			LineNumberReader lineReader = null;

			try {
				handleInput(lineReader = new LineNumberReader(inputStreamReader = new InputStreamReader(inputStream)));
			} finally {
				if (lineReader != null) {
					lineReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			}

			System.out.println();
		}
	}

	public final void execute() throws Exception {
		for (String resource : getInputs()) {
			InputStream inputStream = getClass().getResourceAsStream(resource);
			Scanner scanner = null;

			try {
				if (inputStream == null) {
					throw new RuntimeException("Unable to find resource '" + resource + "'");
				}

				handleInput(scanner = new Scanner(inputStream));
			} finally {
				if (scanner != null) {
					scanner.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}

			System.out.println();
		}
	}

	protected void handleInput(Scanner scanner) throws IOException, Exception {
		// This implementation works most of the time, let subclasses override it if needed
		final int useCases = scanner.nextInt();

//		log(String.format("Found %d use cases", useCases));

		for (int k = 0; k < useCases; k++) {
			System.out.println(String.format("Case #%d: %s", k + 1, solve(scanner)));
		}
	}

	protected String solve(Scanner scanner) throws Exception {
		// Method to be overridden by subclasses to solve use cases
		throw new UnsupportedOperationException("Method not implemented");
	}

	protected void handleInput(LineNumberReader reader) throws IOException, Exception {
		// This implementation works most of the time, let subclasses override it if needed
		final int useCases = Integer.parseInt(reader.readLine());

//		log(String.format("Found %d use cases", useCases));

		for (int k = 0; k < useCases; k++) {
			System.out.println(String.format("Case #%d: %s", k + 1, solve(reader)));
		}
	}

	protected String solve(LineNumberReader reader) throws Exception {
		// Method to be overridden by subclasses to solve use cases
		throw new UnsupportedOperationException("Method not implemented");
	}

	protected static String repeat(String string, int times) {
		final StringBuilder buffer = new StringBuilder();

		for (int i = 0; i < times; i++) {
			buffer.append(string);
		}

		return buffer.toString();
	}
}
