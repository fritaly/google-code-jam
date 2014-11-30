package com.github.fritaly.googlecodejam.y2010;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class OddManOut extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new OddManOut().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfGuests = scanner.nextInt();

		log("Number of guests: " + numberOfGuests);

		final List<Integer> codes = new ArrayList<>(numberOfGuests);

		for (int i = 0; i < numberOfGuests; i++) {
			codes.add(scanner.nextInt());
		}

		Collections.sort(codes);

		final Set<Integer> set1 = new TreeSet<>();
		final Set<Integer> set2 = new TreeSet<>();

		Set<Integer> set = set1;

		for (Integer code : codes) {
			set.add(code);

			if (set == set1) {
				set = set2;
			} else {
				set = set1;
			}
		}

		if (set1.size() > set2.size()) {
			set1.removeAll(set2);

			set = set1;
		} else {
			set2.removeAll(set1);

			set = set2;
		}

		return Integer.toString(set.iterator().next());
	}
}