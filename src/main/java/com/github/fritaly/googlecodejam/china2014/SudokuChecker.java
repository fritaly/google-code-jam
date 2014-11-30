package com.github.fritaly.googlecodejam.china2014;

import java.util.Collections;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class SudokuChecker extends AbstractPuzzler {

	private class Grid {
		final int[][] cells;

		final int n;

		public Grid(Scanner scanner) {
			final int n = scanner.nextInt();

			log("N: " + n);

			this.n = n;
			this.cells = new int[n * n][n * n];

			for (int x = 0; x < cells.length; x++) {
				for (int y = 0; y < cells.length; y++) {
					this.cells[x][y] = scanner.nextInt();
				}
			}
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			int maxLength = 0;

			for (int x = 0; x < cells.length; x++) {
				for (int y = 0; y < cells.length; y++) {
					final int length = Integer.toString(this.cells[x][y]).length();

					maxLength = Math.max(maxLength, length);
				}
			}

			for (int x = 0; x < cells.length; x++) {
				for (int y = 0; y < cells.length; y++) {
					builder.append(String.format("%" + maxLength + "d", cells[x][y]));
					builder.append(" ");
				}

				builder.append("\n");
			}

			return builder.toString();
		}

		int size() {
			return cells.length;
		}

		Set<Integer> getNumbersInColumn(int x) {
			final Set<Integer> numbers = new TreeSet<>();

			for (int y = 0; y < size(); y++) {
				numbers.add(Integer.valueOf(cells[x][y]));
			}

			return numbers;
		}

		Set<Integer> getNumbersInRow(int y) {
			final Set<Integer> numbers = new TreeSet<>();

			for (int x = 0; x < size(); x++) {
				numbers.add(Integer.valueOf(cells[x][y]));
			}

			return numbers;
		}

		Set<Integer> getExpectedNumbers() {
			final Set<Integer> numbers = new TreeSet<>();

			for (int i = 1; i <= size(); i++) {
				numbers.add(Integer.valueOf(i));
			}

			return Collections.unmodifiableSet(numbers);
		}

		Set<Integer> getNumbersInSubGrid(int xsg, int ysg) {
			final Set<Integer> numbers = new TreeSet<>();

			for (int x = xsg * n ; x < (xsg + 1) * n; x++) {
				for (int y = ysg * n; y < (ysg + 1) * n; y++) {
					numbers.add(Integer.valueOf(cells[x][y]));
				}
			}

			return numbers;
		}

		boolean isValid() {
			// What are the expected numbers ?
			final Set<Integer> expectedNumbers = getExpectedNumbers();

			// Check the columns
			for (int x = 0; x < size(); x++) {
				final Set<Integer> numbers = getNumbersInColumn(x);

				if (numbers.size() != expectedNumbers.size()) {
					return false;
				}

				numbers.removeAll(expectedNumbers);

				if (!numbers.isEmpty()) {
					return false;
				}
			}

			// Check the rows
			for (int y = 0; y < size(); y++) {
				final Set<Integer> numbers = getNumbersInRow(y);

				if (numbers.size() != expectedNumbers.size()) {
					return false;
				}

				numbers.removeAll(expectedNumbers);

				if (!numbers.isEmpty()) {
					return false;
				}
			}

			// Check the sub-grids
			for (int xsg = 0; xsg < n; xsg++) {
				for (int ysg = 0; ysg < n; ysg++) {
					final Set<Integer> numbers = getNumbersInSubGrid(xsg, ysg);

					if (numbers.size() != expectedNumbers.size()) {
						return false;
					}

					numbers.removeAll(expectedNumbers);

					if (!numbers.isEmpty()) {
						return false;
					}
				}
			}

			return true;
		}
	}

	public static void main(String[] args) throws Exception {
		new SudokuChecker().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final Grid grid = new Grid(scanner);

		log(grid);

		return grid.isValid() ? "Yes" : "No";
	}
}