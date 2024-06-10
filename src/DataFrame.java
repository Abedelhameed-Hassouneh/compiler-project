import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class DataFrame {
    private List<String> columnNames;
    private Map<String, List<Object>> data;

    public DataFrame() {
        this.columnNames = new ArrayList<>();
        this.data = new LinkedHashMap<>(); // Using LinkedHashMap to maintain insertion order
    }

    public void addColumn(String columnName) {
        columnNames.add(columnName);
        for (List<Object> row : data.values()) {
            row.add("");
        }
    }

    public void addRow(String index, List<Object> rowData) {
        if (rowData.size() != columnNames.size()) {
            throw new IllegalArgumentException("Row data does not match column count.");
        }
        if (data.containsKey(index)) {
            throw new IllegalArgumentException("Index already exists: " + index);
        }
        data.put(index, new ArrayList<>(rowData));
    }

    public Object getValue(String index, int columnIndex) {
        List<Object> row = data.get(index);
        if (row == null) {
            throw new IllegalArgumentException("Index not found: " + index);
        }
        return row.get(columnIndex);
    }

    public Object getValue(String index, String columnName) {
        int columnIndex = columnNames.indexOf(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        return getValue(index, columnIndex);
    }

    public void setValue(String index, int columnIndex, Object value) {
        List<Object> row = data.get(index);
        if (row == null) {
            throw new IllegalArgumentException("Index not found: " + index);
        }
        row.set(columnIndex, value);
    }

    public void setValue(String index, String columnName, Object value) {
        int columnIndex = columnNames.indexOf(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        setValue(index, columnIndex, value);
    }

    public void printDataFrame() {
        System.out.println("Index\t" + String.join("\t", columnNames));
        for (Map.Entry<String, List<Object>> entry : data.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

    // Method to read CSV file and populate the DataFrame
    public void readCSV(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty.");
        }
        String headerLine = lines.get(0);
        String[] headers = headerLine.split(",");
        for (String header : headers) {
            addColumn(header.trim());
        }
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] values = line.split(",", -1); // -1 to include empty strings
            List<Object> rowData = new ArrayList<>();
            Collections.addAll(rowData, values); // Add each cell as a string

            // Padding the row if it has fewer columns than headers
            while (rowData.size() < columnNames.size()) {
                rowData.add("");
            }

            addRow("row" + i, rowData);
        }
    }
}
