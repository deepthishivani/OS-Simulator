import java.util.ArrayList;
import java.util.List;

public class CPUScheduler {

    // ---------- FCFS ----------
    public static void runFCFS(List<Process> processList) {
        if (processList.isEmpty()) {
            System.out.println("No processes to schedule. Add some first.");
            return;
        }

        List<Process> queue = new ArrayList<>(processList);
        queue.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        int currentTime = 0;
        double totalWaiting = 0, totalTurnaround = 0;

        for (Process p : queue) {
            if (currentTime < p.getArrivalTime()) {
                currentTime = p.getArrivalTime();
            }
            int completion = currentTime + p.getBurstTime();
            int turnaround = completion - p.getArrivalTime();
            int waiting    = turnaround - p.getBurstTime();

            p.setSchedulingResults(completion, turnaround, waiting);
            p.setState("TERMINATED");

            currentTime = completion;
            totalWaiting += waiting;
            totalTurnaround += turnaround;
        }
        printResults("FCFS", queue, totalWaiting, totalTurnaround);
    }

    // ---------- SJF (non-preemptive) ----------
    public static void runSJF(List<Process> processList) {
        if (processList.isEmpty()) {
            System.out.println("No processes to schedule. Add some first.");
            return;
        }

        List<Process> remaining = new ArrayList<>(processList);
        List<Process> finished  = new ArrayList<>();

        int currentTime = 0;
        double totalWaiting = 0, totalTurnaround = 0;

        while (!remaining.isEmpty()) {
            Process shortest = null;
            for (Process p : remaining) {
                if (p.getArrivalTime() <= currentTime) {
                    if (shortest == null || p.getBurstTime() < shortest.getBurstTime()) {
                        shortest = p;
                    }
                }
            }
            if (shortest == null) {   // nothing arrived yet — CPU idle
                currentTime++;
                continue;
            }

            int completion = currentTime + shortest.getBurstTime();
            int turnaround = completion - shortest.getArrivalTime();
            int waiting    = turnaround - shortest.getBurstTime();

            shortest.setSchedulingResults(completion, turnaround, waiting);
            shortest.setState("TERMINATED");

            currentTime = completion;
            totalWaiting += waiting;
            totalTurnaround += turnaround;

            remaining.remove(shortest);
            finished.add(shortest);
        }
        printResults("SJF", finished, totalWaiting, totalTurnaround);
    }

    // ---------- Round Robin (preemptive) ----------
    public static void runRoundRobin(List<Process> processList, int quantum) {
        if (processList.isEmpty()) {
            System.out.println("No processes to schedule. Add some first.");
            return;
        }

        // Reset remaining time to full burst before we start.
        for (Process p : processList) {
            p.setRemainingTime(p.getBurstTime());
        }

        // Processes in arrival order, so they enter the ready queue correctly.
        List<Process> arrivalOrder = new ArrayList<>(processList);
        arrivalOrder.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        List<Process> readyQueue = new ArrayList<>();
        List<Process> finished   = new ArrayList<>();

        int currentTime = 0;
        int nextToArrive = 0;   // index into arrivalOrder of next process not yet queued
        double totalWaiting = 0, totalTurnaround = 0;

        while (finished.size() < processList.size()) {

            // Queue any processes that have arrived by now.
            while (nextToArrive < arrivalOrder.size()
                   && arrivalOrder.get(nextToArrive).getArrivalTime() <= currentTime) {
                readyQueue.add(arrivalOrder.get(nextToArrive));
                nextToArrive++;
            }

            // Nobody ready — CPU idle, tick the clock.
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process current = readyQueue.remove(0);   // front of the queue

            // Run for one quantum, or until it finishes — whichever is smaller.
            int runTime = Math.min(quantum, current.getRemainingTime());
            currentTime += runTime;
            current.setRemainingTime(current.getRemainingTime() - runTime);

            // Processes that arrived WHILE this one ran enter the queue
            // BEFORE the preempted process goes back — this ordering matters.
            while (nextToArrive < arrivalOrder.size()
                   && arrivalOrder.get(nextToArrive).getArrivalTime() <= currentTime) {
                readyQueue.add(arrivalOrder.get(nextToArrive));
                nextToArrive++;
            }

            if (current.getRemainingTime() > 0) {
                readyQueue.add(current);          // not done — back of the queue
            } else {
                int completion = currentTime;     // done — record results
                int turnaround = completion - current.getArrivalTime();
                int waiting    = turnaround - current.getBurstTime();

                current.setSchedulingResults(completion, turnaround, waiting);
                current.setState("TERMINATED");

                totalWaiting += waiting;
                totalTurnaround += turnaround;
                finished.add(current);
            }
        }
        printResults("Round Robin (quantum=" + quantum + ")", finished, totalWaiting, totalTurnaround);
    }

    // ---------- shared results printer ----------
    private static void printResults(String name, List<Process> queue, double totalWaiting, double totalTurnaround) {
        System.out.println("\n--- " + name + " Scheduling Results ---");
        System.out.println("PID\tArrival\tBurst\tCompletion\tTurnaround\tWaiting");
        for (Process p : queue) {
            System.out.println(
                  "P" + p.getPid()
                + "\t" + p.getArrivalTime()
                + "\t" + p.getBurstTime()
                + "\t" + p.getCompletionTime()
                + "\t\t" + p.getTurnaroundTime()
                + "\t\t" + p.getWaitingTime()
            );
        }
        int n = queue.size();
        System.out.printf("%nAverage Waiting Time: %.2f%n", totalWaiting / n);
        System.out.printf("Average Turnaround Time: %.2f%n", totalTurnaround / n);
    }
}