package com.github.fritaly.googlecodejam.y2010;

import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class HotDogProliferation extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new HotDogProliferation().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private boolean isOver(Collection<AtomicInteger> collection) {
		for (AtomicInteger integer : collection) {
			if (integer.get() >= 2) {
				return false;
			}
		}

		return true;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfCorners = scanner.nextInt();

		final Map<Integer, AtomicInteger> vendorsPerCorner = new TreeMap<>();

		for (int i = 0; i < numberOfCorners; i++) {
			final int corner = scanner.nextInt();
			final int numberOfVendors = scanner.nextInt();

			vendorsPerCorner.put(Integer.valueOf(corner), new AtomicInteger(numberOfVendors));
		}

		int n = 0;

		while (!isOver(vendorsPerCorner.values())) {
			for (Map.Entry<Integer, AtomicInteger> entry : vendorsPerCorner.entrySet()) {
				if (entry.getValue().get() > 1) {
					final int corner = entry.getKey();
					final int previousCorner = corner - 1;
					final int nextCorner = corner + 1;

					if (!vendorsPerCorner.containsKey(previousCorner)) {
						vendorsPerCorner.put(Integer.valueOf(previousCorner), new AtomicInteger());
					}
					if (!vendorsPerCorner.containsKey(nextCorner)) {
						vendorsPerCorner.put(Integer.valueOf(nextCorner), new AtomicInteger());
					}

					vendorsPerCorner.get(previousCorner).incrementAndGet();
					vendorsPerCorner.get(nextCorner).incrementAndGet();
					vendorsPerCorner.get(corner).addAndGet(-2);
					break;
				}
			}

			n++;
		}

		return Integer.toString(n);
	}
}