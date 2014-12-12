package com.github.fritaly.googlecodejam.y2013;

import java.io.LineNumberReader;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class TicTacToeTomek extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new TicTacToeTomek().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final char[][] array = new char[4][4];

		final StringBuilder builder = new StringBuilder();

		for (int y = 0; y < 4; y++) {
			final String line = reader.readLine();

			for (int x = 0; x < 4; x++) {
				array[x][y] = line.charAt(x);
			}

			builder.append(line).append("\n");
		}

		if (isLogEnabled()) {
			log("\n" + builder.toString());
		}

		// Skip the blank line
		reader.readLine();

		boolean xWon = false, oWon = false;

		for (int y = 0; y < 4; y++) {
			final String line = new String(new char[] { array[0][y], array[1][y], array[2][y], array[3][y] });

			xWon = xWon || "XXXX".equals(line.replace('T', 'X'));
			oWon = oWon || "OOOO".equals(line.replace('T', 'O'));
		}

		for (int x = 0; x < 4; x++) {
			final String line = new String(new char[] { array[x][0], array[x][1], array[x][2], array[x][3] });

			xWon = xWon || "XXXX".equals(line.replace('T', 'X'));
			oWon = oWon || "OOOO".equals(line.replace('T', 'O'));
		}

		final String diagonal1 = new String(new char[] { array[0][0], array[1][1], array[2][2], array[3][3] });

		xWon = xWon || "XXXX".equals(diagonal1.replace('T', 'X'));
		oWon = oWon || "OOOO".equals(diagonal1.replace('T', 'O'));

		final String diagonal2 = new String(new char[] { array[0][3], array[1][2], array[2][1], array[3][0] });

		xWon = xWon || "XXXX".equals(diagonal2.replace('T', 'X'));
		oWon = oWon || "OOOO".equals(diagonal2.replace('T', 'O'));

		if (xWon) {
			return oWon ? "Draw" : "X won";
		} else {
			return oWon ? "O won" : builder.toString().contains(".") ? "Game has not completed" : "Draw";
		}
	}
}