package com.github.fritaly.googlecodejam.y2009;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.fritaly.googlecodejam.AbstractPuzzler;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

public class AlienLanguage extends AbstractPuzzler {

	private static class StringMatcher {

		private final List<CharMatcher> matchers;

		public StringMatcher(List<CharMatcher> matchers) {
			this.matchers = matchers;
		}

		public boolean matches(String string) {
			for (int i = 0; i < string.length(); i++) {
				if (!matchers.get(i).matches(string.charAt(i))) {
					return false;
				}
			}

			return true;
		}
	}

	private static abstract interface CharMatcher {

		public boolean matches(char c);
	}

	private static class MultiCharMatcher implements CharMatcher {
		private final String chars;

		public MultiCharMatcher(String chars) {
			// Ex: "(abc)"
			this.chars = chars;
		}

		@Override
		public boolean matches(char c) {
			return chars.contains(Character.toString(c));
		}
	}

	private static class SingleCharMatcher implements CharMatcher {
		private final char c;

		public SingleCharMatcher(char c) {
			// Ex: "a"
			this.c = c;
		}

		@Override
		public boolean matches(char c) {
			return (this.c == c);
		}
	}

	public static void main(String[] args) throws Exception {
		new AlienLanguage().run();
	}

	private static final Pattern PATTERN1 = Pattern.compile("^\\(([^()]+)\\)(.*)");

	private static final Pattern PATTERN2 = Pattern.compile("^([^()])(.*)");

	private List<CharMatcher> parse(String pattern) throws ParseException {
		final List<CharMatcher> matchers = new ArrayList<>();

		final Matcher matcher1 = PATTERN1.matcher(pattern);
		final Matcher matcher2 = PATTERN2.matcher(pattern);

		if (matcher1.matches()) {
			matchers.add(new MultiCharMatcher(matcher1.group(1)));

			if (!"".equals(matcher1.group(2))) {
				matchers.addAll(parse(matcher1.group(2)));
			}
		} else if (matcher2.matches()) {
			matchers.add(new SingleCharMatcher(matcher2.group(1).charAt(0)));

			if (!"".equals(matcher2.group(2))) {
				matchers.addAll(parse(matcher2.group(2)));
			}
		} else {
			throw new ParseException("Error when parsing pattern '" + pattern + "'");
		}

		return matchers;
	}

	@Override
	protected void handleInput(LineNumberReader reader) throws IOException, Exception {
		final String header = reader.readLine();

		final String[] chunks = header.split(" ");

		final int wordLength = Integer.parseInt(chunks[0]);
		final int dictionarySize = Integer.parseInt(chunks[1]);
		final int useCases = Integer.parseInt(chunks[2]);

		log("The words' length is " + wordLength);

		// Read the dictionary
		final Set<String> dictionary = new TreeSet<>();

		for (int i = 0; i < dictionarySize; i++) {
			dictionary.add(reader.readLine());
		}

		log("The dictionary contains " + dictionarySize + " words");

//		log(String.format("Found %d use cases", useCases));

		for (int k = 0; k < useCases; k++) {
			final String pattern = reader.readLine();

			log("Pattern: " + pattern);

			// Parse the pattern
			final StringMatcher stringMatcher = new StringMatcher(parse(pattern));

			// Evaluate each word in the dictionary, how many match ?
			int matches = 0;

			for (String word : dictionary) {
				if (stringMatcher.matches(word)) {
					matches++;
				}
			}

			System.out.println(String.format("Case #%d: %s", k + 1, Integer.toString(matches)));
		}
	}
}