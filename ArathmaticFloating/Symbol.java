/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.arathmaticfloating_muaath.yageen;

/**
 *
 * @author Moaaz
 */
public class Symbol {
    
    private char symbol;
    private double low;
    private double high;
    private double rangeLow;
    private double rangeHigh;
    private double probality;

    public Symbol() {
    }
    
    public Symbol(char symbol, double low, double high, double rangeLow, double rangeHigh, double probality) {
        this.symbol = symbol;
        this.low = low;
        this.high = high;
        this.rangeLow = rangeLow;
        this.rangeHigh = rangeHigh;
        this.probality = probality;
    }

    public Symbol(char symbol, double low, double high, double probality) {
        this.symbol = symbol;
        this.low = low;
        this.high = high;
        this.probality = probality;
    }
    
    
    
    public void E1(){
        this.low = 2 * this.low;
        this.high = 2 * this.high;
    }
    
    public void E2(){
        this.low = (2 * this.low) - 1;
        this.high = (2 * this.high) - 1;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getRangeLow() {
        return rangeLow;
    }

    public void setRangeLow(double rangeLow) {
        this.rangeLow = rangeLow;
    }

    public double getRangeHigh() {
        return rangeHigh;
    }

    public void setRangeHigh(double rangeHigh) {
        this.rangeHigh = rangeHigh;
    }

    public double getProbality() {
        return probality;
    }

    public void setProbality(double probality) {
        this.probality = probality;
    }
    
    
    
}

