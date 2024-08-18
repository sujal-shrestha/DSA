import java.util.*;

public class FriendshipRequests {
    public static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }

    public static List<String> friendRequests(int n, int[][] restrictions, int[][] requests) {
        UnionFind uf = new UnionFind(n);
        List<String> result = new ArrayList<>();

        for (int[] request : requests) {
            int u = request[0];
            int v = request[1];
            int rootU = uf.find(u);
            int rootV = uf.find(v);
            boolean canBeFriends = true;
            for (int[] restriction : restrictions) {
                int rootX = uf.find(restriction[0]);
                int rootY = uf.find(restriction[1]);
                if ((rootU == rootX && rootV == rootY) || (rootU == rootY && rootV == rootX)) {
                    canBeFriends = false;
                    break;
                }
            }
            if (canBeFriends) {
                result.add("approved");
                uf.union(u, v);
            } else {
                result.add("denied");
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int n = 3;
        int[][] restrictions = {{0, 1}};
        int[][] requests = {{0, 2}, {2, 1}};
        System.out.println(friendRequests(n, restrictions, requests)); // Output: ["approved", "denied"]
    }
}
