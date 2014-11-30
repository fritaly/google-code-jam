package com.github.fritaly.googlecodejam.y2010;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class GetToWork extends AbstractPuzzler {

	private class Town {

		// The employees living in this town
		final List<Employee> employees = new ArrayList<>();

		final boolean hasOffice;

		final int id;

		public Town(int id, boolean hasOffice) {
			this.id = id;
			this.hasOffice = hasOffice;
		}

		// Returns the number of cars needed to commute all the employees to the
		// office
		int getNumberOfCars() {
			if (hasOffice) {
				return 0;
			}

			int cars = 0;

			// Use a queue with the drivers at the head and the non-drivers at the tail
			final LinkedList<Employee> queue = new LinkedList<>(employees);

			// Use the drivers with the biggest capacity of passengers first
			Collections.sort(queue, new Comparator<Employee>() {
				@Override
				public int compare(Employee o1, Employee o2) {
					return o2.numberOfPassengers - o1.numberOfPassengers;
				}
			});

			log("Queue: " + queue);

			while (!queue.isEmpty()) {
				final Employee currentDriver = queue.removeFirst();

				// How many passengers max for this driver ?
				final int passengers = currentDriver.numberOfPassengers - 1;

				for (int i = 0; i < passengers && !queue.isEmpty(); i++) {
					// Remove the non-drivers first then the drivers
					queue.removeLast();
				}

				cars++;
			}

			return cars;
		}

		int getNumberOfPassengers() {
			int count = 0;

			for (Employee employee : employees) {
				if (employee.isLicensed()) {
					count += employee.numberOfPassengers - 1;
				}
			}

			return count;
		}

		int getNumberOfNonLicensedEmployees() {
			int count = 0;

			for (Employee employee : employees) {
				if (!employee.isLicensed()) {
					count++;
				}
			}

			return count;
		}

		@Override
		public String toString() {
			return String.format("Town[%d, %s]", id, employees);
		}
	}

	private static class Employee {
		final int town;

		// 0 (non-licensed), 1 (licensed, no passenger)
		final int numberOfPassengers;

		public Employee(Scanner scanner) {
			this.town = scanner.nextInt();
			this.numberOfPassengers = scanner.nextInt();
		}

		boolean isLicensed() {
			return (numberOfPassengers > 0);
		}

		@Override
		public String toString() {
			return String.format("Employee[passengers=%d]", numberOfPassengers);
		}
	}

	public static void main(String[] args) throws Exception {
		new GetToWork().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfTowns = scanner.nextInt();
		final int officeTown = scanner.nextInt();

		// Create the towns
		final List<Town> towns = new ArrayList<>();

		for (int i = 1; i <= numberOfTowns; i++) {
			towns.add(new Town(i, (i == officeTown)));
		}

		final int numberOfEmployees = scanner.nextInt();

		log("");
		log("Employees: " + numberOfEmployees);

		// Create the employees and assign them to towns
		for (int i = 0; i < numberOfEmployees; i++) {
			final Employee employee = new Employee(scanner);

			towns.get(employee.town - 1).employees.add(employee);
		}

		boolean possible = true;

		for (Town town : towns) {
			log(town);
			log("# of employees: " + town.employees.size());
			log("# of passengers: " + town.getNumberOfPassengers());
			log("# of non-licensed drivers: " + town.getNumberOfNonLicensedEmployees());

			if (town.getNumberOfNonLicensedEmployees() <= town.getNumberOfPassengers()) {
				log("# of cars: " + town.getNumberOfCars());
			}

			if (town.id == officeTown) {
				continue;
			}
			if (town.getNumberOfNonLicensedEmployees() > town.getNumberOfPassengers()) {
				possible = false;
				break;
			}
		}

		if (!possible) {
			return "IMPOSSIBLE";
		}

		final StringBuilder builder = new StringBuilder();

		for (Town town : towns) {
			builder.append(town.getNumberOfCars());
			builder.append(" ");
		}

		builder.setLength(builder.length() - 1);

		return builder.toString();
	}
}