import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PCB {
    private final String filePath;
    private final String jobName;
    private final int arrivalTime;
    private final int recordCount;
    private ArrayList<Integer> ages = new ArrayList<>();
    private Map<String, Integer> statusCount = new HashMap<>();
    private double averageAge;
    private int minimumAge;
    private int maximumAge;
    private int turnaroundTime;
    private int currentLine = 1;
    private int startTime = -1;
    private int finishTime = -1;
    PCB(String filePath, String jobName, int arrivalTime, int recordCount){
        this.filePath = filePath;
        this.jobName = jobName;
        this.arrivalTime = arrivalTime;
        this.recordCount = recordCount;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getJobName() {
        return jobName;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public double getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(double averageAge) {
        this.averageAge = averageAge;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public int getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(int maximumAge) {
        this.maximumAge = maximumAge;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }
    public boolean isJobDone(){
        return currentLine > recordCount;
    }
    public ArrayList<Integer> getAges() {
        return ages;
    }
    public void addAge(int age) {
        ages.add(age);
    }
    public void addPatientStatus(String status){
        statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
    }

    public int getStartTime() {
        return startTime;
    }
    public void setStartTime(int time) {
        if (startTime == -1) { // Set start time only once
            startTime = time;
        }
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int time) {
        finishTime = time;
    }

    @Override
    public String toString() {
        return "PCB{" +
                "jobName='" + jobName + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", recordCount=" + recordCount +
                ", statusCount=" + statusCount +
                ", averageAge=" + averageAge +
                ", minimumAge=" + minimumAge +
                ", maximumAge=" + maximumAge +
                ", turnaroundTime=" + turnaroundTime +
                '}';
    }
}
