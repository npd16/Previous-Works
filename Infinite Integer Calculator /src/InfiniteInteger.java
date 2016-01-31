import javax.sound.sampled.Line;


public class InfiniteInteger implements Comparable<InfiniteInteger>
{
	// TO DO: Instance Variables
	// Note that it is a good idea to declare them final to
	// prevent you from accidentally modify them.
	
	private final int[] ARRAY;
	private final int NUMBEROFENTRIES;
	
	
	/**
	 * Constructor: Constructs this infinite integer from a string
	 * representing an integer.
	 * @param s  a string represents an integer
	 */
	public InfiniteInteger(String s)
	{
		int counter = 0;
		boolean flag = false;
		int entries = s.length();			
		String holder = new String();
		
		for(int i = 0; i < entries; i++){
			if(s.charAt(i) == '-'){
				flag = true;
				counter++;
			}
		}
		int x = 0;
		if(s.charAt(0) == '-')
			x = 1;
		while(s.charAt(x) == '0'){
			counter++;
			x++;
			if(x >= entries)
				break;
		}
		
		//for loop making new string with no 0s
		for(int i = 0 + counter; i < entries; i++){
			holder += s.charAt(i);
		}
		
		entries = holder.length();
		
		int[] temp = new int[entries];
		
		for(int i = 0; i < temp.length; i++){ //copies chars from holder and makes them int and stores them in temp
			temp[i] = holder.charAt(i);
			temp[i] -= 48;
		}
		
		if (entries == 0 && s.charAt(0) == '0'){
			entries = 1;
			temp = new int[1];
			temp[0] = 0;
		}
		
			
		NUMBEROFENTRIES = entries;
		if(flag)
			temp[0] *= -1;
		ARRAY = temp;
			
	}

	/**
	 * Constructor: Constructs this infinite integer from an integer.
	 * @param anInteger  an integer
	 */
	public InfiniteInteger(int anInteger)
	{
		int counter = 0;
		boolean isNegative = false;
		if(anInteger < 0)
			isNegative = true;
		if(isNegative)
			anInteger *= -1;
		for(int i = anInteger; i > 0; i /= 10)
			counter++;
		if(anInteger == 0)
			NUMBEROFENTRIES = 1;
		else
			NUMBEROFENTRIES = counter;
		counter--;
		int[] temp = new int[NUMBEROFENTRIES];
		
		for(int i = anInteger; i > 0 && counter >= 0; i/= 10){
			temp[counter] = i%10;
			counter--;
		}
		if(isNegative)
			temp[0] *= -1;
		if(anInteger == 0)
			for(int i: temp)
				i = 0;
		ARRAY = temp;
	}
	
	/**
	 * Gets the number of digits of this infinite integer.
	 * @return an integer representing the number of digits
	 * of this infinite integer.
	 */
	public int getNumberOfDigits()
	{
		return NUMBEROFENTRIES;
	}

	/**
	 * Checks whether this infinite integer is a negative number.
	 * @return true if this infinite integer is a negative number.
	 * Otherwise, return false.
	 */
	public boolean isNegative()
	{
		boolean flag = false;
		if(ARRAY[0] < 0)
			flag = true;
		return flag;
	}

	/**
	 * Calculates the result of this infinite integer plus anInfiniteInteger
	 * @param anInfiniteInteger the infinite integer to be added to this
	 * infinite integer.
	 * @return a NEW infinite integer representing the result of this
	 * infinite integer plus anInfiniteInteger
	 */
	public InfiniteInteger plus(final InfiniteInteger anInfiniteInteger)
	{		
		InfiniteInteger result;
		InfiniteInteger fixedResult;
		int entries = 0;
		int more = 1;
		int[] temp;
		boolean flag = true;
		boolean isNeg = false;
		String line = new String();
		int j = 0;
		
		//If one but not the other Infinite Integer is negative change the negative one and send it to minus
		if(ARRAY[0] < 0 && anInfiniteInteger.ARRAY[0] > 0){ 
			if(NUMBEROFENTRIES >= anInfiniteInteger.NUMBEROFENTRIES){
				temp = ARRAY;
				temp[0] *= -1;
				for(int i: temp)
					line += i;
				result = new InfiniteInteger(line);
				line = "";				
				if(result.compareTo(anInfiniteInteger) != -1)
					temp = result.minus(anInfiniteInteger).ARRAY;
				else{
					temp = anInfiniteInteger.minus(result).ARRAY;
					temp[0] *= -1;
				}
			}
			else{
				temp = anInfiniteInteger.ARRAY;
				temp[0] *= -1;
				for(int i: temp)
					line += i;
				result = new InfiniteInteger(line);
				line = "";
				temp = result.minus(this).ARRAY;
				}
			temp[0] *= -1;
			for(int i: temp)
				line += i;
			fixedResult = new InfiniteInteger(line);
			return fixedResult;
			
		}
		
		if(ARRAY[0] > 0 && anInfiniteInteger.ARRAY[0] < 0){
			temp = anInfiniteInteger.ARRAY;
			temp[0] *= -1;
			for(int i: temp)
				line += i;
			result = new InfiniteInteger(line);
			temp = minus(result).ARRAY;
			line = "";
			for(int i: temp)
				line += i;
			fixedResult = new InfiniteInteger(line);
			return fixedResult;

		}
		
		if((ARRAY[0] + anInfiniteInteger.ARRAY[0] >= 10 || ARRAY[0] + anInfiniteInteger.ARRAY[0] <= -10) && NUMBEROFENTRIES == anInfiniteInteger.NUMBEROFENTRIES)
			more--;
		
		if(isNegative() && anInfiniteInteger.isNegative())
			isNeg = true;
		
		if(NUMBEROFENTRIES >= anInfiniteInteger.NUMBEROFENTRIES){
			entries += anInfiniteInteger.NUMBEROFENTRIES;
			temp = new int[NUMBEROFENTRIES + 1];
			j = NUMBEROFENTRIES - 1;
			for(int i = temp.length-1; j >= 0; i--){
				temp[i] = Math.abs(ARRAY[j]);
				j--;
			}
			j = temp.length - 1;
		}
		else{
			entries += NUMBEROFENTRIES;
			temp = new int[anInfiniteInteger.NUMBEROFENTRIES + 1];
			j = anInfiniteInteger.NUMBEROFENTRIES - 1;
			for(int i = temp.length-1; j >= 0; i--){
				temp[i] = Math.abs(anInfiniteInteger.ARRAY[j]);
				j--;
			}
			j = temp.length - 1;
			flag = false;
		}
		
		for(int i = entries - 1; i >= 0; i--){
			if(flag)
				temp[j] += Math.abs(anInfiniteInteger.ARRAY[i]); 
			else
				temp[j] += Math.abs(ARRAY[i]);
			if(temp[j] >= 10){
				temp[j-1] += 1;
				temp[j] %= 10;
			}
			j--;
		}
		
		for(int i = temp.length-1; i >= 1; i--)
			if(temp[i] >= 10){
				temp[i-1] += 1;
				temp[i] %= 10;
			}
		
		if(ARRAY[0] <= 0 && anInfiniteInteger.ARRAY[0] <= 0)
			temp[more] *= -1;
		
		if(isNeg && temp[0] > 0)
			temp[0] *= -1;
		
		for(int i = 0; i < temp.length; i++)
			line += temp[i];
		
		result = new InfiniteInteger(line);
		return result;
		
	}

	/**
	 * Calculates the result of this infinite integer subtracted by anInfiniteInteger
	 * @param anInfiniteInteger the infinite integer to subtract.
	 * @return a NEW infinite integer representing the result of this
	 * infinite integer subtracted by anInfiniteInteger
	 */
	public InfiniteInteger minus(final InfiniteInteger anInfiniteInteger)
	{
		InfiniteInteger result;
		InfiniteInteger fixedResult;
		int entries = 0;
		int[] temp;
		boolean flag = true;
		boolean isNeg = isNegative();
		String line = new String();
		int j = 0;
		
		//checks to see if one if positive and other is negative. If so it changes the negative value to a positive value and sends it to plus (-500 - 500 = -500 + -500)
		if(ARRAY[0] < 0 && anInfiniteInteger.ARRAY[0] >= 0){
			temp = ARRAY;
			temp[0] *= -1;
			for(int i: temp)
				line += i;
			result = new InfiniteInteger(line);
			temp = result.plus(anInfiniteInteger).ARRAY;
			temp[0] *= -1;
			line = "";
			for(int i: temp)
				line += i;
			fixedResult = new InfiniteInteger(line);
			return fixedResult;
			
		}
		
		if(ARRAY[0] >= 0 && anInfiniteInteger.ARRAY[0] < 0){
			temp = anInfiniteInteger.ARRAY;
			temp[0] *= -1;
			for(int i: temp)
				line += i;
			result = new InfiniteInteger(line);
			return plus(result);
		}
		if(isNeg){
			if(compareTo(anInfiniteInteger) == 1){
				temp = anInfiniteInteger.ARRAY;
				entries = NUMBEROFENTRIES;
				flag = false;
			}
			else{
				temp = ARRAY;
				entries = anInfiniteInteger.NUMBEROFENTRIES;
			}
			temp[0] *= -1;
		}
		else{
			if(compareTo(anInfiniteInteger) == -1){
				temp = anInfiniteInteger.ARRAY;
				entries = NUMBEROFENTRIES;
				flag = false;
			}
			else{
				temp = ARRAY;
				entries = anInfiniteInteger.NUMBEROFENTRIES;
			}
		}
		
		j = temp.length - 1;
		for(int i = entries - 1 ; i >= 0; i--){
			if(flag){
				while(temp[j] < Math.abs(anInfiniteInteger.ARRAY[i])){
					temp[j-1] -= 1;
					temp[j] += 10;
				}
				temp[j] -= Math.abs(anInfiniteInteger.ARRAY[i]);
			}
			else{
				while(temp[j] < Math.abs(ARRAY[i])){
					temp[j-1] -= 1;
					temp[j] += 10;
				}
				temp[j] -= Math.abs(ARRAY[i]);
			}
			j--;
		}
		
		//fixes negatives
		while(j > 0){
			if(temp[j] < 0){
				temp[j-1] -= 1;
				temp[j] += 10;
			}
			j--;
		}
		
		if (temp.length == NUMBEROFENTRIES && temp[0] == 0){
			temp[0] = 0;
		}
		int y = 0; //changes the first number to negative if ARRAY is smaller than anInfiniteInteger.ARRAY
		while(temp[y] == 0){
			y++;
			if(y >= temp.length){
				y = 0;
				break;
			}
		}
		if(isNeg)
			temp[y] *= -1;
		
		if(!flag)
			temp[y] *= -1;
		
		for(int i = 0; i < temp.length; i++)
			line += temp[i];		
		result = new InfiniteInteger(line);
		
		return result;
	}

	/**
	 * Calculates the result of this infinite integer multiplied by anInfiniteInteger
	 * @param anInfiniteInteger the multiplier.
	 * @return a NEW infinite integer representing the result of this
	 * infinite integer multiplied by anInfiniteInteger.
	 */
	public InfiniteInteger multiply(final InfiniteInteger anInfiniteInteger)
	{
		int[] resultArray = new int[NUMBEROFENTRIES * anInfiniteInteger.NUMBEROFENTRIES + 1];
		int[] temp;
		int z = 0;
		for(int x = anInfiniteInteger.NUMBEROFENTRIES - 1; x >= 0; x--){
			temp = new int[NUMBEROFENTRIES + 1];
			int k = temp.length - 1;
			int j = resultArray.length - 1;
			for(int y = NUMBEROFENTRIES - 1; y >= 0; y--){
				temp[k] += Math.abs(ARRAY[y]) * Math.abs(anInfiniteInteger.ARRAY[x]);
				if(temp[k] >= 10){
					temp[k-1] = temp[k] / 10;
					temp[k] %= 10;
				}
				k--;
			}
			for(int i = temp.length - 1; i >= 0; i--){
				resultArray[j-z] += temp[i];
				if(resultArray[j-z] >= 10){
					resultArray[j-z-1] += 1;
					resultArray[j-z] -= 10;
				}
				j--;
			}
			z++;
		}
		int counter = 0;
		while(resultArray[counter] == 0){
			counter++;
			if(counter >= resultArray.length){
				counter = 0;
				break;
			}
		}
		
		if(isNegative())
			resultArray[counter] *= -1;
		if(anInfiniteInteger.isNegative())
			resultArray[counter] *= -1;
		
		String line = new String();
		for(int i: resultArray)
			line += i;
		
		InfiniteInteger result = new InfiniteInteger(line);
		
		return result;
	}
	
	/**
	 * Generates a string representing this infinite integer. If this infinite integer
	 * is a negative number a minus symbol should be in the front of numbers. For example,
	 * "-12345678901234567890". But if the infinite integer is a positive number, no symbol
	 * should be in the front of the numbers (e.g., "12345678901234567890").
	 * @return a string representing this infinite integer number.
	 */
	public String toString()
	{
		String line = "";
		for (int i = 0; i < NUMBEROFENTRIES; i++)
			line = line + ARRAY[i];
		return line;
	}
	
	/**
	 * Compares this infinite integer with anInfiniteInteger
	 * @return either -1, 0, or 1 as follows:
	 * If this infinite integer is less than anInfiniteInteger, return -1.
	 * If this infinite integer is equal to anInfiniteInteger, return 0.
	 * If this infinite integer is greater than anInfiniteInteger, return 1.
	 */
	public int compareTo(final InfiniteInteger anInfiniteInteger)
	{
		int result = 0;
		boolean flag = false;
		if(ARRAY[0] < 0 && anInfiniteInteger.ARRAY[0] < 0)
			flag = true;
		
		if(ARRAY[0] > 0 && anInfiniteInteger.ARRAY[0] == 0)
			return 1;
		if(ARRAY[0] < 0 && anInfiniteInteger.ARRAY[0] == 0)
			return -1;
		if(ARRAY[0] == 0 && anInfiniteInteger.ARRAY[0] > 0)
			return -1;
		if(ARRAY[0] == 0 && anInfiniteInteger.ARRAY[0] < 0)
			return 1;
		
		if(ARRAY[0] > 0 && anInfiniteInteger.ARRAY[0] < 0)
			return 1;
		if(ARRAY[0] < 0 && anInfiniteInteger.ARRAY[0] > 0)
			return -1;
		
		if(NUMBEROFENTRIES > anInfiniteInteger.NUMBEROFENTRIES && ARRAY[0] > 0)
			return 1;
		if(NUMBEROFENTRIES > anInfiniteInteger.NUMBEROFENTRIES && ARRAY[0] < 0)
			return -1;
		if(NUMBEROFENTRIES < anInfiniteInteger.NUMBEROFENTRIES && ARRAY[0] > 0)
			return -1;
		if(NUMBEROFENTRIES < anInfiniteInteger.NUMBEROFENTRIES && ARRAY[0] < 0)
			return 1;
		
		for(int i = 0; i < NUMBEROFENTRIES; i++){
			if(Math.abs(ARRAY[i]) > Math.abs(anInfiniteInteger.ARRAY[i])){
				result = 1;
				if(flag)
					result *= -1;
				return result;
			}
			if(Math.abs(ARRAY[i]) < Math.abs(anInfiniteInteger.ARRAY[i])){
				result = -1;
				if(flag)
					result *= -1;
				return result;
			}
		}
		
		if(flag)
			result *= -1;		
		
		return result;
	}
}