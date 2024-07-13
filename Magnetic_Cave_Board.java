import java.util.ArrayList;
import java.util.Scanner;

public class Magnetic_Cave_Board {
	private char[][] board;
	private char currentPlayer;
	private char player1;
	private char player2;

	public Magnetic_Cave_Board() {
		board = new char[8][8];
		player1 = '■';
		player2 = '□';
		currentPlayer = player1;
		initializeBoard();
	}

	public void printBoard() {
		System.out.println("   A B C D E F G H");
		for (int row = 0; row < 8; row++) {
			System.out.print((row + 1) + "|");
			for (int col = 0; col < 8; col++) {
				System.out.print(board[row][col] + " ");
			}
			System.out.print("|" + (8 - row));
			System.out.println();
		}
		System.out.println("   A B C D E F G H");
	}

	private void initializeBoard() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				board[row][col] = '-';
			}
		}
	}

	public boolean makeMove(int row, int column) { // this method checks if the position is valid and empty and places
													// the currentPlayer
		if (row < 0 || row >= 8 || column < 0 || column >= 8) {
			return false;
		}

		if (column != 0 && column != 7 && board[row][column - 1] == '-' && board[row][column + 1] == '-') {
			return false;
		}

		if (board[row][column] != '-') {
			return false;
		}

		board[row][column] = currentPlayer;
		return true;
	}

	public char getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean isPlayerWinner(char player) { // this method is to check the rows, columns, or diagonal if there are
													// five block of one of the players to win
		for (int row = 0; row < 8; row++) { // check the rows and count for 5 columns
			for (int col = 0; col < 4; col++) {
				if (board[row][col] == player && board[row][col + 1] == player && board[row][col + 2] == player
						&& board[row][col + 3] == player && board[row][col + 4] == player) {
					return true;
				}
			}
		}
		for (int row = 0; row < 4; row++) { // check the columns and count for 5 rows
			for (int col = 0; col < 8; col++) {
				if (board[row][col] == player && board[row + 1][col] == player && board[row + 2][col] == player
						&& board[row + 3][col] == player && board[row + 4][col] == player) {
					return true;
				}
			}
		}
		for (int row = 0; row < 4; row++) { // check the columns and count the blocks from the top right to the left
			for (int col = 4; col < 8; col++) {
				if (board[row][col] == player && board[row + 1][col - 1] == player && board[row + 2][col - 2] == player
						&& board[row + 3][col - 3] == player && board[row + 4][col - 4] == player) {
					return true;
				}
			}
		}
		for (int row = 0; row < 4; row++) { // check the columns and count the blocks from the top left to the right
			for (int col = 0; col < 4; col++) {
				if (board[row][col] == player && board[row + 1][col + 1] == player && board[row + 2][col + 2] == player
						&& board[row + 3][col + 3] == player && board[row + 4][col + 4] == player) {
					return true;
				}
			}
		}

		return false; // if no one wins then return false
	}

	public boolean isBoardFull() { // this method is to check if the board if full to determine if the game is a
									// tie
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] == '-') {
					return false;
				}
			}
		}
		return true;
	}

	public void switchPlayers() {
		currentPlayer = (currentPlayer == player1) ? player2 : player1;
	}

	public int minimax(char[][] board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
		if (depth == 0 || isPlayerWinner(player1) || isPlayerWinner(player2) || isBoardFull()) {
			return evaluateBoard();
		}
		if (isMaximizingPlayer) { // maximizing player
			int maxValue = Integer.MIN_VALUE;
			ArrayList<int[]> validMoves = getValidMoves();
			for (int[] move : validMoves) {
				int row = move[0];
				int col = move[1];
				board[row][col] = currentPlayer;
				switchPlayers();
				int Minimax = minimax(board, depth - 1, alpha, beta, false);
				board[row][col] = '-';
				switchPlayers();
				maxValue = Math.max(maxValue, Minimax);
				alpha = Math.max(alpha, Minimax);
				if (beta <= alpha) {
					break;
				}
			}
			return maxValue;
		} else { // minimizing player
			int minValue = Integer.MAX_VALUE;
			ArrayList<int[]> validMoves = getValidMoves();
			for (int[] move : validMoves) {
				int row = move[0];
				int col = move[1];
				board[row][col] = currentPlayer;
				switchPlayers();
				int Minimax = minimax(board, depth - 1, alpha, beta, true);
				board[row][col] = '-';
				switchPlayers();
				minValue = Math.min(minValue, Minimax);
				beta = Math.min(beta, Minimax);
				if (beta <= alpha) {
					break;
				}
			}
			return minValue;
		}
	}

	public void makeAIMove(int depth, char player) { // this method calls the minimax method and compares the scores and
														// chooses the best score and places the player in that position
		int maxValue = Integer.MIN_VALUE;
		int bestRow = -1;
		int bestCol = -1;
		ArrayList<int[]> validMoves = getValidMoves();
		for (int[] move : validMoves) {
			int row = move[0];
			int col = move[1];
			board[row][col] = currentPlayer;
			switchPlayers();
			int Minimax = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
			board[row][col] = '-';
			switchPlayers();
			if (Minimax > maxValue) {
				maxValue = Minimax;
				bestRow = row;
				bestCol = col;
			}
		}

		if (bestRow != -1 && bestCol != -1) {
			makeMove(bestRow, bestCol);
		}
		if (isPlayerWinner(player) == true) {
			printBoard();
			System.out.println("Automatic" + player + "wins!");
		}

		else if (isBoardFull()) {
			printBoard();
			System.out.println("It's a draw!");
		}

		switchPlayers();

	}

	public void manualMode() { // this method is for the first choice which alternate between both players and
								// each is manually played
		Scanner scanner1 = new Scanner(System.in);
		String entry;
		int row, column;
		while (!isBoardFull() && !isPlayerWinner(player1) && !isPlayerWinner(player2)) {
			printBoard();

			System.out.print("Player " + currentPlayer + ", enter your position (eg: A1 or H1): ");
			entry = scanner1.next().toUpperCase();

			column = entry.charAt(0) - 'A'; // calculate column number for player1 and player2

			if (currentPlayer == player1) {
				row = entry.charAt(1) - '1'; // calculate row for player1 which is from the left
			} else {
				row = '8' - entry.charAt(1); // calculate row for player2 which is from the right
			}

			if (!makeMove(row, column)) {
				System.out.println("Not Accepted Value, Try Again!!");
				continue;
			}
			switchPlayers(); // switch between players to alternate between them
		}

		printBoard();
		// keep checking if one of the players wins or a tie to terminate the game
		// accordingly
		if (isPlayerWinner(player1)) {
			System.out.println("Player ■ wins!");
		} else if (isPlayerWinner(player2)) {
			System.out.println("Player □ wins!");
		} else {
			System.out.println("It's a tie!");
		}

		scanner1.close();
	}

	public void onePlayerManualMode() { // this method is for the one manual player which is beside the automatic one
		Scanner scanner2 = new Scanner(System.in);
		String entry;
		int row, column;

		System.out.print("Player " + currentPlayer + ", enter your position (eg: A1 or H1): ");

		while (true) {
			if (scanner2.hasNext()) {
				entry = scanner2.next().toUpperCase();
				column = entry.charAt(0) - 'A';

				if (currentPlayer == player1) {
					row = entry.charAt(1) - '1';
				} else {
					row = '8' - entry.charAt(1);
				}

				if (!makeMove(row, column)) {
					System.out.println("Not Accepted Value, Try Again!!");
					System.out.print("Player " + currentPlayer + ", enter your position (eg: A1 or H1): ");
				} else {
					break;
				}
			} else {
				System.out.println("The input is not correct, exitting the game!!");
				scanner2.close();
				return;
			}
		}

		switchPlayers();
	}

	public void manualAndAutoMode() { // this method is for the manual player1 and automatic player2 which alternates
										// between them according to the player
		while (!isBoardFull() && !isPlayerWinner(player1) && !isPlayerWinner(player2)) {
			printBoard();
			if (currentPlayer == player1) {
				onePlayerManualMode(); // manual for player1
			} else {
				makeAIMove(2, currentPlayer); // automatic for player2
			}
		}

		printBoard();

		if (isPlayerWinner(player1)) {
			System.out.println("Player ■ wins!");
		} else if (isPlayerWinner(player2)) {
			System.out.println("Player □ wins!");
		} else {
			System.out.println("It's a tie!");
		}
	}

	public void AutoAndManualMode() {// this method is for the manual player1 and automatic player2 which alternates
										// between them according to the player
		while (!isBoardFull() && !isPlayerWinner(player1) && !isPlayerWinner(player2)) {
			printBoard();
			if (currentPlayer == player1) {
				makeAIMove(2, currentPlayer); // player1 automatic
			} else {
				onePlayerManualMode(); // player2 manual
			}
		}

		printBoard();

		if (isPlayerWinner(player1)) {
			System.out.println("Player ■ wins!");
		} else if (isPlayerWinner(player2)) {
			System.out.println("Player □ wins!");
		} else {
			System.out.println("It's a tie!");
		}
	}

	public ArrayList<int[]> getValidMoves() { // this method gathers all possible moves which are valid in the
												// board and saves them in an arrayList
		ArrayList<int[]> validMoves = new ArrayList<>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (makeMove(row, col)) {
					validMoves.add(new int[] { row, col });
					board[row][col] = '-';
				}
			}
		}
		return validMoves;
	}

	public int evaluateBoard() { // this method return an evaluation score of the currentPlayer and opponent and
									// determines the next possible moves to achieve
		int score = 0;
		char opponentPlayer; // finding the currentPlayer and opponent to determine the score accordingly
		if (currentPlayer == player1) {
			opponentPlayer = player2;
		} else {
			opponentPlayer = player1;
		}
		// Evaluate rows
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 4; col++) {
				int currentplayerCount = 0;
				int opponentCount = 0;
				for (int i = col; i < col + 5; i++) {
					if (board[row][i] == currentPlayer) {
						currentplayerCount++;
					} else if (board[row][i] == opponentPlayer) {
						opponentCount++;
					}
				}
				if (currentplayerCount == 5) {
					return Integer.MAX_VALUE; // Current player wins the game
				} else if (opponentCount == 5) {
					return Integer.MIN_VALUE; // Opponent player wins the game
				}
				score += Math.pow(currentplayerCount, 2); // return the score if no one won yet
				score -= Math.pow(opponentCount, 2);
			}
		}
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 4; row++) {
				int currentplayerCount = 0;
				int opponentCount = 0;
				for (int i = row; i < row + 5; i++) {
					if (board[i][col] == currentPlayer) {
						currentplayerCount++;
					} else if (board[i][col] == opponentPlayer) {
						opponentCount++;
					}
				}
				if (currentplayerCount == 5) {
					return Integer.MAX_VALUE;
				} else if (opponentCount == 5) {
					return Integer.MIN_VALUE;
				}
				score += Math.pow(currentplayerCount, 2);
				score -= Math.pow(opponentCount, 2);
			}
		}
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				int currentplayerCount = 0;
				int opponentCount = 0;
				for (int i = 0; i < 5; i++) {
					if (board[row + i][col + i] == currentPlayer) {
						currentplayerCount++;
					} else if (board[row + i][col + i] == opponentPlayer) {
						opponentCount++;
					}
				}
				if (currentplayerCount == 5) {
					return Integer.MAX_VALUE;
				} else if (opponentCount == 5) {
					return Integer.MIN_VALUE;
				}
				score += Math.pow(currentplayerCount, 2);
				score -= Math.pow(opponentCount, 2);
			}
		}
		for (int row = 0; row < 4; row++) {
			for (int col = 4; col < 8; col++) {
				int currentplayerCount = 0;
				int opponentCount = 0;
				for (int i = 0; i < 5; i++) {
					if (board[row + i][col - i] == currentPlayer) {
						currentplayerCount++;
					} else if (board[row + i][col - i] == opponentPlayer) {
						opponentCount++;
					}
				}
				if (currentplayerCount == 5) {
					return Integer.MAX_VALUE;
				} else if (opponentCount == 5) {
					return Integer.MIN_VALUE;
				}
				score += Math.pow(currentplayerCount, 2);
				score -= Math.pow(opponentCount, 2);
			}
		}

		return score;
	}
}
