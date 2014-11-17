package com.github.fritaly.googlecodejam;

import java.io.LineNumberReader;

public class AlienNumbers extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new AlienNumbers().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String[] chunks = reader.readLine().split(" ");

		final String number = chunks[0];
		final String sourceBase = chunks[1];
		final String targetBase = chunks[2];

		log("Number: " + number);
		log("Source base: " + sourceBase);
		log("Target base: " + targetBase);

		final int sourceBaseSize = sourceBase.length();
		final int targetBaseSize = targetBase.length();

		long decimalNumber = 0;

		// Convert the number from the source base to the decimal base
		for (int i = 0; i < number.length(); i++) {
			final char c = number.charAt(number.length() - 1 - i);
			final int index = sourceBase.indexOf(c);

			decimalNumber += index * Math.pow(sourceBaseSize, i);
		}

		log("Decimal number: " + decimalNumber);

		// Convert the number from the decimal base to the target base

		// How many digits do we need to encode the decimal value in the target base ?
		final int numberOfDigits = (int) Math.ceil((Math.log(decimalNumber) / Math.log(targetBaseSize)));

		log("# of digits: " + numberOfDigits);

		long remainder = decimalNumber;

		final StringBuilder result = new StringBuilder();

		for (int i = numberOfDigits; i >= 0; i--) {
			final long powerOfN = (int) Math.pow(targetBaseSize, i);

			final long q = remainder / powerOfN;

			if ((q == 0) && (result.length() == 0)) {
				// Skip leading zeros
				continue;
			}

			result.append(targetBase.charAt((int) q));

			remainder = remainder - (q * powerOfN);
		}

		log("Result: " + result.toString());

		return result.toString();
	}
}