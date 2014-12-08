package com.github.fritaly.googlecodejam.y2009;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import com.github.fritaly.googlecodejam.AbstractPuzzler;
import com.github.fritaly.googlecodejam.Incomplete;

@Incomplete
public class CrazyRows extends AbstractPuzzler {

	private class Matrix {
		final List<String> rows = new ArrayList<>();

		final int size;

		public Matrix(LineNumberReader reader) throws NumberFormatException, IOException {
			this.size = Integer.parseInt(reader.readLine());

			for (int y = 0; y < size; y++) {
				rows.add(reader.readLine());
			}
		}

		int sort() {
			int n = 0;

			for (int i = 0; i < rows.size() - 1; i++) {
				for (int j = i + 1; j < rows.size(); j++) {
					final String s1 = rows.get(i);
					final String s2 = rows.get(j);

					if (s1.lastIndexOf('1') > i) {
						rows.set(i, s2);
						rows.set(j, s1);

						n++;
					}
				}
			}

			return n++;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			for (String row : rows) {
				builder.append(row).append("\n");
			}

			builder.setLength(builder.length() - 1);

			return builder.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		new CrazyRows().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final Matrix matrix = new Matrix(reader);

		log("Matrix: \n" + matrix);

		final int n = matrix.sort();

		log("Matrix: \n" + matrix);

		return Integer.toString(n);
	}
}