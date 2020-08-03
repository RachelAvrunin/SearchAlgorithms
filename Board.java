import java.awt.Color;
import java.util.Arrays;

/**
 * this class represent the board game, it has a matrix of numbers,
 * a matrix of colors, how much it cost to get to the stage and so on
 * @author Rachel Avrunin
 */
public class Board{

	final int redValue = 30;
	final int greenValue = 1;
	final int blackValue = -1; //error - cannot be moved

	Board parent;
	String reachWith;
	int[][] board;
	Color [][] colors;
	Boolean [] possibleMoves=new Boolean[4];
	int emptyIn[]=new int[2];
	int myPrice;
	Boolean isOut=false;

	/**
	 * initial the first board and its possible moves
	 * @param board matrix of numbers
	 * @param colors matrix of colors
	 */
	public Board(int[][] board, Color [][] colors){
		this.parent=null;
		int height = board.length;
		int width = board[0].length;
		this.board=new int[height][width];
		this.colors=new Color[height][width];

		for (int i = 0 ; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.board[i][j]=board[i][j];
				this.colors[i][j]=colors[i][j];
				if(this.board[i][j]==0) { // if its the empy place
					this.emptyIn[0]=i;
					this.emptyIn[1]=j;
				}	
			}
		}
		myPrice=0;
		reachWith=null;
		updatePossibleMoves();
	}

	/**
	 * copy a board 
	 * @param other is a board to copy from
	 */
	public Board(Board other){
		this.parent=null;
		int height = other.board.length;
		int width = other.board[0].length;
		this.board=new int[height][width];
		this.colors=new Color[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.board[i][j]=other.board[i][j];
				this.colors[i][j]=other.colors[i][j];
				if(this.board[i][j]==0) { // if its the empy place
					this.emptyIn[0]=i;
					this.emptyIn[1]=j;
				}	
			}
		}
		this.myPrice=other.myPrice;
	}

	/**
	 * calculate which state can be continue from here
	 */
	private void updatePossibleMoves() {
		int height = colors.length;
		int width = colors[0].length;
		//left
		possibleMoves[0] = (emptyIn[1] == width-1 || (emptyIn[1]+1 < width && colors[emptyIn[0]][emptyIn[1]+1]==Color.black))? false: true;
		possibleMoves[0] = (reachWith!=null && reachWith.charAt(reachWith.length()-1)=='R')? false :possibleMoves[0];
		//up
		possibleMoves[1] = (emptyIn[0] == height-1 || (emptyIn[0] +1 < height && colors[emptyIn[0]+1][emptyIn[1]]==Color.black))? false: true;
		possibleMoves[1] = (reachWith!=null && reachWith.charAt(reachWith.length()-1)=='D')? false :possibleMoves[1];
		//right
		possibleMoves[2] = (emptyIn[1] == 0 || (emptyIn[1]-1 >=0 && colors[emptyIn[0]][emptyIn[1]-1]==Color.black))? false: true;
		possibleMoves[2] = (reachWith!=null && reachWith.charAt(reachWith.length()-1)=='L')? false :possibleMoves[2];
		//down
		possibleMoves[3] = (emptyIn[0] == 0 || (emptyIn[0]-1 >=0 && colors[emptyIn[0]-1][emptyIn[1]]==Color.black))? false: true;
		possibleMoves[3] = (reachWith!=null && reachWith.charAt(reachWith.length()-1)=='U')? false :possibleMoves[3];
	}

	/**
	 * check if the number matrix is identical
	 * @param other number matrix
	 * @return true if equal or false otherwise
	 */
	public Boolean isBoardEqual(int [][] other) {
		Boolean flag=true;
		for (int i = 0; i < other.length && flag; i++) {
			for (int j = 0; j < other[0].length; j++) {
				if(this.board[i][j] != other[i][j])
					flag=false;
			}
		}
		return flag;
	}

	/**
	 * create a board which is represent a single move 
	 * @param moveTo represent where to move from, 
	 * 0 states left, 1 is up, 2 is right and 3 is down
	 * @return a new board after the move was made
	 * @throws Exception if sent an illegal move 
	 */
	public Board createMove(int moveTo) throws Exception {
		Board newMove=new Board(this);
		newMove.parent=this;
		Color c =null;
		switch(moveTo){
		case 0: //go left
			newMove.board[emptyIn[0]][emptyIn[1]] = this.board[emptyIn[0]][emptyIn[1]+1];
			newMove.board[emptyIn[0]][emptyIn[1]+1] = 0;
			c = this.colors[emptyIn[0]][emptyIn[1]+1];
			newMove.colors[emptyIn[0]][emptyIn[1]] = c;
			newMove.colors[emptyIn[0]][emptyIn[1]+1] = null;
			newMove.emptyIn[1]++;
			newMove.reachWith=this.board[emptyIn[0]][emptyIn[1]+1]+"L";
			break;
		case 1: //go up
			newMove.board[emptyIn[0]][emptyIn[1]] = this.board[emptyIn[0]+1][emptyIn[1]];
			newMove.board[emptyIn[0]+1][emptyIn[1]] = 0;
			c = this.colors[emptyIn[0]+1][emptyIn[1]];
			newMove.colors[emptyIn[0]][emptyIn[1]] = c;
			newMove.colors[emptyIn[0]+1][emptyIn[1]] = null;
			newMove.emptyIn[0]++;
			newMove.reachWith=this.board[emptyIn[0]+1][emptyIn[1]]+"U";
			break;
		case 2: //go right
			newMove.board[emptyIn[0]][emptyIn[1]] = this.board[emptyIn[0]][emptyIn[1]-1];
			newMove.board[emptyIn[0]][emptyIn[1]-1] = 0;
			c = this.colors[emptyIn[0]][emptyIn[1]-1];
			newMove.colors[emptyIn[0]][emptyIn[1]] = c;
			newMove.colors[emptyIn[0]][emptyIn[1]-1] = null;
			newMove.emptyIn[1]--;
			newMove.reachWith=this.board[emptyIn[0]][emptyIn[1]-1]+"R";
			break;
		case 3: //go down
			newMove.board[emptyIn[0]][emptyIn[1]] = this.board[emptyIn[0]-1][emptyIn[1]];
			newMove.board[emptyIn[0]-1][emptyIn[1]] = 0;
			c = this.colors[emptyIn[0]-1][emptyIn[1]];
			newMove.colors[emptyIn[0]][emptyIn[1]] = c;
			newMove.colors[emptyIn[0]-1][emptyIn[1]] = null;
			newMove.emptyIn[0]--;
			newMove.reachWith=this.board[emptyIn[0]-1][emptyIn[1]]+"D";
			break;
		default:
			throw new Exception("there is onely 4 possible moves!");
		}

		if (c == Color.red)
			newMove.myPrice+=redValue;
		else if (c == Color.green)
			newMove.myPrice+=greenValue;
		else 
			throw new Exception("cannot move black!");
		newMove.updatePossibleMoves();
		return newMove;
	}

	/**
	 * calculate the path to the board by using reachWith 
	 * which is what move was made to get to this point
	 * and the parent's path recursively
	 * @return a string of the path
	 */
	public String path() {
		String thisPath="";
		if (parent!=null)
			if (parent.parent==null)
				thisPath = reachWith;
			else
				thisPath = parent.path()+"-"+reachWith;
		return thisPath;
	}

	/**
	 * heuristic function for the A*, IDA* and DFBnB algorithms.
	 * it helps estimate a board distance from the goal.
	 * I use the Manhattan distance with multiplying the cost of the move
	 * 
	 * @return the sum of distances from the goal
	 */
	public int heuristic() {  //weighted Manhattan
		int price = 0;
		int col = board[0].length;
		int value =0;
		int colorValue=0;
		int expected=0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				expected++;
				value = board[i][j];

				if (value != 0 && value != expected) {
					value--;
					if(colors[i][j]==Color.green)
						colorValue=greenValue;
					else if(colors[i][j]==Color.red)
						colorValue=redValue;
					else
						return Integer.MAX_VALUE; //cannot move black

					int expectedI = value / col;
					int expectedJ = value % col;
					price+= colorValue*(Math.abs(i-expectedI) + Math.abs(j-expectedJ));
				}
			}
		}
		return price;
	}
	
	/**
	 * calculate the heuristic value + the price of the board 
	 * @return f() function of the board
	 */
	public int f() { 
		return myPrice+heuristic();
	}

	/**
	 * returns a string representing the board.
	 * @return a string representation of the number matrix.
	 */
	@Override
	public String toString() {
		String res="";
		for (int i = 0; i < board.length; i++) {
			res+= Arrays.toString(board[i])+"\n";
		}
		return res.substring(0, res.length()-1);
	}
}