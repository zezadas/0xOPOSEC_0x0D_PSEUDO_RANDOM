import java.util.Random;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class HelloWorldApp {
    static String encryption_mode = "AES/CBC/PKCS5Padding"; //Use AES with CBC and padding
    public static void main(String[] args) {
       
       if (args.length==2){
            if (args[0].equals("decode")){
                decode(args[1]);
            }
            else if(args[0].equals("encode")){
                encode(args[1]);
            }
            else{
                System.out.print("encode|decode file_input|file_output");
                System.exit(0); 
            }
        }
        else{
            System.out.print("encode|decode file_input|file_output");
            System.exit(0); 
        }
    }
    
    
    public static void encode(String filename){
        Random rand = new Random(5040508);
        //flag{The_tr uth_is_ou t_there}
        
        int x=new Random().nextInt(1000000);
        //int x = 507161+1;
        //int x = 777584+1;
        //int x = 309043+1;
        
        for (int i=0; i<x;i++){
            rand.nextInt(256);
        }
        
        try {
                String comment="read text from input";
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Hey kid, do you have any leaks?\ntell me:\n");
                String secret_message = br.readLine();
                
                comment="creating iv key";
                byte iv[] = new byte[16];
                for (int i=0;i<16;i++){
                    int rand_int1 = rand.nextInt(256);
                    iv[i]=(byte) rand_int1;
                }
                
                comment="creating key";
                byte key[] = new byte[16];
                for (int i=0;i<16;i++){
                    int rand_int1 = rand.nextInt(256);
                    key[i]=(byte) rand_int1;
                }
                
                comment = "encrypting content";
                byte[] cipher = encrypt(secret_message, key,iv);
                
                comment="write content to file";
                FileOutputStream fileOuputStream = new FileOutputStream(filename);
                fileOuputStream.write(iv);//cipher
                fileOuputStream.write(cipher);
                fileOuputStream.close();

                System.out.print("Keep this key: ");
                System.out.print(bytesToHex(key));
                
            }catch (Exception e) {
                e.printStackTrace();
            } 
    }
    
    public static void decode(String filename){
        String comment = "read file, split iv and encrypted content and decode with the input key";
        try {
            File initialFile = new File(filename);
            InputStream targetStream = new FileInputStream(initialFile);
            byte[] array = targetStream.readAllBytes();
            byte iv[] = new byte[16];
            for (int i =0;i<iv.length;i++){
                iv[i]=array[i];
            }
            //System.out.print(bytesToHex(iv));
            
            System.out.print("Please provide a key:\n");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String secret_key = br.readLine();
            byte key[] = hexStringToByteArray(secret_key);
            byte encrypted_text[] = new byte[array.length-iv.length];
            
            //copy to 
            for (int i=iv.length;i<array.length;i++){
                encrypted_text[i-iv.length]=array[i];
            }
            
            String plain_text = decrypt(encrypted_text,key,iv);
            System.out.print(plain_text);
            System.out.print("\n");
            }
            catch (Exception e) {
                e.printStackTrace();
            } 
    }
    
    public static void noNeedToLookBelowThis(){}
    
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
   
    
    public static byte[] encrypt(String plainText, byte[] enc_key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(encryption_mode);
        SecretKeySpec key = new SecretKeySpec(enc_key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(iv));
        return cipher.doFinal(plainText.getBytes());
    }
    
    public static String decrypt(byte[] cipherText, byte[] enk_key, byte[] iv) throws Exception{
        Cipher cipher = Cipher.getInstance(encryption_mode);
        SecretKeySpec key = new SecretKeySpec(enk_key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(iv));
        return new String(cipher.doFinal(cipherText));
        }
    
}
