package com.github.fritaly.googlecodejam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AlwaysTurnLeft extends AbstractPuzzler {

	private static enum Direction {
		NORTH, WEST, SOUTH, EAST;

		Direction getOpposedDirection() {
			switch (this) {
			case EAST:
				return WEST;
			case NORTH:
				return SOUTH;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
			}

			throw new UnsupportedOperationException();
		}

		Direction getClockwiseDirection() {
			switch (this) {
			case EAST:
				return SOUTH;
			case NORTH:
				return EAST;
			case SOUTH:
				return WEST;
			case WEST:
				return NORTH;
			}

			throw new UnsupportedOperationException();
		}

		Direction getAntiClockwiseDirection() {
			switch (this) {
			case EAST:
				return NORTH;
			case NORTH:
				return WEST;
			case SOUTH:
				return EAST;
			case WEST:
				return SOUTH;
			}

			throw new UnsupportedOperationException();
		}
	}

	private static class Room {
		Room north, south, east, west;

		final int x, y;

		public Room(int x, int y) {
			this.x = x;
			this.y = y;
		}

		Room getRoom(Direction direction) {
			switch (direction) {
			case EAST:
				return east;
			case NORTH:
				return north;
			case SOUTH:
				return south;
			case WEST:
				return west;
			default:
				throw new RuntimeException("Unexpected direction: " + direction);
			}
		}

		void setRoom(Direction direction, Room room) {
			switch (direction) {
			case EAST:
				this.east = room;
				break;
			case NORTH:
				this.north = room;
				break;
			case SOUTH:
				this.south = room;
				break;
			case WEST:
				this.west = room;
				break;
			default:
				throw new RuntimeException("Unexpected direction: " + direction);
			}
		}

		String toCharacter() {
			int n = 0;

			if (north != null) {
				n += 1;
			}
			if (south != null) {
				n += 2;
			}
			if (west != null) {
				n += 4;
			}
			if (east != null) {
				n += 8;
			}

			return Integer.toString(n, 16);
		}

		@Override
		public String toString() {
			return String.format("Room[%d,%d]", x, y);
		}
	}

	public static void main(String[] args) throws Exception {
		new AlwaysTurnLeft().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private Room createRoom(Room room, Direction direction) {
		Room result = null;

		switch (direction) {
		case EAST:
			result =  new Room(room.x + 1, room.y);
			break;
		case NORTH:
			result =  new Room(room.x, room.y - 1);
			break;
		case SOUTH:
			result =  new Room(room.x, room.y + 1);
			break;
		case WEST:
			result =  new Room(room.x - 1, room.y);
			break;
		default:
			throw new RuntimeException("Unexpected direction: " + direction);
		}

		// Link the 2 rooms
		result.setRoom(direction.getOpposedDirection(), room);
		room.setRoom(direction, result);

		return result;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final String pathEntranceToExit = scanner.next();
		final String pathExitToEntrance = scanner.next();

		log("Path (entrance -> exit): " + pathEntranceToExit);
		log("Path (exit -> entrance): " + pathExitToEntrance);

		final Room startRoom = new Room(0, 0);

		// The entrance is on the north, so we're looking towards south at the
		// beginning
		Direction direction = Direction.SOUTH;

		final List<Room> rooms = new ArrayList<>();
		rooms.add(startRoom);

		Room currentRoom = startRoom;

		for (int i = 0; i < pathEntranceToExit.length(); i++) {
			final char c = pathEntranceToExit.charAt(i);

			switch (c) {
			case 'W':
				// Does the next room exist ?
				Room nextRoom = currentRoom.getRoom(direction);

				if (nextRoom == null) {
					// Create the missing room and link it to the current one
					rooms.add(nextRoom = createRoom(currentRoom, direction));
				}

				currentRoom = nextRoom;
				break;
			case 'L':
				direction = direction.getAntiClockwiseDirection();
				break;
			case 'R':
				direction = direction.getClockwiseDirection();
				break;
			default:
				throw new RuntimeException("Unexpected instruction: " + c);
			}
		}

		// Turn around
		direction = direction.getOpposedDirection();

		// The last room is the exit
		final Room exitRoom = currentRoom;

		// Start again in the opposite direction
		for (int i = 0; i < pathExitToEntrance.length(); i++) {
			final char c = pathExitToEntrance.charAt(i);

			switch (c) {
			case 'W':
				// Does the next room exist ?
				Room nextRoom = currentRoom.getRoom(direction);

				if (nextRoom == null) {
					// Create the missing room and link it to the current one
					rooms.add(nextRoom = createRoom(currentRoom, direction));
				}

				currentRoom = nextRoom;
				break;
			case 'L':
				direction = direction.getAntiClockwiseDirection();
				break;
			case 'R':
				direction = direction.getClockwiseDirection();
				break;
			default:
				throw new RuntimeException("Unexpected instruction: " + c);
			}
		}

		// Remove the start & end rooms from the list (they're not part of the
		// maze)
		rooms.remove(startRoom);
		rooms.remove(exitRoom);

		// Rebuild the maze
		int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

		for (Room room : rooms) {
			minX = Math.min(minX, room.x);
			minY = Math.min(minY, room.y);
			maxX = Math.max(maxX, room.x);
			maxY = Math.max(maxY, room.y);
		}

		final StringBuilder builder = new StringBuilder();
		builder.append("\n");

		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				for (Room room : rooms) {
					if ((room.x == x) && (room.y == y)) {
						builder.append(room.toCharacter());
					}
				}
			}

			builder.append("\n");
		}

		// Chomp the last line feed
		builder.setLength(builder.length() - 1);

		return builder.toString();
	}
}