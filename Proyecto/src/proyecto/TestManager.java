package proyecto;

/**
 * Ejecuta experimento automatizado comparando hilos de plataforma vs virtuales.
 */
public class TestManager {
    public static void main(String[] args) throws InterruptedException {

        //-------------------------------------------------------------------------------
        //rutas
        String pathFileSource = "C:\\Users\\sergi\\OneDrive - UNIVERSIDAD NACIONAL AUTÓNOMA DE MÉXICO\\Documents\\Maestria UNAM\\EGC\\Programación Avanzada\\Examen_final_EGC\\ProyectoPrograAvanzada\\DatosConcatenados";
        String fileName = "2015-2021-JQRO_minuto_contaminates_meteorologia.csv";
        String outputh_path = "C:\\Users\\sergi\\OneDrive - UNIVERSIDAD NACIONAL AUTÓNOMA DE MÉXICO\\Documents\\Maestria UNAM\\EGC\\Programación Avanzada\\Examen_final_EGC\\ProyectoPrograAvanzada\\Proyecto";
        //-------------------------------------------------------------------------------


        //hardcode de info
        int[] columns = {1, 2, 3, 4}; // O3,O3_flag,SO2,SO2_flag
        int idCondicion = 2;          // O3_flag
        String columnCondition = "OK";

        Manager manager = new Manager();

        //no. de tareas
        int[] cargasDePrueba = {10000, 50000, 100000};

        System.out.println("---------------------------------------------------------");
        System.out.println("  INICIANDO EXPERIMENTO: PLATAFORMA VS VIRTUALES  ");
        System.out.println("---------------------------------------------------------");

        for (int numTareas : cargasDePrueba) {
            System.out.println("\n\n*** > EVALUANDO CARGA: " + numTareas + " TAREAS **** ");

            //Hilos de plataforma
            manager.concurrentFiltering(pathFileSource, fileName, outputh_path, columns, idCondicion, columnCondition, numTareas, false);

            Thread.sleep(2000);

            //Hilos virtuales
            manager.concurrentFiltering(pathFileSource, fileName, outputh_path, columns, idCondicion, columnCondition, numTareas, true);

            Thread.sleep(2000);
        }

        System.out.println("---------------------------------------------------------");
        System.out.println("---------------------------------------------------------");
        System.out.println("               EXPERIMENTO CONCLUIDO              ");
        System.out.println("---------------------------------------------------------");
    }
}