import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class ProcessScheduler {
    public static void main(String[] args) throws FileNotFoundException {
        // Array of file details; you might load these from a configuration or database
        Object[][] fileDetails = {
                {"src/New York.txt", "New York", 0, 150},
                {"src/London.txt", "London", 20, 100},
                {"src/Lagos.txt", "Lagos", 25, 120},
                {"src/Johannesburg.txt", "Johannesburg", 30, 88},
                {"src/Windhoek.txt", "Windhoek", 35, 15},
                {"src/Shanghai.txt", "Shanghai", 50, 164},
                {"src/Delhi.txt", "Delhi", 55, 158}
        };

        PCB[] jobs = new PCB[fileDetails.length];
        for (int i = 0; i < fileDetails.length; i++) {
            jobs[i] = new PCB(
                    (String)fileDetails[i][0],
                    (String)fileDetails[i][1],
                    (Integer)fileDetails[i][2],
                    (Integer)fileDetails[i][3]
            );
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
                    if(currentJob.getCurrentLine() == 1){
                        currentJob.setStartTime(CPUCycle); // Set the start time at the beginning of processing
                    }
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
                        String line = sc.nextLine();
                        System.out.println(line);
                        //retrieving age from record and add to the arraylist
                        String[] details = line.split(",");
                        int age = Integer.parseInt(details[2].trim());
                        currentJob.addAge(age);
                        //incrementing patient status count
                        String status = details[5].trim();
                        currentJob.addPatientStatus(status);


                        currentJob.setCurrentLine(currentJob.getCurrentLine() + 1);
                        CPUCycle++;
                        linesToProcess--;
                    }
                    sc.close();

                    if(currentJob.isJobDone()){
                        currentJob.setFinishTime(CPUCycle); // Set the finish time when job completes

                        ArrayList<Integer> retrievedAges = currentJob.getAges();
                        int[] ages = new int[retrievedAges.size()];
                        for(int i = 0; i < retrievedAges.size(); i++){
                            ages[i] = retrievedAges.get(i);
                        }
                        int aveAge = calculateAvgAge(ages);
                        int minAge = calculateMinAge(ages);
                        int maxAge = calculateMaxAge(ages);
                        int turnaroundTime = calculateTurnaroundTime(currentJob.getStartTime(), currentJob.getFinishTime());

                        currentJob.setAverageAge(aveAge);
                        currentJob.setMinimumAge(minAge);
                        currentJob.setMaximumAge(maxAge);
                        currentJob.setTurnaroundTime(turnaroundTime);
                        System.out.println(currentJob);
                    }
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

        double averageTurnaroundTime = 0;
        int[] turnaroundTimes = new int[jobs.length];

        // Increment turnaround Time then divide it by the total number of jobs
        for(int i = 0; i < jobs.length; i++){
            turnaroundTimes[i] = jobs[i].getTurnaroundTime();
            System.out.println(jobs[i].toString());
        }
        averageTurnaroundTime = calculateAverageTurnaroundTime(turnaroundTimes);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }

    public static int calculateAvgAge(int[] ages){
        int averageAge = 0;
        int sum = 0;
        int count = ages.length;
        for(int age : ages){
            sum += age;
        }
        averageAge = sum / count;
        return averageAge;
    }
    public static int calculateMinAge(int[] ages){
        //Initialize minAge to the first element of the array
        int minAge = ages[0];

        // Iterate through the array starting from the second element
        for(int i = 1; i < ages.length; i++){
            //Update minAge if the current age is smaller
            if(ages[i] < minAge){
                minAge = ages[i];
            }
        }

        return minAge;
    }

    public static int calculateMaxAge(int[] ages){
        //Initialize maxAge to the first element of the array
        int maxAge = ages[0];

        // Iterate through the array starting from the second element
        for(int i = 1; i < ages.length; i++){
            //Update maxAge if the current age is greater
            if (ages[i] > maxAge){
                maxAge = ages[i];
            }
        }

        return maxAge;
    }

    public static int calculateTurnaroundTime(int arrivalTime, int completionTime){
        //Calculate turnaround time
        int turnaroundTime = completionTime - arrivalTime;
        return turnaroundTime;
    }

    public static double calculateAverageTurnaroundTime(int[] turnaroundTimes){
        if(turnaroundTimes == null || turnaroundTimes.length == 0){
            return 0; // Return 0 if the array is empty or null
        }

        int totalTurnaroundTime = 0;
        for(int time : turnaroundTimes){
            totalTurnaroundTime += time;
        }

        double averageTurnaroundTime = (double) totalTurnaroundTime / turnaroundTimes.length;
        return averageTurnaroundTime;
    }
}