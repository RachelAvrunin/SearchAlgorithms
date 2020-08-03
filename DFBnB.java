import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * this class represent a DFBnB search for a Colored NxM-tile puzzle 
 * @author Rachel Avrunin
 */
public class DFBnB {
	
	/**
	 * DFBnB search for the lowest cost in a Colored NxM-tile puzzle
	 * @param start represent the board to start with 
	 * @param goal represent the matrix of numbers we need to get to
	 * @param withOpen 
	 * @return result the path from start to the goal, its cost, 
	 * how many boards was made in the search and how much time did it took
	 * @throws Exception if trying to make an illegal move
	 */
	public static Result DFBnB_Search(Board start, int[][] goal, Boolean withOpen) throws Exception{
		//initialize variables
		long startTime= System.currentTimeMillis();
		int numOfVertices=1;
		// check if we started at a goal mode
		if(start.isBoardEqual(goal)) {
			return new Result("",numOfVertices,start.myPrice,0);		
		}
		Stack<Board> stack = new Stack<>();
		Hashtable <String, Board> ht = new Hashtable<String, Board>();
		// insert the start board to the queue
		stack.push(start);
		ht.put(start.toString(), start);
		int thresh; //initialize with n! or maxInteger
		int n=goal.length*goal[0].length;
		if(n<13)
			thresh=fact(n);
		else
			thresh=Integer.MAX_VALUE;
		Result result = new Result("no path", numOfVertices, 0, 0);

		while(!stack.isEmpty()) {
			printOpen(withOpen,stack);
			Board board = stack.pop();
			//if we already visited the board just remove it
			if(board.isOut)
				ht.remove(board.toString());
			else {
				board.isOut=true;
				stack.push(board);
				//create a priority Queue for the sons
				//and a reverse one to put nodes after we checked them
				Comparator<Board> comparator=(Board b1, Board b2)->(b1.f()-b2.f());
				Queue<Board> queue = new PriorityQueue<Board>(comparator);
				Comparator<Board> reverseComparator=(Board b1, Board b2)->(b2.f()-b1.f());
				Queue<Board> reverseQueue = new PriorityQueue<Board>(reverseComparator);
				//for each possible move create a son
				for (int i = 0; i < 4; i++) {
					if(board.possibleMoves[i]) {
						Board son= board.createMove(i);
						numOfVertices++;
						queue.add(son);
					}
				}
				while(!queue.isEmpty()) {
					Board son = queue.poll();
					//if the sons f() function is bigger then the thrash 
					//it means no point on checking him or the sons after him (because they are arranged by f()) 
					if(son.f() >= thresh)
						queue.clear();
					//if it has been created but didnt leave the stack yet 
					//and has a better f() value replace it
					else if(ht.containsKey(son.toString())) {
						Board sonTag=ht.get(son.toString());
						if(!sonTag.isOut && sonTag.f() > son.f()){
							stack.remove(sonTag);
							ht.remove(son.toString());
							reverseQueue.add(son);
						}
					} 
					// if we reached here, f(g) < thresh
					else if(son.isBoardEqual(goal)) { 
						thresh=son.f();
						result.path=son.path();
						result.cost=son.myPrice;
						queue.clear();
					}
					else // if it is not created before and not the goal insert it to the reverse queue
						reverseQueue.add(son);
				}
				while(!reverseQueue.isEmpty()){
					Board son = reverseQueue.poll();
					stack.add(son);
					ht.put(son.toString(), son);
				}

			}
		}
		//after all iterations update the amount of vertices creates and time passed
		result.numOfVertices=numOfVertices;
		result.time=(System.currentTimeMillis()-startTime)/1000.0;
		return result;
	}
	
	/**
	 * if withOpen - prints all the open list from the stack 
	 * @param withOpen boolean that determine if to print open list
	 * @param stack Stack of boards represents the open list
	 */
	private static void printOpen(boolean withOpen, Stack<Board> stack) {
		if(withOpen) {
			System.out.println("open list contains:");
			for (Iterator<Board> itr = stack.iterator(); itr.hasNext();){
				Board b=itr.next();
				if (!b.isOut)
					System.out.println(b+"\n");
			}
		}
	}
	
	/**
	 * calculate the factorial of n
	 * @param n is an int to calculate
	 * @return the factorial value
	 */
	private static int fact(int n) {
		int acc = 1;
		for (int i = 2; i <= n; i++) {
			acc*=i;
		}
		return acc;
	}
}
