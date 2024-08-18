import java.util.ArrayList;
import java.util.List;

public class OptimizeBoarding {
    public static List<Integer> optimizeBoarding(List<Integer> head, int k) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < head.size(); i += k) {
            List<Integer> chunk = head.subList(i, Math.min(i + k, head.size()));
            if (chunk.size() == k) {
                for (int j = chunk.size() - 1; j >= 0; j--) {
                    result.add(chunk.get(j));
                }
            } else {
                result.addAll(chunk);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Integer> head = List.of(1, 2, 3, 4, 5);
        int k = 2;
        System.out.println(optimizeBoarding(head, k)); // Output: [2, 1, 4, 3, 5]
    }
}
