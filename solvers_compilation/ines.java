
import java.util.Random;
​
public class RandomBreaker {
    
    public static String iv = "bbbbbbded5a15b60b0d7fdfe922e53a5";
    
    public static void main(String[] args) {
        
        byte[] ivData = hexStringToByteArray(iv);
        
        Random random = new Random(5040508L);
        for (int i = 0; i < 1000000; i++) {
            if (matchesNext16(random, ivData)) {
                String keyHex = byteArrayToHex(getNext16Bytes(random));
                System.out.println(keyHex);
                return;
            }
        }
    }
    
    private static boolean matchesNext16(Random random, byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            byte nextByte = (byte)random.nextInt(256);
            if (nextByte != bytes[i]) {
                return false;
            }
        }
        return true;
    }
    
    private static byte[] getNext16Bytes(Random random) {
        byte[] bytes = new byte[16];
        for (int i = 0; i < 16; i++) {
            bytes[i] = (byte)random.nextInt(256);
        }
        return bytes;
    }
    
    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
    
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
}
