package com.github.fritaly.googlecodejam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ItzChess extends AbstractPuzzler {

	private static abstract class Piece {

		final Position position;

		final ChessBoard board;

		public Piece(ChessBoard board, Position position) {
			this.board = board;
			this.position = position;
		}

		Position getPosition() {
			return position;
		}

		int getX() {
			return position.x;
		}

		int getY() {
			return position.y;
		}

		protected boolean addIfPositionExistsAndIsOccupied(List<Position> list, Position position) {
			if (position != null) {
				if (board.hasPiece(position.x, position.y)) {
					list.add(position);

					return true;
				}
			}

			return false;
		}

		protected abstract char getSymbol();

		// Returns all the attackable positions for this piece by taking into
		// account the other pieces on the chess board (some pieces can't be
		// attacked because they're hidden by other pieces)
		protected abstract List<Position> getAttackablePositions();
	}

	private static class King extends Piece {

		public King(ChessBoard board, Position position) {
			super(board, position);
		}

		@Override
		protected char getSymbol() {
			return 'K';
		}

		@Override
		protected List<Position> getAttackablePositions() {
			final List<Position> list = new ArrayList<>();

			for (int x = -1; x <= +1; x++) {
				for (int y = -1; y <= +1; y++) {
					if ((x == 0) && (y == 0)) {
						continue;
					}

					// All those nearby positions can be attacked
					addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + x, getY() + y));
				}
			}

			return list;
		}
	}

	private static class Queen extends Piece {

		public Queen(ChessBoard board, Position position) {
			super(board, position);
		}

		@Override
		protected char getSymbol() {
			return 'Q';
		}

		@Override
		protected List<Position> getAttackablePositions() {
			final List<Position> list = new ArrayList<>();

			// Take into account the other pieces blocking the attack

			// Same moves as bishop
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + d, getY() + d))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + d, getY() - d))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - d, getY() + d))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - d, getY() - d))) {
					break;
				}
			}

			// Same moves as rook
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + d, getY()))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - d, getY()))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX(), getY() + d))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX(), getY() - d))) {
					break;
				}
			}

			return list;
		}
	}

	private static class Knight extends Piece {

		public Knight(ChessBoard board, Position position) {
			super(board, position);
		}

		@Override
		protected char getSymbol() {
			return 'N';
		}

		@Override
		protected List<Position> getAttackablePositions() {
			final List<Position> list = new ArrayList<>();

			// The knight can jump over pieces blocking other pieces' attacks

			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + 2, getY() + 1));
			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + 2, getY() - 1));

			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + 1, getY() + 2));
			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + 1, getY() - 2));

			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - 1, getY() + 2));
			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - 1, getY() - 2));

			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - 2, getY() + 1));
			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - 2, getY() - 1));

			return list;
		}
	}

	private static class Bishop extends Piece {

		public Bishop(ChessBoard board, Position position) {
			super(board, position);
		}

		@Override
		protected char getSymbol() {
			return 'B';
		}

		@Override
		protected List<Position> getAttackablePositions() {
			final List<Position> list = new ArrayList<>();

			// Take into account the other pieces blocking the attack

			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + d, getY() + d))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + d, getY() - d))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - d, getY() + d))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - d, getY() - d))) {
					break;
				}
			}

			return list;
		}
	}

	private static class Rook extends Piece {

		public Rook(ChessBoard board, Position position) {
			super(board, position);
		}

		@Override
		protected char getSymbol() {
			return 'R';
		}

		@Override
		protected List<Position> getAttackablePositions() {
			final List<Position> list = new ArrayList<>();

			// Take into account the other pieces blocking the attack

			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + d, getY()))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - d, getY()))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX(), getY() + d))) {
					break;
				}
			}
			for (int d = 1; d <= 8; d++) {
				if (addIfPositionExistsAndIsOccupied(list, board.getPosition(getX(), getY() - d))) {
					break;
				}
			}

			return list;
		}
	}

	private static class Pawn extends Piece {

		public Pawn(ChessBoard board, Position position) {
			super(board, position);
		}

		@Override
		protected char getSymbol() {
			return 'P';
		}

		@Override
		protected List<Position> getAttackablePositions() {
			final List<Position> list = new ArrayList<>();

			// Pawn moving upwards (no downwards move allowed as per the spec)
			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() + 1, getY() + 1));
			addIfPositionExistsAndIsOccupied(list, board.getPosition(getX() - 1, getY() + 1));

			return list;
		}
	}

	private static final String COORDINATES = "HGFEDCBA";

	private static class Position {
		final int x, y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		String toText() {
			return String.format("%s%d", Character.toString(COORDINATES.charAt(x)), (y + 1));
		}

		String toXY() {
			return String.format("[%d,%d]", x, y);
		}

		@Override
		public String toString() {
			return toText();
		}

		public static Position fromText(String text) {
			final char column = text.charAt(0);
			final char row = text.charAt(1);

			return new Position(COORDINATES.indexOf(column), Integer.parseInt(Character.toString(row)) - 1);
		}
	}

	private class ChessBoard {

		final Piece[][] pieces = new Piece[8][8];

		public ChessBoard(Scanner scanner) {
			final int numberOfPieces = scanner.nextInt();

			// There's an extra line feed we need to skip
			scanner.nextLine();

			for (int i = 0; i < numberOfPieces; i++) {
				final String line = scanner.nextLine();

				final Position position = Position.fromText(line.substring(0, line.indexOf('-')));

				final String pieceType = line.substring(line.indexOf('-') + 1);

				final Piece piece;

				if ("K".equals(pieceType)) {
					piece = new King(this, position);
				} else if ("Q".equals(pieceType)) {
					piece = new Queen(this, position);
				} else if ("R".equals(pieceType)) {
					piece = new Rook(this, position);
				} else if ("B".equals(pieceType)) {
					piece = new Bishop(this, position);
				} else if ("N".equals(pieceType)) {
					piece = new Knight(this, position);
				} else if ("P".equals(pieceType)) {
					piece = new Pawn(this, position);
				} else {
					throw new RuntimeException("Unsupported piece type: " + pieceType);
				}

				// Install the piece on the chess board
				pieces[position.x][position.y] = piece;
			}
		}

		Piece getPiece(int x, int y) {
			if (!exists(x, y)) {
				return null;
			}

			return pieces[x][y];
		}

		List<Piece> getPieces() {
			final List<Piece> list = new ArrayList<>();

			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					final Piece piece = pieces[x][y];

					if (piece != null) {
						list.add(piece);
					}
				}
			}

			return list;
		}

		boolean hasPiece(int x, int y) {
			return (getPiece(x, y) != null);
		}

		Position getPosition(int x, int y) {
			if (!exists(x, y)) {
				return null;
			}

			return new Position(x, y);
		}

		boolean exists(int x, int y) {
			return ((0 <= x) && (x < 8)) && ((0 <= y) && (y < 8));
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();

			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					final Piece piece = pieces[x][y];

					if (piece != null) {
						builder.append(piece.getSymbol());
					} else {
						builder.append(".");
					}
				}

				builder.append("\n");
			}

			return builder.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		new ItzChess().execute();
	}

	@Override
	protected boolean isLogEnabled() {
		return true;
	}

	@Override
	protected String solve(Scanner scanner) throws Exception {
		final ChessBoard board = new ChessBoard(scanner);

		log("");
		log(board);

		int count = 0;

		for (Piece piece : board.getPieces()) {
			final List<Position> positions = piece.getAttackablePositions();

			log(String.format("%s[%s] can kill pieces: %s", piece.getSymbol(), piece.getPosition().toText(), positions));

			count += positions.size();
		}

		log("");

		return Integer.toString(count);
	}
}