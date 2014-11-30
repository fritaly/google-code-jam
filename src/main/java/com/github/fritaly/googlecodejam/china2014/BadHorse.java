package com.github.fritaly.googlecodejam.china2014;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class BadHorse extends AbstractPuzzler {

	private static class Pair {
		final String first, second;

		public Pair(String first, String second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Pair) {
				final Pair other = (Pair) obj;

				return (this.first.equals(other.first) && this.second.equals(other.second))
						|| (this.first.equals(other.second) && this.second.equals(other.first));
			}

			return false;
		}

		@Override
		public String toString() {
			return String.format("[%s, %s]", first, second);
		}
	}

	private class Group {

		final Set<String> members = new TreeSet<>();

		final String name;

		Group(String name) {
			this.name = name;
		}

		Group(Group other) {
			this.name = other.name;
			this.members.addAll(other.members);
		}

		void addMember(String member) {
			members.add(member);

			log("Added " + member + " to group " + name);
		}

		boolean canHost(String candidate, List<Pair> incompatiblePairs) {
			for (String member : members) {
				if (incompatiblePairs.contains(new Pair(member, candidate))) {
					return false;
				}
			}

			return true;
		}

		@Override
		public String toString() {
			return String.format("Group %s: %s", name, members);
		}
	}

	public static void main(String[] args) throws Exception {
		new BadHorse().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected List<String> getInputs() {
		// The input files don't follow the usual naming conventions
		return Arrays.asList("BadHorse-sample.in", "BadHorse-small-practice-1.in", "BadHorse-small-practice-2.in");
	}

	private boolean arrange(List<Pair> incompatiblePairs, Group group1, Group group2, List<Pair> remainingPairs) {
		if (remainingPairs.isEmpty()) {
			log(group1);
			log(group2);

			return true;
		}

		final Pair pair = remainingPairs.get(0);

		final String player1 = pair.first;
		final String player2 = pair.second;

		if (group1.canHost(player1, incompatiblePairs)) {
			if (group2.canHost(player2, incompatiblePairs)) {
				final Group g1 = new Group(group1);
				g1.addMember(player1);

				final Group g2 = new Group(group2);
				g2.addMember(player2);

				final List<Pair> list = new ArrayList<>(remainingPairs);
				list.remove(0);

				final boolean result = arrange(incompatiblePairs, g1, g2, list);

				if (result) {
					return true;
				}
			}
		}
		if (group2.canHost(player1, incompatiblePairs)) {
			if (group1.canHost(player2, incompatiblePairs)) {
				final Group g1 = new Group(group1);
				g1.addMember(player2);

				final Group g2 = new Group(group2);
				g2.addMember(player1);

				final List<Pair> list = new ArrayList<>(remainingPairs);
				list.remove(0);

				final boolean result = arrange(incompatiblePairs, g1, g2, list);

				if (result) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int count = scanner.nextInt();

		final List<Pair> incompatiblePairs = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			incompatiblePairs.add(new Pair(scanner.next(), scanner.next()));
		}

		log("Incompatible pairs: " + incompatiblePairs);

		final boolean solution = arrange(Collections.unmodifiableList(incompatiblePairs), new Group("A"), new Group("B"),
				Collections.unmodifiableList(incompatiblePairs));

		return solution ? "Yes" : "No";
	}
}