package com.github.fritaly.googlecodejam;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Rotate extends AbstractPuzzler {

	private final class Table {

		final char[][] cells;

		public Table(int size) {
			cells = new char[size][size];

			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					cells[x][y] = ' ';
				}
			}
		}

		@Override
		public String toString() {
			final StringBuilder buffer = new StringBuilder();

			for (int y = size() - 1; y >= 0; y--) {
				for (int x = 0; x < cells.length; x++) {
					buffer.append(cells[x][y]);
				}

				buffer.append("\n");
			}

			return buffer.toString();
		}

		int size() {
			return cells.length;
		}

		Table rotate() {
			final Table result = new Table(size());

			for (int x = 0; x < size(); x++) {
				for (int y = 0; y < size(); y++) {
					result.cells[x][size() - 1 - y] = cells[y][x];
				}
			}

			return result;
		}

		void shift(int x, int y) {
			for (int i = y; i < size() - 1; i++) {
				cells[x][i] = cells[x][i+1];
			}

			cells[x][size() - 1] = '.';
		}

		boolean hasLetterAbove(int x, int y) {
			for (int i = y + 1; i < size(); i++) {
				if (cells[x][i] != '.') {
					return true;
				}
			}

			return false;
		}

		Table gravity() {
			final Table result = new Table(size());

			// First clone the table
			for (int x = 0; x < size(); x++) {
				for (int y = 0; y < size(); y++) {
					result.cells[x][y] = cells[x][y];
				}
			}

			// Then let the letters fall by gravity
			for (int x = 0; x < size(); x++) {
				for (int y = 0; y < size(); y++) {
					while (result.hasLetterAbove(x, y) && result.cells[x][y] == '.') {
						result.shift(x, y);
					}
				}
			}

			return result;
		}

		String repeat(char c, int times) {
			final StringBuilder buffer = new StringBuilder();

			for (int i = 0; i < times; i++) {
				buffer.append(c);
			}

			return buffer.toString();
		}

		Set<String> findWinners(int n) {
			final Set<String> winners = new TreeSet<>();
			final String red = repeat('R', n);
			final String blue = repeat('B', n);

			final List<String> sequences = new ArrayList<>();

			final StringBuilder buffer = new StringBuilder();

			// Check the rows
			for (int y = 0; y < size(); y++) {
				buffer.setLength(0);

				for (int x = 0; x < size(); x++) {
					buffer.append(cells[x][y]);
				}

				sequences.add(buffer.toString());
			}

			// Check the columns
			for (int x = 0; x < size(); x++) {
				buffer.setLength(0);

				for (int y = 0; y < size(); y++) {
					buffer.append(cells[x][y]);
				}

				sequences.add(buffer.toString());
			}

			// Check the diagonals
			for (int j = -size(); j < size(); j++) {
				buffer.setLength(0);

				for (int x = -size(); x < size(); x++) {
					// Diagonal x = y+j
					if ((0 <= x) && (x < size())) {
						if ((0 <= x+j) && (x+j < size())) {
							buffer.append(cells[x][x+j]);
						}
					}
				}

				log("Diagonal: " + buffer.toString());

				sequences.add(buffer.toString());
			}
			for (int j = 0; j < 2 * size(); j++) {
				buffer.setLength(0);

				for (int x = -size(); x < size(); x++) {
					// Diagonal x = -y+j
					if ((0 <= x) && (x < size())) {
						if ((0 <= -x+j) && (-x+j < size())) {
							buffer.append(cells[x][-x+j]);
						}
					}
				}

				log("Diagonal: " + buffer.toString());

				sequences.add(buffer.toString());
			}


			for (String sequence : sequences) {
				if (sequence.contains(red)) {
					winners.add("Red");
				}
				if (sequence.contains(blue)) {
					winners.add("Blue");
				}
			}

			return winners;
		}
	}

	public static void main(String[] args) throws Exception {
		new Rotate().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}


	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String data = reader.readLine();

		final int tableSize = Integer.parseInt(data.split(" ")[0]);
		final int min = Integer.parseInt(data.split(" ")[1]);

		final Table table = new Table(tableSize);

		// x: column, y: row
		for (int y = tableSize - 1; y >= 0; y--) {
			final String line = reader.readLine();

			for (int x = 0; x < tableSize; x++) {
				final char c = line.charAt(x);

				table.cells[x][y] = c;
			}
		}

		log(table.toString());
		log("");
		log(table.rotate().toString());
		log("");
		log(table.rotate().gravity().toString());
		log("");
		log(table.rotate().gravity().findWinners(min).toString());

		final Set<String> winners = table.rotate().gravity().findWinners(min);

		if (winners.isEmpty()) {
			return "Neither";
		}
		if (winners.size() == 2) {
			return "Both";
		}
		if (winners.size() == 1) {
			return winners.iterator().next();
		}

		return null;
	}
}