import java.nio.charset.Charset;
import java.util.Random;

public class Add128 implements SymCipher{
	byte[] key;
	
	
	public Add128(){
		key = new byte[128];
		Random rand = new Random();
		rand.nextBytes(key);
	}
	
	public Add128(byte[] a){
		key = a;
	}
	public byte[] getKey() {
		return key;
	}

	public byte[] encode(String S) {
		byte[] bytes;
		bytes = S.getBytes(Charset.forName("UTF-8"));
		
		int j = 0;
		for (int i = 0; i < bytes.length; i++){
			if(j == bytes.length){
				j = 0;
			}
			bytes[i] += key[j];
			j++;
		}
		return bytes;
		
	}

	public String decode(byte[] bytes) {
		int j = 0;
		for(int i = 0; i < bytes.length; i++){
			if(j == bytes.length){
				j = 0;
			}
			bytes[i] -= key[j];
			j++;
		}
		String s;
		s = new String(bytes, Charset.forName("UTF-8"));
		return s;
	}
}