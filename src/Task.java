/**
 * Created by swapn on 10/3/2016.
 */
public class Task {
    int executionTime;
    int period;
    int deadline;
    int priority;
    int threshold;
    int blockingTime = 0;
    int wcst = 0;
    int wcft = 0;
    int rt = 0;
    int heuristicValue = 0;

    public Task(int executionTimeValue, int periodValue, int deadlineValue,
                int priorityValue, int thresholdValue) {
        executionTime = executionTimeValue;
        period = periodValue;
        deadline = deadlineValue;
        priority = priorityValue;
        threshold = thresholdValue;

    }

    public Task(int executionTimeValue, int periodValue, int deadlineValue,
                int priorityValue) {
        executionTime = executionTimeValue;
        period = periodValue;
        deadline = deadlineValue;
        priority = priorityValue;
    }

    public Task(int executionTimeValue, int periodValue, int deadlineValue) {
        executionTime = executionTimeValue;
        period = periodValue;
        deadline = deadlineValue;
    }

}
