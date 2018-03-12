/*
	A command line based Tic-Tac-Toe game 
*/

import java.util.*;
import java.io.*;

public class TicTacToe {
	// the class the board
	class Board {
		char[][] board = new char[3][3];

		Board(char[][] board) {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					this.board[i][j] = board[i][j];
				}
			}
		}

		@Override 
		public boolean equals(Object o) {
			if(o == this) return true;

			// Check if o is an instance of Complex or not
          	// "null instanceof [type]" also returns false 
			if(!(o instanceof Board)) {
				return false;
			}

			Board b = (Board) o;
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					if(board[i][j] != b.board[i][j])
						return false;
				}
			}

			return true;
		}
	}

	private final char[] roles = {'X', 'O'};
	private int turn = 0;

	private Board cur_board;
	private Board blank_board;

	// store all current boards used to go back 
	private Stack<Board> back_stack = new Stack<Board>();
	// store all previous boards used to go forward
	private Stack<Board> forward_stack = new Stack<Board>();

	private boolean new_added = false;
	
	// input buffer
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private void initialization() {
		forward_stack.clear();
		back_stack.clear();
		turn = 0;
		new_added = false;
		
		char[][] board = new char[3][3];
		
		for(int i = 0; i < 3; i++) {
			for(int j =0; j < 3; j++) {
				if(i == 2) {
					board[i][j] = ' ';
				} else {
					board[i][j] = '_';
				}
			}
		}

		cur_board = new Board(board);
		blank_board = new Board(board);
		back_stack.push(new Board(board));
	}

	private void printWelcome() throws IOException{
		System.out.println("Welcome to the command line based Tic-Tac-Toe game!");
		System.out.println("To start with, please select your role: ");
		System.out.println("    1. X");
		System.out.println("    2. O");
		String input = getInput();
		int option = 0;
		while(true) {
			try {
				option = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("Input error, please input digits only!\n");
				System.out.println("# Please select your role: ");
				System.out.println("    1. X");
				System.out.println("    2. O");
				input = getInput();
				continue;
			}
			if(option == 1) {
				System.out.println("# Your role is X");
				break;
			} else if (option == 2) {
				System.out.println("# Your role is O");
				break;
			} else {
				System.out.println("Invalid input, please correctly select your role");
				input = getInput();
			}
		}
		System.out.println("# Role selected. 'X' would go first\n");
	}
		

	private void printOptions(Board board) {
		printBoard(board.board);
		System.out.printf("# Now it is %c's turn. \n", roles[turn]);
		System.out.println("# Please select your next move:");
		System.out.printf("    1. Place %c\n", roles[turn]);
		System.out.println("    2. Go back to previous step");

		// the following should be selected only when the player has gone back
		// for several steps
		System.out.println("    3. Forward to next step");
		System.out.println("    4. Give up, next round");
		System.out.println("    111. Exit game");

	}

	// make this function as simple as possible
	private String getInput() throws IOException{
		System.out.print("  -> ");
		String input = br.readLine();
		return input;
	}

	private void printBoard(char[][] board) {
		System.out.println("# Current board is: ");
		System.out.println("      Column:  1 2 3");
		for(int i = 0; i < 3; i++) {
			StringBuffer str= new StringBuffer("      Row:  " + (i+1) + " ");
			for(int j = 0; j < 3; j++) {
				if(j == 0) {
					str.append(" " + board[i][j]);

				} else {
					str.append("|" + board[i][j]);
				}
				
			}
			System.out.println(str);
		}
	}

	private int checkEndOfGame(Board board) {
		int winner = -1;
		
		// check X roles 
		if(findLine('X', board)) {
			return 0;
		} else if(findLine('O', board)) {
			return 1;
		} 
		
		// now check to see if the board is full 
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(board.board[i][j] == ' ' || board.board[i][j] == '_') {
					// board is not full, can continue
					return -1;
				}
			}
		}
		// board is full, no winner
		return 2;

	}
	
	private boolean findLine(char role, Board board) {
		// the simple way is just to check each row and each column, and the diagonal
		
		//System.out.println(role);
		boolean line_found = false;
		
		// for each row
		for(int i = 0; i < board.board.length; i++) {
			line_found = true;
			for(int j = 0; j < board.board[0].length; j++) {
//				System.out.print(board.board[i][j]);
				if(board.board[i][j] != role) {
					line_found = false;
					break;
				}
			}
			
			if(line_found) {
				return line_found;
			}
		}
		
		// for each column 
		for(int j = 0; j < board.board[0].length; j++) {
			line_found = true;
			for(int i = 0; i < board.board.length; i++) {
				if(board.board[i][j] != role) {
					line_found = false;
					break;
				}
			}
			if(line_found) return line_found;
		}
		
		
		// for diagonal
		line_found = true;
		for(int i = 0; i < board.board.length; i++) {
			if(board.board[i][i] != role) {
				line_found = false;
				break;
			}
		}
		if(line_found) return line_found;

		// for reverse diagonal
		line_found = true;
		for(int i = 0; i < board.board.length; i++) {
			//System.out.println("i is: " + i + ", " + board.board[i][board.board.length - i - 1]);
			if(board.board[i][board.board.length - i - 1] != role) {
				line_found = false;
				break;
			}
			
		}
		// System.out.println();
		return line_found;
	}

	private void placeX(Board board) throws IOException{
		// get input and place the input to the board 
		System.out.printf("## Please input [row column] to place %c:\n", roles[turn]);
		String input = getInput();
		String[] s = input.split(" ");
		int row = 0;
		int col = 0;
		while(true) {
			if(s.length != 2) {
				System.out.println("## invalud input, plese input valid [row column], separate using space");
				input = getInput();
				s = input.split(" ");
				continue;
			}

			try {
				row = Integer.parseInt(s[0]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("Row input error, please input [row, column], digits only!");
				input = getInput();
				s = input.split(" ");
				continue;
			}
			try {
				col = Integer.parseInt(s[1]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("Column input error, please input [row, column], digits only!");
				input = getInput();
				s = input.split(" ");
				continue;
			}

			if(row < 1 || row > 3 || col < 1 || col > 3) {
				System.out.println("Row or Column does not exist, please input correct [row, column]!");
				input = getInput();
				s = input.split(" ");
				continue;
			}

			// finally we get a correct input
			if(board.board[row-1][col-1] != ' ' && board.board[row-1][col-1] != '_') {
				System.out.println("The position has been taken, please input another [row, column]!");
				input = getInput();
				s = input.split(" ");
				continue;
			}
			// finally the input is correct 
			break;
		}
		// finally we get a valid input
		board.board[row-1][col-1] = roles[turn];
		turn++;
		turn = turn % 2;
		
	}

	/*
	 * Start game
	*/
	public void start() throws IOException{

		initialization();

		printWelcome();

		printOptions(cur_board);

		String input = getInput();
		int option;

		Board tmp;

		while(true) {
			try {
				option = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("Input error, please input digits only!\n");

				printOptions(cur_board);
				input = getInput();
				continue;
			}
			switch(option) {
				case 1:
					// select to place the position
					placeX(cur_board);
					// Also need to check whether the game has been ended
					int winner = checkEndOfGame(cur_board);
					if(winner < 0 ){
						// Game has not end, put current board into the back stack 
						Board new_board = new Board(cur_board.board);
						back_stack.push(new_board);
						new_added = true;
						System.out.println("-- back_stack size: " + back_stack.size());
						// should clear the forward_stack
						forward_stack.clear();
					} else {
						if(winner == 2) {
							System.out.println("## Game ended, no winner\n");
							
						} else {
							System.out.printf("## Game ended, the winner is: %c\n", roles[winner]);
						}
						
						System.exit(0);
					}
					break;
				case 2:
					if(back_stack.isEmpty()) {
						System.out.println("No previous steps left");
					} else {
						if(!cur_board.equals(blank_board)) {
							forward_stack.push(new Board(cur_board.board));
						}
						
						if(new_added) {
							// recently added a board 
							// pop the current board 
							back_stack.pop();
							
							// keep the turn
							turn++;
							turn = turn % 2;
							// Get the previous board 
							cur_board = back_stack.pop();
						} else {
							cur_board = back_stack.pop();
							if(back_stack.isEmpty()) {
								// now the game goes to the very beginning
								// the back stack is empty, we store a empty board to it 
								// no need to store it in the forward stack
								back_stack.push(new Board(cur_board.board));
								turn = 0;
							} else {
								turn++;
								turn = turn % 2;
							}
						}

						// System.out.println("-- back_stack size: " + back_stack.size());
						// System.out.println("-- forward_stack size: " + forward_stack.size());
						
						new_added = false;
					}
					
					break;
				case 3:
					if(forward_stack.isEmpty()) {
						System.out.println("No next steps left");
					} else {
						cur_board = forward_stack.pop();
						// also store the previous steps in to the forward stack
						back_stack.push(new Board(cur_board.board));
						// System.out.println(" --back_stack size: " + back_stack.size());
						// System.out.println(" --forward_stack size: " + forward_stack.size());
						
						if(forward_stack.isEmpty()) {
							// the most recent board has been removed
							new_added = true;
						}
						turn++;
						turn = turn % 2;
					}
					break;
				case 4:
					System.out.printf("Player %c Give up, start next round!\n\n", roles[turn]);
					initialization();
					printWelcome();
					break;
				case 111:
					System.exit(0);
					break;
				default:
					System.out.println("Input error, please input valid option\n");
					break;
			}
			printOptions(cur_board);
			input = getInput();
		}


	}
	public static void main(String[] args) throws Exception{
		TicTacToe tictactoe = new TicTacToe();

		tictactoe.start();
//		char[][] tmp = { {'O','X','X'},
//						 {'O','X','O'},
//						 {'X','O','X'}
//						};
//		Board tmp_board = tictactoe.new Board(tmp);
//		System.out.println(tictactoe.checkEndOfGame(tmp_board));

	}
}
