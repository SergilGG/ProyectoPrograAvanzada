package proyecto;


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SplitFile {
    public static void main(String[] args) throws Exception
        {
            int CPUs = Runtime.getRuntime().availableProcessors();
            RandomAccessFile raf = new RandomAccessFile("/home/aguirre/Posgrado/ProgramacionAvanzada/Data/JQRO/DatosConcatenados/2015_JQRO_minuto_contaminates_meteorologia.csv", "r");
            long numSplits = 3*CPUs;
            long sourceSize = raf.length();
            long bytesPerSplit = sourceSize/numSplits ;
            long remainingBytes = sourceSize % numSplits;

            int maxReadBufferSize = 8 * 1024; //8KB
            for(int destIx=1; destIx <= numSplits; destIx++) {
                BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("/home/aguirre/Posgrado/ProgramacionAvanzada/Data/JQRO/SplitData/split"+destIx + ".csv"));
                if(bytesPerSplit > maxReadBufferSize) {
                    long numReads = bytesPerSplit/maxReadBufferSize;
                    long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                    for(int i=0; i<numReads; i++) {
                        readWrite(raf, bw, maxReadBufferSize);
                    }
                    if(numRemainingRead > 0) {
                        readWrite(raf, bw, numRemainingRead);
                    }
                }else {
                    readWrite(raf, bw, bytesPerSplit);
                }
                bw.close();
            }
            if(remainingBytes > 0) {
                BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("split."+(numSplits+1)));
                readWrite(raf, bw, remainingBytes);
                bw.close();
            }
                raf.close();
        }

        static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
            byte[] buf = new byte[(int) numBytes];
            int val = raf.read(buf);
            if(val != -1) {
                bw.write(buf);
            }
            char nextChar = raf.readChar();
            while (nextChar != '\n') {
              System.out.println("Busca salto de linea..: " + nextChar);
              bw.write(nextChar);
              nextChar = raf.readChar();
            }
           
        }
}