package com.github.fritaly.googlecodejam.apac;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class Super2048 extends AbstractPuzzler {

	private static enum Direction {
		UP, LEFT, RIGHT, DOWN;

		public static Direction getDirection(String value) {
			for (Direction direction : values()) {
				if (direction.name().equalsIgnoreCase(value)) {
					return direction;
				}
			}

			return null;
		}
	}

	private static class Grid {
		private final String[][] cells;

		Grid(int size) {
			this.cells = new String[size][size];
		}

		int getWidth() {
			int width = 0;

			for (int i = 0; i < cells.length; i++) {
				for (int j = 0; j < cells.length; j++) {
					width = Math.max(width, cells[i][j].length());
				}
			}

			return width;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			for (int i = 0; i < cells.length; i++) {
				if (builder.length() > 0) {
					builder.append("\n");
				}

				for (int j = 0; j < cells.length; j++) {
					builder.append(String.format("%-" + getWidth() + "s ", cells[i][j]));
				}
			}

			return builder.toString();
		}

		public void move(Direction direction) {
			switch (direction) {
			case LEFT:
			case RIGHT: {
				final boolean right = Direction.RIGHT.equals(direction);

				final LinkedList<String> temp = new LinkedList<>();

				for (int row = 0; row < cells.length; row++) {
					temp.clear();

					for (int i = 0; i < cells[row].length; i++) {
						if (!"0".equals(cells[row][i])) {
							// Remove all the zeros
							temp.add(cells[row][i]);
						}
					}

					if (right) {
						// Reverse the list to remove the elements from the head
						Collections.reverse(temp);
					}

					// Merge the adjacent cells 2 by 2
					final LinkedList<String> outcome = new LinkedList<>();

					while (!temp.isEmpty()) {
						final String first = temp.removeFirst();

						if (temp.isEmpty()) {
							outcome.add(first);
							break;
						}

						final String second = temp.getFirst();

						if (first.equals(second)) {
							// Merge the 2 numbers
							outcome.add(Integer.toString(Integer.parseInt(first) + Integer.parseInt(second)));

							temp.removeFirst();
						} else {
							outcome.add(first);
						}
					}

					// Pad the list with zeroes
					while (outcome.size() < cells.length) {
						outcome.add("0");
					}

					if (right) {
						Collections.reverse(outcome);
					}

					for (int i = 0; i < cells[row].length; i++) {
						cells[row][i] = outcome.get(i);
					}
				}
			}
				break;
			case UP:
			case DOWN: {
				final boolean down = Direction.DOWN.equals(direction);

				final LinkedList<String> temp = new LinkedList<>();

				for (int column = 0; column < cells.length; column++) {
					temp.clear();

					for (int i = 0; i < cells[column].length; i++) {
						if (!"0".equals(cells[i][column])) {
							// Remove all the zeros
							temp.add(cells[i][column]);
						}
					}

					if (down) {
						// Reverse the list to remove the elements from the head
						Collections.reverse(temp);
					}

					// Merge the adjacent cells 2 by 2
					final LinkedList<String> outcome = new LinkedList<>();

					while (!temp.isEmpty()) {
						final String first = temp.removeFirst();

						if (temp.isEmpty()) {
							outcome.add(first);
							break;
						}

						final String second = temp.getFirst();

						if (first.equals(second)) {
							// Merge the 2 numbers
							outcome.add(Integer.toString(Integer.parseInt(first) + Integer.parseInt(second)));

							temp.removeFirst();
						} else {
							outcome.add(first);
						}
					}

					// Pad the list with zeroes
					while (outcome.size() < cells.length) {
						outcome.add("0");
					}

					if (down) {
						Collections.reverse(outcome);
					}

					for (int i = 0; i < cells[column].length; i++) {
						cells[i][column] = outcome.get(i);
					}
				}
			}
				break;
			default:
				throw new UnsupportedOperationException();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new Super2048().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int size = scanner.nextInt();
		final Direction direction = Direction.getDirection(scanner.next());

		final Grid grid = new Grid(size);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				grid.cells[i][j] = scanner.next();
			}
		}

		log("--->");
		log(grid);
		log("---- " + direction.name() + " ----");

		grid.move(direction);

		log(grid);
		log("<---");

		return "\n" + grid.toString();
	}
}