import java.awt.Color;
import java.util.Arrays;

/**
 * Main class, call for the reader, find out which algorithm to run,
 * gather the information of the board etc.
 * run the correct algorithm and sends to the output to the writer
 * @author Rachel Avrunin
 */
public class Ex1 {

	/**
	 * main calls reader 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		search();
	}

	private static void search() throws Exception {
		//read file
		String str=ReadWrite.readFile("input1.txt");
		//separate lines 
		String[] lines = str.split("\\n");
		int function=checkFunctionName(lines[0]); // check function name
		Boolean withTime=checkTime(lines[1]); // check if to measure time
		Boolean withOpen=checkOpen(lines[2]); // check if to print open list
		String[] sizes = lines[3].split("x"); //check matrix size
		//initial matrixes
		int [][] initBoard=new int[Integer.parseInt(sizes[0])][Integer.parseInt(sizes[1])];
		Color [][] initColors=new Color[Integer.parseInt(sizes[0])][Integer.parseInt(sizes[1])];

		int [][] goal = new int[initBoard.length][initBoard[0].length];
		int counter = 1;
		// create goal and board matrixes 
		for (int i = 0; i < Integer.parseInt(sizes[0]); i++) {
			int [] values = extractNumbers(lines[6+i],0);
			for (int j = 0; j < values.length; j++) {
				initBoard[i][j] = values[j];
				goal[i][j] = counter++;
			}
		}
		goal[goal.length-1][goal[0].length-1]=0;

		// find the black and red pieces and insert them to their place
		int[] blacks = extractNumbers(lines[4],7);
		int[] reds = extractNumbers(lines[5],5);
		for (int i = 0; i < initColors.length; i++) {
			for (int j = 0; j < initColors[0].length; j++) {
				int num = initBoard[i][j];
				boolean foundinBlack = false;
				if(blacks != null)
					foundinBlack = Arrays.stream(blacks).anyMatch(x -> x == num);
				boolean foundinRed = false;
				if(reds!=null)
					foundinRed=Arrays.stream(reds).anyMatch(x -> x == num);
				if(foundinBlack)
					initColors[i][j]=Color.black;
				else if(foundinRed)
					initColors[i][j]=Color.red;
				else if(num != 0)
					initColors[i][j]=Color.green;
				else
					initColors[i][j]=null;
			}
		}

		Board start=new Board(initBoard, initColors);
		Result res = callSearch(function, start, goal, withOpen);
		res.needTime=withTime;
		ReadWrite.writeFile("output.txt",res.toString());
	}
	/**
	 * receive a function number and activate it 
	 * @param function number 1-5
	 * @param start board
	 * @param goal to get to 
	 * @param withOpen states if to print openList to screen or not
	 * @return the result from the function which is the path from start to the goal, its cost, 
	 * how many boards was made in the search and how much time did it took
	 * @throws Exception if trying to make an illegal move
	 */
	private static Result callSearch(int function, Board start, int[][] goal, Boolean withOpen) throws Exception {
		Result res = null;
		switch(function){
		case 0: // activate BFS
			res=BFS.BFS_Search(start, goal, withOpen);
			break;
		case 1: // activate DFID
			res=DFID.DFID_Search(start, goal, withOpen);
			break;
		case 2: // activate A*
			res=AStar.AStar_Search(start, goal, withOpen);
			break;
		case 3: // activate IDA*
			res=IDAStar.IDAStar_Search(start, goal, withOpen);
			break;
		case 4: // activate DFBnB
			res=DFBnB.DFBnB_Search(start, goal, withOpen);
			break;
		default:
			throw new Exception("there is onely 5 possible functions!");
		}
		return res;
	}

	/**
	 * check string to match function name
	 * @param str string to match
	 * @return the function name to activate
	 */
	private static int checkFunctionName(String str) {
		if (str.equals("BFS"))
			return 0;
		if (str.equals("DFID"))
			return 1;
		if (str.equals("A*"))
			return 2;
		if (str.equals("IDA*"))
			return 3;
		if (str.equals("DFBnB"))
			return 4;
		return -1;
	}

	/**
	 * check string to match if to save time
	 * @param str string to match
	 * @return if to save time
	 */
	private static Boolean checkTime(String str) {
		if (str.equals("with time"))
			return true;
		return false;
	}

	/**
	 * check string to match if to print open list
	 * @param str string to match
	 * @return if to print open list
	 */
	private static Boolean checkOpen(String str) {
		if (str.equals("with open"))
			return true;
		return false;
	}
	
	/**
	 * receive a string, cut the first characters and split
	 * the remaining string by "," and parse it to integers
	 * @param line to string to cut and interpret 
	 * @param num how many characters to cut from the beginning 
	 * @return an array of integers 
	 */
	private static int[] extractNumbers(String line,int num) {
		if(line.length()>num) {
			line=line.substring(num);
			String[] valuesStrings = line.split(",");
			int [] values=new int[valuesStrings.length];
			for (int i = 0; i < values.length; i++)
				if(valuesStrings[i].equals("_"))
					values[i]=0;
				else
					values[i]=Integer.parseInt(valuesStrings[i]);
			return values;
		}
		return null;
	}
}