package com.github.fritaly.googlecodejam.y2011;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class MusicCollection extends AbstractPuzzler {

	private static final class StringComparator implements Comparator<String> {
		private final Comparator<Character> comparator = new CharacterComparator();

		@Override
		public int compare(String s1, String s2) {
			if (s1.length() == s2.length()) {
				for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
					final char c1 = s1.charAt(i);
					final char c2 = s2.charAt(i);

					final int result = comparator.compare(c1, c2);

					if (result != 0) {
						return result;
					}
				}
			}

			return s1.length() - s2.length();
		}
	}

	private static final class CharacterComparator implements Comparator<Character> {
		@Override
		public int compare(Character c1, Character c2) {
			if (c1.equals(c2)) {
				return 0;
			}

			// We normalized all characters to uppercase
			final boolean letter1 = (('A' <= c1) && (c1 <= 'Z'));
			final boolean letter2 = (('A' <= c2) && (c2 <= 'Z'));

			final boolean hyphen1 = (c1 == '-');
			final boolean hyphen2 = (c2 == '-');

			final boolean space1 = (c1 == ' ');
			final boolean space2 = (c2 == ' ');

			// Order: ' ', '-' & letters

			if (letter1) {
				if (letter2) {
					return c1.compareTo(c2);
				} else if (hyphen2) {
					return +1;
				} else if (space2) {
					return +1;
				}
			} else if (hyphen1) {
				if (letter2) {
					return -1;
				} else if (hyphen2) {
					return 0;
				} else if (space2) {
					return +1;
				}
			} else if (space1) {
				if (letter2) {
					return -1;
				} else if (hyphen2) {
					return -1;
				} else if (space2) {
					return 0;
				}
			}

			throw new RuntimeException();
		}
	}

	public static void main(String[] args) throws Exception {
		new MusicCollection().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected List<String> getInputs() {
		return Arrays.asList(String.format("%s-sample.in", getClass().getSimpleName()),
				String.format("%s-small-practice-1.in", getClass().getSimpleName()),
				String.format("%s-small-practice-2.in", getClass().getSimpleName()));
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final int numberOfSongs = Integer.parseInt(reader.readLine());

		final Map<String, Set<String>> resultsByQueries = new TreeMap<>();

		final List<String> songs = new ArrayList<>();

		for (int i = 0; i < numberOfSongs; i++) {
			// Normalize the song to upper case
			final String song = reader.readLine().toUpperCase();

			for (int j = 0; j < song.length(); j++) {
				// We don't start at j+1 because of test case #5
				for (int k = j; k <= song.length(); k++) {
					final String query = song.substring(j, k);

					if (!resultsByQueries.containsKey(query)) {
						resultsByQueries.put(query, new TreeSet<String>());
					}

					resultsByQueries.get(query).add(song);
				}
			}

			songs.add(song);
		}

		log("Songs: " + songs);

		final StringBuilder builder = new StringBuilder();
		builder.append("\n");

		for (String song : songs) {
			// Find all the queries only returning the current song
			final Set<String> queries = new TreeSet<>(new StringComparator());

			for (Map.Entry<String, Set<String>> entry : resultsByQueries.entrySet()) {
				final Set<String> results = entry.getValue();

				if ((results.size() == 1) && results.contains(song)) {
					queries.add(entry.getKey());
				}
			}

			if (queries.isEmpty()) {
				builder.append(":(").append("\n");
			} else if (queries.size() == 1) {
				builder.append("\"" + queries.iterator().next() + "\"").append("\n");
			} else {
				// Return the shortest query
				builder.append("\"" + queries.iterator().next() + "\"").append("\n");
			}
		}

		builder.setLength(builder.length() - 1);

		return builder.toString();
	}
}