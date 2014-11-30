package com.github.fritaly.googlecodejam.china2014;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class Moist extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new Moist().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected List<String> getInputs() {
		return Arrays.asList("Moist-sample.in", "Moist-small-practice-1.in", "Moist-small-practice-2.in");
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfCards = scanner.nextInt();

		scanner.nextLine();

		final List<String> cards = new ArrayList<>();

		for (int i = 0; i < numberOfCards; i++) {
			cards.add(scanner.nextLine());
		}

		log("Cards: " + cards);

		int permutations = 0;

		// Sort the cards
		for (int i = 0; i < numberOfCards - 1; i++) {
			final String card1 = cards.get(i);
			final String card2 = cards.get(i+1);

			if (card1.compareTo(card2) > 0) {
				int j = i;

				while ((j >= 0) && (cards.get(j).compareTo(cards.get(j+1)) > 0)) {
					// Swap the 2 cards
					cards.set(j+1, card1);
					cards.set(j, card2);

					j--;
				}

				permutations++;
			}
		}

		log("Cards: " + cards);

		return Integer.toString(permutations);
	}
}