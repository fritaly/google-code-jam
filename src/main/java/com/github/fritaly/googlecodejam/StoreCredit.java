package com.github.fritaly.googlecodejam;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoreCredit {

	public static void main(String[] args) throws Exception {
		solve("StoreCredit-small-practice.in");
		solve("StoreCredit-large-practice.in");
	}

	static void solve(final String resourceName) throws IOException {
		InputStream inputStream = StoreCredit.class.getResourceAsStream(resourceName);
		InputStreamReader inputStreamReader = null;
		LineNumberReader lineReader = null;

		try {
			lineReader = new LineNumberReader(inputStreamReader = new InputStreamReader(inputStream));

			final int useCases = Integer.parseInt(lineReader.readLine());

			System.out.println(String.format("Found %d use cases", useCases));

			for (int k = 0; k < useCases; k++) {
				// C: the amount of credit you have at the store
				final int amount = Integer.parseInt(lineReader.readLine());

				// I: the number of items in the store
				final int itemCount = Integer.parseInt(lineReader.readLine());

				// Unordered list of item prices
				final List<Integer> itemPrices = new ArrayList<>();

				for (String string : lineReader.readLine().split(" ")) {
					itemPrices.add(Integer.parseInt(string));
				}

				System.out.println("C: " + amount);
				System.out.println("I: " + itemCount);
				System.out.println("P: " + itemPrices);

				// Order the prices
				final List<Integer> orderedPrices = new ArrayList<>(itemPrices);

				Collections.sort(orderedPrices);

				System.out.println("P (ordered): " + orderedPrices);

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
							// Return the (1-based) indices for the unordered input list
							final int indexA = itemPrices.indexOf(itemPriceA) + 1;
							final int indexB = itemPrices.indexOf(itemPriceB) + 1;

							// The lower index should come first
							solution = String.format("(%s, %s)", Math.min(indexA, indexB), Math.max(indexA, indexB));
							break;
						}
						if (total > amount) {
							// No need to keep on looping
							break;
						}
					}
				}

				System.out.println("Solution " + solution + " found in " + tries + " tries");

				System.out.println();
			}
		} finally {
			if (lineReader != null) {
				lineReader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
		}
	}
}