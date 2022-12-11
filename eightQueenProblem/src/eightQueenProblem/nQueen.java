/*
 * Ethan Gutknecht
 * CSE486 - AI
 * 10/27/2022
 * 
 * N Queen Problem Java File
 */

package eightQueenProblem;

public class nQueen {
	// Global Variables
	static int boardDimentions = 7;
	// These arrays will help with negative index cases since we are using recursion
	static int []leftDiag = new int[boardDimentions * boardDimentions];
	static int []rightDiag = new int[boardDimentions * boardDimentions];
	static int []center = new int[boardDimentions * boardDimentions];
	
	/*
	 * DOCUMENTATION: main
	 */
	public static void main(String[] args) {
		int[][] chessBoard = createBoard(boardDimentions);
		
		// Print board before modifications
		printChessBoard(chessBoard);
		System.out.println("---------------------------------");
		
		// There is no answer that is correct if
		// the function returns false, otherwise true
		if (nQueenProblem(chessBoard, 0)) {
			printChessBoard(chessBoard);
        } else {
        	System.out.println("No answer with this size board.");
        }
	}

	/*
	 * DOCUMENTATION: nQueenProblem
	 * 
	 * I had some help online so here are my references for that:
	 * 		https://developers.google.com/optimization/cp/queens
	 * 		https://www.javatpoint.com/n-queens-problems --------(This one helped visually!!!!)
	 *		https://www.geeksforgeeks.org/n-queen-problem-backtracking-3/
	 * 
	 * This was pretty difficult for me to understand for a bit. 
	 * JavaPoint's visuals on this algorithm helped a lot. I ended
	 * up using the back tracing approach witch is what was used
	 * in class and recommended online. I am very happy with how
	 * it turned out.
	 * 
	 */
	public static boolean nQueenProblem(int[][] chessBoard, int column) {
		// base case
		if (column >= chessBoard.length) {
			return true;
		}

		// recursion case
	   for (int i = 0; i < chessBoard.length; i++)
	    {
		   int centerIndex = i;
		   int currentLeftDiagIndex = i - column + chessBoard.length - 1;
		   int currentRightDiagIndex = i + column;
		   // Checks the constraints left diag, right diag, can row. If is able to place
		   // chess piece then attempt to find a solution with this branch.
	        if ( center[centerIndex] != 1 && leftDiag[currentLeftDiagIndex] != 1 && rightDiag[currentRightDiagIndex] != 1)
	        {
	            // Place the queen on the board
	        	// at this position. Also mark it down
	        	// in the arrays that we created above
	        	
	        	chessBoard[i][column] = 1;
	        	leftDiag[currentLeftDiagIndex] = 1;
	        	rightDiag[currentRightDiagIndex] = 1;
	        	center[centerIndex] = 1;

	        	// If recursion returns no solution then remove entry from board
	            if (!nQueenProblem(chessBoard, column + 1)) {
	            	// Back track and remove the queen from
	            	// the current position from saved values.
	            	
		            chessBoard[i][column] = 0;
		            leftDiag[currentLeftDiagIndex] = 0;
		            rightDiag[currentRightDiagIndex] = 0;
		            center[centerIndex] = 0;
	            } else return true;
	        }
	    }

	    return false;
	}

	/*
	 * DOCUMENTATION: createRandomBoard
	 * This function will take an integer and return a 2D
	 * integer array that will represent our chess board.
	 * This chess board will have only one "1" per row
	 * that will represent our queen. For example, if boardDimentions
	 * was equal to 3:
	 *
	 * [[0, 1, 0],
	 * 	[1, 0, 0],
	 * 	[1, 0, 0]]
	 *
	 * When printed, it should look like this:
	 *
	 * | 0 | 1 | 1 |
	 * | 1 | 0 | 0 |
	 * | 0 | 0 | 0 |
	 */
	public static int[][] createBoard(int boardDimentions) {
		int[][] chessBoard = new int[boardDimentions][boardDimentions];

		for (int i = 0 ; i < chessBoard.length; i++) {
			for (int j = 0 ; j < chessBoard[i].length; j++) {
				chessBoard[i][j] = 0;
			}
		}
		return chessBoard;
	}

	
	/*
	 * DOCUMENTATION: printChessBoard
	 * This function will take in a NxN 2D array and
	 * print it like it is a chess board. Rotating it
	 * 90 degrees. For example, 3x3:
	 *
	 * [[0, 1, 0],
	 * 	[1, 0, 0],
	 * 	[1, 0, 0]]
	 *
	 * will print to be this:
	 *
	 * | 0 | 1 | 1 |
	 * | 1 | 0 | 0 |
	 * | 0 | 0 | 0 |
	 */
	public static void printChessBoard(int[][] chessBoard) {
		String[] s = new String[chessBoard.length];

		for (int i = 0 ; i < chessBoard.length; i++) {
			for (int j = 0 ; j < chessBoard[i].length; j++) {
				if (i == 0) {
					s[j] = " | " + chessBoard[i][j] + " | ";
				} else {
					s[j] += chessBoard[i][j] + " | ";
				}
			}
		}
		for (String str : s) {
			System.out.println(str);
		}
	}
}
