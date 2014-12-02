package com.github.fritaly.googlecodejam.y2011;

import java.io.LineNumberReader;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class IrregularExpressions extends AbstractPuzzler {

	private static boolean isVowel(Character c) {
		return "AEIOU".contains(Character.toString(c).toUpperCase());
	}

	private static int countVowels(String s) {
		int count = 0;

		for (int i = 0; i < s.length(); i++) {
			if (isVowel(s.charAt(i))) {
				count++;
			}
		}

		return count;
	}

	private static boolean isSyllable(String s) {
		// Ex: "ab", "ra", "cad", "o" and "shabbr"
		return (countVowels(s) == 1);
	}

	private boolean isWord(String s, boolean singleSyllableAllowed) {
		// Each word consists of one or more syllables
		if (singleSyllableAllowed && isSyllable(s)) {
			log("Single syllable word: " + s);

			return true;
		}

		for (int i = 0; i < s.length() - 1; i++) {
			if (isSyllable(s.substring(0, i + 1))) {
				if (isWord(s.substring(i + 1), true)) {
					log("Multi-syllable word: " + s);

					return true;
				}
			}
		}

		return false;
	}

	private boolean isSpell(String s) {
		for (int i = 1; i < s.length() / 2; i++) {
			final String start = s.substring(0, i + 1);

			if (s.startsWith(start) && s.endsWith(start) && isWord(start, false)) {
				final String middle = s.substring(start.length(), s.length() - start.length());

				if (isSyllable(middle)) {
					log(String.format("Spell: %s %s %s", start, middle, start));

					return true;
				}
			}
		}

		return false;
	}

	private boolean isHiddenSpell(String s) {
		for (int i = 0; i < s.length(); i++) {
			for (int j = i; j < s.length(); j++) {
				if (isSpell(s.substring(i, j + 1))) {
					return true;
				}
			}
		}

		return false;
	}

	public static void main(String[] args) throws Exception {
		new IrregularExpressions().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		// Skip the number of ropes, we don't need it
		final String text = reader.readLine();

		log("Text: " + text);

		return isHiddenSpell(text) ? "Spell!" : "Nothing.";
	}
}