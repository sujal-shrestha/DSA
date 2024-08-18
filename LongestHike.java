public class LongestHike {
    public static int longestHike(int[] nums, int k) {
        int maxLength = 0;
        int currentLength = 1;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1] && nums[i] - nums[i - 1] <= k) {
                currentLength++;
            } else {
                maxLength = Math.max(maxLength, currentLength);
                currentLength = 1;
            }
        }
        return Math.max(maxLength, currentLength);
    }

    public static void main(String[] args) {
        int[] nums = {4, 2, 1, 4, 3, 4, 5, 8, 15};
        int k = 3;
        System.out.println(longestHike(nums, k)); // Output: 5
    }
}
