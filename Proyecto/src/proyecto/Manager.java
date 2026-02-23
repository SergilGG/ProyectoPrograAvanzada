package proyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Clase Manager
 * Implementa un ExecutorService paraseleccionar entre hilos de plataforma o hilos virtuales (Project Loom).
 * useVirtualThreads indica el tipo de arquitectura de concurrencia que se utiliza.
 * El numTasks indica la cantidad de tareas que se crearan por experimento. Lo que equivale al no. de workers.
 */
public class Manager{


    public void concurrentFiltering(String path,String fileName, String outputhPath, int[] selectedFields, int conditionColumn, String condition, int numTasks, boolean useVirtualThreads) throws InterruptedException {

        System.out.println("\n------ INICIANDO EXPERIMENTO MANAGER-WORKER -----");
        System.out.println("Motor seleccionado: " + (useVirtualThreads ? "Hilos Virtuales (Loom)" : "Hilos de Plataforma"));
        System.out.println("Número de tareas configuradas: " + numTasks);

        //archivo de salida
        String pathSubdirectoryResults = outputhPath + File.separator + "Resultados";
        new File(pathSubdirectoryResults).mkdirs();

        String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String outputFile = fileName.substring(0, fileName.length()-4) + "_filtered(" + todayDate + ").csv";
        String pathOutputFile = pathSubdirectoryResults + File.separator + outputFile;

        try {
            File fileResults = new File(pathOutputFile);
            fileResults.createNewFile();
        } catch(IOException e) {
            System.err.println("Error al crear el archivo de resultados");
            return;
        }

        //lectura en memoria y particion logica del dataset
        //el metodo del proyecto de la materia creaba el no. de archivos = al no. de hilos obtenidos de getCores de la clase CoresNumber
        //para este proyecto, se pide crear cantidades de 10k, 50k y 100k tareas/workers.
        System.out.println("Leyendo archivo principal y creando " + numTasks + " lotes en memoria...");
        long startSplit = System.currentTimeMillis();

        //metodo auxiliar partitionFileInMemory. Dividimos el dataset original en conjuntos especificos almacenados en memoria directamente.
        List<List<String>> taskBatches = partitionFileInMemory(path + File.separator + fileName, numTasks);

        long endSplit = System.currentTimeMillis();
        System.out.printf("Tiempo de partición lógica: %d ms\n", (endSplit - startSplit));

        //medicion de RAM Inicial
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); //garbache colector
        long memoryUsageBefore = runtime.totalMemory() - runtime.freeMemory();

        //------------------------------------------------------------------------------------
        //SELECCION DE TIPO DE CONCURRENCIA
        //interfaz ExecutorService de java.util.concurrent
        ExecutorService concurrencyEngine;
        if (useVirtualThreads) {
            //usamos hilos virtuales, complemante administrados por JVM
            concurrencyEngine = Executors.newVirtualThreadPerTaskExecutor();
        } else {
            //usamos hilos fisicos
            concurrencyEngine = Executors.newFixedThreadPool(CoresNumber.getCores());
        }

        //------------------------------------------------------------------------------------

        System.out.println("Iniciando procesamiento concurrente...");
        long startConcurrent = System.currentTimeMillis();

        for (int i = 0; i < taskBatches.size(); i++) {
            //pasamos el lote List<String> directamente al worker en lugar de una ruta / taskBatches.get(i)
            Worker worker = new Worker(i, pathOutputFile, taskBatches.get(i), selectedFields, conditionColumn, condition);
            concurrencyEngine.submit(worker);
        }

        //aisla los hilos de la basura del GC
        long memoryUsageAfterSubmit = runtime.totalMemory() - runtime.freeMemory();
        double engineMemoryFootprint = (memoryUsageAfterSubmit - memoryUsageBefore) / (1024.0 * 1024.0);

        //manejo de sincronizacion
        concurrencyEngine.shutdown(); //cerramos la recepcion de nuevas tareas
        boolean areFinished = concurrencyEngine.awaitTermination(2, TimeUnit.HOURS); //esperar a que todos finalicen

        long endConcurrent = System.currentTimeMillis();

        //-------------------------------------------------------------------------------
        if (areFinished) {
            System.out.println("\n******* RESULTADOS DEL EXPERIMENTO ******");
            System.out.printf("Tiempo total de ejecución concurrente: %d ms\n", (endConcurrent - startConcurrent));
            System.out.printf("Consumo estimado de memoria (solo hilos): %,.2f MB\n", engineMemoryFootprint);
        } else {
            System.err.println("El procesamiento excedió el tiempo límite.");
        }
    }

    /**
     * Lee el CSV secuencialmente y reparte las líneas entre un número de listas equivalente al número de tareas usando un algoritmo Round-Robin.
     */
    private List<List<String>> partitionFileInMemory(String completePath, int numTasks) {

        //lista de listas  = numTasks
        List<List<String>> batches = new ArrayList<>(numTasks);

        //creamos 'numTasks' listas vacias
        for (int i = 0; i < numTasks; i++) {
            batches.add(new ArrayList<>());
        }

        //BufferedReader evita saturar la RAM
        try (BufferedReader reader = new BufferedReader(new FileReader(completePath))) {
            String linea;
            int contador = 0;

            //DISTRIBUCIÓN ROUND-ROBIN
            while ((linea = reader.readLine()) != null) {
                //distribuimos las lineas en no. equitativo en las numTasks listas.
                batches.get(contador % numTasks).add(linea);
                contador++;
            }
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo fuente: " + e.getMessage());
        }

        //listas de datos llenas
        return batches;
    }
}