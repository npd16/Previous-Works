import java.util.Random;
// No other import statement is allowed

public class Maze
{
	int width;
	int height;
	
	boolean[][] north;
	boolean[][] south;
	boolean[][] east;
	boolean[][] west;
	
	Random z;
	
	/**
	 * Constructor
	 * @param aWidth the number of chambers in each row
	 * @param aHeight the number of chamber in each column
	 */
	public Maze(int aWidth, int aHeight)
	{
		width = aWidth;
		height = aHeight;
		
		north = new boolean[height][width];
		south = new boolean[height][width];
		east = new boolean[height][width];
		west = new boolean[height][width];
		
		z = new Random();
		
		for(int i = 0; i < width; i++)
			north[0][i] = true;
		for(int i = 0; i < height; i++)
			west[i][0] = true;
		for(int i = width-1; i >= 0; i--)
			south[height-1][i] = true;
		for(int i = height-1; i >= 0; i--)
			east[i][width-1] = true;
		
		buildMaze(0,height,0,width);
		
	}

	/**
	 * getWidth
	 * @return the width of this maze
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * getHeight
	 * @return the height of this maze
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * isNorthWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain a north wall. Otherwise, return false
	 */
	public boolean isNorthWall(int row, int column)
	{
		return north[row][column];
	}
	
	/**
	 * isEastWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain an east wall. Otherwise, return false
	 */
	public boolean isEastWall(int row, int column)
	{
		return east[row][column];
	}
	
	/**
	 * isSouthWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain a south wall. Otherwise, return false
	 */
	public boolean isSouthWall(int row, int column)
	{
		return south[row][column];
	}
	
	/**
	 * isWestWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain a west wall. Otherwise, return false
	 */
	public boolean isWestWall(int row, int column)
	{
		return west[row][column];
	}
	
	private void buildMaze(int startX, int stopX, int startY, int stopY){
		int vertical = stopY - startY;
		int horizontal = stopX - startX;
		
		if(vertical <= 1 || horizontal <= 1)
			return;
		
		else{
			int x = z.nextInt(horizontal);
			while(x == 0)
				x = z.nextInt(horizontal);
			int y = z.nextInt(vertical);
			while(y == 0)
				y = z.nextInt(vertical);
			
			x += startX;
			y += startY;
			
			//Drawing a line
			for(int i = 0; i < vertical; i++){
				north[x][i+startY] = true;
				if(x > 0)
					south[x-1][i+startY] = true;
			}
			for(int i = 0; i < horizontal; i++){
				west[i+startX][y] = true;
				if(y > 0)
					east[i+startX][y-1] = true;
			}
			//Determines what wall not to take a section from
			int notWall = z.nextInt(4);
			int x2 = z.nextInt(horizontal);
			int y2 = z.nextInt(vertical);
			//Removing of cells
			if(notWall != 0){
				while(x2 + startX >= x)
					x2 = z.nextInt(horizontal);
				west[x2+startX][y] = false;
				if(y > 0)
					east[x2+startX][y-1] = false;
				x2 = z.nextInt(horizontal);
			}
			if(notWall != 1){
				while(y2 + startY < y)
					y2 = z.nextInt(vertical);
				north[x][y2+startY] = false;
				if(x > 0)
					south[x-1][y2+startY] = false;
				y2 = z.nextInt(vertical);
			}
			if(notWall != 2){
				while(x2 + startX < x)
					x2 = z.nextInt(horizontal);
				west[x2+startX][y] = false;	
				if(y > 0)
					east[x2+startX][y-1] = false;
				x2 = z.nextInt(horizontal);
			}
			if(notWall != 3){
				while(y2 + startY >= y)
					y2 = z.nextInt(vertical);		
				north[x][y2+startY] = false;
				if(x > 0)
					south[x-1][y2+startY] = false;
				y2 = z.nextInt(vertical);
			}
			buildMaze(startX,x,startY,y);
			buildMaze(x,stopX,startY,y);
			buildMaze(startX,x,y,stopY);
			buildMaze(x,stopX,y,stopY);
		}
				
	}
}