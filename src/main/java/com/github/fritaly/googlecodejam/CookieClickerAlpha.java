package com.github.fritaly.googlecodejam;

import java.io.LineNumberReader;

public class CookieClickerAlpha extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new CookieClickerAlpha().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private String format(double f) {
		return String.format("%.7f", f);
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String[] chunks = reader.readLine().split(" ");

		// C: Cost (in cookies) to buy a farm
		final double farmCost = Float.parseFloat(chunks[0]);

		// F: Production boost due to a cookie farm
		final double boost = Float.parseFloat(chunks[1]);

		// X: Number of cookies to win the game
		final double target = Float.parseFloat(chunks[2]);

		log(String.format("Farm cost: %f, Boost: %f, Target: %f", farmCost, boost, target));

		// The base production rate is 2 cookies per second
		final double baseRate = 2.0d;

		double solution = Double.MAX_VALUE;

		for (int numberOfFarms = 0; numberOfFarms < 10; numberOfFarms++) {
			double productionRate = baseRate;
			double timeElapsed = 0;

			while (true) {
				// What's the time needed to build a new farm ?
				final double timeToBuildFarm = (farmCost / productionRate);

				// With this new farm, what will be the time needed to win ?
				final double timeToWinWithFarm = target / (productionRate + boost);

				// Without building the farm, what will be the time needed to win ?
				final double timeToWinWithoutFarm = target / productionRate;

				if (timeToWinWithoutFarm < (timeToBuildFarm + timeToWinWithFarm)) {
					// Stop building farms
					break;
				}

				// Build a new farm
				timeElapsed += timeToBuildFarm;
				productionRate += boost;
			}

			// Wait until we have enough cookies to win
			timeElapsed += (target / productionRate);

			if (timeElapsed < solution) {
				solution = timeElapsed;
			}
		}

		return format(solution);
	}
}