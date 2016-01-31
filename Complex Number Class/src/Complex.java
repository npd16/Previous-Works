public class Complex {
	private double real;
	private double imag;
	
	public Complex(){
		real = 0;
		imag = 0;
	}
	
	public Complex(double a, double b){
		real = a;
		imag = b;
	}
	
	public Complex(Complex C){
		real = C.real;
		imag = C.imag;
	}
	
	public void setComplex(double a, double b){
		real = a;
		imag = b;
	}

	public void setReal(double a){
		real = a;
	}
	
	public void setImag(double b){
		imag = b;
	}
	
	public double getReal(){
		return real;
	}
	
	public double getImag(){
		return imag;
	}
	
	public String toString(){
		return real + " + " + imag + "i";
	}
	
	public void copy(Complex C){
		real = C.real;
		imag = C.imag;
	}
	
	public boolean equals(Complex C){
		if(real == C.real && imag == C.imag)
			return true;
		else
			return false;
	}
	
	public void plus(Complex C){
		real += C.real;
		imag += C.imag;				
	}
	
	public void minus(Complex C){
		real -= C.real;
		imag -= C.imag;				
	}
	
	public void times(Complex C){
		double temp = real;
		real = (real * C.real) - (imag * C.imag);
		imag = (temp * C.imag) + (imag * C.real);			
	}
	
	public void divBy(Complex C){
		double e = Math.pow(C.real, 2) + Math.pow(C.imag, 2);
		double temp = real;
		real = ((real * C.real) + (imag * C.imag))/e;
		imag = ((imag * C.real) - (temp * C.imag))/e;
	}
	
	public static Complex sumOf(Complex X, Complex Y){
		Complex Z = new Complex();
		Z.setReal(X.real + Y.real);
		Z.setImag(X.imag + Y.imag);
		return Z;
	}
	
	public static Complex prodOf(Complex X, Complex Y){
		Complex Z = new Complex();
		Z.setReal(X.real*Y.real - X.imag*Y.imag);
		Z.setImag(X.real*Y.imag + X.imag*Y.real);
		return Z;
	}
	
	public static Complex DiffOf(Complex X, Complex Y){
		Complex Z = new Complex();
		Z.setReal(X.real - Y.real);
		Z.setImag(X.imag - Y.imag);
		return Z;
	}
	
	public static Complex QuoteOf(Complex X, Complex Y){
		double e = Math.pow(Y.real, 2) + Math.pow(Y.imag, 2);
		Complex Z = new Complex();
		Z.setReal((X.real*Y.real + X.imag*Y.imag)/e);
		Z.setImag((X.imag*Y.real - X.real*Y.imag)/e);
		return Z;
	}
	
	public Complex conjugate(){
		imag *= -1;
		return this;
	}
}