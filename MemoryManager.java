import java.util.ArrayList;
import java.util.List;

public class MemoryManager {

    // Run all three algorithms on the SAME input, then compare them.
    public static void runPageReplacement(int[] ref, int frameCount) {
        if (ref.length == 0) {
            System.out.println("Empty reference string. Nothing to do.");
            return;
        }
        System.out.println("\nReference string: " + arrayToString(ref));
        System.out.println("Number of frames: " + frameCount);

        int fifoFaults = runFIFO(ref, frameCount);
        int lruFaults  = runLRU(ref, frameCount);
        int optFaults  = runOptimal(ref, frameCount);

        int total = ref.length;
        System.out.println("\n===== COMPARISON =====");
        System.out.printf("%-10s %-12s %-8s %-10s%n", "Algorithm", "PageFaults", "Hits", "HitRatio");
        printCompareRow("FIFO", fifoFaults, total);
        printCompareRow("LRU", lruFaults, total);
        printCompareRow("Optimal", optFaults, total);
        System.out.println("\nFewer faults = better. Optimal is the theoretical best (it can see the future).");
    }

    private static void printCompareRow(String name, int faults, int total) {
        int hits = total - faults;
        double ratio = (double) hits / total;
        System.out.printf("%-10s %-12d %-8d %-10.2f%n", name, faults, hits, ratio);
    }

    // ---------- FIFO ----------
    private static int runFIFO(int[] ref, int frameCount) {
        List<Integer> frames = new ArrayList<>();   // index 0 = oldest page (front of the queue)
        int faults = 0;

        System.out.println("\n--- FIFO ---");
        System.out.println("Page\tFrames\t\tStatus");

        for (int page : ref) {
            String status;
            if (frames.contains(page)) {
                status = "Hit";                      // page already in a frame
            } else {
                faults++;
                status = "Fault";
                if (frames.size() < frameCount) {
                    frames.add(page);                // empty slot available
                } else {
                    frames.remove(0);                // evict the oldest (front)
                    frames.add(page);                // newcomer goes to the back
                }
            }
            System.out.println(page + "\t" + frames + "\t\t" + status);
        }
        System.out.println("FIFO total page faults: " + faults);
        return faults;
    }

    // ---------- LRU ----------
    private static int runLRU(int[] ref, int frameCount) {
        List<Integer> frames = new ArrayList<>();   // index 0 = least recently used
        int faults = 0;

        System.out.println("\n--- LRU ---");
        System.out.println("Page\tFrames\t\tStatus");

        for (int page : ref) {
            String status;
            if (frames.contains(page)) {
                status = "Hit";
                // Used again -> move it to the most-recently-used end (the back).
                frames.remove(Integer.valueOf(page));
                frames.add(page);
            } else {
                faults++;
                status = "Fault";
                if (frames.size() == frameCount) {
                    frames.remove(0);                // evict least recently used (front)
                }
                frames.add(page);                    // newcomer is most recently used
            }
            System.out.println(page + "\t" + frames + "\t\t" + status);
        }
        System.out.println("LRU total page faults: " + faults);
        return faults;
    }

    // ---------- Optimal ----------
    private static int runOptimal(int[] ref, int frameCount) {
        List<Integer> frames = new ArrayList<>();
        int faults = 0;

        System.out.println("\n--- Optimal ---");
        System.out.println("Page\tFrames\t\tStatus");

        for (int i = 0; i < ref.length; i++) {
            int page = ref[i];
            String status;
            if (frames.contains(page)) {
                status = "Hit";
            } else {
                faults++;
                status = "Fault";
                if (frames.size() < frameCount) {
                    frames.add(page);
                } else {
                    int victim = findOptimalVictim(frames, ref, i + 1);  // look ahead
                    frames.set(victim, page);        // replace the chosen page in place
                }
            }
            System.out.println(page + "\t" + frames + "\t\t" + status);
        }
        System.out.println("Optimal total page faults: " + faults);
        return faults;
    }

    // Pick the frame whose page is used FARTHEST in the future (or never again).
    private static int findOptimalVictim(List<Integer> frames, int[] ref, int startIndex) {
        int victimIndex = 0;
        int farthest = -1;

        for (int f = 0; f < frames.size(); f++) {
            int pageInFrame = frames.get(f);
            int nextUse = Integer.MAX_VALUE;          // assume "never used again"

            for (int j = startIndex; j < ref.length; j++) {
                if (ref[j] == pageInFrame) {
                    nextUse = j;                      // found its next use
                    break;
                }
            }

            if (nextUse > farthest) {                 // farther away = better to evict
                farthest = nextUse;
                victimIndex = f;
            }
        }
        return victimIndex;
    }

    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
}