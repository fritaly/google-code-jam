package com.github.fritaly.googlecodejam.y2014;

import java.io.LineNumberReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class Addition extends AbstractPuzzler {

	private static interface Operand {
	}

	private static interface BinaryOperator {
	}

	private static class Variable implements Operand {
		final String name;

		public Variable(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Variable) {
				return this.name.equals(((Variable) obj).name);
			}

			return false;
		}

		@Override
		public String toString() {
			// return String.format("Variable[%s]", name);
			return name;
		}
	}

	private static class Constant implements Operand {
		final int value;

		public Constant(int value) {
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Constant) {
				return this.value == ((Constant) obj).value;
			}

			return false;
		}

		@Override
		public String toString() {
			// return String.format("Constant[%d]", value);
			return Integer.toString(value);
		}
	}

	private static class Equals implements BinaryOperator {
		final Operand left, right;

		public Equals(Operand left, Operand right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Plus) {
				final Plus plus = (Plus) obj;

				// The equals operator is commutative
				return (this.left.equals(plus.left) && this.right.equals(plus.right)) ||
						(this.left.equals(plus.right) && this.right.equals(plus.left));
			}

			return false;
		}

		@Override
		public String toString() {
			// return String.format("Equals[left=%s, right=%s]", left, right);
			return String.format("%s = %s", left, right);
		}
	}

	private static class Plus implements BinaryOperator, Operand {
		final Operand left, right;

		public Plus(Operand left, Operand right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Plus) {
				final Plus plus = (Plus) obj;

				// The plus operator is commutative
				return (this.left.equals(plus.left) && this.right.equals(plus.right)) ||
						(this.left.equals(plus.right) && this.right.equals(plus.left));
			}

			return false;
		}

		@Override
		public String toString() {
			// return String.format("Plus[left=%s, right=%s]", left, right);
			return String.format("%s + %s", left, right);
		}
	}

	private static final Pattern EQUALS_PATTERN = Pattern.compile("^([^=]+)=([^=]+)$");
	private static final Pattern PLUS_PATTERN = Pattern.compile("^([^+]+)\\+([^+]+)$");

	private static Object parse(String expression) {
		if (expression.matches("^-?[0-9]+$")) {
			return new Constant(Integer.parseInt(expression));
		}
		if (expression.matches("^[a-z]+$")) {
			return new Variable(expression);
		}

		final Matcher equalsMatcher = EQUALS_PATTERN.matcher(expression);

		if (equalsMatcher.matches()) {
			return new Equals((Operand) parse(equalsMatcher.group(1)), (Operand) parse(equalsMatcher.group(2)));
		}

		final Matcher plusMatcher = PLUS_PATTERN.matcher(expression);

		if (plusMatcher.matches()) {
			return new Plus((Operand) parse(plusMatcher.group(1)), (Operand) parse(plusMatcher.group(2)));
		}

		throw new IllegalArgumentException("Unable to parse expression '" + expression + "'");
	}

	public static void main(String[] args) throws Exception {
		new Addition().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return true;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final int count = Integer.parseInt(reader.readLine());

		final Map<Plus, Constant> facts = new LinkedHashMap<>();

		for (int i = 0; i < count; i++) {
			final Equals fact = (Equals) parse(reader.readLine());

			log(fact);

			facts.put((Plus) fact.left, (Constant) fact.right);
		}

		final int count2 = Integer.parseInt(reader.readLine());

		for (int i = 0; i < count2; i++) {
			final Plus expression = (Plus) parse(reader.readLine());

			Constant result = null;

			for (Plus plus : facts.keySet()) {
				if (plus.equals(expression)) {
					result = facts.get(plus);
					break;
				}
			}

			log(expression + " -> " + result);
		}

		return null;
	}
}