package com.github.fritaly.googlecodejam.y2009;

import java.io.LineNumberReader;

import com.github.fritaly.googlecodejam.AbstractPuzzler;
import com.github.fritaly.googlecodejam.Incomplete;

@Incomplete
public class AllYourBase extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new AllYourBase().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private String getUniqueDigits(String text) {
		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			if (!builder.toString().contains(Character.toString(text.charAt(i)))) {
				builder.append(text.charAt(i));
			}
		}

		if (builder.length() == 1) {
			// The base size can't be 1, it's at least 2, add another digit to the base
			builder.append(Character.valueOf((char) (builder.charAt(0) - 1)));
		}

		return builder.toString();
	}

	private String createBase(String chars) {
		// The first character can't denote the zero
		if (chars.length() > 2) {
			return Character.toString(chars.charAt(1)) + Character.toString(chars.charAt(0)) + chars.substring(2);
		}

		return Character.toString(chars.charAt(1)) + Character.toString(chars.charAt(0));
	}

	private long decode(String text, String base) {
		long result = 0;

		for (int i = text.length() - 1; i >= 0; i--) {
			final char c = text.charAt(i);

			result += base.indexOf(c) * Math.pow(base.length(), text.length() - 1 - i);
		}

		return result;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String data = reader.readLine();

		log("Data: " + data);
		log("Digits: " + getUniqueDigits(data));
		log("Size of base: " + getUniqueDigits(data).length());
		log("Base: " + createBase(getUniqueDigits(data)));

		return Long.toString(decode(data, createBase(getUniqueDigits(data))));
	}
}