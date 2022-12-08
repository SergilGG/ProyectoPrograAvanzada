
package proyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author aguirre
 */
public class UserInteraction {
    
    // Show a table with colums and its id's 
        public static void showInterface(String path, String fileName){
            String line;
            String[] fields;
            try{
                
                // Reading the source file to get the header
                File f1 = new File(path, fileName);
                
                // get only the first line, the header
                try (BufferedReader Header = new BufferedReader(new FileReader(f1))) {
                    
                    // get only the first line, the header
                    line = Header.readLine();
                    
                    //Split the line by comma
                    fields = line.split(",");
                    
                    System.out.printf("--------------------------------%n");
                    System.out.printf("       Colums to select         %n");
                    System.out.printf("--------------------------------%n");
                    System.out.printf("| %-18s | %-7s |%n", "Name", "id");
                    System.out.printf("--------------------------------%n");
                    
                    for (int i = 1; i < fields.length; i++){
                        System.out.printf("| %-18s | %-7s |%n", fields[i], i);
                    }
                    System.out.printf("--------------------------------%n");
                    Header.close();
                } 
            }catch (IOException e) {
                  System.err.println(e);
            }            
            
        }

        private static int inputIdColumn() {
            Scanner input = new Scanner(System.in);
            while (true) {
                System.out.println("\nInsert id Column:");
                try {
                    return input.nextInt();
                }
                catch (java.util.InputMismatchException e) {
                    input.nextLine();
                    System.out.printf("Data type incorrect, try again\n");
                }
            }
        }    
        
        private static String inputCondition() {
            Scanner input = new Scanner(System.in);
            while (true) {
                System.out.println( "\nEnter the filter condition: ");
                try {
                    return input.nextLine();
                }
                catch (java.util.InputMismatchException e) {
                    input.nextLine();
                    System.out.printf("Data type incorrect, try again\n");
                }
            }
        }    
        
        public static int[] askForColumns( ){
            
            ArrayList<Integer> columns = new ArrayList<>(); 
            int columnID ;
            
            System.out.printf("\nInsert the id's of the columns to be filtered ");
            System.out.printf("\nType 0 to get all ");
            System.out.printf("\nType -1 to end\n");
            
            // get the column id 
            columnID = inputIdColumn();

            columns.add(columnID);
            
            while(columnID != -1) {
                if(columnID == 0){
                    //Include all the id's of the columns 
                    columns.removeAll(columns);
                    for(int i = 1; i < 26; i++){
                        columns.add(i);
                        //columns.remove(Integer.valueOf(0));
                    }
                    columnID = -1;
                }
                else{
                    // get id by id 
                    columnID = inputIdColumn();
                    columns.add(columnID);
                }

            }            

            // Removes the element at the end, flag -1
            columns.remove(columns.size()-1); 
            
            // Cast to array
            int[] arr = columns.stream().mapToInt(i -> i).toArray(); 

            System.out.println("The dynamic array is: " + columns);            
            return arr;
            
        }
        
        public static int askForColumnIdCondition( ){
            // get the id column condition to filter 
            System.out.printf("\nFilter condition, example: to get O3_Flag = OK");  
            System.out.printf("\nType id Column 2 and column Condition OK\n");   
            int columnID_condition ;
            columnID_condition = inputIdColumn();
            return columnID_condition;
        }       
    
        public static String askForColumnCondition( ){
            // get the condition to filter
            String condition;
            condition = inputCondition();
           
           // System.out.printf("Condition " + condition);
            return condition;
        } 
}
