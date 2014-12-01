package com.github.fritaly.googlecodejam.y2011;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class InvestingAtTheMarket extends AbstractPuzzler {

	private static class Period {
		final int startMonth, endMonth;

		final int startPrice, endPrice;

		public Period(int startMonth, int startPrice, int endMonth, int endPrice) {
			this.startMonth = startMonth;
			this.startPrice = startPrice;
			this.endMonth = endMonth;
			this.endPrice = endPrice;
		}

		@Override
		public String toString() {
			return String.format("[%d:%d -> %d:%d (%+d)]", startMonth, startPrice, endMonth, endPrice, (endPrice - startPrice));
		}

		int computeProfit(int money) {
			if (money < startPrice) {
				// We can't even buy an item (we can't buy a fraction of it)
				return 0;
			}

			return (endPrice - startPrice) * (money / startPrice);
		}
	}

	public static void main(String[] args) throws Exception {
		new InvestingAtTheMarket().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int money = scanner.nextInt();

		final List<Integer> prices = new ArrayList<>();

		for (int i = 0; i < 12; i++) {
			prices.add(scanner.nextInt());
		}

		log("Money: " + money);
		log("Prices: " + prices);

		Period bestPeriod = null;

		for (int i = 0; i < prices.size() - 1; i++) {
			for (int j = i+1; j < prices.size(); j++) {
				final Period period = new Period(i+1, prices.get(i), j+1, prices.get(j));

				if (bestPeriod == null) {
					bestPeriod = period;
				} else if (bestPeriod.computeProfit(money) < period.computeProfit(money)) {
					bestPeriod = period;
				} else if (bestPeriod.computeProfit(money) == period.computeProfit(money)) {
					// If two scenarios exist that result in the same profit,
					// you should choose to buy at the lowest price per unit.
					if (period.startPrice < bestPeriod.startPrice) {
						bestPeriod = period;
					}
				}
			}
		}

		if ((bestPeriod == null) || (bestPeriod.computeProfit(money) == 0)) {
			return "IMPOSSIBLE";
		}

		return String.format("%d %d %d", bestPeriod.startMonth, bestPeriod.endMonth, bestPeriod.computeProfit(money));
	}
}