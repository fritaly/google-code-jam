package com.github.fritaly.googlecodejam.y2014;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class ChargingChaos extends AbstractPuzzler {

	private static class Flow implements Comparable<Flow> {
		private final String data;

		public Flow(String data) {
			this.data = data;
		}

		@Override
		public String toString() {
			return data;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Flow) {
				final Flow flow = (Flow) obj;

				return this.data.equals(flow.data);
			}

			return false;
		}

		@Override
		public int compareTo(Flow other) {
			return this.data.compareTo(other.data);
		}

		Flow flip(int n) {
			// n is 1-based
			final char c = data.charAt(n - 1);

			return new Flow(data.substring(0, n-1) + (c == '0' ? '1' : '0') + data.substring(n));
		}
	}

	private static List<Flow> flip(List<Flow> list, int n) {
		final List<Flow> result = new ArrayList<>();

		for (Flow flow : list) {
			result.add(flow.flip(n));
		}

		return result;
	}

	private static boolean match(List<Flow> actual, List<Flow> expected) {
		// We don't want to alter the input lists
		final List<Flow> list1 = new ArrayList<>(actual);
		final List<Flow> list2 = new ArrayList<>(expected);

		Collections.sort(list1);
		Collections.sort(list2);

		for (int i = 0; i < list1.size(); i++) {
			if (!list1.get(i).equals(list2.get(i))) {
				return false;
			}
		}

		return true;
	}

	private static boolean matchPartially(List<Flow> actual, List<Flow> expected, int n) {
		// We don't want to alter the input lists
		final List<Flow> list1 = new ArrayList<>(actual);
		final List<Flow> list2 = new ArrayList<>(expected);

		Collections.sort(list1);
		Collections.sort(list2);

		for (int i = 0; i < list1.size(); i++) {
			// Only match on the n first bits
			final String start1 = list1.get(i).data.substring(0, n);
			final String start2 = list2.get(i).data.substring(0, n);

			if (!start1.equals(start2)) {
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) throws Exception {
		new ChargingChaos().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private int search(List<Flow> initialFlows, List<Flow> expectedFlows, int n) {
		if (match(initialFlows, expectedFlows)) {
			return 0;
		}

		if (!matchPartially(initialFlows, expectedFlows, n-1)) {
			return -1;
		}

		final int result1 = search(initialFlows, expectedFlows, n+1);

		if (result1 != -1) {
			return result1;
		}

		final int result2 = search(flip(initialFlows, n), expectedFlows, n+1);

		if (result2 != -1) {
			return result2 + 1;
		}

		return -1;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfDevices = scanner.nextInt();
		final int flowLength = scanner.nextInt();

		final List<Flow> initialFlows = new ArrayList<>();

		for (int i = 0; i < numberOfDevices; i++) {
			initialFlows.add(new Flow(scanner.next()));
		}

		log("Initial flows: " + initialFlows);

		final List<Flow> expectedFlows = new ArrayList<>();

		for (int i = 0; i < numberOfDevices; i++) {
			expectedFlows.add(new Flow(scanner.next()));
		}

		log("Expected flows: " + expectedFlows);

		final int result = search(initialFlows, expectedFlows, 1);

		return (result == -1) ? "NOT POSSIBLE" : Integer.toString(result);
	}
}