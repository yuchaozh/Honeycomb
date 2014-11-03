/**
 * Pair structure to store the x, y index of the cell
 * 
 * @author yuchaozh
 *
 */
class Pair
{
	private int x;
	private int y;
	Pair(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * get the x index of the cell
	 * @return
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * get the y index of the cell
	 * @return
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * print the x, y index
	 */
	public void print()
	{
		System.out.println("x: " + x + " y: " + y);
	}
}
