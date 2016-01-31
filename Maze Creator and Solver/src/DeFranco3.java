//Nick DeFranco
//3/7/2013
//Assignment 3 - Getting 1 Using Recursion 

import javax.swing.*;

public class DeFranco3 {

	public static void main(String[] args) {
		String input = JOptionPane.showInputDialog("Please enter an integer greater than or equal to 1");
		int num = Integer.parseInt(input);
		
		System.out.print("[");
		System.out.print(" " + lower(num));
		System.out.print(" ]" );
	}
	
	public static int lower(int n){
		if(n <= 1)
			return 1;
		else if(n % 2 == 0){
			System.out.print(" " + n );
			return lower(n/2);
		}
		else{
			System.out.print(" " + n );
			return lower(n * 3 + 1);			
		}
	}

}
