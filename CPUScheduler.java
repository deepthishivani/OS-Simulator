import java.util.ArrayList;
import java.util.List;

public class CPUScheduler {

    // A small helper class to record one segment of the timeline:
    // "process pid ran from start to end".
    static class GanttSegment {
        int pid;
        int start;
        int end;
        GanttSegment(int pid, int start, int end) {
            this.pid = pid;
            this.start = start;
            this.end = end;
        }
    }

    // ---------- FCFS ----------
    public static void runFCFS(List<Process> processList) {
        if (processList.isEmpty()) {
            System.out.println("No processes to schedule. Add some first.");
            return;
        }

        List<Process> queue = new ArrayList<>(processList);
        queue.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        List<GanttSegment> timeline = new ArrayList<>();
        int currentTime = 0;
        double totalWaiting = 0, totalTurnaround = 0;

        for (Process p : queue) {
            if (currentTime < p.getArrivalTime()) {
                currentTime = p.getArrivalTime();
            }
            int start = currentTime;
            int completion = currentTime + p.getBurstTime();

            timeline.add(new GanttSegment(p.getPid(), start, completion));

            int turnaround = completion - p.getArrivalTime();
            int waiting    = turnaround - p.getBurstTime();

            p.setSchedulingResults(completion, turnaround, waiting);
            p.setState("TERMINATED");

            currentTime = completion;
            totalWaiting += waiting;
            totalTurnaround += turnaround;
        }
        printGantt("FCFS", timeline);
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
        List<GanttSegment> timeline = new ArrayList<>();

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
            if (shortest == null) {
                currentTime++;
                continue;
            }

            int start = currentTime;
            int completion = currentTime + shortest.getBurstTime();

            timeline.add(new GanttSegment(shortest.getPid(), start, completion));

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
        printGantt("SJF", timeline);
        printResults("SJF", finished, totalWaiting, totalTurnaround);
    }

    // ---------- Round Robin (preemptive) ----------
    public static void runRoundRobin(List<Process> processList, int quantum) {
        if (processList.isEmpty()) {
            System.out.println("No processes to schedule. Add some first.");
            return;
        }

        for (Process p : processList) {
            p.setRemainingTime(p.getBurstTime());
        }

        List<Process> arrivalOrder = new ArrayList<>(processList);
        arrivalOrder.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        List<Process> readyQueue = new ArrayList<>();
        List<Process> finished   = new ArrayList<>();
        List<GanttSegment> timeline = new ArrayList<>();

        int currentTime = 0;
        int nextToArrive = 0;
        double totalWaiting = 0, totalTurnaround = 0;

        while (finished.size() < processList.size()) {

            while (nextToArrive < arrivalOrder.size()
                   && arrivalOrder.get(nextToArrive).getArrivalTime() <= currentTime) {
                readyQueue.add(arrivalOrder.get(nextToArrive));
                nextToArrive++;
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process current = readyQueue.remove(0);

            int runTime = Math.min(quantum, current.getRemainingTime());
            int start = currentTime;
            currentTime += runTime;
            current.setRemainingTime(current.getRemainingTime() - runTime);

            // Record this slice of the timeline.
            timeline.add(new GanttSegment(current.getPid(), start, currentTime));

            while (nextToArrive < arrivalOrder.size()
                   && arrivalOrder.get(nextToArrive).getArrivalTime() <= currentTime) {
                readyQueue.add(arrivalOrder.get(nextToArrive));
                nextToArrive++;
            }

            if (current.getRemainingTime() > 0) {
                readyQueue.add(current);
            } else {
                int completion = currentTime;
                int turnaround = completion - current.getArrivalTime();
                int waiting    = turnaround - current.getBurstTime();

                current.setSchedulingResults(completion, turnaround, waiting);
                current.setState("TERMINATED");

                totalWaiting += waiting;
                totalTurnaround += turnaround;
                finished.add(current);
            }
        }
        printGantt("Round Robin (quantum=" + quantum + ")", timeline);
        printResults("Round Robin (quantum=" + quantum + ")", finished, totalWaiting, totalTurnaround);
    }

    // ---------- Gantt chart printer ----------
    private static void printGantt(String name, List<GanttSegment> timeline) {
        System.out.println("\n--- " + name + " Gantt Chart ---");

        // Top row: the process boxes.
        StringBuilder bars = new StringBuilder("|");
        for (GanttSegment seg : timeline) {
            bars.append("  P").append(seg.pid).append("  |");
        }
        System.out.println(bars.toString());

        // Bottom row: the time markers under each boundary.
        StringBuilder times = new StringBuilder();
        if (!timeline.isEmpty()) {
            times.append(timeline.get(0).start);
        }
        for (GanttSegment seg : timeline) {
            // pad so the number sits roughly under the bar boundary
            int boxWidth = ("  P" + seg.pid + "  ").length();
            String endStr = String.valueOf(seg.end);
            for (int s = 0; s < boxWidth + 1 - endStr.length(); s++) {
                times.append(" ");
            }
            times.append(endStr);
        }
        System.out.println(times.toString());
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