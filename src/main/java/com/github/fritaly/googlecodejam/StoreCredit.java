package com.github.fritaly.googlecodejam;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StoreCredit extends AbstractPuzzler {

	private static final boolean LOG = false;

	private static void log(String message) {
		if (LOG) {
			System.out.println(message);
		}
	}

	public static void main(String[] args) throws Exception {
		new StoreCredit().run();
	}

	private static final class IndexedPrice {
		private final int price, index;

		IndexedPrice(int price, int index) {
			this.price = price;
			this.index = index;
		}
	}

	private static IndexedPrice removePrice(Collection<IndexedPrice> prices, int price) {
		for (Iterator<IndexedPrice> iter = prices.iterator(); iter.hasNext();) {
			final IndexedPrice indexedPrice =  iter.next();

			if (indexedPrice.price == price) {
				iter.remove();

				return indexedPrice;
			}
		}

		return null;
	}

	@Override
	protected String solve(LineNumberReader lineReader) throws Exception {
		// C: the amount of credit you have at the store
		final int amount = Integer.parseInt(lineReader.readLine());

		// I: the number of items in the store
		final int itemCount = Integer.parseInt(lineReader.readLine());

		// Unordered list of item prices
		final List<Integer> itemPrices = new ArrayList<>();

		for (String string : lineReader.readLine().split(" ")) {
			itemPrices.add(Integer.parseInt(string));
		}

		// Store the indices for each price (the price can be non-unique !)
		final List<IndexedPrice> indexedPrices = new ArrayList<>();

		for (int i = 0; i < itemPrices.size(); i++) {
			indexedPrices.add(new IndexedPrice(itemPrices.get(i), i));
		}

		log("C: " + amount);
		log("I: " + itemCount);
		log("P: " + itemPrices);

		// Order the prices
		final List<Integer> orderedPrices = new ArrayList<>(itemPrices);

		Collections.sort(orderedPrices);

		log("P (ordered): " + orderedPrices);

		int tries = 0;
		String solution = null;

		for (int a = 0; (a < itemCount - 1) && (solution == null); a++) {
			final int itemPriceA = orderedPrices.get(a);

			if (itemPriceA > amount) {
				// No need to keep on looping
				tries++;
				break;
			}

			for (int b = a + 1; b < itemCount; b++) {
				final int itemPriceB = orderedPrices.get(b);

				tries++;

				if (itemPriceB > amount) {
					// No need to keep on looping
					break;
				}

				final int total = itemPriceA + itemPriceB;

				if (total == amount) {
					// Determine the index of the item prices in the initial unordered list
					// Ensure not to return the same index twice (prices aren't
					// necessarily unique)
					final int indexA = removePrice(indexedPrices, itemPriceA).index;
					final int indexB = removePrice(indexedPrices, itemPriceB).index;

					// Double check the result is ok
					if (itemPrices.get(indexA) + itemPrices.get(indexB) != amount) {
						throw new RuntimeException("Try again !");
					}

					// Return (1-based) indices
					// The lower index should come first
					solution = String.format("%s %s", Math.min(indexA, indexB) + 1, Math.max(indexA, indexB) + 1);
					break;
				}
				if (total > amount) {
					// No need to keep on looping
					break;
				}
			}
		}

		log("Solution " + solution + " found in " + tries + " tries");

		return solution;
	}
}