package com.github.fritaly.googlecodejam.apac;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.fritaly.googlecodejam.AbstractPuzzler;

public class CubeIV extends AbstractPuzzler {

	private static class Coords {
		final int x, y;

		public Coords(int x, int y) {
			this.x = x;
			this.y = y;
		}

		Coords north() {
			return new Coords(x, y-1);
		}

		Coords south() {
			return new Coords(x, y+1);
		}

		Coords west() {
			return new Coords(x-1, y);
		}

		Coords east() {
			return new Coords(x+1, y);
		}

		@Override
		public String toString() {
			return String.format("[%d,%d]", x, y);
		}
	}

	private static class Room {
		final Maze maze;
		final int id, x, y;

		int longestPath = -1;

		public Room(Maze maze, int id, int x, int y) {
			this.maze = maze;
			this.id = id;
			this.x = x;
			this.y = y;
		}

		Room getNorthRoom() {
			return maze.getRoom(getCoords().north());
		}

		Room getSouthRoom() {
			return maze.getRoom(getCoords().south());
		}

		Room getWestRoom() {
			return maze.getRoom(getCoords().west());
		}

		Room getEastRoom() {
			return maze.getRoom(getCoords().east());
		}

		List<Room> getNextRooms() {
			final List<Room> result = new ArrayList<>();

			final Room northRoom = getNorthRoom();

			if ((northRoom != null) && (northRoom.id == this.id + 1)) {
				result.add(northRoom);
			}

			final Room southRoom = getSouthRoom();

			if ((southRoom != null) && (southRoom.id == this.id + 1)) {
				result.add(southRoom);
			}

			final Room westRoom = getWestRoom();

			if ((westRoom != null) && (westRoom.id == this.id + 1)) {
				result.add(westRoom);
			}

			final Room eastRoom = getEastRoom();

			if ((eastRoom != null) && (eastRoom.id == this.id + 1)) {
				result.add(eastRoom);
			}

			return result;
		}

		Coords getCoords() {
			return new Coords(x, y);
		}

		@Override
		public String toString() {
			return String.format("#%d[%d,%d]", id, x, y);
		}

		int getLongestPath() {
			if (longestPath == -1) {
				final List<Room> nextRooms = getNextRooms();

				int result = 1;

				for (Room room : nextRooms) {
					// One move to the next room
					final int localLongestPath = 1 + room.getLongestPath();

					if (localLongestPath > result) {
						result = localLongestPath;
					}
				}

				this.longestPath = result;
			}

			return longestPath;
		}
	}

	private static class Maze {

		final Room[][] rooms;

		final int size;

		public Maze(Scanner scanner) {
			this.size = scanner.nextInt();

			this.rooms = new Room[size][size];

			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					this.rooms[x][y] = new Room(this, scanner.nextInt(), x, y);
				}
			}
		}

		int getMaxRoomId() {
			return size * size;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			final int width = Integer.toString(getMaxRoomId()).length();

			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					builder.append(String.format("%" + width + "d", rooms[x][y].id)).append(" ");
				}

				builder.append("\n");
			}

			return builder.toString();
		}

		boolean isValid(Coords coords) {
			return ((0 <= coords.x) && (coords.x <= size - 1)) && ((0 <= coords.y) && (coords.y <= size - 1));
		}

		Room getRoom(Coords coords) {
			return isValid(coords) ? rooms[coords.x][coords.y] : null;
		}

		Room findRoomById(int roomId) {
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					if (rooms[x][y].id == roomId) {
						return rooms[x][y];
					}
				}
			}

			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		new CubeIV().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final Maze maze = new Maze(scanner);

		log(maze);

		// Evaluate the longest path for each room
		int longestPath = 0;
		int roomId = -1;

		for (int i = 1; i <= maze.getMaxRoomId(); i++) {
			final Room room = maze.findRoomById(i);

			log(room + " -> " + room.getLongestPath());

			if (longestPath < room.getLongestPath()) {
				longestPath = room.getLongestPath();
				roomId = room.id;
			} else if (longestPath == room.getLongestPath()) {
				if (room.id < roomId) {
					// In case there are multiple such people, the person who is
					// in the smallest room will win.
					roomId = room.id;
				}
			}
		}

		return String.format("%d %d", roomId, longestPath);
	}
}