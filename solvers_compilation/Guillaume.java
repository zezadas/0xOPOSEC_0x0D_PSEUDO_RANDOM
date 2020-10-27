import java.util.Random;
​
​
​
class Solution {                                                                                
    static String encryption_mode = "AES/CBC/PKCS5Padding";                                          
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();                         
                                                                                                      
                                                                                           
                                                                                                    
    public static void main(String[] arrstring) {                                                   
        
		//int [] iv1 = { 187, 187, 187, 222, 213, 161, 91, 96, 176, 215, 253, 254, 146, 46, 83, 165 };		
		//int [] iv1 = { 45,205,205,205,167,81,78,71,253,70,232,224,131,52,234,222};
		int [] iv1 = {218,218,218,77,67,225,149,149,57,20,202,35,216,74,223,82};
		int compteur = 0; 
		int seq = 0;
		
        Random random = new Random(5040508L);                                                       
        int n = new Random().nextInt(1000000);
		while (true) {
             int value = random.nextInt(256);                                                                    
			 
			 if (value == iv1[compteur]) {
				 compteur++;
			 } else {
				 compteur = 0;
			 }
			 
			 if (compteur == 16) {
				 break;
			 }
			 
        }
		
		int i = 0;
		byte[] arrby2 = new byte[16];
		
		for( i=0; i<16; i++) {
			int result = random.nextInt(256);
			arrby2[i] = (byte) result;
		}
		System.out.println(Solution.bytesToHex(arrby2));
	}
	
	
	public static String bytesToHex(byte[] arrby) {
        char[] arrc = new char[arrby.length * 2];
        for (int i = 0; i < arrby.length; ++i) {
            int n = arrby[i] & 0xFF;
            arrc[i * 2] = HEX_ARRAY[n >>> 4];
            arrc[i * 2 + 1] = HEX_ARRAY[n & 0xF];
        }
        return new String(arrc);
    }
​
}		
