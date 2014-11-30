package com.github.fritaly.googlecodejam.y2010;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class RopeIntranet extends AbstractPuzzler {

	private static class Wire {

		final int y1, y2;

		public Wire(Scanner scanner) {
			this.y1 = scanner.nextInt();
			this.y2 = scanner.nextInt();
		}

		@Override
		public String toString() {
			return String.format("[%d:%d]", y1, y2);
		}

		boolean intersects(Wire wire) {
			if ((this.y1 > wire.y1) && (this.y2 < wire.y2)) {
				return true;
			}
			if ((this.y1 < wire.y1) && (this.y2 > wire.y2)) {
				return true;
			}

			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		new RopeIntranet().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfWires = scanner.nextInt();

		final List<Wire> wires = new ArrayList<>();

		for (int i = 0; i < numberOfWires; i++) {
			wires.add(new Wire(scanner));
		}

		log("Wires: " + wires);

		if (wires.size() == 1) {
			return "0";
		}

		int intersections = 0;

		for (int i = 0; i < wires.size() - 1; i++) {
			final Wire wire1 = wires.get(i);

			for (int j = i; j < wires.size(); j++) {
				final Wire wire2 = wires.get(j);

				if (wire1.intersects(wire2)) {
					intersections++;
				}
			}
		}

		return Integer.toString(intersections);
	}
}