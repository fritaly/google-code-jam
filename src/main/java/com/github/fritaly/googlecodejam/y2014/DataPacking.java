package com.github.fritaly.googlecodejam.y2014;

import java.io.LineNumberReader;
import java.util.Collections;
import java.util.LinkedList;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class DataPacking extends AbstractPuzzler {

	public static void main(String[] args) throws Exception {
		new DataPacking().run();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String[] data = reader.readLine().split(" ");

		final int numberOfFiles = Integer.parseInt(data[0]);
		final int maximumCapacity = Integer.parseInt(data[1]);

		final String[] array = reader.readLine().split(" ");

		final LinkedList<Integer> fileSizes = new LinkedList<>();

		for (String fileSize : array) {
			fileSizes.add(Integer.parseInt(fileSize));
		}

		Collections.sort(fileSizes);

		int numberOfCds = 0;

		log("Max capacity: " + maximumCapacity);

		while (!fileSizes.isEmpty()) {
			log("# of files: " + fileSizes);

			if (fileSizes.size() == 1) {
				fileSizes.clear();

				numberOfCds++;
				break;
			}
			if (fileSizes.size() == 2) {
				final int fileSize1 = fileSizes.remove(0);
				final int fileSize2 = fileSizes.remove(0);

				if (fileSize1 + fileSize2 > maximumCapacity) {
					numberOfCds += 2;
				} else {
					numberOfCds++;
				}

				break;
			}

			// Remove the biggest file from the list
			final int fileSize1 = fileSizes.removeLast();

			final int remainingCapacity = maximumCapacity - fileSize1;

			// Search the biggest file whose size is lower than the remaining
			// capacity
			int fileSize2 = -1;

			for (Integer fileSize : fileSizes) {
				if (fileSize > remainingCapacity) {
					break;
				}

				if (fileSize > fileSize2) {
					fileSize2 = fileSize;
				}
			}

			if (fileSize2 != -1) {
				// Remove the second file from the list of file sizes
				fileSizes.remove(fileSizes.indexOf(Integer.valueOf(fileSize2)));
			}

			numberOfCds++;
		}

		return Integer.toString(numberOfCds);
	}
}