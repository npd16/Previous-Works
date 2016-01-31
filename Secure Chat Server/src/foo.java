import java.nio.charset.Charset;
import java.util.Random;

public class foo {
	public static void main(String args[]){
		byte[]key = new byte[256];
		byte[]reverse = new byte[256];
		
		Random rand = new Random();
		for(int i = 0; i < 256; i++){
			byte[] random = new byte[1];
			rand.nextBytes(random);
			byte num = random[0];
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
		
		int store = 0;
		for(int i = 0; i < 256; i++){
			store = key[i] + 128;
			reverse[store] = (byte)i;
		}	
		
		byte[] bytes;
		bytes = "HelloThere".getBytes(Charset.forName("UTF-8"));
		for(int i = 0; i < bytes.length; i++){
			bytes[i] = key[bytes[i]];
		}
		
		store = 0;
		for(int i = 0; i < bytes.length; i++){
			store = bytes[i] + 128;
			bytes[i] = reverse[store];
		}
		String s = new String(bytes,Charset.forName("UTF-8"));
		
		System.out.println(s);
		
		
	}
}
