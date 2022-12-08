/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
