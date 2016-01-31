//Nick DeFranco
//1/29/2013
//Sudoku Puzzle Solver

import java.util.*;
import java.io.*;
import javax.swing.*;

public class DeFranco1 {
	
	public static void main(String[] args) throws FileNotFoundException{
		int puzzle[][] = new int[9][9];
		int total[] = new int[27];
		boolean flag = true;
		boolean sudoku = true;
		String file;
		
		while(flag){
			for(int x = 0; x < 27; x++)
				total[x] = 0;
			file = JOptionPane.showInputDialog("What is the name of the file?");
			Scanner input = new Scanner(new FileReader(file + ".txt"));
			puzzleMaker(puzzle, input);	 
			findTotal(total, puzzle);
			print(sudoku, total);
			String confirmation = JOptionPane.showInputDialog("Do you have another puzzle that needs checked?");
			if (confirmation.charAt(0) == 'N' || confirmation.charAt(0) == 'n')
				flag = false;
		}

	}
	
	private static int[][] puzzleMaker(int[][] puzzle, Scanner input){
		for (int x = 0; x < 9; x++)
			for (int y = 0; y < 9; y++)
				puzzle[x][y] = input.nextInt();
		return puzzle;
	}
	
	private static int[] findTotal(int[] total, int[][] puzzle){
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				total[x] += puzzle[x][y];
		
		for(int y = 0; y < 9; y++)
			for(int x = 0; x < 9; x++)
				total[9+y] += puzzle[x][y];

		int c = 18;
		
		for(int a = 0; a < 9; a += 3)
			for(int b = 0; b < 9; b += 3){
				getSumBlock(total, puzzle, a, b, c);
				c++;
			}
		
		return total;
		
	}
	
	private static boolean compare(int[] total, boolean sudoku){
		int n = 0;
		while(n < 27){
			if(total[n] == 45 && sudoku)
				n++;
			
			else{
				sudoku = false;
				n++;
			}
		}
		return sudoku;			
			
	}
	
	private static void print(boolean sudoku, int[] total){
		if(compare(total, sudoku)){
			JOptionPane.showMessageDialog(null,"This puzzle is a correct sudoku puzzle");
			JOptionPane.showMessageDialog(null,"Congratulations!!");
		}
		else{
			JOptionPane.showMessageDialog(null, "This is puzzle is an incorrect sudoku puzzle");
			error(total);
		}
		
		
	}
	
	private static int[] getSumBlock(int[] total, int[][] puzzle, int a, int b, int c){
		for(int x = 0 + a; x < 3 + a; x++)
			for(int y = 0 + b; y < 3 + b; y++)
				total[c] += puzzle[x][y];
		return total;
	}
	
	private static void error(int[] total){
		String line = "The error(s) are in: ";
		for(int x = 0; x < total.length; x++)
			if(total[x] != 45){
				int num = x / 9;
				int num2 = x % 9;
				switch(num){
				case 0:
					line += "Row " + (num2 + 1) + ", ";
					break;
				case 1:
					line += "Column " + (num2 + 1 ) + ", ";
					break;
				case 2:
					line += "Square " + (num2 + 1) + ", ";
					break;
				default:
					break;
				}
			}
		JOptionPane.showMessageDialog(null, line);		
	}

}
