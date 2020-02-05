/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lz_78.moaaz_yageen;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Moaaz
 */
public class LZ78 {
    
    String temps = "";
    public char tempc ;
    public int lastIndex;
    ArrayList<String> dictionary = new ArrayList();
    ArrayList<Code> Tags = new ArrayList();
    
    public ArrayList Comperssion (String str){
        dictionary.clear();
        Tags.clear();
        dictionary.add("-");
        int size = str.length();
        for (int i = 0; i<size;) {
            tempc = str.charAt(i);
            temps += tempc;
            lastIndex = 0;
            if (i == size-1){
                Tags.add(new Code(0,str.charAt(i)));
                ++i;
            }
            if (dictionary.contains(temps) && i < size-1){
                while(dictionary.contains(temps)){
                    lastIndex = dictionary.indexOf(temps);
                    temps += str.charAt(++i);
//                    System.out.println(i);
                }
                dictionary.add(temps);
                Tags.add(new Code(lastIndex,str.charAt(i)));
                i++;
//                System.out.println(i);
                temps = "";
            }else if (i<size-1){
//              System.out.println(temps +  "  else");
                dictionary.add(temps);
                Tags.add(new Code(lastIndex,str.charAt(i)));
//                System.out.println(i);
                i++;
                temps = "";
            }
           
            
        }
        System.out.println(dictionary);
        
        return Tags;
    }
    
        ArrayList<Integer> arrIndex = new ArrayList();
        ArrayList<String> arrSymbols = new ArrayList();
        ArrayList<String> arrdic = new ArrayList();
        int index , n , x ; 
        String str, next , t ,tt ;
    
    public void decompression() {
	Scanner input = new Scanner(System.in);
        System.out.println("How Many Tages Will You Enter?");
        n= input.nextInt();
        System.out.println("Enter The inedx and the Symbol of the tag");
        
        for (int i=0; i<n ; i++) {
        	 index= input.nextInt();
        	 arrIndex.add(index);
        	 next= input.next();
        	 arrSymbols.add(next) ;
        }
        arrdic.add("-");
        for(int i=0 ; i<arrIndex.size(); i++) {
        	 x = arrIndex.get(i);
        	 t = arrSymbols.get(i);
            if (x == 0) {
        	   arrdic.add(t);
            }
            else{
        	   tt = arrdic.get(x);
        	   str = tt + t ;
        	   arrdic.add(str);
            }
        }
        System.out.println("The Dictionary of Decomperssion Text is :");
        System.out.println(arrdic); 
        System.out.println("The Decomperssion Text is :");
        for (int i=1; i<arrdic.size(); i++) {
            System.out.print(arrdic.get(i)); 
        }
    }
}
