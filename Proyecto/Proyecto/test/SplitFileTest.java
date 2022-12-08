


import java.io.File;
import java.io.IOException;
import static proyecto.SplitFile2.Split;
import java.time.*; // Este paquete contiene LocalDate, LocalTime y LocalDateTime.
import java.time.format.*;  // Este paquete contiene DateTimeFormatter.
import java.util.Date;
import static proyecto.SplitFile2.Split;

/**
 *
 * @author aguirre
 */
public class SplitFileTest {
     public static void main(String[] args){
         String path = "/home/aguirre/Posgrado/ProgramacionAvanzada/Data/JQRO/DatosConcatenados";
         String fileName = "2015-2021-JQRO_minuto_contaminates_meteorologia.csv";
         
        // Split the number of files into n*CPU's subfiles.
       Date tiempoInicialSplit = new Date();
       int finalNumFiles = Split(path, fileName);
       Date tiempoFinalSplit = new Date();
       double tiempoSplit;
       tiempoSplit = tiempoFinalSplit.getTime() - tiempoInicialSplit.getTime();
       
       System.out.printf("\n Tiempo split: %,.6f ms ", tiempoSplit);          
         
         System.out.println("finalNumFiles: "+ finalNumFiles +" in file: " + fileName);
         //String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
         //System.out.println("Fecha con formato: " + fecha);

     }  
}
