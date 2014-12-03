package com.github.fritaly.googlecodejam.y2010;

import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class SnapperChain extends AbstractPuzzler {

	// Here's what happens with 4 snappers when snapping

	// 0	OFF,OFF,OFF,OFF
	// 1	ON,OFF,OFF,OFF
	// 2	OFF,ON,OFF,OFF
	// 3	ON,ON,OFF,OFF
	// 4	OFF,OFF,ON,OFF
	// 5	ON,OFF,ON,OFF
	// 6	OFF,ON,ON,OFF
	// 7	ON,ON,ON,OFF
	// 8	OFF,OFF,OFF,ON
	// 9	ON,OFF,OFF,ON
	// 10	OFF,ON,OFF,ON
	// 11	ON,ON,OFF,ON
	// 12	OFF,OFF,ON,ON
	// 13	ON,OFF,ON,ON
	// 14	OFF,ON,ON,ON
	// 15	ON,ON,ON,ON
	// 16	OFF,OFF,OFF,OFF (same as 0)
	// 17	ON,OFF,OFF,OFF (same as 1)
	// 18	OFF,ON,OFF,OFF (same as 2)
	// 19	ON,ON,OFF,OFF (same as 3)
	// ...

	// We only need to snap (K % (2^N)), encode this number in binary and check
	// whether every bit is set to determine the lamp status

	public static void main(String[] args) throws Exception {
		new SnapperChain().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final int numberOfSnappers = scanner.nextInt();
		final int numberOfSnaps = scanner.nextInt();

		log("# of snappers: " + numberOfSnappers);
		log("# of snaps: " + numberOfSnaps);
		log("# of snaps % (2^N): " + numberOfSnaps % (1 << numberOfSnappers));

		final int n = numberOfSnaps % (1 << numberOfSnappers);

		return (n == (1 << numberOfSnappers) - 1) ? "ON" : "OFF";
	}
}