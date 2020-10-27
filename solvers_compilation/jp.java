
/*
  OPOSEC Challenge - October 2020
  
  Cipher method uses a stream cipher with a limited PRNG.
  The number of seeds for the PRNG is of 1000000.
  Actually, generating 1000000+32 from the PRNG will yeld all keys
  IV is also derived from the keystream and Key follows the IV.
​
  Approach: Generate 1M+32 bytes of the PRNG, find offset of IV, Key is 16 bytes after
​
*/
​
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
​
class FindKey {
  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
  private static final int KEYSTREAM_SIZE = 1000000 + 32;
  private static final long SEED = 5040508L;
  private static final String encryption_mode = "AES/CBC/PKCS5Padding";
​
  public static void main(String[] args) {
​
    for (int i = 0; i < args.length; i++) {
      process_file(args[i]);
    }
    
    System.out.println("");
  }
​
  public static void process_file(String fname) {
    FileInputStream fstream;
    byte[] iv = new byte[16];
    File f = new File(fname);
​
    try {
      fstream = new FileInputStream(f);
      fstream.read(iv);
      fstream.close();
    } catch(Exception e) {
      System.out.println("ERROR");
      return;
    }
​
    Random prng1 = new Random(SEED);
    byte[] keystream = new byte[KEYSTREAM_SIZE];
    for (int i = 0; i < KEYSTREAM_SIZE; i++) {
      keystream[i] = (byte) prng1.nextInt(256);
    }
​
    int offset = 0;
    int match = 0;
    while (offset < KEYSTREAM_SIZE - 32) {
      match = 0;
      for (int i = 0; i < 16; i++) {
        if (keystream[i + offset] != iv[i]) {
          break;
        } else {
          match++;
        }
      }
​
      if (match == 16) break;
​
      offset++;
    }
​
    if (match < 16) {
      System.out.println("Sorry, not found!");
      return;
    }
​
    byte[] key = new byte[16];
    System.arraycopy(keystream, offset + 16, key, 0, 16);
    decode(fname, key);
  }
​
  //Decompiled
​
  public static String bytesToHex(byte[] var0) {
    char[] var1 = new char[var0.length * 2];
​
    for (int var2 = 0; var2 < var0.length; ++var2) {
      int var3 = var0[var2] & 255;
      var1[var2 * 2] = HEX_ARRAY[var3 >>> 4];
      var1[var2 * 2 + 1] = HEX_ARRAY[var3 & 15];
    }
​
    return new String(var1);
  }
​
  public static void decode(String var0, byte[] var8) {
    String var1 = "read file, split iv and encrypted content and decode with the input key";
​
    try {
      File var2 = new File(var0);
      FileInputStream var3 = new FileInputStream(var2);
      byte[] var4 = var3.readAllBytes();
      byte[] var5 = new byte[16];
​
      for (int var6 = 0; var6 < var5.length; ++var6) {
        var5[var6] = var4[var6];
      }
​
      byte[] var9 = new byte[var4.length - var5.length];
​
      for (int var10 = var5.length; var10 < var4.length; ++var10) {
        var9[var10 - var5.length] = var4[var10];
      }
​
      String var12 = decrypt(var9, var8, var5);
      System.out.print(var12);
      //System.out.print("\n");
    } catch(Exception var11) {
      var11.printStackTrace();
    }
​
  }
​
  public static byte[] hexStringToByteArray(String var0) {
    int var1 = var0.length();
    byte[] var2 = new byte[var1 / 2];
​
    for (int var3 = 0; var3 < var1; var3 += 2) {
      var2[var3 / 2] = (byte)((Character.digit(var0.charAt(var3), 16) << 4) + Character.digit(var0.charAt(var3 + 1), 16));
    }
​
    return var2;
  }
  public static String decrypt(byte[] var0, byte[] var1, byte[] var2) throws Exception {
    Cipher var3 = Cipher.getInstance(encryption_mode);
    SecretKeySpec var4 = new SecretKeySpec(var1, "AES");
    var3.init(2, var4, new IvParameterSpec(var2));
    return new String(var3.doFinal(var0));
  }
}
