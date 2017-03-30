import java.util.*;
/**
 * Class that implements a Knight Travel problem
 * @author		Fernando López Ostenero
 * @version		1.0
 *
 */
public class KnightTravelProblem {
	
	/**
	 * Internal class that implements a Square of a board
	 * as a pair of (file,rank) coordinates
	 * @author		Fernando López Ostenero
	 * @version		1.0
	 */
	private class Square {
		private int file;
		private int rank;
		/**
		 * Class constructor creates a new Square.
		 * @param r		rank of the new Square
		 * @param f		file of the new Square
		 */
		public Square(int f, int r) {
			file = f;
			rank = r;
		}
		/**
		 * Getter that returns the rank of the Square
		 * @return	an int representing the rank of the Square
		 */
		public int getRank() {
			return rank;
		}
		/**
		 * Getter that returns the file of the Square
		 * @return	an int representing the file of the Square
		 */
		public int getFile() {
			return file;
		}
	}

	// Attributes that define the Knight Travel problem 
	private int size;					// Board size
	private Square initSquare;			// Initial Square of the Knight
	
	// Attributes used to solve the problem
	private boolean[][] board;			// Stores the status (free or not) of each Square
	private Square currentSquare;		// Current Square of the Knight
	private ArrayList<Square> path;		// Building Path
	private ArrayList<Square> solution;	// Full Solution
	private boolean found;				// A boolean to indicate if a solution has been found
	
	/**
	 * Class Constructor creates a new KnightTravel object problem.
	 * @param s						size of the board
	 * @param f						file of the initial knight position
	 * @param r						rank of the initial knight position
	 * @throws OutOfBoardException	if the initial Square is out of the board
	 */
	public KnightTravelProblem(int s,int f, int r) throws OutOfBoardException {
		// Initialize only the attributes to define the problem
		size = s;								// Board size
		if ( inBoard(f,r) ) {
			// If initial Square is inside board...
			initSquare = new Square(f,r);		// ...set initial Square of the problem
		} else {
			throw new OutOfBoardException();	// Error: initial Square out of board
		}
	}
	/**
	 * Main method to solve the problem. Initializes the attributes to solve the problem
	 * and calls {@link #bt(Square)} to find the solution applying backtracking.
	 */
	public void knightTravel() {
		// Initialize the attributes to solve the problem
		initBoard();						// Init board (all Squares are free)               
		currentSquare = null;				// Knight is not on board (yet)
		path = new ArrayList<Square>();		// Building Path is empty
		solution = new ArrayList<Square>();	// There's no solution yet, so empty path
		found = false;						// Solution not found yet
		bt(initSquare);						// Init backtracking with the initial Square
	}
	/**
	 * Obtains a string with the first solution found to the 
	 * current KnightTravel problem
	 * 
	 * @return		a String with the solution found by buildPath()
	 * @see			#solution
	 * @see			#knightTravel()			
	 */
	public String printSolution() {
		StringBuffer output = new StringBuffer();			// New String Buffer
		if ( solution != null ) {
			// Execute only if there's a solution (may be empty)
			output.append("[");
			Iterator<Square> it = this.solution.iterator();
			while( it.hasNext() ) {
				// Print each Square as (file,rank)
				Square s = it.next();
				output.append("("+ s.getFile() + "," + s.getRank() +")");
				if (it.hasNext()) { output.append(","); }
			}
			output.append("]");
		}
		return output.toString();							// Return String Buffer as String
	}	
	/**
	 * Builds the path applying backtracking scheme.
	 * Called by:
	 * <ul>
	 * <li>{@link #knightTravel()}</li>
	 * </ul>
	 * @param newSquare		the new Square to be visited
	 * @see					#knightTravel()
	 */
	private void bt(Square newSquare) {
		visitSquare(newSquare);					// Visits the new Square
		if ( !found ) {
			// Execute only if no solution has been found
			if ( fullPath() ) {
				// Execute only if the new visited Square completes the path
				found = true;							// Solution found
				solution = new ArrayList<Square>(path); // Store solution
			} else {
				ArrayList<Square> next = validSquares();	// List of valid Squares to jump
				Iterator<Square> it = next.iterator();
				while ( it.hasNext() && !found ) {
					// Execute only while there are more items and no solution has been found
					bt(it.next());						// Backtracking
				}
			}
		}
		freeSquare();							// Frees the last visited Square
	}
	/**
	 * Initializes the board. All Squares are marked as free (true)
	 * Called by:
	 * <ul>
	 * <li>{@link #knightTravel()}</li>
	 * </ul>
	 */
	private void initBoard() {
		// Creates the board as bi-dimensional array
		board = new boolean[size][size];
		for ( int auxF = 0 ; auxF < size ; auxF++ ) {
			for ( int auxR = 0 ; auxR < size ; auxR++ ) {
				board[auxF][auxR] = true; 				// Free Square (can be visited)
			}
		}
	}
	/**
	 * Visits a new Square:
	 * <ul>
	 * <li>Updates current Square as the new Square</li>
	 * <li>Marks Square as not free on the board</li>
	 * <li>Adds the Square to the building path</li>
	 * </ul>
	 * Called by:
	 * <ul>
	 * <li>{@link #bt(Square)}</li>
	 * </ul>
	 * @param s		the new Square to visit
	 * @see			#bt(Square)
	 */
	private void visitSquare(Square s) {
		currentSquare = s;								// Updates current Square
		board[s.getFile()-1][s.getRank()-1] = false;	// Marks Square as not free
		path.add(new Square(s.getFile(),s.getRank()));	// Adds the Square to the building path
	}
	/**
	 * Frees the last visited Square:
	 * <ul>
	 * 	<li>Marks Square as free on the board</li>
	 * 	<li>Updates current Square as the previous visited Square (if exists)</li>
	 *  <li>Erases last visited Square from building path</li>
	 * </ul>
	 * Called by:
	 * <ul>
	 * <li>{@link #bt(Square)}</li>
	 * </ul>
	 * @see			#bt(Square)
	 */
	// Libera la última posición visitada
	private void freeSquare() {
	 	// Marks Square as free on the board
		board[currentSquare.getFile()-1][currentSquare.getRank()-1] = true;
		if ( path.size() > 1 ) {
			// If there are more than one Squares on building path, there's a previous Square
			currentSquare = path.get(path.size()-2);	// Previous visited Square
		} else {
			// If there are just one Square on building path, there's no previous Square
			currentSquare = null;						// Knight is not on the board
		}
		path.remove(path.size()-1);						// Erases last visited Square
	}
	
	/**
	 * Checks if building path has a full solution to the problem. 
	 * Called from {@link #bt(Square)}
	 * @return		a boolean that indicates if building path is a solution or not
	 * @see			#bt(Square)
	 */
	private boolean fullPath() {
		return path.size() == size*size;
	}
	/**
	 * Creates a list of valid Squares to whom Knight can jump from current Square. 
	 * Called by:
	 * <ul>
	 * <li>{@link #bt(Square)}.</li>
	 * </ul>
	 * @return		an ArrayList of valid Squares 
	 * @see			#bt(Square)
	 * @see			#validSquare(int, int)
	 */
	private ArrayList<Square> validSquares() {
		ArrayList<Square> output = new ArrayList<Square>();
		int f = currentSquare.getFile();
		int r = currentSquare.getRank();
		// Checks the (maximum) 8 Squares to which a Knight can jump 
		if ( validSquare(f+2,r+1) ) { output.add(new Square(f+2,r+1)); }
		if ( validSquare(f+1,r+2) ) { output.add(new Square(f+1,r+2)); }
		if ( validSquare(f-1,r+2) ) { output.add(new Square(f-1,r+2)); }
		if ( validSquare(f-2,r+1) ) { output.add(new Square(f-2,r+1)); }
		if ( validSquare(f-2,r-1) ) { output.add(new Square(f-2,r-1)); }
		if ( validSquare(f-1,r-2) ) { output.add(new Square(f-1,r-2)); }
		if ( validSquare(f+1,r-2) ) { output.add(new Square(f+1,r-2)); }
		if ( validSquare(f+2,r-1) ) { output.add(new Square(f+2,r-1)); }
		return output;
	}
	/**
	 * Checks if a Square is a valid Square to jump to:
	 * <ul>
	 * <li>It's inside the board</li>
	 * <li>It's free</li>
	 * </ul>
	 * Called by:
	 * <ul>
	 * <li>{@link #validSquares()}</li>
	 * </ul>
	 * @param f		file of the Square to be checked
	 * @param r		rank of the Square to be checked
	 * @return		a boolean that indicates if the Square is or not valid to jump to
	 * @see			#inBoard(int, int)
	 * @see			#validSquares()
	 */
	private boolean validSquare(int f, int r) {
		 return inBoard(f,r) && board[f-1][r-1];
	}
	/**
	 * Checks if a Square is inside the board. 
	 * Called by:
	 * <ul>
	 * <li>{@link #KnightTravelProblem(int, int, int)}</li>
	 * <li>{@link #validSquares()}</li>
	 * </ul>
	 * @param f		file of the Square to be checked
	 * @param r		rank of the Square to be checked
	 * @return		a boolean that indicates if the Square is or not inside the board
	 */
	private boolean inBoard(int f,int r) {
		return ( (1<=f) & (f<=size) && (1<=r) && (r<=size) );
	}
}
