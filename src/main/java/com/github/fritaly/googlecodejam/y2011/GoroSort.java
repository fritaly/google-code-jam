package com.github.fritaly.googlecodejam.y2011;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class GoroSort extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new GoroSort().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfInteger = scanner.nextInt();

		final List<Integer> array = new ArrayList<>();

		for (int i = 0; i < numberOfInteger; i++) {
			array.add(Integer.valueOf(scanner.nextInt()));
		}

		log("Array: " + array);

		// What should be the final result ?
		final List<Integer> sortedArray = new ArrayList<>(array);

		Collections.sort(sortedArray);

		log("Sorted array: " + sortedArray);

		// How many integers are to be swapped ?
		int count = 0;

		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) != sortedArray.get(i)) {
				count++;
			}
		}

		log(count + " integers to swap");

		return String.format("%.6f", (float) count);
	}
}