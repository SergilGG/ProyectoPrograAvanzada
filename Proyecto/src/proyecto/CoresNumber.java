package proyecto;

/**
 *
 * @author sergi
 */
public class CoresNumber {
        public static int getCores(){
        int CPUs = Runtime.getRuntime().availableProcessors();
        System.out.println(CPUs);
            return CPUs;
    }
}
