import java.nio.charset.Charset;
import java.util.*;

public class Substitute implements SymCipher{
	byte[] key;
	byte[] reverse;
	int store;
	
	public Substitute(){
		key = new byte[256];
		reverse = new byte[256];
		store = 0;
		
		Random rand = new Random();
		for(int i = 0; i < 256; i++){
			byte[] random = new byte[1];
			rand.nextBytes(random);
			byte num = (byte)(random[0] + 128);
			boolean flag = true;
			if(i == 0){
				key[i] = num;
			}
			else{
				for(int j = 0; j < i; j++){
					if(num == key[j]){
						flag = false;
						i--;
						break;
					}
				}
				if(flag){
					key[i] = num;
				}
			}
		}
		
		for(int i = 0; i < 256; i++){
			store = key[i] + 128;
			reverse[store] = (byte)i;
		}		
	}
	
	public Substitute(byte[] a){
		key = a;
		reverse = new byte[key.length];
		store = 0;
		for(int i = 0; i < key.length; i++){
			store = key[i] + 128;
			reverse[store] = (byte)i;
		}
	}
	
	public byte[] getKey() {
		return key;
	}

	public byte[] encode(String S) {
		byte[] bytes;
		bytes = S.getBytes(Charset.forName("UTF-8"));
		for(int i = 0; i < bytes.length; i++){
			bytes[i] = key[bytes[i]];
		}
		return bytes;
	}

	public String decode(byte[] bytes) {
		for(int i = 0; i < bytes.length; i++){
			store = bytes[i] + 128;
			bytes[i] = reverse[store];
		}
		String s = new String(bytes,Charset.forName("UTF-8"));
		return s;
	}

}
