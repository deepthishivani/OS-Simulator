public class Banker {

    // n = number of processes, m = number of resource types.
    public static void checkSafety(int[][] allocation, int[][] max, int[] available) {

        int n = allocation.length;       // rows = processes
        int m = available.length;        // columns = resource types

        // ----- Step 0: compute Need = Max - Allocation -----
        int[][] need = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }

        printMatrix("Need (Max - Allocation)", need);

        // ----- Step 1: Work = copy of Available, Finish[] all false -----
        int[] work = new int[m];
        for (int j = 0; j < m; j++) {
            work[j] = available[j];
        }
        boolean[] finish = new boolean[n];   // defaults to all false in Java

        int[] safeSequence = new int[n];     // records the order processes finish in
        int count = 0;                       // how many have finished so far

        // ----- Step 2-3: repeatedly find a process that can finish -----
        // Keep looping until we make a full pass with NObody able to finish.
        boolean madeProgress = true;
        while (madeProgress) {
            madeProgress = false;

            for (int i = 0; i < n; i++) {
                // Skip if already finished.
                if (finish[i]) continue;

                // Can process i finish? Every resource in its Need must be <= Work.
                boolean canFinish = true;
                for (int j = 0; j < m; j++) {
                    if (need[i][j] > work[j]) {
                        canFinish = false;
                        break;
                    }
                }

                if (canFinish) {
                    // Pretend it runs: it releases everything it holds back into Work.
                    for (int j = 0; j < m; j++) {
                        work[j] += allocation[i][j];
                    }
                    finish[i] = true;
                    safeSequence[count] = i;
                    count++;
                    madeProgress = true;   // we found one, so loop again for more
                }
            }
        }

        // ----- Step 4: did everyone finish? -----
        if (count == n) {
            System.out.println("\nSystem is in a SAFE state.");
            System.out.print("Safe sequence: ");
            for (int k = 0; k < n; k++) {
                System.out.print("P" + safeSequence[k]);
                if (k < n - 1) System.out.print(" -> ");
            }
            System.out.println();
        } else {
            System.out.println("\nSystem is in an UNSAFE state (deadlock possible).");
            System.out.print("Processes that could NOT finish: ");
            for (int i = 0; i < n; i++) {
                if (!finish[i]) System.out.print("P" + i + " ");
            }
            System.out.println();
        }
    }

    private static void printMatrix(String title, int[][] matrix) {
        System.out.println("\n" + title + ":");
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("P" + i + ": ");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}