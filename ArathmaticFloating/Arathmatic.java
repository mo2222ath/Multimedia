/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.arathmaticfloating_muaath.yageen;
import java.awt.RenderingHints.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Moaaz
 */
public class Arathmatic {
    
    HashMap<Character,Integer> Frequence;
    HashMap<Character,Symbol> Symbols ;
    public String orgStr;
    public double LOW,HIGH,code;
    String CodeBinary = "";
    String CodeScaling = "";
    Symbol Sym = new Symbol();
    
    public Arathmatic(String S) {
        this.orgStr = S;
        Frequence = new HashMap<>();
        Symbols = new HashMap<>();
    }

    public Arathmatic() {
    }
    
    
    
    public Arathmatic(HashMap<Character,Integer> Freq) {
//        Frequence = new HashMap<>();
        Frequence = Freq;
        Symbols = new HashMap<>();
    }
    
    
    //TO COUNT THE FREQUENCIES OF CHARACTER ...
    public void CountFrequencies(){
        Character ch;
        Integer weight;
        for (int i=0; i<orgStr.length(); i++){
                ch = orgStr.charAt(i);
                if (Frequence.containsKey(ch) == false)
                        weight = 1;
                else
                        weight = Frequence.get(ch) + 1;
                Frequence.put(ch, weight);
        }
    }

    // TO SET THE VALUES SYMBOL,LOW,HIGH,PROBAILITY AND RANGES 
    public void SetSymbols(){
        for(Map.Entry<Character, Integer> entry: Frequence.entrySet()){
            char key = entry.getKey();
            int val = entry.getValue();
            Sym.setSymbol(key);
            Sym.setProbality(val/orgStr.length());
            Symbols.put(key,Sym);
        }
        boolean flag = true;
        Map.Entry<Character,Symbol> arrival = Symbols.entrySet().iterator().next(); // TO GET THE FRIST VALUE IN MAP
        char fristkey = arrival.getKey();
        char PreKey = fristkey;
        for(Map.Entry<Character, Symbol> entry: Symbols.entrySet()){
            char key = entry.getKey();
            if(flag){
                Symbols.get(key).setLow(0);
                Symbols.get(key).setHigh(Symbols.get(key).getProbality());
                Symbols.get(key).setRangeLow(0);
                Symbols.get(key).setRangeHigh(Symbols.get(key).getProbality());
                flag = false;
            }else{
                Symbols.get(key).setLow(Symbols.get(PreKey).getHigh());
                Symbols.get(key).setHigh(Symbols.get(PreKey).getHigh() + Symbols.get(key).getProbality());
                Symbols.get(key).setRangeLow(Symbols.get(key).getLow());
                Symbols.get(key).setRangeHigh(Symbols.get(key).getHigh());
                PreKey = key;
            }
        }
    } 
    
    // TO GET THE NEW DESTERBUTION OF CHARACTERS
    public void NewDestribution (double l , double h){
        for(Map.Entry<Character, Symbol> entry: Symbols.entrySet()){
            char key = entry.getKey();
            LOW = Symbols.get(key).getLow() + (Symbols.get(key).getRangeHigh()-Symbols.get(key).getLow()) * Symbols.get(key).getRangeLow();
            HIGH = Symbols.get(key).getLow() + (Symbols.get(key).getRangeHigh()-Symbols.get(key).getLow()) * Symbols.get(key).getRangeHigh();
            Symbols.get(key).setLow(LOW);
            Symbols.get(key).setHigh(HIGH);
        }
    }
    
    // TO GET THE BINARY NUMBER OF THE CODE
    public String getbinaryFloat(double l,double h){
        String binaryFloat = "";
        int pow = 1;
        double f = 1/Math.pow(2, 1);
        while(true){
            if(f<l){
                binaryFloat += "1";
            }else if(f>h){
                binaryFloat = binaryFloat.substring(0, binaryFloat.length()-1);
                binaryFloat += "01";
            }else{
                break;
            }
            pow++;
            f += 1/Math.pow(2,pow);
        }
        return binaryFloat;
    }
    
    public double getFloatCode(String code){
        double result =0.0;
        for (int i = 1; i <= code.length(); i++) {
            if(code.charAt(i) == '1'){
                result += 1/Math.pow(2, i);
            }
        }
        return result;
    }
    
    public char findSymbol(double C , HashMap<Character,Symbol> Prob){
        char result = 0;
        for(Map.Entry<Character,Symbol> entry: Prob.entrySet()){
            char key = entry.getKey();
            if(Prob.get(key).getLow()<0.5 && Prob.get(key).getHigh() > 0.5){
                result = Symbols.get(key).getSymbol();
                return result;
            }
        }
        return '0';
    }
    
    public void Encode(){
        char s;
        for (int i = 0; i < orgStr.length(); i++){
            s = orgStr.charAt(i);
            while(true){
                if(Symbols.get(s).getLow()<0.5 && Symbols.get(s).getHigh() > 0.5){
                    NewDestribution(Symbols.get(s).getLow(),Symbols.get(s).getHigh());
                    break;
                }
                else if(Symbols.get(s).getLow() > 0.5){
                    Symbols.get(s).E1();
                    CodeScaling += "0";
                    
                }else{
                    Symbols.get(s).E2();
                    CodeScaling += "1";
                }
            }
        }
        code = (Symbols.get(orgStr.charAt(orgStr.length()-1)).getLow() +  Symbols.get(orgStr.charAt(orgStr.length()-1)).getHigh())/2;
        String binaryFloat = getbinaryFloat(Symbols.get(orgStr.charAt(orgStr.length()-1)).getLow() , Symbols.get(orgStr.charAt(orgStr.length()-1)).getHigh());
        System.out.println("The Scaling Code is :" + CodeScaling);
        System.out.println("The Binary Code is :" + binaryFloat);
        System.out.println("The Code is :" + code);
        
    }

    public void Decode(double Code ,int limit ,HashMap<Character,Symbol> prob ){
        int count = 1;
        char key;
        String result = "";

        while(true){
            key = findSymbol(Code,prob);
            if(key != '0'){
                result += key;
                LOW = Symbols.get(key).getLow();
                HIGH = Symbols.get(key).getHigh();
                while(true){
                    if(LOW<0.5 && HIGH>0.5){
                        Code = (Code-LOW)/(HIGH-LOW);
                        break;
                    }
                    else if(Symbols.get(key).getLow() > 0.5){
                        LOW = 2 * LOW;
                        HIGH = 2 * HIGH;
                    }else{
                        LOW = 2 * LOW - 1;
                        HIGH = 2 * HIGH - 1;
                    }
                }
            }else{
                System.out.println("Error !!");
                System.out.println("The Decode until error is :" + result);
            }
            count++;
            if(count==limit){break;}
        }
        System.out.println("The Decode characters is :" + result);
    }
    
    
    public static void main(String[] args) {
        Symbol s ;
        Arathmatic a = new Arathmatic();
        HashMap<Character,Symbol> p = new HashMap<>();
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            char symbol; double low; double high; double probality;
            symbol = in.next().charAt(0);
            low = in.nextDouble();
            high = in.nextDouble();
            probality = in.nextDouble();
            s = new Symbol(symbol, low , high , probality);
            p.put(symbol , s);
        }
        double Code = 0.773;
        a.Decode(Code ,4 , p );
    }
    
}
