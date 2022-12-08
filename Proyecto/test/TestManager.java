
package proyecto;

import java.io.File;
import java.util.Date;
import static proyecto.UserInteraction.askForColumnCondition;
import static proyecto.UserInteraction.askForColumnIdCondition;
import static proyecto.UserInteraction.askForColumns;
import static proyecto.UserInteraction.showInterface;

/**
 *
 * @author aguirre
 */
public class TestManager {
    public static void main(String[] args) throws InterruptedException {

        // set path of file Source
        String pathFileSource = "/home/aguirre/Posgrado/ProgramacionAvanzada/Data/JQRO/DatosConcatenados";
        
        // Set filename
        String fileName = "2015-2021-JQRO_minuto_contaminates_meteorologia.csv";
        
        
        //Show table to select columns and conditions to filter 
         showInterface(pathFileSource, fileName);
         int columns [ ] = askForColumns();
         int idCondicion  = askForColumnIdCondition( );
         String columnCondition = askForColumnCondition( );        
        
        double tiempoSerial, tiempoConcurrente;
        double speedUp;

        Date tiempoInicialSerial, tiempoFinalSerial, tiempoInicialConcurrente, tiempoFinalConcurrente;       
        Manager manager = new Manager();
        
        tiempoInicialConcurrente = new Date();
        manager.FiltradoConcurrente(pathFileSource, fileName, columns, idCondicion, columnCondition );
        tiempoFinalConcurrente = new Date();
 
        String path_2 = pathFileSource + File.separator + fileName ;
        String path = "/home/aguirre/Posgrado/ProyectoPrograAvanzada/DatosConcatenados/Resultados.csv";

        
        tiempoInicialSerial = new Date();
        Worker example = new Worker(1,path, path_2,  columns,  idCondicion, columnCondition);
        example.run();        
        tiempoFinalSerial = new Date();
        
        tiempoSerial = tiempoFinalSerial.getTime() - tiempoInicialSerial.getTime();
        tiempoConcurrente = tiempoFinalConcurrente.getTime() - tiempoInicialConcurrente.getTime();

        speedUp = calcularSpeedUp(tiempoSerial, tiempoConcurrente);
        System.out.printf("\n Tiempo serial: %,.6f ms ", tiempoSerial);        
        System.out.printf("\n Tiempo concurrente: %,.6f ms ", tiempoConcurrente);
        System.out.printf("\n SpeedUp: %.6f  \n\n", speedUp);        
    }
    
    private static double calcularSpeedUp(double s, double p) {
        double r;
        r = s / p;
        return r;
    }
    
}
