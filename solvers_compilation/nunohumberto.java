//$ java GenRand > random_numbers

import java.util.Random;
public class GenRand {     public static void main(String []args){
     	// Initialize a Random with the same seed.
        Random random = new Random(5040508L);        // Print out a million random numbers, one per line.
        // IVs and keys used by Rui Tinto should end up somewhere in the output.
        for (int i = 0; i < 1000000; ++i) {
            System.out.println(random.nextInt(256));
        }     }
}
