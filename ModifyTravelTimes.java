import java.util.*;

public class ModifyTravelTimes {
    public static int dijkstra(int n, List<int[]> roads, int source, int destination) {
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0]).add(new int[]{road[1], road[2]});
            graph.get(road[1]).add(new int[]{road[0], road[2]});
        }
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[]{0, source});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int d = current[0];
            int node = current[1];
            if (d > dist[node]) continue;
            for (int[] neighbor : graph.get(node)) {
                int newDist = d + neighbor[1];
                if (newDist < dist[neighbor[0]]) {
                    dist[neighbor[0]] = newDist;
                    pq.add(new int[]{newDist, neighbor[0]});
                }
            }
        }
        return dist[destination];
    }

    public static List<int[]> modifyRoads(int n, List<int[]> roads, int source, int destination, int target) {
        int left = 1, right = 2 * (int) Math.pow(10, 9);
        List<int[]> modifiedRoads = new ArrayList<>();

        while (left < right) {
            int mid = (left + right) / 2;
            List<int[]> tempRoads = new ArrayList<>();
            for (int[] road : roads) {
                if (road[2] == -1) {
                    tempRoads.add(new int[]{road[0], road[1], mid});
                } else {
                    tempRoads.add(road);
                }
            }
            if (dijkstra(n, tempRoads, source, destination) <= target) {
                right = mid;
                modifiedRoads = tempRoads;
            } else {
                left = mid + 1;
            }
        }
        return modifiedRoads;
    }

    public static void main(String[] args) {
        int n = 5;
        List<int[]> roads = List.of(new int[]{4, 1, -1}, new int[]{2, 0, -1}, new int[]{0, 3, -1}, new int[]{4, 3, -1});
        int source = 0, destination = 1, target = 5;
        System.out.println(modifyRoads(n, roads, source, destination, target));
    }
}
