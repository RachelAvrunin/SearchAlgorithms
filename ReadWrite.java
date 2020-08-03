import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * this class is to read from a file from the current folder or to write to it 
 * with filenames sent to it
 * @author Rachel Avrunin
 */
public class ReadWrite {

	/**
	 * this function read a file from the current folder and send back all the text
	 * @param fileName represent which filename to read
	 * @return a string contains all the text from the file
	 */
	public static String readFile (String fileName){
		// try to read from the file
		try {
			InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = br.readLine();
			String res="";
			while (str != null) {
				res += str+"\n";
				str = br.readLine();
			}
			res += str+"\n";
			br.close();
			return res;
		}
		catch(IOException ex) {
			System.out.print("Error reading file\n" + ex);
			System.exit(2);
		}
		return null;
	}

	/**
	 * this function write into a file on the current folder
	 * if file not exist is will create it
	 * @param fileName represent which filename to write to
	 * @param str what to write in the file
	 */
	public static void writeFile(String fileName, String str){
		// try write to the file
		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter outs = new PrintWriter(fw);
			outs.println(str);
			outs.close();
			fw.close();
		}
		catch(IOException ex) {
			System.out.print("Error writing file\n" + ex);
		}
	}
}