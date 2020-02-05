/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lz_78.moaaz_yageen;

/**
 *
 * @author Moaaz & Yageen
 */
public class Code {
    int index;
    char next;

    Code(int index,char next){
        this.index = index;
        this.next = next;
    }
    public void setIndex(int i){
        this.index = i;
    }
    public void setNext(char n){
        this.next = n;
    }
    public int getIndex(){
       return index;
    }
    public char getNext(){
       return next;
    }
    public void printCode(){
        System.out.print("<"+index + "," + next + ">" + ",");
    }
}
