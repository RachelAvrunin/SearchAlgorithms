/**
 * this class represent the results of the searches
 * it has the number of boards created in the search,
 * the path returned from the search and the cost of it 
 * and how much time the algorithm took
 * @author Rachel Avrunin
 */
public class Result {

	String path;
	int numOfVertices;
	int cost;
	double time;
	Boolean needTime = true;

	/**
	 * set parameters of answer
	 * @param path the path to the goal 
	 * @param numOfVertices how many states the algorithm created
	 * @param cost how much the final path cost
	 * @param time how much time the algorithm took
	 */
	public Result(String path, int numOfVertices, int cost, double time){
		this.path = path;
		this.numOfVertices = numOfVertices;
		this.cost = cost;
		this.time = time;
	}

	/**
	 * @return a string representing the answer.
	 */
	@Override
	public String toString() {
		String ans= path + "\n" + "Num: " + numOfVertices;
		if(!path.equals("no path"))
			ans+="\n"+"Cost: " + cost;
		if(needTime)
			ans+= "\n"+"seconds " + time;
		return ans;
	}						
}