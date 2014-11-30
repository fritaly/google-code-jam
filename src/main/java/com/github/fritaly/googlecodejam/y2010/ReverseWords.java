package com.github.fritaly.googlecodejam.y2010;

import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class ReverseWords extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new ReverseWords().run();
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

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final List<String> words = Arrays.asList(reader.readLine().split(" "));

//		System.out.println("Words: " + words);

		// Reuse existing logic from JDK
		Collections.reverse(words);

		return join(words);
	}
}