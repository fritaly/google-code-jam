package com.github.fritaly.googlecodejam.y2010;

import java.util.LinkedList;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class SnapperChain extends AbstractPuzzler {

	private static class Snapper {
		// true=ON, false=OFF. Defaults to false
		boolean state = false;

		Snapper previous, next;

		void snap() {
			if (isPowered()) {
				if (next != null) {
					// Propagate to the next snapper before switching the state
					next.snap();
				}

				// Switch the state
				this.state = !state;
			}
		}

		boolean isPowered() {
			if (previous == null) {
				// The first snapper (the one with no previous) is always
				// powered
				return true;
			}

			// A snapper is powered if its previous snapper is also powered and
			// in the ON state
			return (previous.isPowered() && previous.state);
		}
	}

	private static class Chain {

		final LinkedList<Snapper> list = new LinkedList<>();

		public Chain(int length) {
			Snapper previous = null;

			for (int i = 0; i < length; i++) {
				final Snapper snapper = new Snapper();

				if (previous != null) {
					previous.next = snapper;
					snapper.previous = previous;
				}

				previous = snapper;

				list.add(snapper);
			}
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			for (int i = 0; i < list.size(); i++) {
				if (i != 0) {
					builder.append(",");
				}

				builder.append(list.get(i).state ? "ON" : "OFF");
			}

			return builder.toString();
		}

		Snapper getLast() {
			return list.getLast();
		}

		Snapper getFirst() {
			return list.getFirst();
		}
	}

	public static void main(String[] args) throws Exception {
		new SnapperChain().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfSnappers = scanner.nextInt();
		final int numberOfSnaps = scanner.nextInt();

		log("# of snappers: " + numberOfSnappers);
		log("# of snaps: " + numberOfSnaps);

		final Chain chain = new Chain(numberOfSnappers);

		log(chain);

		for (int i = 0; i < numberOfSnaps; i++) {
			chain.getFirst().snap();
			log(chain);
		}

		return chain.getLast().isPowered() && chain.getLast().state ? "ON" : "OFF";
	}
}