import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class ProcessScheduler {
    public static void main(String[] args) throws FileNotFoundException {
        // Array of file details; Details contain file path, name, arrival time, record count
        Object[][] fileDetails = {
                {"src/New York.txt", "New York", 0, 150},
                {"src/London.txt", "London", 20, 100},
                {"src/Lagos.txt", "Lagos", 25, 120},
                {"src/Johannesburg.txt", "Johannesburg", 30, 88},
                {"src/Windhoek.txt", "Windhoek", 35, 15},
                {"src/Shanghai.txt", "Shanghai", 50, 164},
                {"src/Delhi.txt", "Delhi", 55, 158}
        };
        // Array of jobs as PCB objects
        PCB[] jobs = new PCB[fileDetails.length];
        // Loop to iterate over the file details and add PCB objects to the array using those details above.
        for (int i = 0; i < fileDetails.length; i++) {
            jobs[i] = new PCB(
                    (String)fileDetails[i][0],
                    (String)fileDetails[i][1],
                    (Integer)fileDetails[i][2],
                    (Integer)fileDetails[i][3]
            );
        }
        // Instantiate the Scanner object, initialised CPU cycle and current job index
        Scanner sc = null;
        int CPUCycle = 0;
        int currentJobIndex = 0;

        // Try, Catch block; Where all the processing and Round Robin algorithm is done
        try {
            while (true) {
                boolean allJobsDone = true;
                // Loop to check if all the jobs are not done
                for (PCB job : jobs) {
                    if (!job.isJobDone()) {
                        allJobsDone = false;
                        break;
                    }
                }
                // Condition checks if all the jobs are done, if true, it will break out of the while loop
                if (allJobsDone) {
                    break;
                }

                // Initializing a PCB temporary PCB object for the current job
                PCB currentJob = jobs[currentJobIndex % jobs.length];
                // Checking if the job should start, by comparing the CPU Cycle and Arrival time
                if (CPUCycle >= currentJob.getArrivalTime() && !currentJob.isJobDone()) {
                    if(currentJob.getCurrentLine() == 1){
                        currentJob.setStartTime(CPUCycle); // Set the start time at the beginning of processing
                    }
                    // Get the file path of the job and pass it to the Scanner object
                    File file = new File(currentJob.getFilePath());
                    sc = new Scanner(file);
                    // Skip to the current line
                    for (int i = 1; i < currentJob.getCurrentLine(); i++) {
                        if (sc.hasNextLine()) {
                            sc.nextLine();
                        }
                    }
                    int linesToProcess = 5;  // Quantum size
                    // True when file has a next line and the quantum size is less than the processed rows
                    while (sc.hasNextLine() && linesToProcess > 0) {
                        String line = sc.nextLine();
                        System.out.println(line);
                        //Retrieving age from record and adding it to the arraylist
                        String[] details = line.split(",");
                        int age = Integer.parseInt(details[2].trim());
                        currentJob.addAge(age);
                        //Incrementing patient status count
                        String status = details[5].trim();
                        currentJob.addPatientStatus(status);

                        // Increments current line, CPU Cycle and decrements lines to be processed
                        currentJob.setCurrentLine(currentJob.getCurrentLine() + 1);
                        CPUCycle++;
                        linesToProcess--;
                    }
                    sc.close();

                    if(currentJob.isJobDone()){
                        currentJob.setFinishTime(CPUCycle); // Set the finish time when job completes

                        /* List containing the ages from the individual records of the job
                            The values are passed into an array for easier computation with the methods
                         */
                        ArrayList<Integer> retrievedAges = currentJob.getAges();
                        int[] ages = new int[retrievedAges.size()];
                        for(int i = 0; i < retrievedAges.size(); i++){
                            ages[i] = retrievedAges.get(i);
                        }
                        // Average, Minimum, Maximum Age and Turnaround time of the job are computed
                        int aveAge = calculateAvgAge(ages);
                        int minAge = calculateMinAge(ages);
                        int maxAge = calculateMaxAge(ages);
                        int turnaroundTime = calculateTurnaroundTime(currentJob.getStartTime(), currentJob.getFinishTime());

                        // Computed values stored in PCB object
                        currentJob.setAverageAge(aveAge);
                        currentJob.setMinimumAge(minAge);
                        currentJob.setMaximumAge(maxAge);
                        currentJob.setTurnaroundTime(turnaroundTime);
                        System.out.println(currentJob);
                    }
                }
                // Moves current job index to the next job
                currentJobIndex = (currentJobIndex + 1) % jobs.length;
            }
        } catch (FileNotFoundException e) {
            throw e;  // Re-throw to maintain clarity of file not found issues
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

        // Initializing variable for the average turnaround time and an array to collect the turnaround times
        double averageTurnaroundTime = 0;
        int[] turnaroundTimes = new int[jobs.length];

        // Increment turnaround Time then divide it by the total number of jobs
        for(int i = 0; i < jobs.length; i++){
            turnaroundTimes[i] = jobs[i].getTurnaroundTime();
            System.out.println(jobs[i].toString());
        }
        // Calculating the average turnaround time and printing it
        averageTurnaroundTime = calculateAverageTurnaroundTime(turnaroundTimes);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }

    // Method to calculate the average age in each file/job
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
    // Method to calculate the minimum age in each file/job
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

    // Method to calculate the maximum age in each file/job
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

    // Method to calculate the turnaround time of the respective job
    public static int calculateTurnaroundTime(int arrivalTime, int completionTime){
        //Calculate turnaround time
        int turnaroundTime = completionTime - arrivalTime;
        return turnaroundTime;
    }

    // Method to calculate the average turnaround time
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