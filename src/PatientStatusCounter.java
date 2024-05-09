import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PatientStatusCounter {

    public static Map<String, Integer> countPatientStatus(String filePath) {
        Map<String, Integer> statusCount = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length > 5) {  // Ensure the line has enough data points
                    String status = details[5].trim();  // Assuming status is the sixth element
                    statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return statusCount;
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Samue\\Desktop\\Java Projects\\OPS Round Robin\\src\\Johannesburg.txt";  // Replace with the path to your COVID file
        Map<String, Integer> statusCounts = countPatientStatus(filePath);
        statusCounts.forEach((status, count) -> System.out.println("Status: " + status + ", Count: " + count));
    }
}

