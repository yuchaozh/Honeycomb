import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Word Search in the honeycomb
 * Find target words in the honeycomb and output words in order
 * 
 * @author yuchaozh
 */
public class honeyComb 
{
	// the layer of the honeycomb
	private int layer = 0;
	// store the final words list can be found in the honeycomb
	private ArrayList<String> reslut = new ArrayList<String>();
	// map to find the index pair of corresponding word
	private Map<String ,Pair> wordMap = new IdentityHashMap<String, Pair>();
	// store the honeycomb in a two-dimension array
	ArrayList<String> matrix = new ArrayList<String>();
	// if is 1 means that value has been used
	ArrayList<ArrayList<Integer>> mask = new ArrayList<ArrayList<Integer>>();
	
	/**
	 * Read the honeycomb from txt file and initiate data structure
	 * @param str the name of the input file
	 * @throws IOException
	 */
	public void readHoneyComb(String str) throws IOException
	{
		// read content of the txt file
		File inputFile = new File(str);
		Reader rd = new FileReader(inputFile);
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = "";
		layer = Integer.parseInt(br.readLine());
		int row = 0;
		line = br.readLine();
		while (line != null)
		{
			// initiate the matrix
			matrix.add(line);
			// initiate the mask
			ArrayList<Integer> tmp = new ArrayList<Integer>(line.length());
			for (int i = 0; i < line.length(); i++)
			{
				tmp.add(0);
			}
			mask.add(tmp);
			for (int i = 0; i < line.length(); i++)
			{
				// initiate the map
				Pair currentPair = new Pair(row, i);
				wordMap.put(String.valueOf(line.charAt(i)), currentPair);
			}
			row++;
			line = br.readLine();
		}
	}
	
	/**
	 * read the word file we wanna search
	 * @param str the file contains words we want to search
	 * @throws IOException
	 */
	public void readDictionary(String str) throws IOException
	{
		File inputFile = new File(str);
		Reader rd = new FileReader(inputFile);
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = "";
		line = br.readLine();
		while (line != null)
		{
			wordSearch(line);
			line = br.readLine();
		}
		Collections.sort(reslut);
		for(Iterator iterator = reslut.iterator(); iterator.hasNext(); ) 
			System.out.println(iterator.next());
	}
	
	/**
	 * rest the mask everytime we begin a new search
	 */
	public void resetMask()
	{
		for (int i = 0; i < mask.size(); i++)
		{
			for (int a = 0; a < mask.get(i).size(); a++)
			{
				mask.get(i).set(a, 0);
			}
		}
	}
	
	/**
	 * Begin to search the words in the honeycomb
	 * @param str the target words we wanna search
	 */
	public void wordSearch(String str)
	{
		resetMask();
		// if the word is null, false
		if (str == null)
			return;
		// if the word is one digit, simple check
		String tmp = String.valueOf(str.charAt(0));
		if (str.length() == 1)
		{
			if (!wordMap.containsKey(tmp))
				return;
			else 
			{
				if (!reslut.contains(str))
				{
					reslut.add(str);
				}
			}
			
		}
		// else call recursive function to check rest
		ArrayList<Pair> ss = getIndexFromMap(tmp);
		for (Pair p : ss)
		{
			String sub = str.substring(1);
			//Pair tmpPair = find(sub, p);
			boolean red = true;
			red = findedIt(sub, p, str);
			if (red == true)
			{
				if (!reslut.contains(str))
				{
					reslut.add(str);
				}
				return;
			}
		}
		
	}
	
	/**
	 * Find the word in the honeycomb
	 * @param str the left substring
	 * @param cells the cells we want to check	
	 * @param origin the origin string
	 * @return true: find the word, false: cannot find the word
	 */
	public boolean findedIt(String str, Pair cells, String origin)
	{
		if (str.equals(""))
		{
			if (!reslut.contains(origin))
			{
				reslut.add(origin);
			}
			return true;
		}
		String target = String.valueOf(str.charAt(0));
		ArrayList<Pair> potential = new ArrayList<Pair>();
		potential = getNeighbours(cells);
		for (Pair p : potential)
		{
			int x = p.getX();
			int y = p.getY();
			// tagged pass
			if (mask.get(x).get(y) == 1)
			{
				continue;
			}
			String compare = String.valueOf(matrix.get(x).charAt(y));
			if (target.equals(compare))
			{
				mask.get(x).set(y, 1);
				findedIt(str.substring(1), p, origin);
			}
		}
		return false;
	}
	
	/**
	 * Get the neighbors of the cell in the honeycomb
	 * @param p the position pair of the cell
	 * @return its neighbors
	 */
	public ArrayList<Pair> getNeighbours(Pair p)
	{
		ArrayList<Pair> neighbours = new ArrayList<Pair>();
		int x = p.getX();
		int y = p.getY();
		// the boundary of the honeycomb
		if (x == (layer - 1))
		{
			// can be %, then check three neighbours 
			if (y % layer == 0)
			{
				// if the node is at the top
				
					// beneath
					neighbours.add(new Pair(x - 1, y - y / x));
					// right
					neighbours.add(new Pair(x, y + 1));
					// left
					if (y != 0) // the first one, the left one should be the last one
						neighbours.add(new Pair(x, y - 1));
					else 
						neighbours.add(new Pair(x, 6 * x - 1));
			}
			else // check four neighbours
			{
				// left beneath
				neighbours.add(new Pair(x - 1, y - y / x - 1));
				// right beneath
				neighbours.add(new Pair(x - 1, y - y / x));
				// right
				neighbours.add(new Pair(x, (y + 1) % (x * 6)));
				// left
				neighbours.add(new Pair(x, y - 1));
			}
		}
		else if (x >= 1) // beneath top and above last two layer
		{
			if (y % x == 0) // top 3, 2, 1
			{
				// above
				neighbours.add(new Pair(x + 1, y + y / x));
				if (y == 0)
				{
					// left above
					neighbours.add(new Pair(x + 1, 6 * (x+1) - 1));
					// left same layer
					neighbours.add(new Pair(x, 6 * x - 1));
				}
				else
				{
					// left above
					neighbours.add(new Pair(x + 1, y + y / x -1));
					// left same layer
					neighbours.add(new Pair(x, y - 1));
				}
				// right above
				neighbours.add(new Pair(x + 1, y + y / x + 1));
				// right same layer
				neighbours.add(new Pair(x, (y + 1) % (x*6)));
				
				// beneath
				neighbours.add(new Pair(x - 1, y - y / x));
			}
			else // 2, 2, 2
			{
				// left above
				neighbours.add(new Pair(x + 1, y / x + y));
				// top above
				neighbours.add(new Pair(x + 1, y / x + y + 1));
				// left
				neighbours.add(new Pair(x, y - 1));
				// right 
				neighbours.add(new Pair(x, (y + 1) % (x * 6)));
				// top beneath
				neighbours.add(new Pair(x - 1, y - y / x - 1));
				// right beneath
				neighbours.add(new Pair(x - 1, (y - y / x) % ((x - 1)* 6)));
			}
		}
		else if ((x == 0) && (y == 0)) // last one layers (0, 0)
		{
			neighbours.add(new Pair(x + 1, y));
			neighbours.add(new Pair(x + 1, y + 1));
			neighbours.add(new Pair(x + 1, y + 2));
			neighbours.add(new Pair(x + 1, y + 3));
			neighbours.add(new Pair(x + 1, y + 4));
			neighbours.add(new Pair(x + 1, y + 5));
		}
		return neighbours;
	}
	
	
	/**
	 * get the positions (x, y) pair of the target word
	 * @param str target word
	 * @return (x, y) pair
	 */
	public ArrayList<Pair> getIndexFromMap(String str)
	{
		ArrayList<Pair> result = new ArrayList<Pair>();
		for(Entry<String, Pair> entry : wordMap.entrySet())  
        {  
            if (entry.getKey().equals(str)) 
            	result.add(entry.getValue());
        }  
		return result;
	}
}
