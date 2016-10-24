/**
 * Created by swapn on 10/3/2016.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Step2 {
    Task[] tasks;
    int quotient = 1;


    public boolean readInputFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("task2b.txt"))) {
            String line = br.readLine();
            String[] lineSplit = line.split(",");
            int numberOfTasks = Integer.parseInt(lineSplit[0].trim());
            int columns = Integer.parseInt(lineSplit[1].trim());
            if (columns < 4) {
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
                tasks[i] = new Task(numbers[0], numbers[1], numbers[2], numbers[3]);
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

    String assignPremptionThreshold() throws IOException {
        sort();
        System.out.println("Ci  Ti  Di  Pi   PTi   Bi   Si   Fi   RTi");
        for (int i = 0; i < tasks.length; i++) {
            tasks[i].threshold = tasks[i].priority;
            WCRT(tasks[i]);
            while (tasks[i].rt > tasks[i].deadline) {
                tasks[i].threshold++;
                if (tasks[i].threshold > tasks.length)
                    return "Feasible Assignment Not Found";
                WCRT(tasks[i]);
            }
        }
        System.out.println("\n The Final set of values for task set : \n");
        System.out.println("Ci  Ti  Di  Pi   PTi   Bi   Si   Fi   RTi\n");
        for (int b = 0; b < tasks.length; b++) {
            System.out.println(
                    tasks[b].executionTime+" "+ tasks[b].period+" "+ tasks[b].deadline+" "+ tasks[b].priority+" "+ tasks[b].threshold+" "+
                    tasks[b].blockingTime+" "+ tasks[b].wcst+" "+ tasks[b].wcft+" "+ tasks[b].rt);
        }
        return "Feasible Assignment Found";
    }

    void sort() {
        Task temp;
        for (int r = 0; r < tasks.length - 1; r++) {
            for (int q = 0; q < tasks.length - 1 - r; q++) {
                if (tasks[q].priority > tasks[q + 1].priority) {
                    temp = tasks[q];
                    tasks[q] = tasks[q + 1];
                    tasks[q + 1] = temp;
                }
            }
        }
    }

    void WCRT(Task t) throws IOException {
        // calculate Blocking time for each task
        blockingTime(t);

        // calculating worst case start time for each task
        int s0 = WCST(t, 0);
        int s1 = WCST(t, s0);

        while (s1 != s0) {
            s0 = s1;
            s1 = WCST(t, s1);
        }
        t.wcst = s1;

        int f0 = WCFT(t, 0);
        int f1 = WCFT(t, f0);

        while (f1 != f0) {
            f0 = f1;
            f1 = WCFT(t, f1);
        }
        t.wcft = f1;
        t.rt = t.wcft;
        System.out.println(
                t.executionTime+" "+t.period+" "+ t.deadline+" "+ t.priority+" "+ t.threshold+" "+
                t.blockingTime+" "+ t.wcst+" "+ t.wcft+" "+ t.rt);
    }

    // method to calculate blocking time
    void blockingTime(Task task) {

        for (int s = 0; s < tasks.length; s++) {
            if (tasks[s].threshold >= task.priority && task.priority > tasks[s].priority) {
                if (task.blockingTime < tasks[s].executionTime)
                    task.blockingTime = tasks[s].executionTime;
            }
        }
    }

    // method to calculate worst case start time
    int WCST(Task task, int previousS) {
        int tempResult = 0;
        int tempS = 0;
        for (int p = 0; p < tasks.length; p++) {
            if (tasks[p].priority > task.priority) {
                tempResult += (1 + Math.floorDiv(previousS, tasks[p].period)) * tasks[p].executionTime;
            }
        }
        tempS = task.blockingTime + (quotient - 1) * (task.executionTime) + tempResult;

        return tempS;
    }

    // method to calculate finish time
    int WCFT(Task task, int previousS) {
        int tempResult = 0;
        int tempF = 0;
        for (int p = 0; p < tasks.length; p++) {
            if (tasks[p].priority > task.threshold) {
                tempResult += (((Math.floorDiv(previousS, tasks[p].period)) + (previousS
                        % tasks[p].period == 0 ? 0 : 1)) - (1 + Math
                        .floorDiv(task.wcst, tasks[p].period)))
                        * tasks[p].executionTime;
            }
        }
        tempF = task.wcst + task.executionTime + tempResult;
        return tempF;
    }

    public static void main(String[] args) throws IOException {
        Step2 step = new Step2();
        boolean status = step.readInputFile();
        if (status) {
            String result = step.assignPremptionThreshold();
            System.out.println("\n" + result);
        } else {
            System.out.println("Something went wrong");
        }
    }
}

