package com.github.fritaly.googlecodejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ApocalypseSoon extends AbstractPuzzler {

	// Comparator to easily find the strongest nation among a list
	private static class NationComparator implements Comparator<Nation> {
		@Override
		public int compare(Nation o1, Nation o2) {
			// Sort the nations by strength (descending)
			if (o2.strength == o1.strength) {
				// They will simply choose their strongest neighbor (breaking
				// ties first by Northernmost nation, then by Westernmost)
				if (o2.y != o1.y) {
					return o1.y - o2.y;
				}
				if (o2.x != o1.x) {
					return o1.x - o2.x;
				}

				return 0;
			}

			return o2.strength - o1.strength;
		}
	}

	private class Nation {

		final World world;

		final int x, y;

		int strength;

		// The pending damage to be taken into account at the end of the day
		int damage;

		public Nation(World world, int x, int y, int strength) {
			this.world = world;
			this.x = x;
			this.y = y;
			this.strength = strength;
		}

		public Nation(World world, Nation nation) {
			this.world = world;
			this.x = nation.x;
			this.y = nation.y;
			this.strength = nation.strength;
			this.damage = nation.damage;
		}

		@Override
		public String toString() {
			return String.format("Nation[%d:%d, strength=%d, damage=%d]", x, y, strength, damage);
		}

		boolean isDestroyed() {
			return (this.strength == 0);
		}

		private void addIfNationExists(List<Nation> list, Nation nation) {
			if ((nation != null) && !nation.isDestroyed()) {
				list.add(nation);
			}
		}

		List<Nation> getEnemyNations() {
			final List<Nation> list = new ArrayList<>();

			addIfNationExists(list, world.getNation(x, y - 1));
			addIfNationExists(list, world.getNation(x, y + 1));
			addIfNationExists(list, world.getNation(x + 1, y));
			addIfNationExists(list, world.getNation(x - 1, y));

			return list;
		}

		Nation getStrongestNeighbor() {
			final List<Nation> list = getEnemyNations();

			if (list.isEmpty()) {
				return null;
			}

			Collections.sort(list, new NationComparator());

			return list.iterator().next();
		}

		// At the end of the day, the pending damages are applied and nations
		// possibly die
		void endOfDay() {
			this.strength = Math.max(this.strength - this.damage, 0);
			this.damage = 0;
		}
	}

	private class World {

		final Nation[][] nations;

		final int nationX, nationY;

		public World(Scanner scanner) {
			final int width = scanner.nextInt();
			final int height = scanner.nextInt();

			this.nations = new Nation[width][height];

			this.nationX = scanner.nextInt();
			this.nationY = scanner.nextInt();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					final int strength = scanner.nextInt();

					this.nations[x][y] = new Nation(this, x, y, strength);
				}
			}
		}

		public World(World world) {
			this.nationX = world.nationX;
			this.nationY = world.nationY;
			this.nations = new Nation[world.getWidth()][world.getHeight()];

			for (int x = 0; x < getWidth(); x++) {
				for (int y = 0; y < getHeight(); y++) {
					this.nations[x][y] = new Nation(this, world.getNation(x, y));
				}
			}
		}

		int getWidth() {
			return this.nations.length;
		}

		int getHeight() {
			return this.nations[0].length;
		}

		boolean exists(int x, int y) {
			return ((0 <= x) && (x < getWidth())) && ((0 <= y) && (y < getHeight()));
		}

		Nation getNation(int x, int y) {
			return exists(x, y) ? nations[x][y] : null;
		}

		List<Nation> getNations() {
			final List<Nation> list = new ArrayList<>();

			for (int x = 0; x < getWidth(); x++) {
				for (int y = 0; y < getHeight(); y++) {
					list.add(getNation(x, y));
				}
			}

			return list;
		}

		Nation getMyNation() {
			// The values are 1-based
			return this.nations[nationX - 1][nationY - 1];
		}

		// At the end of the day, the pending damages are applied and nations die
		void endOfDay() {
			for (Nation nation : getNations()) {
				// Update the nation's strength and reset the damage
				nation.endOfDay();
			}
		}

		int findSolution() {
			final Nation myNation = getMyNation();

			if (myNation.isDestroyed()) {
				return 0;
			}

			// Loop over the nations
			for (Nation nation : getNations()) {
				if (nation.isDestroyed()) {
					continue;
				}
				if (nation != myNation) {
					// What's the nation's target (its strongest neighbor)
					final Nation target = nation.getStrongestNeighbor();

					if (target != null) {
						log(nation + " attacks " + target);

						// Store the damage to be applied at the end of the day
						// The damage is cumulative (multiple attacks)
						target.damage += nation.strength;
					}
				} else {
					// Handled below
				}
			}

			// What are my nation's enemy nations ?
			final List<Nation> enemies = myNation.getEnemyNations();

			if (enemies.isEmpty()) {
				// No more enemies left, my nation can survive for ever
				return Integer.MAX_VALUE;
			}

			int bestResult = 0;

			{
				// Try doing nothing this day
				final World newWorld = new World(this);

				// Take into account the damages after all nations attacked
				newWorld.endOfDay();

				if (!newWorld.getMyNation().isDestroyed()) {
					final int result = newWorld.findSolution();

					if (result == Integer.MAX_VALUE) {
						bestResult = Integer.MAX_VALUE;
					} else {
						// My nation survived one more day
						bestResult = 1 + result;
					}
				}
			}

			// Try each possibility of attack
			for (Nation enemy : enemies) {
				final World newWorld = new World(this);
				final Nation newEnemy = newWorld.getNation(enemy.x, enemy.y);

				newEnemy.damage += myNation.strength;

				// Take into account the damages after all nations attacked
				newWorld.endOfDay();

				if (newWorld.getMyNation().isDestroyed()) {
					continue;
				}

				final int result = newWorld.findSolution();

				if (result == Integer.MAX_VALUE) {
					bestResult = Integer.MAX_VALUE;
					break;
				}

				// My nation survived one more day
				if (bestResult < 1 + result) {
					bestResult = 1 + result;
				}
			}

			return bestResult;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			for (int y = 0; y < getHeight(); y++) {
				for (int x = 0; x < getWidth(); x++) {
					builder.append(String.format("%2d", getNation(x, y).strength)).append(" ");
				}

				builder.append("\n");
			}

			return builder.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		new ApocalypseSoon().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final World world = new World(scanner);

		final Nation myNation = world.getMyNation();

		log("My nation: " + myNation);
		log("World: \n" + world);

		final int days = world.findSolution();

		if (days == Integer.MAX_VALUE) {
			return "forever";
		}

		return String.format("%d day(s)", days);
	}
}