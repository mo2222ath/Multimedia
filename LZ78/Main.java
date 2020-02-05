
package com.mycompany.lz_78.moaaz_yageen;

import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author Moaaz
 */
public class Main {
    

    public static void main(String[] args) {
      
        Scanner input = new Scanner(System.in);
        String str;
        System.out.println("Enter the letters");
        str = input.nextLine();  
        LZ78 m = new LZ78();
        ArrayList<Code> res= m.Comperssion(str);
        
        for (int i = 0; i <res.size(); i++) {
            res.get(i).printCode();     
        }
        System.out.println("\n");
        m.decompression();
      
    }
    
}
