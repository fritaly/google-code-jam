package com.github.fritaly.googlecodejam.y2013;

import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class BabyHeight extends AbstractPuzzler {

	private static final Height FIVE_INCHES = new Height(5);

	private static final Height FOUR_INCHES = new Height(4);

	private static class Height {
		int inches;

		public Height(int inches) {
			this.inches = inches;
		}

		public Height(String text) {
			final Matcher matcher = Pattern.compile("^([0-9]+)'([0-9]+)\"$").matcher(text);

			if (!matcher.matches()) {
				throw new IllegalArgumentException();
			}

			this.inches = Integer.parseInt(matcher.group(1)) * 12 + Integer.parseInt(matcher.group(2));
		}

		Height plus(Height other) {
			return new Height(this.inches + other.inches);
		}

		Height minus(Height other) {
			return new Height(this.inches - other.inches);
		}

		Height half(boolean lower) {
			if (inches % 2 == 1) {
				if (lower) {
					// Round up to shrink the range
					return new Height((inches + 1) / 2);
				} else {
					// Round down to shrink the range
					return new Height((inches - 1) / 2);
				}
			}

			return new Height(inches / 2);
		}

		@Override
		public String toString() {
			return String.format("%d'%d\"", (inches / 12), (inches % 12));
		}
	}

	public static void main(String[] args) throws Exception {
		new BabyHeight().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return true;
	}

	protected List<String> getInputs() {
		return Arrays.asList(String.format("%s-sample.in", getName()), String.format("%s-small-practice.in", getName()));
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String[] array = reader.readLine().split(" ");

		final boolean girl = "G".equals(array[0]);
		final Height motherHeight = new Height(array[1]);
		final Height fatherHeight = new Height(array[2]);

		final Height result;

		if (girl) {
			result = motherHeight.plus(fatherHeight).minus(FIVE_INCHES);
		} else {
			result = motherHeight.plus(fatherHeight).plus(FIVE_INCHES);
		}

		final Height lowerBound = result.half(true).minus(FOUR_INCHES);
		final Height upperBound = result.half(false).plus(FOUR_INCHES);

		return String.format("%s to %s", lowerBound, upperBound);
	}
}