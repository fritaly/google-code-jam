package com.github.fritaly.googlecodejam.y2008;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class TrainTimeTable extends AbstractPuzzler {

	private static enum Location {
		A, B;

		Location getDestination() {
			return equals(A) ? B : A;
		}
	}

	private static enum EventType {
		ARRIVAL, DEPARTURE;
	}

	private static class Event implements Comparable<Event> {

		final int time;

		final Location location;

		final EventType type;

		public Event(int time, Location location, EventType type) {
			this.time = time;
			this.location = location;
			this.type = type;
		}

		@Override
		public String toString() {
			return String.format("Event[%s - %s from %s]", formatTime(time), type.name(), location.name());
		}

		@Override
		public int compareTo(Event other) {
			if (this.time == other.time) {
				if (this.location == other.location) {
					return this.type.compareTo(other.type);
				}

				return this.location.compareTo(other.location);
			}

			return this.time - other.time;
		}
	}

	private static class Trip {
		final Location origin;

		// The departure & arrival times are stored as minutes from midnight
		final int departure, arrival;

		public Trip(Location origin, String times) {
			this.origin = origin;

			final String[] array = times.split(" ");

			this.departure = parseTime(array[0]);
			this.arrival = parseTime(array[1]);
		}

		@Override
		public String toString() {
			return String.format("Trip[%s, %s -> %s]", origin.name(), formatTime(departure), formatTime(arrival));
		}
	}

	private static int parseTime(String time) {
		return Integer.parseInt(time.substring(0, time.indexOf(':'))) * 60 + Integer.parseInt(time.substring(time.indexOf(':') + 1));
	}

	private static String formatTime(int minutes) {
		return String.format("%02d:%02d", (minutes / 60), (minutes % 60));
	}

	public static void main(String[] args) throws Exception {
		new TrainTimeTable().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int turnAroundTime = scanner.nextInt();

		// Skip the line feed
		scanner.nextLine();

		final int numberOfTripsFromA = scanner.nextInt();
		final int numberOfTripsFromB = scanner.nextInt();

		// Skip the line feed
		scanner.nextLine();

		final List<Trip> trips = new ArrayList<>();

		for (int i = 0; i < numberOfTripsFromA; i++) {
			trips.add(new Trip(Location.A, scanner.nextLine()));
		}
		for (int i = 0; i < numberOfTripsFromB; i++) {
			trips.add(new Trip(Location.B, scanner.nextLine()));
		}

		log("Trips: " + trips);

		final LinkedList<Event> events = new LinkedList<>();

		for (Trip trip : trips) {
			events.add(new Event(trip.departure, trip.origin, EventType.DEPARTURE));
			events.add(new Event(trip.arrival, trip.origin.getDestination(), EventType.ARRIVAL));
		}

		// Sort the events by time
		Collections.sort(events);

		log("Events: " + events);

		// List containing the trains available in stations A & B. The integer
		// value denotes when the train is usable (after turning around)
		final LinkedList<Integer> trainsInA = new LinkedList<>();
		final LinkedList<Integer> trainsInB = new LinkedList<>();

		int trainsAddedInA = 0, trainsAddedInB = 0;

		while (!events.isEmpty()) {
			final Event event = events.removeFirst();

			if (event.type == EventType.DEPARTURE) {
				// Departure of a train
				if (event.location == Location.A) {
					if (trainsInA.isEmpty()) {
						trainsAddedInA++;
					} else {
						if (trainsInA.getFirst() <= event.time) {
							trainsInA.removeFirst();
						} else {
							// The train is turning around
							trainsAddedInA++;
						}
					}
				} else {
					if (trainsInB.isEmpty()) {
						trainsAddedInB++;
					} else {
						if (trainsInB.getFirst() <= event.time) {
							trainsInB.removeFirst();
						} else {
							// The train is turning around
							trainsAddedInB++;
						}
					}
				}
			} else {
				// Arrival of a train. Take into account the turn around time
				// before reusing the train
				if (event.location == Location.A) {
					trainsInA.add(Integer.valueOf(event.time + turnAroundTime));
				} else {
					trainsInB.add(Integer.valueOf(event.time + turnAroundTime));
				}
			}
		}

		return String.format("%d %d", trainsAddedInA, trainsAddedInB);
	}
}