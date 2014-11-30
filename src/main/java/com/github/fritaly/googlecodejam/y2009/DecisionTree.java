package com.github.fritaly.googlecodejam.y2009;

import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class DecisionTree extends AbstractPuzzler {

	private class Animal {
		final String name;

		final Set<String> features = new TreeSet<>();

		public Animal(String data) {
			final String[] array = data.split(" ");

			this.name = array[0];

			final int numberOfFeatures = Integer.parseInt(array[1]);

			for (int i = 0; i < numberOfFeatures; i++) {
				features.add(array[i + 2]);
			}
		}

		@Override
		public String toString() {
			return String.format("%s[features=%s]", name, features);
		}
	}

	private class Node {
		final double weight;

		final String feature;

		final Node left, right;

		public Node(Scanner scanner) {
			// The method findInLine() ignores delimiters
			scanner.findInLine("\\(");

			this.weight = Double.parseDouble(scanner.findInLine("\\d+(\\.\\d+)"));

			if (scanner.hasNext("[a-z]+")) {
				this.feature = scanner.next("[a-z]+");
				this.left = new Node(scanner);
				this.right = new Node(scanner);
			} else {
				this.feature = null;
				this.left = this.right = null;
			}

			scanner.findInLine("\\)");
		}

		private void toString(StringBuilder builder, int indentation) {
			final String prefix = repeat("  ", indentation);

			builder.append(prefix).append(String.format("[%.6f", weight));

			if (feature != null) {
				builder.append(" ").append(feature).append("\n");

				left.toString(builder, indentation + 1);
				builder.append("\n");
				right.toString(builder, indentation + 1);
				builder.append("\n");
			}

			builder.append(prefix).append("]");
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			toString(builder, 0);

			return builder.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		new DecisionTree().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private double evaluate(Animal animal, Node decisionTree, double value) {
		value = decisionTree.weight * value;

		final String feature = decisionTree.feature;

		if (feature != null) {
			if (animal.features.contains(feature)) {
				return evaluate(animal, decisionTree.left, value);
			} else {
				return evaluate(animal, decisionTree.right, value);
			}
		}

		return value;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final int numberOfLines = Integer.parseInt(reader.readLine());

		final StringBuilder buffer = new StringBuilder();

		for (int i = 0; i < numberOfLines; i++) {
			buffer.append(reader.readLine()).append(" ");
		}

		log(buffer);

		final Node decisionTree = new Node(new Scanner(buffer.toString()));

		log(decisionTree);

		final int numberOfAnimals = Integer.parseInt(reader.readLine());

		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < numberOfAnimals; i++) {
			final Animal animal = new Animal(reader.readLine());

			log(animal);

			builder.append("\n");
			builder.append(String.format("%.7f", evaluate(animal, decisionTree, 1.0d)));
		}

		return builder.toString();
	}
}