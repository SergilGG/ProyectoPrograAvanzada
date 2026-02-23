# Comparación de Concurrencia: Hilos de Plataforma vs Hilos Virtuales (Java 21)

Este repositorio contiene el código fuente del experimento diseñado para evaluar el rendimiento y consumo de memoria entre los Hilos de Plataforma clásicos y los Hilos Virtuales (Project Loom) en Java 21.

El sistema implementa una arquitectura Manager-Worker para procesar y filtrar concurrente un conjunto de datos meteorológicos masivo (RUOA) simulando operaciones I/O-bound.

## Requisitos

* Java Development Kit (JDK) 21 o superior.
* Conjunto de datos en formato CSV (ej. registros de la RUOA).

## Estructura del Código

El código principal se encuentra en la rama `VirtualVsPlatformThreads`, específicamente en el directorio `Proyecto/src/proyecto/`.

Las clases fundamentales del experimento son:

* `TestManager.java`: Clase principal (Main) que ejecuta las pruebas automatizadas (10k, 50k y 100k tareas).
* `Manager.java`: Maneja la lectura del archivo, la partición lógica en memoria RAM (Round-Robin) y la instanciación del motor concurrente (`ExecutorService`).
* `Worker.java`: Representa la tarea individual. Simula un bloqueo (I/O-bound), filtra los datos y escribe los resultados utilizando un monitor de exclusión mutua global.
* `CoresNumber.java`: Clase utilitaria para detectar los hilos lógicos disponibles en el procesador físico.

## Ejecución

Para correr el experimento:

1. Tener configurada la ruta correcta de el archivo `.csv` en `TestManager.java`.
2. Compilar y ejecutar la clase `TestManager.java`.
3. El programa ejecutará las pruebas en ambos motores de concurrencia y arrojará los tiempos de ejecución y el uso de memoria en la consola.
4. Los archivos filtrados resultantes se generarán automáticamente en un subdirectorio llamado `Resultados/`.