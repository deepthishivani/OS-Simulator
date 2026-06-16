public class Process {

    // Original fields
    private int pid;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private String state;

    // NEW: fields to store scheduling results
    private int completionTime;
    private int turnaroundTime;
    private int waitingTime;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.state = "NEW";
    }

    // Getters for the original fields
    public int getPid() { return pid; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    // NEW: store all three scheduling results in one call
    public void setSchedulingResults(int completionTime, int turnaroundTime, int waitingTime) {
        this.completionTime = completionTime;
        this.turnaroundTime = turnaroundTime;
        this.waitingTime = waitingTime;
    }

    // NEW: getters for the results
    public int getCompletionTime() { return completionTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public int getWaitingTime()    { return waitingTime; }

    @Override
    public String toString() {
        return "P" + pid
                + " | Arrival: " + arrivalTime
                + " | Burst: " + burstTime
                + " | Priority: " + priority
                + " | State: " + state;
    }
}