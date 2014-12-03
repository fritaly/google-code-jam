package com.github.fritaly.googlecodejam.y2011;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class RPI extends AbstractPuzzler {

	private static int countMatches(String text, char c, int skippedIndex) {
		int count = 0;

		for (int i = 0; i < text.length(); i++) {
			if (i == skippedIndex) {
				continue;
			}
			if (text.charAt(i) == c) {
				count++;
			}
		}

		return count;
	}

	private static class Team {

		final Tournament tournament;

		final int id;

		final String data;

		public Team(Tournament tournament, int id, String data) {
			this.tournament = tournament;
			this.id = id;
			this.data = data;
		}

		int getWonGames(int skippedIteam) {
			return countMatches(data, '1', skippedIteam);
		}

		int getLostGames(int skippedIteam) {
			return countMatches(data, '0', skippedIteam);
		}

		int getPlayedGames(int skippedIteam) {
			return getWonGames(skippedIteam) + getLostGames(skippedIteam);
		}

		List<Team> getOpponents() {
			final List<Team> opponents = new ArrayList<>();

			for (int i = 0; i < data.length(); i++) {
				final char c = data.charAt(i);

				if ((c == '0') || (c == '1')) {
					opponents.add(tournament.getTeam(i));
				}
			}

			return opponents;
		}

		double getWinPercentage() {
			return getWinPercentage(-1);
		}

		double getWinPercentage(int skippedIteam) {
			return ((double) getWonGames(skippedIteam)) / getPlayedGames(skippedIteam);
		}

		double getOpponentsWinPercentage() {
			double result = 0;

			final List<Team> opponents = getOpponents();

			for (Team opponent : opponents) {
				result += opponent.getWinPercentage(this.id);
			}

			return (result / opponents.size());
		}

		double getOpponentsOpponentsWinPercentage() {
			double result = 0;

			final List<Team> opponents = getOpponents();

			for (Team opponent : opponents) {
				result += opponent.getOpponentsWinPercentage();
			}

			return (result / opponents.size());
		}

		double getRatingsPercentageIndex() {
			return 0.25d * getWinPercentage() + 0.5d * getOpponentsWinPercentage() + 0.25d * getOpponentsOpponentsWinPercentage();
		}
	}

	private static class Tournament {
		final List<Team> teams = new ArrayList<>();

		public Tournament(Scanner scanner) {
			final int numberOfTeams = scanner.nextInt();

			// Skip the extra line feed
			scanner.nextLine();

			for (int i = 0; i < numberOfTeams; i++) {
				teams.add(new Team(this, i, scanner.nextLine()));
			}
		}

		Team getTeam(int id) {
			// The team id and the team index match
			return teams.get(id);
		}
	}

	public static void main(String[] args) throws Exception {
		new RPI().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return true;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final Tournament tournament = new Tournament(scanner);

		final StringBuilder builder = new StringBuilder();
		builder.append("\n");

		for (Team team : tournament.teams) {
			builder.append(String.format("%.12f", team.getRatingsPercentageIndex())).append("\n");
		}

		builder.setLength(builder.length() - 1);

		return builder.toString();
	}
}