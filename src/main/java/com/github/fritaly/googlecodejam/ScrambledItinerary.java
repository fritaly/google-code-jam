package com.github.fritaly.googlecodejam;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

public class ScrambledItinerary extends AbstractPuzzler {

	private static class Trip {

		final String origin, destination;

		public Trip(Scanner scanner) {
			this.origin = scanner.next();
			this.destination = scanner.next();
		}

		@Override
		public String toString() {
			return String.format("%s-%s", origin, destination);
		}
	}

	public static void main(String[] args) throws Exception {
		new ScrambledItinerary().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private static String join(Collection<Trip> trips) {
		final StringBuilder builder = new StringBuilder();

		for (Trip trip : trips) {
			if (builder.length() > 0) {
				builder.append(" ");
			}

			builder.append(trip);
		}

		return builder.toString();
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfTrips = scanner.nextInt();

		final LinkedList<Trip> trips = new LinkedList<>();

		for (int i = 0; i < numberOfTrips; i++) {
			trips.add(new Trip(scanner));
		}

		log("Trips: " + trips);

		// Sort the trip
		final LinkedList<Trip> result = new LinkedList<>();

		while (!trips.isEmpty()) {
			final Trip trip = trips.removeFirst();

			if (result.isEmpty()) {
				result.add(trip);
			} else {
				if (trip.origin.equals(result.getLast().destination)) {
					result.addLast(trip);
				} else if (trip.destination.equals(result.getFirst().origin)) {
					result.addFirst(trip);
				} else {
					// Add it back to the list for a future use
					trips.addLast(trip);
				}
			}
		}

		return join(result);
	}

}