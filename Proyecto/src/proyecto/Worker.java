package proyecto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Clase Worker que procesa lotes en memoria y simula reatrdos por operaciones I/O bound.
 */
public class Worker implements Runnable {

    private final String pathOutputFile;

    private final List<String> dataBatch; //se recibe una lista de cadenas directamente de memoria
    public static final String sep = ",";
    private final int[] selectedFields;
    private final int conditionColumn;
    private final String condition;
    private List<String> searchedData;
    private final int workerId;

    //candado GLOBAL
    private static final Object writeLock = new Object();

    public Worker(int workerId, String pathOutputFile, List<String> dataBatch, int[] selectedFields, int conditionColumn, String condition){
        this.workerId = workerId;
        this.pathOutputFile = pathOutputFile;
        this.dataBatch = dataBatch;
        this.selectedFields = selectedFields;
        this.conditionColumn = conditionColumn;
        this.condition = condition;
        this.searchedData = new ArrayList<>();
    }

    @Override
    public void run() {
        //--------------------------------------------------------------------
        //delay simulado entre 10 y 30 ms
        try {
            //usamos ThreadLocalRandom ya que es mas eficiente que Math.random()
            long sleepTime = ThreadLocalRandom.current().nextLong(10, 31);
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("El Worker " + workerId + " fue interrumpido durante su fase de bloqueo.");
        }
        //--------------------------------------------------------------------

        //INICIA
        searchData();
    }

    private void searchData() {
        String[] completeFields;

        //se itera sobre la lista de datos que paso el manager
        for (String data : this.dataBatch) {
            completeFields = data.split(sep);

            //decartamos lineas vacias
            if (completeFields.length <= this.conditionColumn) continue;

            //quitamos espacios en blanco
            String strToCheck = completeFields[this.conditionColumn].trim();

            if (strToCheck.contains(this.condition.trim())){
                //StringBuilder para concatenar cadenas en bucles. REDUCE uso de memoria.
                StringBuilder dataToWrite = new StringBuilder(completeFields[0] + ",");

                for(int i : selectedFields){
                    if (i < completeFields.length) {
                        dataToWrite.append(completeFields[i]).append(",");
                    }
                }

                //eliminamos la ultima coma
                dataToWrite.setLength(dataToWrite.length() - 1);
                dataToWrite.append("\n");

                this.searchedData.add(dataToWrite.toString());
            }
        }

        //guardar solo en coincidencias
        if (!this.searchedData.isEmpty()) {
            try {
                writeData();
            } catch (IOException ex) {
                System.err.println("Error de I/O al escribir resultados en Worker " + workerId + ": " + ex.getMessage());
            }
        }
    }

    // -------------------------------------------------
    //             SECCION CRITICA
    //usamos el monitor 'synchronized'.
    public void writeData() throws IOException {
        File destiny = new File(this.pathOutputFile);

        //usamos el monitor con un candado global para garantizar el funcionamiento en la seccion critica.
        synchronized (writeLock) {
            try (BufferedWriter outputSubFile = new BufferedWriter(new FileWriter(destiny, true))) { //FileWriter contiene mecanismo en modo append
                for(String data : this.searchedData){
                    outputSubFile.write(data);
                }
            }
        }
    }
    // ----------------------------------------------------
}