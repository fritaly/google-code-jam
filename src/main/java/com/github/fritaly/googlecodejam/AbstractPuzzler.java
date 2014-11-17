package com.github.fritaly.googlecodejam;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;

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
			InputStream inputStream = ReverseWords.class.getResourceAsStream(resource);
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
}
