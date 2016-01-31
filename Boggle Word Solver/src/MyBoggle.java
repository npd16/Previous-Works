import java.io.*;
import java.util.*;

public class MyBoggle {
	public static void main(String[] args) throws IOException{
		Scanner fileScan = new Scanner(new FileInputStream("dictionary.txt"));
		String str;
		StringBuilder sb = new StringBuilder();
		DictionaryInterface D = new SimpleDictionary();
		
		if(args.length < 2)
			System.out.println("No Board Selected");
		
		else if(args.length == 2){
			if(args[0].equals("-d"))
				System.out.println("No Board Selected");
			else{
				showBoard(args[1]);
				D = makeDictionary(fileScan,D);
				makeGame(args[1],D);
			}
			
		}
		
		else if(args.length > 2){
			if(args[0].equals("-b")){
				showBoard(args[1]);
				if (args[3].toLowerCase().equals("dlb")){
					D = new DLBDictionary();
					D = makeDictionary(fileScan,D);
					makeGame(args[1],D);
				}
				else{
					D = makeDictionary(fileScan,D);
					makeGame(args[1],D);
				}
				
			}
			else if(args[0].equals("-d")){
				showBoard(args[3]);
				if(args[1].toLowerCase().equals("dlb")){
					D = new DLBDictionary();
					D = makeDictionary(fileScan,D);
					makeGame(args[3],D);
				}
				else{
					D = makeDictionary(fileScan,D);
					makeGame(args[3],D);
				}
			}
					
		}
		
	
	}
	
	private static void showBoard(String file) throws IOException{
		Scanner fileScan = new Scanner(new FileInputStream(file));
		String board = fileScan.nextLine();
		for(int i = 0; i < 4; i++)
			System.out.println("| " + board.charAt(4*i+0) + " | " + board.charAt(4*i+1) + " | " + board.charAt(4*i+2) + " | " + board.charAt(4*i+3)+ " |" );
		
	}
	
	private static void makeGame(String file, DictionaryInterface D) throws IOException{
		DictionaryInterface validWords = new SimpleDictionary();
		char[][] board = new char[4][4];
		boolean done = false;
		ArrayList<String> playerWords = new ArrayList<String>();
		ArrayList<String> boardWords = new ArrayList<String>();
		ArrayList<Integer> starCounter = new ArrayList<Integer>();
		int found;
		Scanner fileScan = new Scanner(new FileInputStream(file));
		String input = fileScan.nextLine();
		boolean star = false;
		StringBuilder word = new StringBuilder();
		boolean[][] beenThere = new boolean[4][4];
		int x = 0;
		int y = 0;
		int counter = 0;
		
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++){
				board[j][i] = input.charAt(4 * i + j);
				if(board[j][i] == '*'){
					star = true;
					starCounter.add(j);
					starCounter.add(i);
					counter++;
				}
			}
		
		if(star && counter == 1){
			for(int letter = 0; letter < 26; letter++){
				board[x][y] = (char) (letter + 65);
				for(int i = 0; i < 4; i++)
					for(int j = 0; j < 4; j++){
						findWords(validWords,D,board,word,boardWords,beenThere,i,j);
						beenThere = new boolean[4][4];
						word.delete(0,word.length());
					}
			}
		}
		
		else if (star && counter == 2){
			for(int letter = 0; letter < 26; letter++){
				board[starCounter.get(0)][starCounter.get(1)] = (char) (letter + 65);
				for(int letter2 = 0; letter2 < 26; letter2++){
					board[starCounter.get(2)][starCounter.get(3)] = (char) (letter2 + 65);
					for(int i = 0; i < 4; i++)
						for(int j = 0; j < 4; j++){
							findWords(validWords,D,board,word,boardWords,beenThere,i,j);
							beenThere = new boolean[4][4];
							word.delete(0,word.length());
						}
				}
			}
		}
		
		else if(!star){
			for(int i = 0; i < 4; i++)
				for(int j = 0; j < 4; j++){
					findWords(validWords,D,board,word,boardWords,beenThere,i,j);
					beenThere = new boolean[4][4];
					word.delete(0,word.length());
				}
		}
		
		else{
			System.out.println("Can not handle board with more than 2 *");
			return;
		}
		
		while(!done){
			System.out.println("Please enter a valid word.");
			fileScan = new Scanner(System.in);
			StringBuilder userInput = new StringBuilder(fileScan.nextLine().toLowerCase());
			found = validWords.search(userInput);
			System.out.print(userInput.toString());
			
			switch(found){
			case 0: //Not a word nor Prefix
				System.out.println(" is not a valid word.");
				break;
			case 1: //Prefix
				System.out.println(" is a prefix but not a word");
				break;
			case 2: //Word
				System.out.println(" is a valid word");
				if(playerWords.contains(userInput.toString()))
					System.out.println("This is a duplicate entry");
				else
					playerWords.add(userInput.toString());
				break;
			case 3: //Word and Prefix
				System.out.println(" is a valid word and a prefix");
				if(playerWords.contains(userInput.toString()))
					System.out.println("This is a duplicate entry.");
				else
					playerWords.add(userInput.toString());
				break;
			}
			
			System.out.println("Would you like to enter any more words(yes or no)");
			fileScan = new Scanner(System.in);
			input = new String(fileScan.nextLine().toLowerCase());
			if(input.equals("no"))
				done = true;
		}
		
		System.out.println("Your words:");
		for(int i = 0; i < playerWords.size(); i++){
			System.out.println(playerWords.get(i));
		}
		Collections.sort(boardWords);
		System.out.println("Possible words:");
		for(int i = 0; i < boardWords.size(); i++){
			System.out.println(boardWords.get(i));
		}
		System.out.println("Number of your words: " + playerWords.size());
		System.out.println("Number of possible words: " + boardWords.size());
		System.out.println("Percent of words guessed:" + (((double)playerWords.size()/ (double)boardWords.size()) * 100) + "%");
	}
	
	private static void findWords(DictionaryInterface validWords, DictionaryInterface D, char[][] board, StringBuilder word, ArrayList<String> bWords, boolean[][] bT, int y, int x){
		if(x < 0 || x > 3 || y < 0 || y > 3 ){//Array Index out of Bounds Check
			return;
		}
		else{
			int found;
			if(bT[x][y] == false){
				word.append(board[x][y]);
				word.replace(0, word.length(), word.toString().toLowerCase());
				bT[x][y] = true;
	
				if(word.length() >= 3){ //Seeing if Boggle Word  Prune to fix prefixes
					found = D.search(word);
					switch(found){
					case 0: //Not a word nor Prefix
						word.delete(word.length()-1, word.length());
						bT[x][y] = false;
						return;
					case 3: //Word and Prefix
						if(!bWords.contains(word.toString())){
							bWords.add(word.toString());
							validWords.add(word.toString());
						}
						break;
					case 1: //Prefix
						break;
					case 2: //Word
						if(!bWords.contains(word.toString())){
							bWords.add(word.toString());
							validWords.add(word.toString());
						}
						word.delete(word.length()-1, word.length());
						bT[x][y] = false;
						return;
					default:
						break;
					}
				}
				findWords(validWords,D,board,word,bWords,bT,y,x+1);	
				findWords(validWords,D,board,word,bWords,bT,y+1,x+1);
				findWords(validWords,D,board,word,bWords,bT,y+1,x);
				findWords(validWords,D,board,word,bWords,bT,y+1,x-1);
				findWords(validWords,D,board,word,bWords,bT,y,x-1);
				findWords(validWords,D,board,word,bWords,bT,y-1,x-1);
				findWords(validWords,D,board,word,bWords,bT,y-1,x);
				findWords(validWords,D,board,word,bWords,bT,y-1,x+1);
				if(word.length() > 0)
				word.delete(word.length()-1, word.length());
				bT[x][y] = false;
			}
			else 
				return;
		}
	}
	
	private static DictionaryInterface makeDictionary(Scanner scan, DictionaryInterface D){
		String line;
		while(scan.hasNext()){
			line = scan.nextLine();
			D.add(line);
		}
		return D;
	}
}
