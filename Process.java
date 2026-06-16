// This class is the "blueprint" for a single process.
// Every process in our simulator will be an object created from this class.
public class Process {

    // --- Fields: the data each process holds ---
    // 'private' means these can only be changed through this class,
    // which protects the data from accidental misuse (this is called encapsulation).
    private int pid;          // Process ID — a unique number for each process
    private int arrivalTime;  // The time the process arrives in the system
    private int burstTime;    // How much CPU time the process needs to finish
    private int priority;     // Priority value (lower number = higher priority, by convention)
    private String state;     // Current state: NEW, READY, RUNNING, WAITING, TERMINATED

    // --- Constructor: the "recipe" for building a Process object ---
    // When we write 'new Process(...)', this runs and fills in the fields.
    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;                 // 'this.pid' = the field; 'pid' = the value passed in
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.state = "NEW";             // Every process starts life in the NEW state
    }

    // --- Getters: methods that let other classes safely READ our private fields ---
    public int getPid() { return pid; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public String getState() { return state; }

    // A setter — we DO allow the state to change as the process runs.
    public void setState(String state) { this.state = state; }

    // --- toString: defines how a Process looks when we print it ---
    // Java calls this automatically when you print the object.
    @Override
    public String toString() {
        return "P" + pid
                + " | Arrival: " + arrivalTime
                + " | Burst: " + burstTime
                + " | Priority: " + priority
                + " | State: " + state;
    }
}