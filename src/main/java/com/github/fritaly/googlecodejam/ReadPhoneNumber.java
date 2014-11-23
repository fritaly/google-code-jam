package com.github.fritaly.googlecodejam;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadPhoneNumber extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new ReadPhoneNumber().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private String getMultiple(int count) {
		switch(count) {
		case 2: return "double";
		case 3: return "triple";
		case 4: return "quadruple";
		case 5: return "quintuple";
		case 6: return "sextuple";
		case 7: return "septuple";
		case 8: return "octuple";
		case 9: return "nonuple";
		case 10: return "decuple";
		default:
			throw new IllegalArgumentException();
		}
	}

	private String toText(int count, char c) {
		if (count == 1) {
			return toText(c);
		} else if (count > 10) {
			final StringBuilder builder = new StringBuilder();

			for (int i = 0; i < count; i++) {
				if (builder.length() > 0) {
					builder.append(" ");
				}

				builder.append(toText(c));
			}

			return builder.toString();
		}

		return String.format("%s %s", getMultiple(count), toText(c));
	}

	private String toText(char c) {
		switch (c) {
		case '0':
			return "zero";
		case '1':
			return "one";
		case '2':
			return "two";
		case '3':
			return "three";
		case '4':
			return "four";
		case '5':
			return "five";
		case '6':
			return "six";
		case '7':
			return "seven";
		case '8':
			return "eight";
		case '9':
			return "nine";
		default:
			throw new IllegalArgumentException();
		}
	}

	private List<String> split(String text, String format) {
		final List<String> result = new ArrayList<>();

		final String[] groups = format.split("-");

		int startIndex = 0;

		for (int i = 0; i < groups.length; i++) {
			final int length = Integer.parseInt(groups[i]);

			result.add(text.substring(startIndex, startIndex+length));

			startIndex += length;
		}

		return result;
	}

	private String toWords(String text) throws IOException {
		PushbackReader reader = new PushbackReader(new StringReader(text));

		final StringBuilder builder = new StringBuilder();

		int character = 0;
		int count = 0;

		while ((character = reader.read()) != -1) {
			count++;

			while (true) {
				// Check the next character
				final int nextCharacter = reader.read();

				if (nextCharacter == -1) {
					break;
				} else if (nextCharacter == character) {
					count++;
				} else {
					reader.unread(nextCharacter);
					break;
				}
			}

			if (builder.length() > 0) {
				builder.append(" ");
			}

			builder.append(toText(count, (char) character));

			count = 0;
		}

		return builder.toString();
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final String phoneNumber = scanner.next();
		final String format = scanner.next();

		log(phoneNumber + " " + format);

		final List<String> groups = split(phoneNumber, format);

		log("Groups: " + groups);

		final StringBuilder builder = new StringBuilder();

		for (String group : groups) {
			if (builder.length() > 0) {
				builder.append(" ");
			}

			builder.append(toWords(group));
		}

		return builder.toString();
	}
}