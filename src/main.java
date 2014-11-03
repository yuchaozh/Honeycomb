import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Main file for the honeycomb project
 * To run the project, you need to put the two source file in the same directory of the project
 * 
 * @author yuchaozh
 *
 */
public class main 
{
	public static void main(String[] args) throws IOException
	{
		String sourceFile = "";
		String dictionary = "";
		if (args.length < 2)
		{
			System.out.println("Please input the source file and the dictionary file");
			System.exit(0);
		}
		sourceFile = args[0];
		dictionary = args[1];
		honeyComb hc = new honeyComb();
		// read honecomb.txt file
		hc.readHoneyComb(sourceFile);
		hc.readDictionary(dictionary);
	}
}
