package com.github.fritaly.googlecodejam.y2011;

import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class BotTrust extends AbstractPuzzler {

	private class Bot {

		final Color color;

		// The bot starts at coordinate 1
		int coordinate = 1;

		final LinkedList<Action> actions = new LinkedList<>();

		public Bot(Color color) {
			this.color = color;
		}

		void move() {
			if (!actions.isEmpty()) {
				final Action nextAction = actions.getFirst();

				if (nextAction.button != coordinate) {
					if (nextAction.button > coordinate) {
						coordinate++;

						log(color + " moved from " + (coordinate - 1) + " to " + coordinate);
					} else if (nextAction.button < coordinate) {
						coordinate--;

						log(color + " moved from " + (coordinate + 1) + " to " + coordinate);
					}
				} else {
					log(color + " is waiting");
				}
			}
		}

		void pressButton() {
			this.actions.removeFirst();

			log(color + " pressed the button in " + coordinate);
		}

		boolean canPressButton() {
			// The bot can press the button if it's facing it
			return (this.coordinate == actions.getFirst().button);
		}

		@Override
		public String toString() {
			return String.format("Bot[%s, %s]", color.name(), actions);
		}
	}

	private static enum Color {
		BLUE, ORANGE;

		Color getOtherColor() {
			return equals(BLUE) ? ORANGE : BLUE;
		}

		static Color getColor(String value) {
			if ("O".equals(value)) {
				return ORANGE;
			} else if ("B".equals(value)) {
				return BLUE;
			}

			throw new IllegalArgumentException();
		}
	}

	private static class Action {
		final int button;

		public Action(int button) {
			this.button = button;
		}

		@Override
		public String toString() {
			return String.format("Action[%d]", button);
		}
	}

	public static void main(String[] args) throws Exception {
		new BotTrust().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String[] data = reader.readLine().split(" ");

		// Map to lookup the bots by color
		final Map<Color, Bot> bots = new TreeMap<>();

		final Bot blue = new Bot(Color.BLUE);
		final Bot orange = new Bot(Color.ORANGE);

		bots.put(Color.BLUE, blue);
		bots.put(Color.ORANGE, orange);

		// The sequence of buttons to press
		final LinkedList<Color> sequence = new LinkedList<>();

		for (int i = 0; i < data.length / 2; i++) {
			final Color color = Color.getColor(data[i * 2 + 1]);
			final int button = Integer.parseInt(data[i * 2 + 2]);

			log(color + " " + button);

			switch (color) {
			case BLUE:
				blue.actions.add(new Action(button));
				break;
			case ORANGE:
				orange.actions.add(new Action(button));
				break;
			default:
				throw new RuntimeException();
			}

			sequence.add(color);
		}

		// The next bot to press a button
		Color nextBot = sequence.removeFirst();

		// The time elapsed in seconds
		int seconds = 0;

		while (true) {
			seconds++;

			final Bot bot = bots.get(nextBot);

			boolean buttonPressed = false;

			// A bot can wait, move or press a button during a turn
			if (bot.canPressButton()) {
				bot.pressButton();

				buttonPressed = true;
			} else {
				bot.move();
			}

			// The other bot can only move (or wait)
			bots.get(nextBot.getOtherColor()).move();

			if (buttonPressed) {
				if (sequence.isEmpty()) {
					break;
				}

				// Retrieve the next to press a button
				nextBot = sequence.removeFirst();
			}
		}

		return Integer.toString(seconds);
	}
}