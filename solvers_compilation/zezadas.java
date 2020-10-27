import java.util.Random;
class Solver {
    static String encryption_mode = "AES/CBC/PKCS5Padding"; //Use AES with CBC and padding
    
    public static void main(String[] args) {
        
        Random rand = new Random(5040508); //
 
        if (args.length==2){
            if (args[0].equals("searchFor")){
                byte headers[] = hexStringToByteArray(args[1]);
                searchFor(rand,headers);
            }
            else if(args[0].equals("searchFrom")){
                int index = Integer.parseInt(args[1]);  
                searchFrom(rand,index);
            }
            else if(args[0].equals("dump")){
                for (int i=0;i<1000000;i++){
                    System.out.print(String.format("%02X", rand.nextInt(256)));
                } 
            }
            else{
                System.out.print("searchFor|searchFrom hexheader|index");
                System.exit(0); 
            }
        }
        else{
            System.out.print("searchFor|searchFrom hexheader|index");
            System.exit(0); 
        }
    }
    
    public static void searchFor(Random rand, byte headers[]){
        int prng_index=0;
        int index_iv=0;
        int counter = 0;
        int rand_int1 =0;
        //BBBBBBDE
        //2DCDCDCD
        //DADADA
        
        int find_me[] = new int[headers.length];
        
        for(int i = 0; i < headers.length; i++){
            find_me[i] = headers[i] & 255 ;
            //System.out.print(find_me[i]);
            //System.out.print("\n");
        }
    
        while(counter<1000000){
            rand_int1 = rand.nextInt(256);
            //System.out.print(String.format("%02X", rand_int1));
            if (rand_int1==find_me[index_iv]){
                index_iv++;
                
            }
            else{
                index_iv=0;
                if (rand_int1==find_me[index_iv]){
                    index_iv++;
                }
            }
            
            if(index_iv>=find_me.length){
                System.out.print(Integer.toString(counter-find_me.length) + "\n");
                //System.out.print(String.format("%02X", rand.nextInt(256)));
                //System.out.print("\n");
                index_iv=0;
            }
            //System.out.print(index_iv);
            counter++;
        }
    }
    
    public static void searchFrom(Random rand, int index){
        int rand_int1 =0;
        int x=index;
        for (int i=0;i<=x;i++){
            rand_int1 = rand.nextInt(256); 
            //System.out.print(String.format("0x%02X", rand_int1)+"\n");
        }
        System.out.print("---iv---"+"\n");
        for (int i=0;i<16;i++){
            rand_int1 = rand.nextInt(256); 
            System.out.print(String.format("%02X", rand_int1));
        }
        System.out.print("\n---key---"+"\n");
        for (int i=0;i<16;i++){
            rand_int1 = rand.nextInt(256); 
            System.out.print(String.format("%02X", rand_int1));
        }
        System.out.print("\n");
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
}
