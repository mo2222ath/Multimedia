/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

/**
 *
 */
public class discriptor {
    int numZero ;
    int numberOfCategory ;
    int value;

    public discriptor() {
    }

    @Override
    public String toString() {
        return "(" + numZero + "," + numberOfCategory + ") and the value is: " + value;
    }
    

    public discriptor(int nonzero, int number , int value) {
        this.numZero = nonzero;
        this.numberOfCategory = number;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNumZero() {
        return numZero;
    }

    public void setNumZero(int nonzero) {
        this.numZero = nonzero;
    }

    public int getNumberOfCatagory() {
        return numberOfCategory;
    }

    public void setNumberOfCatagory(int catogrynum) {
        this.numberOfCategory = catogrynum;
    }
    @Override
    public boolean equals(Object o)
    { 
        discriptor s;
        if(!(o instanceof discriptor)) 
        { 
            return false; 
        } 
          
        else
        { 
            s=(discriptor)o; 
            if(this.numberOfCategory==s.getNumberOfCatagory() && this.numZero==s.getNumZero()) 
            {
                return true; 
            } 
        } 
        return false; 
    } 
} 

