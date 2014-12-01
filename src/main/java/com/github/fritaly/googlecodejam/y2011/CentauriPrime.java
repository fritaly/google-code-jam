package com.github.fritaly.googlecodejam.y2011;

import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class CentauriPrime extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new CentauriPrime().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected List<String> getInputs() {
		return Arrays.asList(String.format("%s-sample.in", getClass().getSimpleName()),
				String.format("%s-small-practice-1.in", getClass().getSimpleName()),
				String.format("%s-small-practice-2.in", getClass().getSimpleName()));
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String cityName = reader.readLine();

		// Normalise the city name to lower case
		switch (cityName.toLowerCase().charAt(cityName.length() - 1)) {
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
			return String.format("%s is ruled by a queen.", cityName);
		case 'y':
			return String.format("%s is ruled by nobody.", cityName);
		default:
			return String.format("%s is ruled by a king.", cityName);
		}
	}
}