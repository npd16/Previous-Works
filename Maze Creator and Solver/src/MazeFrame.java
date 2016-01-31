import java.util.ArrayList;
import javax.swing.JFrame;

public class MazeFrame
{
	public static void main(String[] args) throws InterruptedException
	{
		int width = 15;
		int height = 15;
		JFrame frame = new JFrame();
		Maze maze = new Maze(width, height);
		ArrayList<Pair<Integer,Integer>> solution = new ArrayList<Pair<Integer,Integer>>();
		MazeComponent mc = new MazeComponent(maze, solution);
		frame.setSize(800,800);
		frame.setTitle("Maze");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mc);
		frame.setVisible(true);
		
		solution.add(new Pair<Integer,Integer>(0,0));
		Thread.sleep(1000);
		int x = solution.get(0).fst();
		int y = solution.get(0).snd();
		solveMaze(solution, mc, maze,x,y);
		mc.repaint();
	}
	
	/** Solve Maze: recursively solve the maze
	 * 
	 * @param solution   : The array list solution is needed so that every recursive call,
	 *                     a new (or more) next position can be added or removed.
	 * @param mc         : This is the MazeComponent. We need that only for the purpose of
	 *                     animation. We need to call mc.repaint() every time a new position
	 *                     is added or removed. For example,
	 *                       :
	 *                     solution.add(...);
	 *                     mc.repaint();
	 *                     Thread.sleep(sleepTime);
	 *                       :
	 *                     solution.remove(...);
	 *                     mc.repaint();
	 *                     Thread.sleep(sleepTime);
	 *                       :
	 * @param maze       : The maze data structure to be solved. 
	 * @return a boolean value to previous call to tell the previous call whether a solution is
	 *         found.
	 * @throws InterruptedException: We need this because of our Thread.sleep(50);
	 */
	public static boolean solveMaze(ArrayList<Pair<Integer,Integer>> solution, MazeComponent mc, Maze maze, int x, int y) throws InterruptedException
	{
		int preX;
		int preY;
		if(solution.size() == 1){
			preX = solution.get(solution.size()-1).fst();
			preY = solution.get(solution.size()-1).snd();
		}
		else{
			preX = solution.get(solution.size()-2).fst();
			preY = solution.get(solution.size()-2).snd();
		}
		
		boolean done = false;
		
			if(x == maze.getHeight()-1 && y == maze.getWidth()-1)
				return true;
			else{
				if(checkEast(maze,x,y) && y+1 != preY){
					solution.add(new Pair <Integer,Integer>(x,y+1));
					mc.repaint();
					Thread.sleep(100);
					done = solveMaze(solution,mc,maze,x,y+1);
					if(done)
						return true;
					solution.remove(solution.size()-1);
					mc.repaint();
					Thread.sleep(100);
				}
				if(checkSouth(maze,x,y) && x+1 != preX){
					solution.add(new Pair <Integer,Integer>(x+1,y));
					mc.repaint();
					Thread.sleep(100);
					done = solveMaze(solution,mc,maze,x+1,y);
					if(done)
						return true;
					solution.remove(solution.size()-1);
					mc.repaint();
					Thread.sleep(100);
				}
				if(checkWest(maze,x,y) && y-1 != preY){
					solution.add(new Pair <Integer,Integer>(x,y-1));
					mc.repaint();
					Thread.sleep(100);
					done = solveMaze(solution,mc,maze,x,y-1);
					if(done)
						return done;
					solution.remove(solution.size()-1);
					mc.repaint();
					Thread.sleep(100);
				}
				if(checkNorth(maze,x,y) && x-1 != preX){
					solution.add(new Pair <Integer,Integer>(x-1,y));
					mc.repaint();
					Thread.sleep(100);
					done = solveMaze(solution,mc,maze,x-1,y);
					if(done)
						return done;
					solution.remove(solution.size()-1);
					mc.repaint();
					Thread.sleep(100);
				}
				
			
			return false;
			
		}
	}
	
	private static boolean checkEast(Maze maze, int x, int y){
		return !(maze.isEastWall(x, y));
	}
	private static boolean checkSouth(Maze maze, int x, int y){
		return !(maze.isSouthWall(x,y));
	}
	private static boolean checkWest(Maze maze, int x, int y){
		return !(maze.isWestWall(x,y));
	}
	private static boolean checkNorth(Maze maze, int x, int y){
		return !(maze.isNorthWall(x,y));
	}
}