
public class EightQueens
{
	public static final int NUM_ROWS = 8;
	public static final int NUM_COLUMNS = 8;
	public static boolean found = false;
	
	public static boolean[][] board = new boolean[NUM_ROWS][NUM_COLUMNS];
	public static boolean[] rowOccupied = new boolean[NUM_ROWS];
	public static boolean[] columnOccupied = new boolean[NUM_COLUMNS];
	public static boolean[] RLdiagOccup = new boolean[2 * NUM_ROWS - 1];
	public static boolean[] LRdiagOccup = new boolean[2 * NUM_ROWS - 1];
	
	public static boolean isLegal(int row, int column)
	{
		if(rowOccupied[row])
			return false;
		
		if(columnOccupied[column])
			return false;
		
		if(RLdiagOccup[row+column])
			return false;
		
		if(LRdiagOccup[row-column+7])
			return false;
		
		return true;
	}
	
	public static void add(int row, int column)
	{
		board[row][column] = true;
		rowOccupied[row] = true;
		columnOccupied[column] = true;
		RLdiagOccup[row + column] = true;
		LRdiagOccup[row-column+7] = true;
	}

	public static void remove(int row, int column)
	{
		board[row][column] = false;
		rowOccupied[row] = false;
		columnOccupied[column] = false;
		RLdiagOccup[row + column] = false;
		LRdiagOccup[row-column+7] = false;
	}
	
	private static void showSolution()
	{
		for(int row = 0; row < NUM_ROWS; row++)
		{
			for(int column = 0; column < NUM_COLUMNS; column++)
			{
				if(board[row][column])
				{
					System.out.print("Q");
				}
				else
				{
					System.out.print("-");
				}
			}
			
			System.out.println();
		}
		System.out.println();
	}
	
	public static boolean place(int column)
	{
		if(column == NUM_COLUMNS)
		{
			return true;
		}
		else
		{
			for(int row = 0; row < NUM_ROWS; row++)
			{
				if(isLegal(row,column))
				{
					add(row,column);
					if(place(column + 1))
					{
						return true;
					}

					remove(row,column);
				}
			}
		}
		
		return false;
	}
	
	public static void main(String[] args)
	{
		place(0);
		showSolution();
	}
}