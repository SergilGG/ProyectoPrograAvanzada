
package proyecto;

import static proyecto.SplitFile2.Split;

/**
 *
 * @author aguirre
 */
public class SplitFileTest {
     public static void main(String[] args){
         String path = "/home/aguirre/Posgrado/ProgramacionAvanzada/Data/JQRO/Contaminantes/minuto";
         String fileName = "2014-2022-JQRO_minuto.csv";
         Split(path, fileName);
     }  
}
