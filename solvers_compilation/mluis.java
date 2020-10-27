import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.nio.ByteBuffer;
class HelloWorldApp {
    static String encryption_mode = "AES/CBC/PKCS5Padding";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    HelloWorldApp() {
    }

    public static void main(String[] arrstring) {
        if (arrstring.length == 2) {
            if (arrstring[0].equals("decode")) {
                HelloWorldApp.decode(arrstring[1]);
            } else if (arrstring[0].equals("encode")) {
                HelloWorldApp.encode(arrstring[1]);
            }else if (arrstring[0].equals("crack")){
                HelloWorldApp.crack(arrstring[1]);
            } else {
                System.out.print("encode|decode file_input|file_output|crack file_input");
                System.exit(0);
            }
        } else {
            System.out.print("encode|decode file_input|file_output|crack file_input");
            System.exit(0);
        }
    }

    public static void crack(String string) {
        try{
            File file = new File(string);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arrby = fileInputStream.readAllBytes();
            byte[] arrby2 = new byte[16];
            for (int i = 0; i < arrby2.length; ++i) {
                arrby2[i] = arrby[i];
            }
            byte[] keystream = new byte[1000000+32];
	    Random r = new Random(5040508L);
	    r.ints(1000000+32, 0, 256).reduce(0, (idx, val) -> {
	        keystream[idx] = (byte)val;
	        return idx+1;
	    });
	        
	    int matchIndex = 0;
	    int keyIndex = 0;
	    for(int i = 0; i < keystream.length; i++){
	        if(matchIndex == 16){
	            keyIndex = i;
	            break;
	        }
	        if(keystream[i] == arrby2[matchIndex]){
	            matchIndex += 1;
	        }else{
	            matchIndex = 0;
	        }
	    }
	    byte[] key = new byte[16];
	    for(int i = 0; i < 16; i++){
	        key[i] = keystream[keyIndex+i];
	    }
            System.out.println(HelloWorldApp.bytesToHex(key));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void encode(String string) {
        Random random = new Random(5040508L);
        int n = new Random().nextInt(1000000);
        for (int i = 0; i < n; ++i) {
            random.nextInt(256);
        }
        try {
            int n2;
            String string2 = "read text from input";
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Hey kid, do you have any leaks?\ntell me:\n");
            String string3 = bufferedReader.readLine();
            string2 = "creating iv key";
            byte[] arrby = new byte[16];
            for (int i = 0; i < 16; ++i) {
                n2 = random.nextInt(256);
                arrby[i] = (byte)n2;
            }
            string2 = "creating key";
            byte[] arrby2 = new byte[16];
            for (n2 = 0; n2 < 16; ++n2) {
                int n3 = random.nextInt(256);
                arrby2[n2] = (byte)n3;
            }
            string2 = "encrypting content";
            byte[] arrby3 = HelloWorldApp.encrypt(string3, arrby2, arrby);
            string2 = "write content to file";
            FileOutputStream fileOutputStream = new FileOutputStream(string);
            fileOutputStream.write(arrby);
            fileOutputStream.write(arrby3);
            fileOutputStream.close();
            System.out.print("Keep this key: ");
            System.out.print(HelloWorldApp.bytesToHex(arrby2));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void decode(String string) {
        String string2 = "read file, split iv and encrypted content and decode with the input key";
        try {
            File file = new File(string);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arrby = fileInputStream.readAllBytes();
            byte[] arrby2 = new byte[16];
            for (int i = 0; i < arrby2.length; ++i) {
                arrby2[i] = arrby[i];
            }
            System.out.print("Please provide a key:\n");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String string3 = bufferedReader.readLine();
            byte[] arrby3 = HelloWorldApp.hexStringToByteArray(string3);
            byte[] arrby4 = new byte[arrby.length - arrby2.length];
            for (int i = arrby2.length; i < arrby.length; ++i) {
                arrby4[i - arrby2.length] = arrby[i];
            }
            String string4 = HelloWorldApp.decrypt(arrby4, arrby3, arrby2);
            System.out.print(string4);
            System.out.print("\n");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void noNeedToLookBelowThis() {
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

    public static byte[] hexStringToByteArray(String string) {
        int n = string.length();
        byte[] arrby = new byte[n / 2];
        for (int i = 0; i < n; i += 2) {
            arrby[i / 2] = (byte)((Character.digit(string.charAt(i), 16) << 4) + Character.digit(string.charAt(i + 1), 16));
        }
        return arrby;
    }

    public static byte[] encrypt(String string, byte[] arrby, byte[] arrby2) throws Exception {
        Cipher cipher = Cipher.getInstance(encryption_mode);
        SecretKeySpec secretKeySpec = new SecretKeySpec(arrby, "AES");
        cipher.init(1, (Key)secretKeySpec, new IvParameterSpec(arrby2));
        return cipher.doFinal(string.getBytes());
    }

    public static String decrypt(byte[] arrby, byte[] arrby2, byte[] arrby3) throws Exception {
        Cipher cipher = Cipher.getInstance(encryption_mode);
        SecretKeySpec secretKeySpec = new SecretKeySpec(arrby2, "AES");
        cipher.init(2, (Key)secretKeySpec, new IvParameterSpec(arrby3));
        return new String(cipher.doFinal(arrby));
    }
}
