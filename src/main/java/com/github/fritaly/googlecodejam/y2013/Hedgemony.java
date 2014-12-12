package com.github.fritaly.googlecodejam.y2013;

import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class Hedgemony extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new Hedgemony().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfBushes = scanner.nextInt();

		final double[] bushes = new double[numberOfBushes];

		for (int i = 0; i < numberOfBushes; i++) {
			bushes[i] = scanner.nextInt();
		}

		for (int i = 1; i < bushes.length - 1; i++) {
			bushes[i] = Math.min(bushes[i], (bushes[i - 1] + bushes[i + 1]) / 2);
		}

		return String.format("%.6f", bushes[bushes.length - 2]);
	}
}