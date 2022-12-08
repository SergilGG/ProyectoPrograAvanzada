/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.NativeString.trim;

/**
 *
 * @author sergi
 */

public class Worker  implements Runnable{

    private final String pathOutputFile; 
    private final String pathSourceFile; 
    public static final String sep=",";
    private final int[] selectedFields;
    public static String [] fields;
    private final int idCondition;
    private final String condition;
    private List<String> searchedData;
    private final int idWorker;
    
    public Worker(int idWorker, String pathOutputFile, String pathSourceFile, int[ ] selectedFields, int idCondition, String condition){
        this.pathOutputFile = pathOutputFile;
        this.pathSourceFile = pathSourceFile;
        this.selectedFields = selectedFields;
        this.idCondition = idCondition;
        this.condition = condition;
        this.searchedData = new ArrayList<String>();
        this.idWorker = idWorker;
        
        //System.out.println("\nObjeto Worker creado con id " + this.idWorker);
    }
    
    @Override
    public void run() {
       // System.out.println("Ejecutando run con id " + this.idWorker);
        boolean searchData = searchData();
        
    } 

    private boolean searchData() {
        // Método para realizar el filtrado de columnas y filas
      //  System.out.println("Ejecutando searchData con id " + this.idWorker);
        String data;
        String[] completeFields;

        // Creación de objeto tipo File para el archivo fuente.
        File source = new File(this.pathSourceFile); 
      
        try {
            BufferedReader originSubFile = new BufferedReader(new FileReader(source));
            
            // Se lee cada línea del sub-archivo que se le asignó.
            while ((data = originSubFile.readLine()) != null) {
                 completeFields = data.split(sep);
                 
                 //System.out.println("completeFields: " +  Arrays.toString(completeFields));
                 
                 //Se asigna a una variable la columna para el filtro de filas.
  
                    String strToCheck = trim(completeFields[this.idCondition]);                 
                 
                 //Se verifica si la línea leida para el indicador elegido contiene la condición requerida.
                 if (strToCheck.contains(trim(this.condition))){
                     String dataToWrite = completeFields[0] + ",";
                     for(int i : selectedFields){
                        dataToWrite = dataToWrite + completeFields[i] + ",";
                     }
                     
                     // Se elimina la última "," que sobra en la línea.
                    dataToWrite = dataToWrite.substring(0,  dataToWrite.length()-1);
                    dataToWrite = dataToWrite + "\n";
                    //System.out.print(dataToWrite);
             
                    //Se añade cada elemento a un arreglo de cadenas.
                    this.searchedData.add(dataToWrite);
                    }
                    
                 }
         writeData() ;
         //System.out.print("Está escribiendo el worker " + this.idWorker + "\n");
                       
                 
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            ex.getStackTrace();
            
        } catch (IOException ex) {
            System.out.println("IOException");
            ex.getStackTrace();
        }
        return true;
    }
    
    
    public synchronized void writeData() throws IOException{
        // Creación de objeto tipo File para el archivo de salida.
        
        File destiny = new File(this.pathOutputFile);
        try (BufferedWriter outputSubFile = new BufferedWriter(new FileWriter(destiny, true))) {
            for(String data :  this.searchedData){
                //Se escribe en el archivo de salida por cada
                //System.out.print("\nEstá escribiendo el worker " + this.idWorker);
                outputSubFile.write(data);
            }
        } catch (IOException ex) {
            System.out.println("IOException");
            ex.getStackTrace();
        }
    }
        
}
