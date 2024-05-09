public class PCB {
    private final String filePath;
    private final String jobName;
    private final int arrivalTime;
    private final int recordCount;
    private int currentLine = 1;
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

    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }
    public boolean isJobDone(){
        return currentLine > recordCount;
    }
}
