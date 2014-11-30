package com.github.fritaly.googlecodejam.y2011;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class Magicka extends AbstractPuzzler {

	private static class Combination {
		final Element element1, element2;

		final Element combinedElement;

		public Combination(Element element1, Element element2, Element combinedElement) {
			this.element1 = element1;
			this.element2 = element2;
			this.combinedElement = combinedElement;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Combination) {
				final Combination other = (Combination) obj;

				return (this.element1.equals(other.element1) && this.element2.equals(other.element2)) ||
						(this.element1.equals(other.element2) && this.element2.equals(other.element1));
			}

			return false;
		}
	}

	private static class Element {
		final char id;

		final List<Combination> combinations;

		final List<Combination> opposedElements;

		public Element(char id, List<Combination> combinations, List<Combination> opposedElements) {
			// The list of combinations is shared across instances
			this.id = id;
			this.combinations = combinations;
			this.opposedElements = opposedElements;
		}

		Element combine(Element other) {
			final Combination temp = new Combination(this, other, null);

			for (Combination combination : combinations) {
				if (combination.equals(temp)) {
					return combination.combinedElement;
				}
			}

			// The 2 elements don't combine
			return null;
		}

		boolean isOpposedTo(Element other) {
			final Combination temp = new Combination(this, other, null);

			for (Combination combination : opposedElements) {
				if (combination.equals(temp)) {
					return true;
				}
			}

			return false;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Element) {
				return (this.id == ((Element) obj).id);
			}

			return false;
		}

		@Override
		public String toString() {
			return Character.toString(id);
		}
	}

	public static void main(String[] args) throws Exception {
		new Magicka().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return true;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfElements = scanner.nextInt();

		final Map<Character, Element> elements = new TreeMap<>();

		final List<Combination> combinations = new ArrayList<>();
		final List<Combination> opposedElements = new ArrayList<>();

		// Create the 8 base elements upfront
		for (Character id : Arrays.asList('Q', 'W', 'E', 'R', 'A', 'S', 'D', 'F')) {
			elements.put(id, new Element(id, combinations, opposedElements));
		}

		for (int i = 0; i < numberOfElements; i++) {
			final String data = scanner.next();

			final char element1 = data.charAt(0);
			final char element2 = data.charAt(1);
			final char element3 = data.charAt(2);

			if (!elements.containsKey(element3)) {
				elements.put(element3, new Element(element3, combinations, opposedElements));
			}

			// Populate the list of combinations shared across elements
			combinations.add(new Combination(elements.get(element1), elements.get(element2), elements.get(element3)));
		}

		final int numberOfOpposedElements = scanner.nextInt();

		for (int i = 0; i < numberOfOpposedElements; i++) {
			final String data = scanner.next();

			final char element1 = data.charAt(0);
			final char element2 = data.charAt(1);

			opposedElements.add(new Combination(elements.get(element1), elements.get(element2), null));
		}

		final int count = scanner.nextInt();

		final String invokedElements = scanner.nextLine().trim();

		log("Elements invoked: " + invokedElements);

		final LinkedList<Element> elementList = new LinkedList<>();

		for (int i = 0; i < invokedElements.length(); i++) {
			final char id = invokedElements.charAt(i);

			final Element element = elements.get(id);

			if (element != null) {
				elementList.add(element);
			} else {
				// Create the element on-the-fly
				elementList.add(new Element(id, combinations, opposedElements));
			}

			// Check the elements after every new element added to the list
			while (true) {
				boolean updated = false;

				if (elementList.size() >= 2) {
					final Element lastElement = elementList.getLast();
					final Element otherElement = elementList.get(elementList.size() - 2);

					final Element combinedElement = lastElement.combine(otherElement);

					if (combinedElement != null) {
						// Combine the 2 elements
						elementList.removeLast();
						elementList.removeLast();
						elementList.add(combinedElement);

						updated = true;
					} else if (lastElement.isOpposedTo(otherElement)) {
						// Remove the 2 elements
						elementList.removeLast();
						elementList.removeLast();

						updated = true;
					}
				} else {
					break;
				}

				if (!updated) {
					break;
				}
			}
		}

		log("List: " + elementList);

		return null;
	}
}