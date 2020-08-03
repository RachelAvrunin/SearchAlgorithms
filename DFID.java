import java.util.Hashtable;

/**
 * this class represent a DFID search for a Colored NxM-tile puzzle 
 * @author Rachel Avrunin
 */
public class DFID {

	/**
	 * DFID search for the shortest way in a NxM-tile puzzle
	 * @param start represent the board to start with 
	 * @param goal represent the matrix of numbers we need to get to
	 * @param withOpen states if to print openList to screen or not
	 * @return result the path from start to the goal, its cost, 
	 * how many boards was made in the search and how much time did it took
	 * @throws Exception if trying to make an illegal move
	 */
	public static Result DFID_Search(Board start, int[][] goal, Boolean withOpen) throws Exception{
		//initialize variables
		long startTime= System.currentTimeMillis();
		int numOfVertices=1;
		Result res = null;
		// go over all depths from 1 to infinity or if "fail" returns
		for (int depth = 1; depth <Integer.MAX_VALUE; depth++){
			// create new hash every iteration 
			Hashtable<String, Board> h = new Hashtable<String, Board>();
			// run limited DFS up to the depth 
			res=limitedDFS(start, goal, depth, h, withOpen);
			// sum all board created from results
			numOfVertices+=res.numOfVertices;
			res.numOfVertices = numOfVertices;
			double timeTook=(System.currentTimeMillis()-startTime)/1000.0;
			res.time=timeTook;
			// if a regular path returned 
			if(!res.path.equals("cutoff") && !res.path.equals("fail"))
				return res;
			// if there is no more moves
			else if(res.path.equals("fail")) {
				res.path = "no path";
				return res;
			}
		}
		return res;
	}

	/**
	 * function is a recursively DFS search until a given depth 
	 * @param board to start iteration 
	 * @param goal to reach to 
	 * @param limit maximum depth to reach 
	 * @param ht hashTable for loop-avoidance
	 * @param withOpen states if to print openList to screen or not
	 * @return result the path from start to the goal, the iteration cost, 
	 * how many boards was made in the iteration
	 * if not found returns cutoff if can reach to another board but
	 * coudn't because limit or fail if branch complete
	 * @throws Exception if trying to make an illegal move
	 */
	private static Result limitedDFS(Board board, int[][] goal, int limit, Hashtable<String, Board> ht, Boolean withOpen) throws Exception {
		//check if board is goal
		if(board.isBoardEqual(goal)) {
			return new Result(board.path(),0,board.myPrice,0);
		}
		else if(limit==0) //check if reached depth limit
			return new Result("cutoff",0,0,0); 
		else {
			int numOfVertices=0;
			ht.put(board.toString(), board); // save board to hash table
			boolean iscutoff = false;
			//for each possible move create a son	
			for (int i = 0; i < 4; i++) { 
				if(board.possibleMoves[i]) {
					Board g = board.createMove(i);
					numOfVertices++;  
					//if already exist continue to next move
					if(!ht.containsKey(g.toString())) { 
						printOpen(withOpen, g);
						//call recursively to limited DFS with son to find if branch leads to goal
						Result res= limitedDFS(g, goal, limit-1, ht, withOpen);
						numOfVertices+=res.numOfVertices;
						//if returned cutoff
						if(res.path.equals("cutoff")) 
							iscutoff = true;
						else if (!res.path.equals("fail")) {
							res.numOfVertices = numOfVertices;
							return res;
						}
					}
				}
			}
			ht.remove(board.toString()); //release memory of n
			if (iscutoff)
				return new Result("cutoff",numOfVertices,0,0);
			else
				return new Result("fail",numOfVertices,0,0);
		}
	}

	/**
	 * if withOpen - prints all the open list which is one board each time 
	 * @param withOpen boolean that determine if to print open list
	 * @param b is a boards which is the open list
	 */
	private static void printOpen(boolean withOpen, Board b) {
		if(withOpen) {
			System.out.println("open list contains:");
			System.out.println(b+"\n");
		}
	}
}