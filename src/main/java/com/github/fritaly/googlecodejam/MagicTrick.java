package com.github.fritaly.googlecodejam;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagicTrick extends AbstractPuzzler {

	private static final class Grid {
		final int[][] cards;

		public Grid(LineNumberReader reader) throws IOException {
			this.cards = new int[4][4];

			for (int i = 0; i < 4; i++) {
				final String[] chunks = reader.readLine().split(" ");

				this.cards[i][0] = Integer.parseInt(chunks[0]);
				this.cards[i][1] = Integer.parseInt(chunks[1]);
				this.cards[i][2] = Integer.parseInt(chunks[2]);
				this.cards[i][3] = Integer.parseInt(chunks[3]);
			}
		}

		List<Integer> getCards(int row) {
			final List<Integer> result = new ArrayList<>();

			final int[] array = cards[row];

			for (int i = 0; i < array.length; i++) {
				result.add(Integer.valueOf(array[i]));
			}

			return result;
		}

		@Override
		public String toString() {
			final StringBuilder buffer = new StringBuilder();

			for (int i = 0; i < cards.length; i++) {
				for (int j = 0; j < cards.length; j++) {
					buffer.append(String.format("%02d ", cards[i][j]));
				}

				buffer.append("\n");
			}

			return buffer.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		new MagicTrick().run();
	}

	protected List<String> getInputs() {
		// No large input available here
		return Arrays.asList(String.format("%s-sample.in", getName()), String.format("%s-small-practice.in", getName()));
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final int firstAnswer = Integer.parseInt(reader.readLine());

		final Grid grid1 = new Grid(reader);

		final int secondAnswer = Integer.parseInt(reader.readLine());

		final Grid grid2 = new Grid(reader);

		log(grid1);
		log(grid2);
		log("Answer #1: " + firstAnswer);
		log("Answer #2: " + secondAnswer);

		// What are the 4 possible cards from the first arrangement ?
		final List<Integer> firstCards = grid1.getCards(firstAnswer - 1);

		log("Possible cards #1: " + firstCards);

		// and the 4 possible cards from the 2nd arrangement ?
		final List<Integer> secondCards = grid2.getCards(secondAnswer - 1);

		log("Possible cards #2: " + secondCards);

		// How many common cards among the 2 lists ?
		final List<Integer> commonCards = new ArrayList<>(firstCards);
		commonCards.retainAll(secondCards);

		if (commonCards.isEmpty()) {
			// The volunteer cheated
			return "Volunteer cheated!";
		}
		if (commonCards.size() > 1) {
			return "Bad magician!";
		}

		return Integer.toString(commonCards.iterator().next());
	}
}