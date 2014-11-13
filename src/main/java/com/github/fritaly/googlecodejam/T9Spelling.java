package com.github.fritaly.googlecodejam;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class T9Spelling {

	public static void main(String[] args) throws Exception {
		solve("T9Spelling-sample.in");

		System.out.println();

		solve("T9Spelling-small-practice.in");

		System.out.println();

		solve("T9Spelling-large-practice.in");
	}

	public static String char2T9(char c) {
		switch(c) {
		case 'a': return "2";
		case 'b': return "22";
		case 'c': return "222";
		case 'd': return "3";
		case 'e': return "33";
		case 'f': return "333";
		case 'g': return "4";
		case 'h': return "44";
		case 'i': return "444";
		case 'j': return "5";
		case 'k': return "55";
		case 'l': return "555";
		case 'm': return "6";
		case 'n': return "66";
		case 'o': return "666";
		case 'p': return "7";
		case 'q': return "77";
		case 'r': return "777";
		case 's': return "7777";
		case 't': return "8";
		case 'u': return "88";
		case 'v': return "888";
		case 'w': return "9";
		case 'x': return "99";
		case 'y': return "999";
		case 'z': return "9999";
		case ' ': return "0";
		default:
			throw new IllegalArgumentException("Unexpected character '" + c + "'");
		}
	}

	private static String transpose(String text) {
		final StringBuilder buffer = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			final String transposed = char2T9(text.charAt(i));

			if ((buffer.length() > 0) && (buffer.charAt(buffer.length() - 1) == transposed.charAt(0))) {
				// Need to insert a blank between the 2 transposed characters
				buffer.append(" ");
			}

			buffer.append(transposed);
		}

		return buffer.toString();
	}

	static void solve(final String resourceName) throws IOException {
		InputStream inputStream = T9Spelling.class.getResourceAsStream(resourceName);
		InputStreamReader inputStreamReader = null;
		LineNumberReader lineReader = null;

		try {
			lineReader = new LineNumberReader(inputStreamReader = new InputStreamReader(inputStream));

			final int useCases = Integer.parseInt(lineReader.readLine());

			for (int k = 0; k < useCases; k++) {
				System.out.println(String.format("Case #%d: %s", k + 1, transpose(lineReader.readLine())));
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