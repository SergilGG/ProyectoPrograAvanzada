
package proyecto;

import static proyecto.UserInteraction.askForColumnCondition;
import static proyecto.UserInteraction.askForColumnIdCondition;
import static proyecto.UserInteraction.askForColumns;
import static proyecto.UserInteraction.showInterface;

/**
 *
 * @author aguirre
 */
public class UserInteractionTest {
     public static void main(String[] args){
         String path = "/home/aguirre/Posgrado/ProgramacionAvanzada/Data/JQRO";
         String fileName = "header.csv";
         
         showInterface(path, fileName);
         int columns [ ] = askForColumns();
         int idColumn  = askForColumnIdCondition( );
         String columnCondition = askForColumnCondition( );
     }
}
