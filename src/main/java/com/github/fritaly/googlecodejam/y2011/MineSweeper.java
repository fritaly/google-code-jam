package com.github.fritaly.googlecodejam.y2011;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;
import com.github.fritaly.googlecodejam.Incomplete;

@Incomplete
public class MineSweeper extends AbstractPuzzler {

	private class Cell {

		final Grid grid;

		final int x, y;

		char c;

		public Cell(Grid grid, int x, int y, char c) {
			this.grid = grid;
			this.x = x;
			this.y = y;
			this.c = c;
		}

		@Override
		public String toString() {
			return String.format("Cell[%d,%d]", x, y);
		}

		private void addIfNotNull(List<Cell> list, Cell cell) {
			if (cell != null) {
				list.add(cell);
			}
		}

		boolean hasMine() {
			return (this.c == '*');
		}

		void click() {
			final int mines = getNumberOfMines();

			this.c = Integer.toString(mines).charAt(0);

			if (mines == 0) {
				for (Cell neighbor : getNeighbors()) {
					if (neighbor.c == '.') {
						neighbor.click();
					}
				}
			}

			log("Clicked " + this);
		}

		int getNumberOfMines() {
			List<Cell> neighbors = getNeighbors();

			int mines = 0;

			for (Cell neighbor : neighbors) {
				if (neighbor.hasMine()) {
					mines++;
				}
			}

			return mines;
		}

		List<Cell> cachedNeighbors;

		List<Cell> getNeighbors() {
			if (cachedNeighbors == null) {
				final List<Cell> neighbors = new ArrayList<>(8);

				addIfNotNull(neighbors, grid.getCell(x - 1, y - 1));
				addIfNotNull(neighbors, grid.getCell(x, y - 1));
				addIfNotNull(neighbors, grid.getCell(x + 1, y - 1));

				addIfNotNull(neighbors, grid.getCell(x - 1, y));
				addIfNotNull(neighbors, grid.getCell(x + 1, y));

				addIfNotNull(neighbors, grid.getCell(x - 1, y + 1));
				addIfNotNull(neighbors, grid.getCell(x, y + 1));
				addIfNotNull(neighbors, grid.getCell(x + 1, y + 1));

				cachedNeighbors = Collections.unmodifiableList(neighbors);
			}

			return cachedNeighbors;
		}
	}

	private class Grid {
		final Cell[][] cells;

		final int size;

		public Grid(Scanner scanner) {
			this.size = scanner.nextInt();

			// Skip the line feed
			scanner.nextLine();

			this.cells = new Cell[size][size];

			for (int y = 0; y < size; y++) {
				final String line = scanner.nextLine();

				for (int x = 0; x < size; x++) {
					cells[x][y] = new Cell(this, x, y, line.charAt(x));
				}
			}
		}

		Cell getClickableCell() {
			// First return the cells with no mine nearby (to unveil as many
			// cells as possible)
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					final Cell cell = getCell(x, y);

					if ((cell.c == '.') && (cell.getNumberOfMines() == 0)) {
						return cell;
					}
				}
			}

			// Then return the remaining cells
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					final Cell cell = getCell(x, y);

					if (cell.c == '.') {
						return cell;
					}
				}
			}

			return null;
		}

		boolean exists(int x, int y) {
			return ((0 <= x) && (x < size)) && ((0 <= y) && (y < size));
		}

		Cell getCell(int x, int y) {
			return exists(x, y) ? cells[x][y] : null;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					builder.append(cells[x][y].c);

				}

				builder.append("\n");
			}

			builder.setLength(builder.length() - 1);

			return builder.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		new MineSweeper().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final Grid grid = new Grid(scanner);

//		log(grid + "\n");

		Cell cell = null;

		int clicks = 0;

		while ((cell = grid.getClickableCell()) != null) {
			cell.click();

			clicks++;
		}

//		log(grid + "\n");

		return Integer.toString(clicks);
	}
}