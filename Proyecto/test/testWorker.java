
import proyecto.Worker;


/**
 *
 * @author sergi
 */
public class testWorker{
    public static void main(String[] args) {
        
        String path_2 = "archivo_prueba";
        String path = "Resultados.csv";
        int[] columnas = {1,2,3,4};
        int idCondicion = 0;
        String condicion = "-02-";
        
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        
        Worker example = new Worker(1,path, path_2,  columnas,  idCondicion, condicion);
        example.run();
    }   
}
