package proyecto;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;


public class SplitFile2 {
    
    
    //get the number of lines in a file 
    public static int getNumberLines(String path, String fileName){
        
        int lineCount = 0;
        Path pathAbsolute = Paths.get(path, fileName);
        
        try (Stream<String> stream = Files.lines(pathAbsolute)) {

                //get numberof lines of file
                lineCount = (int) stream.count();
                System.out.println("Numero de lineas: "+ lineCount);
                //return lineCount;        //Get number of lines per file
                               
        } catch (IOException ex) {        //Get number of lines per file
            Logger.getLogger(SplitFile2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lineCount;      
    }    
    
    public static void Split(String path, String fileName){
       
       // get number of cores 
       int CPUs = CoresNumber.getCores();
       
       //copy the content of fileName into 
       copyContent(path, fileName, 4*CPUs );
                
    }
    
    public static void copyContent(File File1, File File2, int startLine, int endLine)
        throws Exception
    {   LineNumberReader reader = null;

        // FileOutputStream out = new FileOutputStream(b);
        reader = new LineNumberReader(new FileReader(File2));
        for (int i = startLine; i < endLine; i++) {
          System.out.println(i);
        }
    }


    // copia el contenido del archivo con nombre fileName en la ruta path a numFiles archivos
    public static void copyContent(String path, String fileName, int numFiles ){
        
        //Get number of lines of file
        int numLines = getNumberLines(path, fileName);
        String linea;

        File f1 = new File(path, fileName);
        int tmpNumFiles ;
        
        // get the remaining number of lines 
        int numRemainingLines = numLines%numFiles;
        
        // get the numer of lines per file 
        int linesPerFile = ((numLines- numRemainingLines)/numFiles);
        
        //if there are remaining lines an extra file is created with them 
        if(numRemainingLines != 0){
            tmpNumFiles = numFiles +1;
            }
        else{
            tmpNumFiles = numFiles ;
        }
        
        //there are not enough lines to divide it into n files, write it all in one file
        if(linesPerFile == 0){
            linesPerFile = numRemainingLines;
            tmpNumFiles = 1;
        }
        
        System.out.println("linesPerFile: "+ linesPerFile);
        System.out.println("numRemainingRead: "+ numRemainingLines);
            
        try {
            BufferedReader sourceFile  = new BufferedReader(new FileReader(f1));
            
            int tmpNumLines;
            int i = 1;
            while ((linea = sourceFile.readLine()) != null){
                for (int k = 1; k <= tmpNumFiles; k++) {
                    String newFileName =   "subFile_" + Integer.toString(k) + "_" + fileName ;  

                    File f2 = new File(path, newFileName);
                    try (BufferedWriter outputFile = new BufferedWriter(new FileWriter(f2))) {
                        tmpNumLines = 0;
                        //System.out.println("tmpNumLines: " + tmpNumLines);
                        //System.out.println("Line antes del while: " + linea);
                        
                        while (linea  != null && tmpNumLines < linesPerFile ) {
                            //int numLines = 0;
                            // System.out.println("Line en el while: " + linea);
                            outputFile.write(linea +  "\n");
                            //System.out.println("Escribio la linea : " + linea + " en el archivo " + k);
                            tmpNumLines = tmpNumLines +1;
                            linea = sourceFile.readLine() ;
                            //System.out.println("tmpNumLines: " + tmpNumLines);
                        }
                    }
                    //System.out.println("tmpNumLines: " + tmpNumLines);
                    //System.out.println("Line antes del while: " + linea);
                }  
            }
            sourceFile.close();
        }
        catch (FileNotFoundException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
 


