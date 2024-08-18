import java.util.Arrays;
import java.util.Comparator;

public class ClassroomScheduling {
    public static int mostClassesHeld(int n, int[][] classes) {
        Arrays.sort(classes, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                if (a[0] != b[0]) {
                    return a[0] - b[0];
                }
                return b[1] - a[1];
            }
        });

        int[] roomEndTimes = new int[n];
        int[] roomUsage = new int[n];

        for (int[] c : classes) {
            int start = c[0];
            int end = c[1];
            boolean assigned = false;

            for (int i = 0; i < n; i++) {
                if (roomEndTimes[i] <= start) {
                    roomEndTimes[i] = end;
                    roomUsage[i]++;
                    assigned = true;
                    break;
                }
            }

            if (!assigned) {
                int earliestEnd = Integer.MAX_VALUE;
                int roomToDelay = -1;
                for (int i = 0; i < n; i++) {
                    if (roomEndTimes[i] < earliestEnd) {
                        earliestEnd = roomEndTimes[i];
                        roomToDelay = i;
                    }
                }
                roomEndTimes[roomToDelay] = earliestEnd + (end - start);
                roomUsage[roomToDelay]++;
            }
        }

        int maxUsage = 0;
        int roomWithMaxUsage = 0;
        for (int i = 0; i < n; i++) {
            if (roomUsage[i] > maxUsage) {
                maxUsage = roomUsage[i];
                roomWithMaxUsage = i;
            } else if (roomUsage[i] == maxUsage) {
                if (i < roomWithMaxUsage) {
                    roomWithMaxUsage = i;
                }
            }
        }

        return roomWithMaxUsage;
    }

    public static void main(String[] args) {
        int n = 2;
        int[][] classes = {{0, 10}, {1, 5}, {2, 7}, {3, 4}};
        System.out.println(mostClassesHeld(n, classes)); // Output: 0
    }
}
