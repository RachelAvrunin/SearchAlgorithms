import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * this class represent an A* search for a Colored NxM-tile puzzle 
 * @author Rachel Avrunin
 */
public class AStar {

	/**
	 * A* search for the lowest cost in a Colored NxM-tile puzzle
	 * @param start represent the board to start with 
	 * @param goal represent the matrix of numbers we need to get to
	 * @param withOpen states if to print openList to screen or not
	 * @return result the path from start to the goal, its cost, 
	 * how many boards was made in the search and how much time did it took
	 * @throws Exception if trying to make an illegal move
	 */
	public static Result AStar_Search(Board start, int[][] goal, boolean withOpen) throws Exception{
		//initialize variables
		long startTime= System.currentTimeMillis();
		int numOfVertices=1;
		Hashtable <String, Board> ht = new Hashtable<String, Board>();
		Comparator<Board> comparator=(Board b1, Board b2)->(b1.f()-b2.f());
		Queue<Board> queue = new PriorityQueue<Board>(comparator);
		// insert the start board to the queue
		queue.add(start);
		ht.put(start.toString(), start);
		while (!queue.isEmpty()) {
			printOpen(withOpen, queue);
			Board board = queue.poll();
			//unlike BFS A* checks board as it comes out from the queue and not after creating the sons
			if(board.isBoardEqual(goal)) {
				//checking how much time it takes
				double timeTook=(System.currentTimeMillis()-startTime)/1000.0; 
				return new Result(board.path(),numOfVertices,board.myPrice,timeTook);
			}
			for (int i = 0; i < 4; i++) {
				//create all possible sons
				if(board.possibleMoves[i]) {
					Board son= board.createMove(i);
					numOfVertices++;
					//if already exist continue to next move
					if(!ht.containsKey(son.toString())) {
						ht.put(son.toString(), son);
						queue.add(son);
					}
					// if already created but with a higher price replace it.
					else if(ht.get(son.toString()).myPrice > son.myPrice) {
						boolean found=false;
						for (Iterator<Board> itr = queue.iterator(); found && itr.hasNext();) {
							Board b = itr.next();
							if (b.toString().equals(son.toString())) {
								queue.remove(b);
								queue.add(son);
								found=true;
							}
						}
					}
				}
			}
		}
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