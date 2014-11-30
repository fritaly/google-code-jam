package com.github.fritaly.googlecodejam.y2009;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.fritaly.googlecodejam.AbstractPuzzler;
import com.github.fritaly.googlecodejam.Incomplete;

@Incomplete
public class WelcomeToCodeJam extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new WelcomeToCodeJam().execute();
	}

	private void search(String text, String searchedString, AtomicInteger count) {
		if ("".equals(searchedString)) {
			// Match found
			count.incrementAndGet();
			return;
		}

		// Next character to search ?
		final char c = searchedString.charAt(0);

		// What are all the indexes where this character appears in the input string ?
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == c) {
				// Evaluate this possibility
				search(text.substring(i) + 1, searchedString.substring(1), count);
			}
		}
	}

	@Override
	protected boolean isLogEnabled() {
		return true;
	}

	protected void handleInput(Scanner scanner) throws IOException, Exception {
		final int useCases = scanner.nextInt();

		// This call to nextLine() is necessary
		scanner.nextLine();

//		log(String.format("Found %d use cases", useCases));

		for (int k = 0; k < useCases; k++) {
			System.out.println(String.format("Case #%d: %s", k + 1, solve(scanner)));
		}
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final String text = scanner.nextLine();

		log("Text: " + text);

		final AtomicInteger count = new AtomicInteger();

		search(text, "welcome to code jam", count);

		return String.format("%04d", count.get());
	}
}