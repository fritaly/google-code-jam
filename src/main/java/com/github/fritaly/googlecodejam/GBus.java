package com.github.fritaly.googlecodejam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GBus extends AbstractPuzzler {

	private static class Bus {
		final int startCity, endCity;

		public Bus(Scanner scanner) {
			this.startCity = scanner.nextInt();
			this.endCity = scanner.nextInt();
		}

		boolean traversesCity(int city) {
			return (startCity <= city) && (city <= endCity);
		}

		@Override
		public String toString() {
			return String.format("[%d -> %d]", startCity, endCity);
		}
	}

	public static void main(String[] args) throws Exception {
		new GBus().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfBuses = scanner.nextInt();

		final List<Bus> buses = new ArrayList<>();

		for (int i = 0; i < numberOfBuses; i++) {
			buses.add(new Bus(scanner));
		}

		log("Buses: " + buses);

		final int numberOfCities = scanner.nextInt();

		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < numberOfCities; i++) {
			final int city = scanner.nextInt();

			int count = 0;

			for (Bus bus : buses) {
				if (bus.traversesCity(city)) {
					count ++;
				}
			}

			if (builder.length() > 0) {
				builder.append(" ");
			}

			builder.append(count);
		}

		return builder.toString();
	}
}