package com.github.fritaly.googlecodejam.y2008;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class TextMessagingOutrage extends AbstractPuzzler {

	private static class Letter {
		final int frequency;

		public Letter(int frequency) {
			this.frequency = frequency;
		}

		@Override
		public String toString() {
			return String.format("Letter[frequency=%d]", frequency);
		}
	}

	private static class Keys {

		final List<Key> keys;

		public Keys(List<Key> keys) {
			this.keys = keys;
		}

		public Key getKey(Letter letter) {
			for (Key key : keys) {
				if (key.letters.contains(letter)) {
					return key;
				}
			}

			throw new IllegalArgumentException("Unable to find key mapped to letter " + letter);
		}
	}

	private static class Key {
		final List<Letter> letters = new ArrayList<>();

		public Key() {
		}

		int getKeyPresses(Letter letter) {
			return (this.letters.indexOf(letter) + 1);
		}

		@Override
		public String toString() {
			return String.format("Key[letters=%s]", letters);
		}
	}

	public static void main(String[] args) throws Exception {
		new TextMessagingOutrage().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String[] data = reader.readLine().split(" ");

		final int maxLettersPerKey = Integer.parseInt(data[0]);
		final int numberOfKeys = Integer.parseInt(data[1]);
		final int numberOfLetters = Integer.parseInt(data[2]);

		// Create the keys
		final List<Key> keys = new ArrayList<>();

		for (int i = 0; i < numberOfKeys; i++) {
			keys.add(new Key());
		}

		final String[] frequencyData = reader.readLine().split(" ");

		final List<Letter> letters = new ArrayList<>();

		for (int i = 0; i < numberOfLetters; i++) {
			letters.add(new Letter(Integer.parseInt(frequencyData[i])));
		}

		// Sort the letters by (decreasing) frequency
		Collections.sort(letters, new Comparator<Letter>() {
			@Override
			public int compare(Letter o1, Letter o2) {
				return o2.frequency - o1.frequency;
			}
		});

		log("Letters: " + letters);

		// Loop over the letters and assign them to keys
		final LinkedList<Key> list = new LinkedList<>(keys);

		for (Letter letter : letters) {
			final Key key = list.removeFirst();

			// Assign the letter to the key
			key.letters.add(letter);

			if (list.isEmpty()) {
				// Cycle over the keys again
				list.addAll(keys);
			}
		}

		log("Keys: " + keys);

		final Keys pad = new Keys(keys);

		long presses = 0;

		for (Letter letter : letters) {
			final Key key = pad.getKey(letter);

			presses += key.getKeyPresses(letter) * letter.frequency;
		}

		return Long.toString(presses);
	}
}