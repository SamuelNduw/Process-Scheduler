import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class ProcessScheduler {
    public static void main(String[] args) throws FileNotFoundException {
        String[] filePaths = {  // This array can be populated dynamically
                "src/New York.txt",
                "src/London.txt",
                "src/Lagos.txt",
                "src/Johannesburg.txt",
                "src/Windhoek.txt",
                "src/Shanghai.txt",
                "src/Delhi.txt"
        };
        PCB[] jobs = new PCB[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            // Assume all files start at 0 and have a default record count of 100 for simplicity
            jobs[i] = new PCB(filePaths[i], "Job " + (i + 1), 0, 100);
        }

        Scanner sc = null;
        int CPUCycle = 0;
        int currentJobIndex = 0;

        try {
            while (true) {
                boolean allJobsDone = true;
                for (PCB job : jobs) {
                    if (!job.isJobDone()) {
                        allJobsDone = false;
                        break;
                    }
                }
                if (allJobsDone) {
                    break;
                }

                PCB currentJob = jobs[currentJobIndex % jobs.length];
                if (CPUCycle >= currentJob.getArrivalTime() && !currentJob.isJobDone()) {
                    File file = new File(currentJob.getFilePath());
                    sc = new Scanner(file);
                    // Skip to the current line
                    for (int i = 1; i < currentJob.getCurrentLine(); i++) {
                        if (sc.hasNextLine()) {
                            sc.nextLine();
                        }
                    }
                    int linesToProcess = 5;  // Quantum size
                    while (sc.hasNextLine() && linesToProcess > 0) {
                        System.out.println(sc.nextLine());
                        currentJob.setCurrentLine(currentJob.getCurrentLine() + 1);
                        CPUCycle++;
                        linesToProcess--;
                    }
                    sc.close();
                }
                currentJobIndex = (currentJobIndex + 1) % jobs.length;
            }
        } catch (FileNotFoundException e) {
            throw e;  // Re-throw to maintain clarity of file not found issues
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }
}