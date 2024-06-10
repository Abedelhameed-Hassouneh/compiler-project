import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TableReader {
    public static void main(String[] args) {
        String csvFile = "CompilerTable.csv"; // Update the path to your CSV file
        String line = "";
        String csvSplitBy = ",";

        List<String[]> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // Use comma as separator
                String[] row = line.split(csvSplitBy);
                dataList.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert List to 2D Array
        String[][] dataArray = new String[dataList.size()][];
        dataArray = dataList.toArray(dataArray);

        // Print the 2D Array
        for (String[] row : dataArray) {
            System.out.println(row[0]+"\n");
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}