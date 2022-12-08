
package proyecto;
import java.io.File;
import java.io.IOException;
import static proyecto.SplitFile2.Split;
import java.time.*; // Este paquete contiene LocalDate, LocalTime y LocalDateTime.
import java.time.format.*;  // Este paquete contiene DateTimeFormatter.
import java.util.Date;

/**
 *
 * @author aguirre
 */
public class Manager {
    
    private Thread poolWorkers[];

    public void FiltradoConcurrente(String path, String fileName, int[ ] selectedFields, int idCondition, String condition) throws InterruptedException {
        
        // Split the number of files into n*CPU's subfiles.
       Date tiempoInicialSplit = new Date();
       int finalNumberOfFiles = Split(path, fileName);
       Date tiempoFinalSplit = new Date();
       double tiempoSplit;
       tiempoSplit = tiempoFinalSplit.getTime() - tiempoInicialSplit.getTime();
       
       //System.out.printf("\n Tiempo split: %,.6f ms ", tiempoSplit); 
  
       
        // Make the subdirectory Resultados in the same path of source file 
        String pathSubdirectoryResultados = path + File.separator + "Resultados" ;
        File file = new File( pathSubdirectoryResultados );
        file.mkdirs();
        
        //Create the result output file, this will be the resource shared by the threads.
        String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String outputFile = fileName.substring(0, fileName.length()-4) + "_filtered(" + todayDate + ").csv";
        String pathOutputFile = pathSubdirectoryResultados + File.separator + outputFile;
        
        try {
           File fileResultados = new File( pathOutputFile);
           fileResultados.createNewFile();
           System.out.println("\nCreo el archivo de resultados vacíos: " +  fileResultados);
        } catch(IOException e) {
           System.out.println("\nNo pudo crear el archivo de resultados");
        }        

        // Create a pool of thread fixed as number of sub files
        this.poolWorkers = new Thread[finalNumberOfFiles];
        System.out.println("Creo " + finalNumberOfFiles + " hilos" );
        
         
       
        for (int i = 0; i < finalNumberOfFiles; i++) {
           String pathSourceFile = path + File.separator + "subFile_" + Integer.toString(i+1) + "_" + fileName ;  
           //System.out.println("\nCreo el trabajador " + i + "con file " + pathSourceFile  );
            Worker worker = new Worker(i, pathOutputFile, pathSourceFile, selectedFields, idCondition, condition);
            this.poolWorkers[i] = new Thread(worker);
            this.poolWorkers[i].start();
           /* try {
                this.poolWorkers[i].join();
            } catch (InterruptedException e) {
                 System.out.println(e);
            }*/  
        }
        System.out.printf("\n Tiempo split: %,.6f ms ", tiempoSplit); 
       
        /*for (int i = 0; i < finalNumberOfFiles; i++) {
            try {
                this.poolWorkers[i].join();
            } catch (InterruptedException e) {
            
        }   */   
    }
}

