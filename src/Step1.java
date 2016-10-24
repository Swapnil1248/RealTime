/**
 * Created by swapn on 10/3/2016.
 */

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Step1 {
    private Task[] tasks;
    private int quotient = 1, mul = 0;

    public boolean readInputFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("pre.txt"))) {
            String line = br.readLine();
            String[] lineSplit = line.split(",");
            int numberOfTasks = Integer.parseInt(lineSplit[0].trim());
            int columns = Integer.parseInt(lineSplit[1].trim());
            if (columns < 5) {
                System.out.println("Check number of columns");
                return false;
            }
            tasks = new Task[numberOfTasks];

            for (int i = 0; i < numberOfTasks; i++) {
                String[] data = br.readLine().split(",");
                int[] numbers = new int[data.length];
                for (int j = 0; j < data.length; j++) {
                    numbers[j] = Integer.parseInt(data[j].trim());
                }
                tasks[i] = new Task(numbers[0], numbers[1], numbers[2],
                        numbers[3], numbers[4]);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    void WCRT() throws IOException {
        boolean done = false;
        while (!done) {
            // calculate Blocking time for each task
            blockingTime();

            // calculating worst case start time for each task
            for (int t = 0; t < tasks.length; t++) {

                int s0 = WCST(t, 0);
                int s1 = WCST(t, s0);

                while (s1 != s0) {
                    s0 = s1;
                    s1 = WCST(t, s1);
                }
                tasks[t].wcst = s1;

            }
            System.out.println("Ci  Ti  Di  Pi   PTi   Bi   Si   Fi   RTi");
            // calculating worst case finish time for each task
            for (int t = 0; t < tasks.length; t++) {
                int f0 = WCFT(t, 0);
                int f1 = WCFT(t, f0);

                while (f1 != f0) {
                    f0 = f1;
                    f1 = WCFT(t, f1);
                }
                tasks[t].wcft = f1;

                tasks[t].rt = tasks[t].wcft;

                System.out.println(tasks[t].executionTime + " " + tasks[t].period+ " " +
                        tasks[t].deadline+ " " + tasks[t].priority+ " " +
                        tasks[t].threshold+ " " + tasks[t].blockingTime+ " " +
                        tasks[t].wcst+ " " + tasks[t].wcft+ " " +tasks[t].rt);
            }
            // checking given task set is schedulable or not
            for (int v = 0; v < tasks.length; v++) {
                if (tasks[v].wcft <= (quotient * tasks[v].deadline)) {
                    done = true;
                    mul = quotient;
                } else {
                    done = false;
                    quotient = quotient + 1;
                }
            }
            if (done && mul == 1)
                System.out.println("Task Set is schedulable");
            else
                System.out.println("Task Set is not schedulable");

            done = true;
        }
    }

    void blockingTime() {

        for (int i = 0; i < tasks.length; i++) {
            for (int j = 0; j < tasks.length; j++) {
                if (tasks[j].threshold >= tasks[i].priority && tasks[i].priority > tasks[j].priority) {
                    if (tasks[i].blockingTime < tasks[j].executionTime)
                        tasks[i].blockingTime = tasks[j].executionTime;
                }
            }
        }
    }

    // method to calculate worst case start time
    int WCST(int taskNumber, int previousS) {
        int tempResult = 0;
        int tempS = 0;
        for (int p = 0; p < tasks.length; p++) {
            if (tasks[p].priority > tasks[taskNumber].priority) {
                tempResult += (1 + Math.floorDiv(previousS, tasks[p].period)) * tasks[p].executionTime;
            }
        }
        tempS = tasks[taskNumber].blockingTime + (quotient - 1) * (tasks[taskNumber].executionTime) + tempResult;

        return tempS;
    }

    // method to calculate finish time
    int WCFT(int taskNumber, int previousS) {
        int tempResult = 0;
        int tempF = 0;
        for (int p = 0; p < tasks.length; p++) {
            if (tasks[p].priority > tasks[taskNumber].threshold) {
                tempResult = tempResult
                        + (((Math.floorDiv(previousS, tasks[p].period)) + (previousS
                        % tasks[p].period == 0 ? 0 : 1)) - (1 + Math
                        .floorDiv(tasks[taskNumber].wcst,
                                tasks[p].period)))
                        * tasks[p].executionTime;
            }
        }
        tempF = tasks[taskNumber].wcst + tasks[taskNumber].executionTime + tempResult;

        return tempF;
    }

    public static void main(String args[]) throws IOException {
        Step1 step = new Step1();
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        boolean status = step.readInputFile();
        if (status) {
            step.WCRT();
        } else {
            System.out.println("Something went wrong");
        }

    }

}
