package com.github.fritaly.googlecodejam.y2010;

import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class Bacteria extends AbstractPuzzler {

	private static class Cell {

		final int x, y;

		final Grid grid;

		boolean bacteria;

		boolean nextBacteria;

		public Cell(Grid grid, int x, int y) {
			this.grid = grid;
			this.x = x;
			this.y = y;
		}

		Cell getNorth() {
			return grid.getCell(x, y - 1);
		}

		Cell getWest() {
			return grid.getCell(x - 1, y);
		}

		void update() {
			this.bacteria = this.nextBacteria;
			this.nextBacteria = false;
		}
	}

	private class Grid {

		private static final int WIDTH = 100;

		final Cell[][] cells;

		public Grid(Scanner scanner) {
			this.cells = new Cell[WIDTH][WIDTH];

			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < WIDTH; y++) {
					this.cells[x][y] = new Cell(this, x, y);
				}
			}

			final int numberOfRectangles = scanner.nextInt();

			for (int i = 0; i < numberOfRectangles; i++) {
				final int x1 = scanner.nextInt();
				final int y1 = scanner.nextInt();
				final int x2 = scanner.nextInt();
				final int y2 = scanner.nextInt();

				log(String.format("[%d,%d] -> [%d,%d]", x1, y1, x2, y2));

				for (int x = x1; x <= x2; x++) {
					for (int y = y1; y <= y2; y++) {
						// The coordinates are 1-based
						getCell(x - 1, y - 1).bacteria = true;
					}
				}
			}
		}

		void run() {
			// First compute the next state for all cells
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < WIDTH; y++) {
					final Cell cell = getCell(x, y);

					if (cell.bacteria) {
						// If a bacterium has no neighbor to its north and no
						// neighbor to its west, then it will die.
						if (cell.getNorth() != null) {
							if (cell.getNorth().bacteria) {
								cell.nextBacteria = true;
								continue;
							}
						}
						if (cell.getWest() != null) {
							if (cell.getWest().bacteria) {
								cell.nextBacteria = true;
								continue;
							}
						}

						cell.nextBacteria = false;
					} else {
						// If a cell has no bacterium in it, but there are
						// bacteria in the neighboring cells to the north and to
						// the west, then a new bacterium will be born in that
						// cell.
						if ((cell.getNorth() != null) && cell.getNorth().bacteria) {
							if ((cell.getWest() != null) && cell.getWest().bacteria) {
								cell.nextBacteria = true;
							}
						}
					}
				}
			}

			// Then update the cells' state
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < WIDTH; y++) {
					getCell(x, y).update();
				}
			}
		}

		boolean isEmpty() {
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < WIDTH; y++) {
					if (getCell(x, y).bacteria) {
						return false;
					}
				}
			}

			return true;
		}

		boolean exist(int x, int y) {
			return ((0 <= x) && (x < WIDTH)) && ((0 <= y) && (y < WIDTH));
		}

		Cell getCell(int x, int y) {
			return exist(x, y) ? cells[x][y] : null;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			for (int y = 0; y < WIDTH; y++) {
				for (int x = 0; x < WIDTH; x++) {
					builder.append(getCell(x, y).bacteria ? "1" : "0").append(" ");
				}

				builder.append("\n");
			}

			return builder.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		new Bacteria().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final Grid grid = new Grid(scanner);

		int n = 0;

		while (!grid.isEmpty()) {
			// log(grid);

			grid.run();

			n++;
		}

		return Integer.toString(n);
	}
}