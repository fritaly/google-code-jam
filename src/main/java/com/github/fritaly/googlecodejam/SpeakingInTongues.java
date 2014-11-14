package com.github.fritaly.googlecodejam;

import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;

public class SpeakingInTongues extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new SpeakingInTongues().run();
	}

	private static final String PLAIN = "our language is impossible to understand " +
			"there are twenty six factorial possibilities " +
			"so it is okay if you want to just give up";

	private static final String CIPHERED = "ejp mysljylc kd kxveddknmc re jsicpdrysi " +
			"rbcpc ypc rtcsra dkh wyfrepkym veddknkmkrkcd " +
			"de kr kd eoya kw aej tysr re ujdr lkgc jv";

	private char decipher(char c) {
		// The characters q & z are mapped to each other and missing from the
		// sample message
		if (c == 'q') {
			return 'z';
		} else if (c == 'z') {
			return 'q';
		}

		return PLAIN.charAt(CIPHERED.indexOf(c));
	}

	private String decipher(String string) {
		final StringBuilder buffer = new StringBuilder();

		for (int i = 0; i < string.length(); i++) {
			buffer.append(decipher(string.charAt(i)));
		}

		return buffer.toString();
	}

	@Override
	protected List<String> getInputs() {
		// No large input for this puzzler
		return Arrays.asList(String.format("%s-sample.in", getName()), String.format("%s-small-practice.in", getName()));
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		return decipher(reader.readLine());
	}
}