package com.github.fritaly.googlecodejam;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class TakingMetro extends AbstractPuzzler {

	private static class Station implements Comparable<Station> {
		final int line, station, waitTime;

		final MetroNetwork network;

		// Transient pieces of information required by the Dijkstra algorithm
		transient int travelTime = Integer.MAX_VALUE;

		transient Station previous;

		public Station(MetroNetwork network, int line, int station, int waitTime) {
			this.network = network;
			this.line = line;
			this.station = station;
			this.waitTime = waitTime;
		}

		List<Edge> getEdges() {
			final List<Edge> result = new ArrayList<>();

			for (Edge edge : network.edges) {
				if ((edge.station1 == this) || (edge.station2 == this)) {
					result.add(edge);
				}
			}

			return result;
		}

		@Override
		public int compareTo(Station other) {
			return this.travelTime - other.travelTime;
		}

		@Override
		public String toString() {
			return String.format("Station[%d:%d]", line, station);
		}

		@Override
		public int hashCode() {
			int hash = 7;

			hash = (hash * 31) + line;
			hash = (hash * 31) + station;

			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Station) {
				final Station other = (Station) obj;

				return (this.line == other.line) && (this.station == other.station);
			}
			return false;
		}
	}

	private static class Edge {
		final Station station1, station2;

		final int travelTime;

		public Edge(Station station1, Station station2, int travelTime) {
			this.station1 = station1;
			this.station2 = station2;
			this.travelTime = travelTime;
		}

		Station getDestination(Station source) {
			if (source == station1) {
				return station2;
			} else if (source == station2) {
				return station1;
			}

			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			return String.format("[%d - %d]", station1, station2);
		}
	}

	private static class MetroNetwork {

		final List<Station> stations = new ArrayList<>();

		final List<Edge> edges = new ArrayList<>();

		MetroNetwork(Scanner scanner) {
			final int numberOfMetroLines = scanner.nextInt();

			for (int i = 0; i < numberOfMetroLines; i++) {
				final int lineId = i + 1;
				final int numberOfStations = scanner.nextInt();
				final int waitTime = scanner.nextInt();

				// Create the stations
				for (int j = 0; j < numberOfStations; j++) {
					this.stations.add(new Station(this, lineId, j + 1, waitTime));
				}

				// Create the links between the stations
				for (int j = 0; j < numberOfStations - 1; j++) {
					final Station station1 = getStation(lineId, j + 1);
					final Station station2 = getStation(lineId, j + 2);

					final int travelTime = scanner.nextInt();

					this.edges.add(new Edge(station1, station2, travelTime));
				}
			}

			final int numberOfTunnels = scanner.nextInt();

			for (int i = 0; i < numberOfTunnels; i++) {
				final int line1 = scanner.nextInt();
				final int station1 = scanner.nextInt();

				final int line2 = scanner.nextInt();
				final int station2 = scanner.nextInt();

				final int walkTime = scanner.nextInt();

				this.edges.add(new Edge(getStation(line1, station1), getStation(line2, station2), walkTime));
			}
		}

		Station getStation(int line, int stationId) {
			for (Station station : stations) {
				if ((station.line == line) && (station.station == stationId)) {
					return station;
				}
			}

			return null;
		}

		void reset() {
			for (Station station : stations) {
				station.travelTime = Integer.MAX_VALUE;
				station.previous = null;
			}
		}

		void computeTravelTimes(Station start) {
			// Wait for the metro to show up
			start.travelTime = start.waitTime;

			final PriorityQueue<Station> queue = new PriorityQueue<>();
			queue.add(start);

			while (!queue.isEmpty()) {
				final Station origin = queue.poll();

				for (Edge edge : origin.getEdges()) {
					final Station destination = edge.getDestination(origin);

					int travelTimeToDestination = origin.travelTime + edge.travelTime;

					if (origin.previous != null) {
						if (origin.previous.line != origin.line) {
							// Take into account the expected wait time when changing line
							travelTimeToDestination += origin.waitTime;
						}
					}

					if (travelTimeToDestination < destination.travelTime) {
						queue.remove(destination);

						destination.travelTime = travelTimeToDestination;
						destination.previous = origin;

						queue.add(destination);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new TakingMetro().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		scanner.nextLine();

		final MetroNetwork network = new MetroNetwork(scanner);

		final int numberOfQueries = scanner.nextInt();

		final StringBuilder builder = new StringBuilder();
		builder.append("\n");

		for (int i = 0; i < numberOfQueries; i++) {
			final int startLine = scanner.nextInt();
			final int startStationId = scanner.nextInt();

			final int endLine = scanner.nextInt();
			final int endStationId = scanner.nextInt();

			final Station startStation = network.getStation(startLine, startStationId);
			final Station endStation = network.getStation(endLine, endStationId);

			network.reset();
			network.computeTravelTimes(startStation);

			builder.append((endStation.travelTime == Integer.MAX_VALUE) ? -1 : endStation.travelTime);
			builder.append("\n");
		}

		// Chomp the last line feed character
		builder.setLength(builder.length() - 1);

		return builder.toString();
	}
}