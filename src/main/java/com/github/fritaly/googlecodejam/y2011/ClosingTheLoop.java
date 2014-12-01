package com.github.fritaly.googlecodejam.y2011;

import java.io.LineNumberReader;
import java.util.Collections;
import java.util.LinkedList;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class ClosingTheLoop extends AbstractPuzzler {

	private static enum Color {
		BLUE, RED;

		Color getOtherColor() {
			return equals(BLUE) ? RED : BLUE;
		}

		static Color getColor(char value) {
			if (value == 'R') {
				return RED;
			} else if (value == 'B') {
				return BLUE;
			}

			throw new IllegalArgumentException();
		}
	}

	private static class Rope implements Comparable<Rope> {
		final int length;

		final Color color;

		public Rope(String data) {
			this.color = Color.getColor(data.charAt(data.length() - 1));
			this.length = Integer.parseInt(data.substring(0, data.length() - 1));
		}

		@Override
		public int compareTo(Rope other) {
			return other.length - this.length;
		}

		@Override
		public String toString() {
			return String.format("%d%s", length, color.name().charAt(0));
		}
	}

	public static void main(String[] args) throws Exception {
		new ClosingTheLoop().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		// Skip the number of ropes, we don't need it
		reader.readLine();

		final LinkedList<Rope> blueRopes = new LinkedList<>();
		final LinkedList<Rope> redRopes = new LinkedList<>();

		final String[] data = reader.readLine().split(" ");

		for (int i = 0; i < data.length; i++) {
			final Rope rope = new Rope(data[i]);

			switch (rope.color) {
			case BLUE:
				blueRopes.add(rope);
				break;
			case RED:
				redRopes.add(rope);
				break;
			default:
				throw new RuntimeException("Unexpected color: " + rope.color);
			}
		}

		if (redRopes.isEmpty() || blueRopes.isEmpty()) {
			// We can't tie any knot
			return "0";
		}

		Collections.sort(blueRopes);
		Collections.sort(redRopes);

		final LinkedList<Rope> ropes = new LinkedList<>();

		// Start with the most represented color
		Color color = (blueRopes.size() > redRopes.size()) ? Color.BLUE : Color.RED;

		while (true) {
			final LinkedList<Rope> list = (color == Color.BLUE) ? blueRopes : redRopes;

			if (list.isEmpty()) {
				break;
			}

			ropes.add(list.removeFirst());

			color = color.getOtherColor();
		}

		// Ensure the 2 ends can be tied together
		if (ropes.getFirst().color == ropes.getLast().color) {
			ropes.removeLast();
		}

		log("Ropes: " + ropes);

		int length = 0;

		for (Rope rope : ropes) {
			length += rope.length;
		}

		// Each knot uses 0,5 cm per rope (1 cm per knot)
		length -= ropes.size();

		return Integer.toString(length);
	}
}