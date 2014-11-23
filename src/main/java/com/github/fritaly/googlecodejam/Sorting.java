package com.github.fritaly.googlecodejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Sorting extends AbstractPuzzler {

	private static class AlexBookComparator implements Comparator<Book> {
		@Override
		public int compare(Book o1, Book o2) {
			return o1.id - o2.id;
		}
	}

	private static class BobBookComparator implements Comparator<Book> {
		@Override
		public int compare(Book o1, Book o2) {
			return o2.id - o1.id;
		}
	}

	private static enum Owner {
		Alex, Bob;
	}

	private static class Book {
		final int id;

		public Book(int id) {
			this.id = id;
		}

		Owner getOwner() {
			return (id % 2 == 0) ? Owner.Bob : Owner.Alex;
		}

		@Override
		public String toString() {
			return String.format("Book[%d]", id);
		}
	}

	public static void main(String[] args) throws Exception {
		new Sorting().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfBooks = scanner.nextInt();

		scanner.nextLine();

		final List<Book> books = new ArrayList<>();

		for (int i = 0; i < numberOfBooks; i++) {
			books.add(new Book(scanner.nextInt()));
		}

		log("Books: " + books);

		// Memorize the order of owners (the labels must remain the same)
		final List<Owner> owners = new ArrayList<>();
		final List<Book> alexBooks = new ArrayList<>();
		final List<Book> bobBooks = new ArrayList<>();

		for (Book book : books) {
			owners.add(book.getOwner());

			if (Owner.Alex.equals(book.getOwner())) {
				alexBooks.add(book);
			} else {
				bobBooks.add(book);
			}
		}

		// Sort each list of books according to their specific rule
		Collections.sort(alexBooks, new AlexBookComparator());
		Collections.sort(bobBooks, new BobBookComparator());

		final List<Book> sortedBooks = new ArrayList<>();

		for (Owner owner : owners) {
			if (Owner.Alex.equals(owner)) {
				sortedBooks.add(alexBooks.remove(0));
			} else {
				sortedBooks.add(bobBooks.remove(0));
			}
		}

		final StringBuilder builder = new StringBuilder();

		for (Book book : sortedBooks) {
			if (builder.length() > 0) {
				builder.append(" ");
			}

			builder.append(book.id);
		}

		return builder.toString();
	}
}