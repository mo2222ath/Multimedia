/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 */
public class JPEG {
    Map<Integer,String > Table = new HashMap(); 
    public static ArrayList<discriptor> CompressDiscriptor = new ArrayList<>();
    public static ArrayList<discriptor> DecompressDiscriptor = new ArrayList<>();
    public static String result = "";
    ArrayList<discriptor> Groups = new ArrayList<>();
    public static  HashMap<String , Integer> Probability = new HashMap();
    Map<Integer , Integer> Code = new HashMap();
    discriptor d = new discriptor();
    String tag = "";
    public static HashMap<String,String> huffmanTable = new HashMap();
    public static String codeCompress = "";
    
    
    public int getCategoryNumber(int nonz){
        if (nonz < 0) {
            nonz *= -1;
        }
       String bainaryOfnumber = Integer.toBinaryString(nonz);
       int numberOfCategory = bainaryOfnumber.length();
       return  numberOfCategory;
    }
    
    
    public void RLE(String input){
        int nonZero =0;
        int numOfZeros =0;
        int categoryNumber =0;
        int i = 0;
        while(!input.substring(i, i+1).equals("E")){
            if (input.charAt(i)=='0'){
                numOfZeros++;
            }
            else{
                if (input.substring(i,i+1).equalsIgnoreCase("-")){
                     nonZero = Integer.parseInt(input.substring(i,i+2));
                     i++;
                }else{
                     nonZero = Integer.parseInt(input.substring(i, i+1));
                }
                categoryNumber = getCategoryNumber(nonZero);
                d.setNumZero(numOfZeros);
                d.setNumberOfCatagory(categoryNumber);
                d.setValue(nonZero);
                CompressDiscriptor.add(d);
                numOfZeros =0 ;
                nonZero = 0;
                categoryNumber=0;
                d = new discriptor();
            }
            i++;

        }
        for (int o=0 ; o<CompressDiscriptor.size() ;o++){
            System.out.println(CompressDiscriptor.get(o));
        }
    }
    public void getProbability(){
//        double prob =0;
        int freq =0;
        
        double size = CompressDiscriptor.size();
        for (discriptor Discriptor1 : CompressDiscriptor) {
            freq = Collections.frequency(CompressDiscriptor,Discriptor1);
//            prob=freq/size;
            tag = "("+ Discriptor1.getNumZero() + "," + Discriptor1.getNumberOfCatagory() + ")";
            Probability.put(tag,freq);
        }
        Probability.put("EOB",1);
        
        Probability = sortByValue(Probability);
        
        for(Map.Entry<String, Integer> entry: Probability.entrySet()){
            tag = entry.getKey();
            System.out.println( tag + " and Probability: " + Probability.get(tag));
        }
    }
    
    
    static int binaryToInteger (String binary){
        char []cA = binary.toCharArray();
        int result = 0;
        for (int i = cA.length-1;i>=0;i--){
            //111 , length = 3, i = 2, 2^(3-3) + 2^(3-2)
            //                    0           1  
            if(cA[i]=='1') result+=Math.pow(2, cA.length-i-1);
        }
        return result;
    }
    
    //TO SORT THE HashMap BY VALUE
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());

        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        // put data from sorted list to hashmap 
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    
    public void Compress(String Code){
        
        String Binary = "";
        String negativeBinary = "";
        Huffman h = new Huffman();
        RLE(Code);
        getProbability();
        huffmanTable = h.BuildHuffmanCode(Probability);
        for (int i = 0; i < CompressDiscriptor.size() ; i++) {
            String T = "(" +CompressDiscriptor.get(i).getNumZero() + "," + CompressDiscriptor.get(i).getNumberOfCatagory() + ")";
            int value = CompressDiscriptor.get(i).getValue();
            if (value < 0) {
                Binary = Integer.toBinaryString(value * -1);
            }else{
                Binary = Integer.toBinaryString(value);
            }
            
            
            if(value < 0){
                for (int j =i ; j==i ; j--){ 
                    for (int l =Binary.length() - 1; l >= 0; l--) {
                        if (Binary.charAt(l) == '1') {
                            negativeBinary="0"+negativeBinary;
                         } else {
                            negativeBinary= "1"+negativeBinary;
                         }
                     }
                }
                Binary = negativeBinary;
                negativeBinary = "";
            }
            System.out.println(" value and i="+i+" :" + Binary);
            
           codeCompress += huffmanTable.get(T) + Binary;
        }
        codeCompress += huffmanTable.get("EOB");
//        System.out.println("Compress Code is:" + codeCompress);
    
        WriteToFile(huffmanTable , codeCompress , "codeCompress.txt");
        
    }
    
    public void Decompress(){
        String CodeCompressed = readFromFile(huffmanTable  , "codeCompress.txt" );
//        System.out.println(CodeCompressed);
        String negativeBinary = "";
        int i = 0;
        String binaryCode = CodeCompressed.substring(i, i+1);
        i++;
        
//        System.out.println("Done ...");
//        System.out.println("Compress code is:" + CodeCompressed);
//        System.out.println("Huffman Table: \n" + huffmanTable);
       boolean flag = true;
       while(flag){
           if (huffmanTable.containsValue(binaryCode)) {
                for(Map.Entry<String,String> m :huffmanTable.entrySet()){
                    String key = m.getKey();
                    String value = m.getValue();
                    if (binaryCode.equals(value)){
                        if (key.equals("EOB")){
                            flag = false;
                            break;
                        }else{
//                            System.out.println("Key: " + key);
                            String[] arrOfStr = key.split(",", key.length());
                            String Non = arrOfStr[0].substring(1,arrOfStr[0].length());
                            String Cate = arrOfStr[arrOfStr.length-1].substring(0, arrOfStr[arrOfStr.length-1].length()-1);
                            
                            d = new discriptor();
                            int non = Integer.parseInt(Non);
                            d.setNumZero(non);
                            int cate = Integer.parseInt(Cate);
                            d.setNumberOfCatagory(cate);
                            String C = CodeCompressed.substring(i, i + d.getNumberOfCatagory());
    //                        System.out.println("C:" + C);
                            i += d.getNumberOfCatagory();

                            if (C.charAt(0) == '0') {
                                for (int t = 0; t <C.length() ; t++) {
                                    if (C.charAt(t) == '0') {
                                        negativeBinary = negativeBinary+"1";
                                    }else{
                                        negativeBinary = negativeBinary+"0";
                                    }
                                }
        //                            System.out.println("negativeBinary: " + negativeBinary);
                                int V = binaryToInteger(negativeBinary);
                                d.setValue(V * -1);
                                negativeBinary = "";
                            }else{
                                int V = binaryToInteger(C);
                                d.setValue(V);
                            }
                            DecompressDiscriptor.add(d);
//                        System.out.println(DecompressDiscriptor);
                        }
                    }
                }
                binaryCode ="";
           }
           
           if (i>=CodeCompressed.length()) {
               break;
           }
           if (i == CodeCompressed.length()-1) {
               binaryCode += CodeCompressed.charAt(i);
           }else{
               binaryCode += CodeCompressed.substring(i, i+1);
           }
           i++;
        }
//        System.out.println(DecompressDiscriptor);
        
        for (int j = 0; j < DecompressDiscriptor.size(); j++) {
            if (DecompressDiscriptor.get(j).getNumZero() == 0) {
                result += DecompressDiscriptor.get(j).getValue();
            }else{
                for (int k = 0; k < DecompressDiscriptor.get(j).getNumZero() ; k++) {
                    result +="0";
                }
                result += DecompressDiscriptor.get(j).getValue();
            }
        }
        result +="EOB";
        System.out.println("Result: " + result);
        try{
            File fileTwo=new File("Decompress.txt");
            FileOutputStream fos=new FileOutputStream(fileTwo);
            PrintWriter pw=new PrintWriter(fos);
            pw.println(result);
            pw.flush();
            pw.close();
            fos.close();
        }catch(Exception e){}
    }
    
    public void WriteToFile(HashMap<String,String> huffmanCode , String compressCode , String fileName){
        try{
            File fileTwo=new File(fileName);
            FileOutputStream fos=new FileOutputStream(fileTwo);
            PrintWriter pw=new PrintWriter(fos);
            pw.println(compressCode);
            for(Map.Entry<String,String> m :huffmanCode.entrySet()){
                pw.println(m.getKey()+"="+m.getValue());
            }
            pw.flush();
            pw.close();
            fos.close();
        }catch(Exception e){}
    }
    
    public String  readFromFile(HashMap<String,String> huffmanCode  , String fileName){
        String compressedCode = "";
        
        try{
        File toRead=new File(fileName);
        FileInputStream fis=new FileInputStream(toRead);

        Scanner sc=new Scanner(fis);

        HashMap<String,String> mapInFile=new HashMap<String,String>();

        //read data from file line by line:
        compressedCode = sc.nextLine();
        String currentLine;
        while(sc.hasNextLine()){
            currentLine=sc.nextLine();
            //now tokenize the currentLine:
            StringTokenizer st=new StringTokenizer(currentLine,"=",false);
            //put tokens ot currentLine in huffmanCode
            mapInFile.put(st.nextToken(),st.nextToken());
        }
        fis.close();
        huffmanCode.putAll(mapInFile);
        //print All data in MAP
        for(Map.Entry<String,String> m :mapInFile.entrySet()){
            System.out.println(m.getKey()+" : "+m.getValue());
        }
    }catch(Exception e){}
        return compressedCode;
    }
    
//    public static void main(String[] args) {
//        JPEG j = new JPEG();
//        String Code = "-200200320100-20-100100-1E";
//        
////        j.Compress(Code);
//        j.Decompress();
//        System.out.println(huffmanTable);
//    }
    
}
