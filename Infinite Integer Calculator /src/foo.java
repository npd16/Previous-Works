
public class foo {
	
	public static void main(String[] args){
		
		for(int i = -500; i <= 500; i++)
		{
			for(int j = -500; j <= 500; j++)
			{
				InfiniteInteger x = new InfiniteInteger(i);
				InfiniteInteger y = new InfiniteInteger(j);
				System.out.println(i + " * " + j);
				System.out.println(x.multiply(y));
			}
		}
				
	}
}
