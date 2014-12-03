package com.github.fritaly.googlecodejam.y2011;

import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class SquareTiles extends AbstractPuzzler {

	private static class Grid {
		final char[][] array;

		final int width, height;

		public Grid(Scanner scanner) {
			this.height = scanner.nextInt();
			this.width = scanner.nextInt();

			// Skip the line feed
			scanner.nextLine();

			this.array = new char[width][height];

			for (int y = 0; y < height; y++) {
				final String line = scanner.nextLine();

				for (int x = 0; x < width; x++) {
					array[x][y] = line.charAt(x);
				}
			}
		}

		int getNumberOfBlueTiles() {
			int count = 0;

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (array[x][y] == '#') {
						count++;
					}
				}
			}

			return count;
		}

		void pave() {
			for (int y = 0; y < height - 1; y++) {
				for (int x = 0; x < width - 1; x++) {
					if (array[x][y] != '#') {
						continue;
					}
					if (array[x+1][y] != '#') {
						continue;
					}
					if (array[x][y+1] != '#') {
						continue;
					}
					if (array[x+1][y+1] != '#') {
						continue;
					}

					array[x][y] = '/';
					array[x+1][y] = '\\';
					array[x][y+1] = '\\';
					array[x+1][y+1] = '/';
				}
			}
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					builder.append(array[x][y]);

				}

				builder.append("\n");
			}

			builder.setLength(builder.length() - 1);

			return builder.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		new SquareTiles().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final Grid grid = new Grid(scanner);

		if (grid.getNumberOfBlueTiles() % 4 != 0) {
			return "\nImpossible";
		}

		grid.pave();

		log(grid);

		if (grid.getNumberOfBlueTiles() > 0) {
			return "\nImpossible";
		}

		return "\n" + grid.toString();
	}
}