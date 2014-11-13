package com.github.fritaly.googlecodejam;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReverseWords {

	public static void main(String[] args) throws Exception {
		solve("ReverseWords-sample.in");

		System.out.println();

		solve("ReverseWords-small-practice.in");

		System.out.println();

		solve("ReverseWords-large-practice.in");
	}

	private static String join(Collection<String> collection) {
		final StringBuilder builder = new StringBuilder();

		for (String string : collection) {
			if (builder.length() > 0) {
				builder.append(" ");
			}

			builder.append(string);
		}

		return builder.toString();
	}

	static void solve(final String resourceName) throws IOException {
		InputStream inputStream = ReverseWords.class.getResourceAsStream(resourceName);
		InputStreamReader inputStreamReader = null;
		LineNumberReader lineReader = null;

		try {
			lineReader = new LineNumberReader(inputStreamReader = new InputStreamReader(inputStream));

			final int useCases = Integer.parseInt(lineReader.readLine());

//			System.out.println(String.format("Found %d use cases", useCases));

			for (int k = 0; k < useCases; k++) {
				final List<String> words = Arrays.asList(lineReader.readLine().split(" "));

//				System.out.println("Words: " + words);

				// Reuse existing logic from JDK
				Collections.reverse(words);

				System.out.println(String.format("Case #%d: %s", k, join(words)));
			}
		} finally {
			if (lineReader != null) {
				lineReader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
		}
	}
}