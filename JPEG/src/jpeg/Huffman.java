/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author Moaaz
 */

class HuffmanNode { 
  
    int data; 
    String c; 
  
    HuffmanNode left; 
    HuffmanNode right; 
} 
  

class MyComparator implements Comparator<HuffmanNode> { 
    public int compare(HuffmanNode x, HuffmanNode y) 
    { 
          return x.data - y.data; 
    } 
} 
  
public class Huffman { 
    public static HashMap<String,String> huffmanTable = new HashMap();
    
    public static void setToHuffmanTable(HuffmanNode root, String s) 
    {         
        if (root.left == null && root.right == null) { 
            huffmanTable.put(root.c, s);  
            return;
        }
        setToHuffmanTable(root.left, s + "0"); 
        setToHuffmanTable(root.right, s + "1");
    }
    
    public HashMap<String,String> BuildHuffmanCode(HashMap<String , Integer> Prop)
    { 
        int n = Prop.size();
        PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>(n, (Comparator<? super HuffmanNode>) new MyComparator()); 
  
        for(Map.Entry<String, Integer> entry: Prop.entrySet()){
            String tag = entry.getKey();
            HuffmanNode hn = new HuffmanNode(); 
            hn.c = tag;
            hn.data = Prop.get(tag); 
            hn.left = null; 
            hn.right = null; 
            q.add(hn);
        }
  
        HuffmanNode root = null;
  
        while (q.size() > 1) {
            HuffmanNode x = q.peek();
            q.poll();
            HuffmanNode y = q.peek(); 
            q.poll(); 
            HuffmanNode f = new HuffmanNode(); 
            f.data = x.data + y.data; 
            f.c = "-";
            f.left = x; 
            f.right = y;
            root = f;
            q.add(f);
        } 
        setToHuffmanTable(root, ""); 
        return huffmanTable;
    } 
}

