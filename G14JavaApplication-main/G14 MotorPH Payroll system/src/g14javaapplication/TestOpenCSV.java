package g14javaapplication;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.FileReader;

public class TestOpenCSV {
    public static void main(String[] args) {
        try {
        	
        CSVReader reader = new CSVReader(new FileReader("employees.csv"));
            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println("Read row: " + String.join(", ", line));
            }
        } catch (Exception e) {
            System.out.println("OpenCSV is working - error just in file handling: " + e.getMessage());
        }
    }
}