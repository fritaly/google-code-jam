package com.github.fritaly.googlecodejam.y2015;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class IOError extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new IOError().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return true;
	}
	
	protected List<String> getInputs() {
		// No large input for this problem
		return Arrays.asList(String.format("%s-sample.in", getName()), String.format("%s-small-practice.in", getName()));
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfBytes = scanner.nextInt();
		final String data = scanner.next();
		
		final StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < numberOfBytes; i++) {
			final String chunk = data.substring(i * 8, (i + 1) * 8).replace('O', '0').replace('I', '1');
		
			builder.append((char) Integer.parseInt(chunk, 2));
		}
		
		return builder.toString();
	}
}