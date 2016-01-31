
public class DeFranco4 {

	public static void main(String[] args) {
		Complex c1 = new Complex();
		Complex c2 = new Complex(2,3);
		Complex c3 = new Complex(-3, 5.2);
		Complex c4 = new Complex(c2);
		
		System.out.println(c1);
		System.out.println(c2);
		System.out.println(c3);
		System.out.println(c4);
		
		System.out.println(c2.equals(c3));
		System.out.println(c2.equals(c4));
		
		c1.setComplex(7, -12);
		c2.setImag(6);
		c4.setReal(4);
		System.out.println();
		
		System.out.println(c1);
		System.out.println(c2);
		System.out.println(c4);
		System.out.println();
		
		c1.plus(c2);
		System.out.println(c1);
		c1.times(c4);
		System.out.println(c1);
		
	}

}
