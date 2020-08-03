import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

/**
 * this class represent a IDA* search for a Colored NxM-tile puzzle 
 * @author Rachel Avrunin
 */
public class IDAStar {

	/**
	 * IDA* search for the lowest cost in a Colored NxM-tile puzzle
	 * @param start represent the board to start with 
	 * @param goal represent the matrix of numbers we need to get to
	 * @param withOpen 
	 * @return result the path from start to the goal, its cost, 
	 * how many boards was made in the search and how much time did it took
	 * @throws Exception if trying to make an illegal move
	 */
	public static Result IDAStar_Search(Board start, int[][] goal, Boolean withOpen) throws Exception{
		//initialize variables
		long startTime= System.currentTimeMillis();
		int numOfVertices=1;
		// check if we started at a goal mode
		if(start.isBoardEqual(goal)) {
			return new Result("",numOfVertices,start.myPrice,0);
		}
		Hashtable <String, Board> ht = new Hashtable<String, Board>();
		Stack<Board> stack = new Stack<>();	
		//initial trash to the heuristic function because its a lower bound
		int thresh =start.heuristic();
		//as long as we don't find an answer we keep increasing trash until we find
		while (thresh != Integer.MAX_VALUE) {
			int minF = Integer.MAX_VALUE;
			//initial start board
			start.isOut=false;
			// insert the start board to the queue
			stack.push(start);
			ht.put(start.toString(), start);
			while(!stack.isEmpty()){
				printOpen(withOpen, stack);
				Board board = stack.pop();
				//if we already visited the board just remove it
				if(board.isOut)
					ht.remove(board.toString());
				else {
					board.isOut=true;
					stack.push(board);
					for (int i = 0; i < 4; i++) {
						//create all possible sons
						if(board.possibleMoves[i]){
							Board son = board.createMove(i);
							numOfVertices++;
							int f=son.f();
							//this is not an answer, but if we dont find an answer we update the thresh
							if (f>thresh)
								minF = Math.min(minF, f);
							//check if son already existed 
							else if(ht.containsKey(son.toString())){
								Board sonTag=ht.get(son.toString());
								//if it has been created but didn't leave the stack yet 
								//and has a better f() value replace it
								if(!sonTag.isOut && sonTag.f() > f){
									stack.remove(sonTag);
									ht.remove(son.toString());
								}
							}
							else{ // if ht not contains son
								if(son.isBoardEqual(goal)){
									double timeTook=(System.currentTimeMillis()-startTime)/1000.0;
									return new Result(son.path(),numOfVertices,son.myPrice,timeTook);
								}
								stack.push(son);
								ht.put(son.toString(),son);
							}
						}
					}
				}
			}
			//to avoid unnecessary loops
			if(thresh!=minF)
				thresh = minF;
			else
				thresh=Integer.MAX_VALUE;
		}
		return new Result("no path", numOfVertices, 0, 0);
	}
	
	/**
	 * if withOpen - prints all the open list from the stack 
	 * @param withOpen boolean that determine if to print open list
	 * @param stack Stack of boards represents the open list
	 */
	private static void printOpen(Boolean withOpen, Stack<Board> stack) {
		if(withOpen) {
			System.out.println("open list contains:");
			for (Iterator<Board> itr = stack.iterator(); itr.hasNext();){
				Board b=itr.next();
				if (!b.isOut)
					System.out.println(b+"\n");
			}
		}
	}
}