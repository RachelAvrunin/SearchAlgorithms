import java.util.Hashtable;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;

/**
 * this class represent a BFS search for a Colored NxM-tile puzzle 
 * @author Rachel Avrunin
 */
public class BFS {
	
	/**
	 * BFS search for the shortest way in a NxM-tile puzzle
	 * @param start represent the board to start with
	 * @param goal represent the matrix of numbers we need to get to
	 * @param withOpen states if to print openList to screen or not
	 * @return result the path from start to the goal, its cost, 
	 * how many boards was made in the search and how much time did it took
	 * @throws Exception if trying to make an illegal move
	 */
	public static Result BFS_Search(Board start, int[][] goal, boolean withOpen) throws Exception{
		//initialize variables
		long startTime= System.currentTimeMillis();
		int numOfVertices=1;
		// check if we started at a goal mode
		if(start.isBoardEqual(goal)) {
			return new Result("",numOfVertices,start.myPrice,0);			
		}
		Hashtable <String, Board> ht = new Hashtable<String, Board>();
		Queue <Board> queue = new LinkedList<Board>();
		// insert the start board to the queue
		queue.add(start);
		ht.put(start.toString(), start);
		while (!queue.isEmpty()) {
			printOpen(withOpen, queue);
			Board board = queue.poll();
			//for each possible move create a son
			for (int i = 0; i < 4; i++) {
				if(board.possibleMoves[i]) {
					Board son= board.createMove(i);
					numOfVertices++;
					//if already exist continue to next move
					if(!ht.containsKey(son.toString())) {
						//check if son is goal
						if(son.isBoardEqual(goal)) {
							// return the goal 
							double timeTook=(System.currentTimeMillis()-startTime)/1000.0;
							return new Result(son.path(),numOfVertices,son.myPrice,timeTook);
						}
						//else insert it to queue and hashTable
						queue.add(son);
						ht.put(son.toString(), son);
					}
				}
			}
		}
		// after going on all possible moves if goal not found return no path
		return new Result("no path", numOfVertices, 0, 0);
	}
	
	/**
	 * if withOpen - prints all the open list from the queue 
	 * @param withOpen boolean that determine if to print open list
	 * @param queue Queue of boards represents the open list
	 */
	private static void printOpen(boolean withOpen, Queue<Board> queue) {
		if(withOpen) {
			System.out.println("open list contains:");
			for (Iterator<Board> itr = queue.iterator(); itr.hasNext();){
				Board b=itr.next();
				System.out.println(b+"\n");
			}
		}
	}
}