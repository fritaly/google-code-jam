package com.github.fritaly.googlecodejam.y2010;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import com.github.fritaly.googlecodejam.AbstractPuzzler;
import com.github.fritaly.googlecodejam.Incomplete;

@Incomplete
public class ThemePark extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new ThemePark().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private int sum(List<Integer> integers) {
		int sum = 0;

		for (Integer integer : integers) {
			sum += integer;
		}

		return sum;
	}

	private boolean allGroupsHaveTheSameSize(List<Integer> integers) {
		final Set<Integer> set = new TreeSet<>();

		for (Integer integer : integers) {
			set.add(integer);
		}

		return (set.size() == 1);
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfRuns = scanner.nextInt();
		final int capacity = scanner.nextInt();
		final int numberOfGroups = scanner.nextInt();

		log("Runs: " + numberOfRuns);
		log("Capacity: " + capacity);

		final LinkedList<Integer> queue = new LinkedList<>();

		for (int i = 0; i < numberOfGroups; i++) {
			queue.add(scanner.nextInt());
		}

		log("Groups: " + queue);

		int money = 0;

		if (capacity >= sum(queue)) {
			// All the groups can go into the roller coaster
			return Integer.toString(numberOfRuns * sum(queue));
		} else if (allGroupsHaveTheSameSize(queue)) {
			// All the groups have the same size
			final int groupSize = queue.iterator().next();

			return Integer.toString(numberOfRuns * groupSize * Math.min(capacity / groupSize, queue.size()));
		}

		final LinkedList<Integer> rollerCoaster = new LinkedList<>();

		for (int i = 0; i < numberOfRuns; i++) {
			int remainingSeats = capacity;

			while (!queue.isEmpty() && (queue.getFirst() <= remainingSeats)) {
				final Integer group = queue.removeFirst();

				rollerCoaster.add(group);

				money += group;
				remainingSeats -= group;
			}

			log("Run: " + rollerCoaster);

			queue.addAll(rollerCoaster);
			rollerCoaster.clear();
		}

		return Integer.toString(money);
	}
}