package com.github.fritaly.googlecodejam.y2015;

import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class StandingOvation extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new StandingOvation().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return true;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		scanner.nextInt();
		final String people = scanner.next();
		
		// Total # of people standing
		int total = 0;
		
		// # of people added
		int added = 0;
		
		for (int i = 0; i < people.length(); i++) {
			final int shyness = i;
			
			if (shyness > total) {
				final int diff = shyness - total;
				
				added += diff;
				total += diff;
			}
			
			total += people.charAt(i) - '0';
		}
		
		return Integer.toString(added);
	}
}