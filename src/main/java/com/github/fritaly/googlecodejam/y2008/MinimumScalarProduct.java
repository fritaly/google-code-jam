package com.github.fritaly.googlecodejam.y2008;

import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class MinimumScalarProduct extends AbstractPuzzler {

	// clones the given array of ints
	static int[] clone(int[] array) {
		final int[] clone = new int[array.length];

		System.arraycopy(array, 0, clone, 0, array.length);

		return clone;
	}

	// formats the given list of arrays of ints as a string
	static String format(List<int[]> list) {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("[");

		for (int[] array : list) {
			if (buffer.length() > 1) {
				buffer.append(", ");
			}

			buffer.append(format(array));
		}

		return buffer.append("]").toString();
	}

	// formats the given array of ints as a string
	static String format(int[] array) {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("[");

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				buffer.append(", ");
			}

			buffer.append(array[i]);
		}

		return buffer.append("]").toString();
	}

	static int[] sort(int[] array) {
		final int[] clone = clone(array);

		Arrays.sort(clone);

		return clone;
	}

	static int[] reverse(int[] array) {
		final int[] sorted = sort(array);
		final int[] result = new int[array.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = sorted[sorted.length - i - 1];
		}

		return result;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		// We don't need this
		reader.readLine();

		final Vector v1 = new Vector(reader.readLine());
		final Vector v2 = new Vector(reader.readLine());

		return Integer.toString(v1.getSorted().scalarProduct(v2.getReverseSorted()));
	}

	public static void main(String[] args) throws Exception {
//		int[] a = new int[] { 1, 2, 3, 4 };
//		int[] f = new int[] { 3, 9, 1, 7, 2, 5 };
//
//		System.out.println(format(a));
//		System.out.println(format(clone(a)));
//		System.out.println(format(sort(f)));
//		System.out.println(format(reverse(sort(f))));

		new MinimumScalarProduct().run();
	}

	private static final class Vector {

		private final int[] values;

		Vector(int[] values) {
			this.values = values;
		}

		Vector(String data) {
			final String[] array = data.split(" ");

			final int[] ints = new int [array.length];

			int index = 0;

			for (String string : array) {
				ints[index++] = Integer.parseInt(string);
			}

			this.values = ints;
		}

		@Override
		public String toString() {
			final StringBuilder buffer = new StringBuilder();
			buffer.append("Vector[");

			for (int i = 0; i < values.length; i++) {
				if (i != 0) {
					buffer.append(", ");
				}

				buffer.append(values[i]);
			}

			buffer.append("]");

			return buffer.toString();
		}

		int size() {
			return this.values.length;
		}

		int scalarProduct(Vector other) {
			// Ensure the 2 vectors have the same size
			if (this.size() != other.size()) {
				throw new IllegalArgumentException(String.format("The 2 vectors %s and %s don't have the same size", this, other));
			}

			int result = 0;

			for (int i = 0; i < size(); i++) {
				result += this.values[i] * other.values[i];
			}

			return result;
		}

		Vector getSorted() {
			return new Vector(sort(this.values));
		}

		Vector getReverseSorted() {
			return new Vector(reverse(sort(this.values)));
		}
	}
}