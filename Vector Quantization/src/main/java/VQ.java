
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Moaaz
 */
public class VQ {
    
    public  int [][] originalImage ;
    public String nameOfCompress = "CompressFileMeOnGrayScale.txt";
    public String nameOfDecompress = "DecompressMeOnGrayScale.jpg";
    
    public static int[][] readImage(String filePath)
    {
	int width=0;
	int height=0;
        File file=new File(filePath);
        BufferedImage image=null;
        try
        {
            image=ImageIO.read(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

          width=image.getWidth();
          height=image.getHeight();
        int[][] pixels=new int[height][width];

        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                int rgb=image.getRGB(x, y);
                int alpha=(rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb >> 0) & 0xff;

                pixels[y][x]=r;
            }
        }

        return pixels;
    }
    
    public static void writeImage(int[][] pixels,String outputFilePath,int width,int height)
    {
        File fileout=new File(outputFilePath);
        BufferedImage image2=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB );

        for(int x=0;x<width ;x++)
        {
            for(int y=0;y<height;y++)
            {
                image2.setRGB(x,y,(pixels[y][x]<<16)|(pixels[y][x]<<8)|(pixels[y][x]));
            }
        }
        try
        {
            ImageIO.write(image2, "jpg", fileout);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    void PrintVector (vector v)
    {
        for (int i=0 ; i<v.height ; i++ )
        {
            for (int j=0 ; j<v.width ; j++)
            {
                System.out.print(v.data[i][j] + "  ");
            }
            System.out.println();
        }
        
        System.out.println("---------------------------");
    }
    
    ArrayList <vector> BuildVectors (int [][] originalImage , vector [][] vectors , int numOfRows , int numOfCols , int widthOfVector , int heightOfVector)
    {   
        
        ArrayList <vector> AllVectors = new ArrayList<>();
        vector curVector = new vector ( widthOfVector , heightOfVector );
        
        for (int i=0 ; i<originalImage.length ; i+=heightOfVector)
        {
            for (int j=0 ; j<originalImage[0].length ; j+=widthOfVector)
            {   
               int x = i ;
               int z = j ;
               curVector = new vector ( widthOfVector , heightOfVector );
//                System.out.println("length = " + curVector.data.length);
               
               for (int n=0 ; n<heightOfVector ; n++)
                {
                    for (int m=0 ; m<widthOfVector ; m++)
                    {    
                        //System.out.println("x = " + x + " z = " + z);
                        curVector.data[n][m]= originalImage[x][z++];
                        //System.out.println("date = " + curVector.data[n][m]);
                    }
                    
                    x++;
                    z=j;
                }  
                
               // PrintVector(curVector);
                AllVectors.add(curVector);
            }
        }
        
        int indx =0 ;
        
        for (int i=0 ; i<numOfRows ; i++) // filling the new matrix that consists of vectors onli 
        {
            for (int j=0 ; j<numOfCols ; j++)
            {
                vectors[i][j] = AllVectors.get(indx++);
            }
        }
        
        return AllVectors ;
   }
    
    int IndexOfMinDistance (ArrayList <Double> distance_difference )
    {
        double min_diff = distance_difference.get(0); // assume first element is the min 
        int indx = 0 ;
        
        for (int i=1 ; i<distance_difference.size() ; i++)
        {
            if ( distance_difference.get(i) < min_diff)
            {
               min_diff = distance_difference.get(i);
               indx = i ;
            }
            
        }
        return indx ;
    }
    
     ArrayList<vector> Associate ( ArrayList<vector> split , ArrayList <vector> data )
    {   
        ArrayList <split_element> Split = new ArrayList<>();
        ArrayList <vector> Averages = new ArrayList<> ();
        int width = data.get(0).width;
        int height = data.get(0).height ;
                
        for (int i = 0; i < split.size(); i++)  // inilialization 
        {  
           split_element initial = new split_element() ;
           initial.setValue(split.get(i));
           Split.add(initial);
        }
        
        for (int i=0 ; i<data.size() ; i++) // Associate data
        {
                  vector current = data.get(i);
                  ArrayList <Double> distance_difference = new ArrayList<> ();
                  
                 // PrintVector(data.get(i));
                  
                  for (int j=0 ; j<split.size() ;j++)
                  {   
                      double total_diff = 0 ;
                      
                      for (int w=0 ; w<width ; w++)
                      {
                          for (int h=0 ; h<height ; h++)
                          {
                              double value = current.data[w][h]- split.get(j).data[w][h];
                              double distanc_diff =  Math.pow( value , 2);
                              total_diff +=distanc_diff ;
                          }
                      }
                    
                  //  System.out.println("Total diff = " + total_diff);  
                    distance_difference.add(total_diff);
                      
                 }
                  
                  int indx = IndexOfMinDistance (distance_difference);
                  
                  ArrayList <vector> cur_associated = Split.get(indx).getAssoicated();
                  
                  cur_associated.add(current);
                 
                  split_element New = new split_element(Split.get(indx).getValue() , cur_associated);
                  
                  Split.set(indx , New );
                  
          }
        

        for (int i=0 ; i<Split.size() ; i++) // calculate average for the associated values
        {
            int arraysize = Split.get(i).getAssoicated().size();
            vector avg = new vector(width , height);
            
            for (int w = 0; w < width; w++) 
            {
                for (int h = 0; h < height; h++) 
                {   
                    double total = 0 ;
                   
                    for (int j = 0; j < arraysize; j++) 
                    {   
                        total+= Split.get(i).getAssoicated().get(j).data[w][h];
                    }
                    
                    avg.data[w][h]= total/arraysize;
                } 
               
            }
              
                Averages.add(avg);
            
        }
        
        return Averages ;
    }
    
     ArrayList<vector> SplitVectors (ArrayList <vector> Averages ,  ArrayList <vector> data , int numoflevels ) // split original averages
    {
         int width = Averages.get(0).width ;
         int height = Averages.get(0).height ;
       
         for (int i=0 ; i<Averages.size() ; i++)
        {   
            if (Averages.size()<numoflevels)
            {
              
            ArrayList <vector> split = new ArrayList<>();
            
            for (int j=0 ; j<Averages.size() ; j++)
            {   
              vector left = new vector( width , height);
              vector right = new vector( width , height);
              
               for (int w=0 ; w<width ; w++)
               {
                   for (int h=0 ; h<height ; h++)
                   {   
                       int cast = (int)Averages.get(j).data[w][h] ;
                       
                       left.data[w][h]= cast;
                       right.data[w][h]= cast+1;
                   
                   }
              
               }
              
              split.add(left);
              split.add(right);  
            }
            
            Averages.clear();
           
            Averages = Associate( split , data);
            
            i=0 ;
            
            }
            
            else 
                break;
            
        } 
         
         return Averages ;
    }
    
     ArrayList<vector> Modify ( ArrayList<vector> prev_Averages , ArrayList<vector> new_Averages , ArrayList<vector> data  )
    {
       while (true)
        { 
           int width = new_Averages.get(0).width;
           int height = new_Averages.get(0).height;
           int totaldiff = 0 ;
           int avgdiff = 0 ;
           
           for (int i=0 ; i<new_Averages.size() ; i++)
           {   
               double DiffrenceOf2Vectors = 0 ;
                       
               for (int w=0 ; w<width ; w++)
               {
                   for (int h=0 ; h<height ; h++)
                   {
                       DiffrenceOf2Vectors += Math.abs(prev_Averages.get(i).data[w][h] - new_Averages.get(i).data[w][h]) ;
                   }
               }
              
              totaldiff+=DiffrenceOf2Vectors;
           }
           
           avgdiff = totaldiff / prev_Averages.size() ;
           
           if (avgdiff < 0.0001 )
           {
               break;
           }
           
           else 
           {
               prev_Averages = new_Averages ;
               new_Averages = Associate( new_Averages , data);
           }
           
        }
       
       return new_Averages ;
        
    }
     
    void Quantization ( int numoflevels ,  ArrayList <vector> data , int widthOfVector , int heightOfVector , vector [][] vectors , int numOfRows , int numOfCols  )
    {    
         ArrayList <vector> Averages = new ArrayList<>();         
        // initalize first avg
        vector first_avg = new vector( widthOfVector , heightOfVector );
        
        for (int w = 0; w < widthOfVector; w++) 
        {  
            for (int h = 0; h < heightOfVector; h++)
            {
                 double total = 0 ;
                
                for (int i = 0; i < data.size(); i++) 
                {
                        total += data.get(i).data[w][h];
                    
                }
                
                first_avg.data[w][h] = total/data.size();

           }

        }

        Averages.add(first_avg);
        Averages = SplitVectors (Averages , data , numoflevels );
        ArrayList<vector> prev_Averages = Averages ;
        ArrayList<vector> new_Averages = Associate( Averages , data);
        new_Averages = Modify(prev_Averages, new_Averages, data);
        
        
        ArrayList <vector> codeBook = new ArrayList<>();
        
        for (int i=0 ; i<new_Averages.size() ; i++)
        {
            codeBook.add(new_Averages.get(i));
        }
        
        
        int index = 0;
        
        
        for (int i=0 ; i<widthOfVector ; i++)
        {
            for (int j=0 ; j<numOfCols ; j++)
            {
                vectors[i][j] = data.get(index++);
            }
        }
        
        CompressImage (codeBook , vectors );
 
    } 
    
    void CompressImage ( ArrayList<vector> codeBook , vector [][] vectors )
    {
       int Rows = vectors.length ;
       int Cols = vectors[0].length ;
       int [][] compress_Image = new int [Rows][Cols];
       
       for (int i=0 ; i<Rows ; i++)
       {
           for (int j=0 ; j<Cols ; j++)
           {
                vector currentVector = vectors[i][j];
                ArrayList <Double> distanceDifference = new ArrayList<> ();
                
                for (int k=0 ; k<codeBook.size() ;k++)
                {   
                    double total_diff = 0 ;
                  
                    for (int w=0 ; w<codeBook.get(0).width ; w++)
                    {
                        for (int h = 0; h < codeBook.get(0).height; h++)
                        {
                            double value = currentVector.data[w][h] - codeBook.get(k).data[w][h];
                            double distanc_diff = Math.pow(value, 2);
                            total_diff += distanc_diff;
                        }
                    }
                    
                    distanceDifference.add(total_diff);
                }
                
                int index = IndexOfMinDistance (distanceDifference);
                compress_Image[i][j]= index ;
           }
       }
        SaveCodeBookCompImg (codeBook , compress_Image);
    }
    
    Scanner sc;

    public void open_file(String FileName) {
        try {
            sc = new Scanner(new File(FileName));
        } catch (Exception e) {

        }
    }

    public void close_file() {
        sc.close();
    }

    Formatter out;

    public void openfile(String pass) {
        try {
            out = new Formatter(pass);
        } catch (Exception e) {
        }
    }

    public void closefile() {
        out.close();
    }
    
    void write(String code) {
        out.format("%s", code);
        out.format("%n");
        out.flush();
    }
    
    
    void Decompress ()
    {
        ArrayList<vector> codeBook = new ArrayList <vector>();
        int [][] comp_image = new int [1][1] ; 
        comp_image = Reconstruct( codeBook , comp_image);
        int [][] Decomp_image = new int [originalImage.length][originalImage[0].length];
        
        for (int i=0 ; i<comp_image.length ; i++)
        {
            for (int j=0 ; j<comp_image[0].length ; j++)
            {
                vector cur = new vector();
                cur = codeBook.get(comp_image[i][j]);
                
                int cornerx = i*cur.height;
                int cornery = j*cur.width ;
                
                
                for (int h=0 ; h<cur.height ; h++)
                {
                    
                    for (int k=0 ; k<cur.width ; k++)
                    {
                        Decomp_image[cornerx+h][cornery+k] = (int) cur.data[h][k];
                    }
                }
                
            }
        }
        writeImage(Decomp_image, nameOfDecompress, Decomp_image[0].length, Decomp_image.length);
    }
    
    void SaveCodeBookCompImg ( ArrayList<vector> codeBook , int [][] comp_image )
    {
        openfile(nameOfCompress);
        String codeBookSize = "" + codeBook.size();
        String WidthOfBlock = "" + codeBook.get(0).width;
        String heightOfBlock = "" + codeBook.get(0).height;
        
        write(codeBookSize);
        write(WidthOfBlock);
        write(heightOfBlock);
        
        for (int i=0 ; i<codeBook.size() ; i++)
        {
            for (int w=0 ; w<codeBook.get(i).width ; w++)
            { 
                String row = "";
                for (int h=0 ; h<codeBook.get(i).height ; h++)
                {
                    row += codeBook.get(i).data[w][h] + " ";
                }
                write(row);
            }
        }        
        
        String com_image_height = "" + comp_image.length ;
        write(com_image_height);
        String com_image_width = "" + comp_image[0].length ;
        write(com_image_width);
        
        for (int i=0 ; i<comp_image.length ; i++)
        {   
            String row = "";
            
            for (int j=0 ; j<comp_image[0].length ; j++)
            {
                row+= comp_image[i][j] +" ";
            }
            write(row);
        }
        closefile();
    }
    
    
    int [][] Reconstruct( ArrayList<vector> codeBook , int [][] comp_image)
    {
        open_file(nameOfCompress);
        int codeBookSize = Integer.parseInt(sc.nextLine());
        int WidthOfBlock = Integer.parseInt(sc.nextLine());
        int heightOfBlock = Integer.parseInt(sc.nextLine());
        
        for (int i=0 ; i<codeBookSize ; i++)
        {
            vector currentVector = new vector(WidthOfBlock , heightOfBlock);
             
            for (int w=0 ; w<WidthOfBlock ; w++)
            {  
                String row = sc.nextLine();
                String [] elements = row.split(" ");
                
                for (int h=0 ; h<heightOfBlock ; h++)
                {
                  currentVector.data[w][h]= Double.parseDouble(elements[h]);
                }
            }
            codeBook.add(currentVector);
        }
        
        int com_image_height = Integer.parseInt(sc.nextLine());
        int com_image_width =  Integer.parseInt(sc.nextLine());
        comp_image = new int [com_image_height][com_image_width];
        
        for (int i=0 ; i<comp_image.length ; i++)
        {   
            String line = sc.nextLine();
            String [] row = line.split(" ");
            
            for (int j=0 ; j<comp_image[0].length ; j++)
            {
                comp_image[i][j] = Integer.parseInt(row[j]);
            }
        }
        close_file();
        return comp_image ;
    }
}



class vector 
    {  
        int width ;
        int height ;
        double [][] data ;
        
        public vector () {}
        public vector(int width, int height) {
            this.width = width;
            this.height = height;
            this.data = new double [height][width];
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public double[][] getData() {
            return data;
        }

        public void setData(double[][] data) {
            this.data = data;
        }
        
        
    }
    
    class split_element 
    {
        vector value ;
        ArrayList<vector> assoicated = new ArrayList<>();
        
        public split_element() {}
        
        public split_element(vector value ,ArrayList<vector> assoicated ) {
            this.value = value;
            this.assoicated = assoicated ;
        }

        public vector getValue() {
            return value;
        }

        public void setValue(vector value) {
            this.value = value;
        }

        public ArrayList<vector> getAssoicated() {
            return assoicated;
        }

        public void setAssoicated(ArrayList<vector> assoicated) {
            this.assoicated = assoicated;
        }

        
    }