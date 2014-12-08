package com.github.fritaly.googlecodejam.y2009;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class Watersheds extends AbstractPuzzler {

	private static class Cell {
		final Grid grid;

		final int x, y;

		final int altitude;

		Cell next;

		// Only relevant for a sink cell
		char basin = '?';

		public Cell(Grid grid, int x, int y, int altitude) {
			this.grid = grid;
			this.x = x;
			this.y = y;
			this.altitude = altitude;
		}

		Cell getSink() {
			if (isSink()) {
				return this;
			}

			return (next != null) ? next.getSink() : null;
		}

		@Override
		public String toString() {
			return String.format("[%d:%d]", x, y);
		}

		Cell getNorth() {
			return grid.getCell(x, y - 1);
		}

		Cell getSouth() {
			return grid.getCell(x, y + 1);
		}

		Cell getWest() {
			return grid.getCell(x - 1, y);
		}

		Cell getEast() {
			return grid.getCell(x + 1, y);
		}

		List<Cell> getNeighbors() {
			// Returns the cell in the following order: North, West, East, South
			final List<Cell> neighbors = new ArrayList<>();

			if (getNorth() != null) {
				neighbors.add(getNorth());
			}
			if (getWest() != null) {
				neighbors.add(getWest());
			}
			if (getEast() != null) {
				neighbors.add(getEast());
			}
			if (getSouth() != null) {
				neighbors.add(getSouth());
			}

			return neighbors;
		}

		Cell getNeighborWithLowestAltitude() {
			Cell result = null;

			for (Cell cell : getNeighbors()) {
				if ((result == null) || (cell.altitude < result.altitude)) {
					result = cell;
				}
			}

			return result;
		}

		boolean isSink() {
			for (Cell cell : getNeighbors()) {
				if (this.altitude > cell.altitude) {
					return false;
				}
			}

			return true;
		}
	}

	private class Grid {

		final Cell[][] cells;

		final int width, height;

		public Grid(Scanner scanner) {
			this.height = scanner.nextInt();
			this.width = scanner.nextInt();

			this.cells = new Cell[width][height];

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					this.cells[x][y] = new Cell(this, x, y, scanner.nextInt());
				}
			}
		}

		boolean exists(int x, int y) {
			return ((0 <= x) && (x < width)) && ((0 <= y) && (y < height));
		}

		Cell getCell(int x, int y) {
			return exists(x, y) ? cells[x][y] : null;
		}

		List<Cell> getSinks() {
			final List<Cell> list = new ArrayList<>();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (cells[x][y].isSink()) {
						list.add(cells[x][y]);
					}
				}
			}

			return list;
		}

		void computeWaterFlow() {
			boolean updated = true;

			while (updated) {
				updated = false;

				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						final Cell cell = getCell(x, y);

						Cell lowerCell = cell.getNeighborWithLowestAltitude();

						if ((lowerCell != null) && (lowerCell.altitude < cell.altitude) && (cell.next == null)) {
							cell.next = lowerCell;

							updated = true;
							break;
						}
					}
				}
			}

			// Each basin is labeled by a unique lower-case letter, in such a
			// way that, when the rows of the map are concatenated from top to
			// bottom, the resulting string is lexicographically smallest. (In
			// particular, the basin of the most North-Western cell is always
			// labeled 'a'.)
			char c = 'a';

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					final Cell cell = getCell(x, y);

					if (cell.getSink().basin == '?') {
						cell.getSink().basin = c++;
					}
				}
			}
		}

		@Override
		public String toString() {
			return render(true);
		}

		String render(boolean altitude) {
			final StringBuilder builder = new StringBuilder();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					final Cell cell = cells[x][y];

					if (altitude) {
						builder.append(cell.altitude);
					} else {
						builder.append(cell.getSink().basin);
					}

					builder.append(" ");
				}

				builder.append("\n");
			}

			builder.setLength(builder.length() - 1);

			return builder.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		new Watersheds().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final Grid grid = new Grid(scanner);

		log(grid.render(true));
		log("Sinks: " + grid.getSinks());

		grid.computeWaterFlow();

		return "\n" + grid.render(false);
	}
}