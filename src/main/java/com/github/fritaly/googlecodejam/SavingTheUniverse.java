package com.github.fritaly.googlecodejam;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SavingTheUniverse extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new SavingTheUniverse().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private String selectSearchEngine(List<String> searchEngines, List<String> queries) {
		// Select the search engine which will process the highest number of queries
		String searchEngine = null;

		int highest = 0;

		for (String current: searchEngines) {
			int requests = 0;

			for (String query : queries) {
				if (query.equals(current)) {
					break;
				}

				requests++;
			}

			if (requests > highest) {
				searchEngine = current;
				highest = requests;
			}
		}

		return searchEngine;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final int numberOfSearchEngines = Integer.parseInt(reader.readLine());

		final List<String> searchEngines = new ArrayList<>();

		for (int i = 0; i < numberOfSearchEngines; i++) {
			searchEngines.add(reader.readLine());
		}

		final int numberQueries = Integer.parseInt(reader.readLine());

		final List<String> queries = new ArrayList<>();

		for (int i = 0; i < numberQueries; i++) {
			queries.add(reader.readLine());
		}

		log("Search engines: " + searchEngines);
		log("Queries: " + queries);

		String searchEngine = selectSearchEngine(searchEngines, queries);

		log("Initial search engine: " + searchEngine);

		final LinkedList<String> queue = new LinkedList<>(queries);

		int switches = 0;

		while (!queue.isEmpty()) {
			final String query = queue.getFirst();

			if (query.equals(searchEngine)) {
				// Need to switch to another search engine
				final String backup = searchEngine;

				searchEngine = selectSearchEngine(searchEngines, queue);

				log(String.format("Switched from '%s' to '%s'", backup, searchEngine));

				switches++;
			} else {
				// That's good, proceed to the next query
			}

			queue.removeFirst();
		}

		log("Result: " + switches);
		log("");

		return Integer.toString(switches);
	}
}