public class Process {

    private int pid;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private String state;

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

    public int getPid() { return pid; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public void setSchedulingResults(int completionTime, int turnaroundTime, int waitingTime) {
        this.completionTime = completionTime;
        this.turnaroundTime = turnaroundTime;
        this.waitingTime = waitingTime;
    }

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