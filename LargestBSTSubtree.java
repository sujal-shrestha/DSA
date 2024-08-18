public class LargestBSTSubtree {
    public static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
            left = right = null;
        }
    }

    public static int[] helper(TreeNode node) {
        if (node == null) return new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0, 1};

        int[] left = helper(node.left);
        int[] right = helper(node.right);

        if (left[3] == 1 && right[3] == 1 && left[1] < node.val && node.val < right[0]) {
            return new int[]{Math.min(left[0], node.val), Math.max(right[1], node.val), left[2] + right[2] + node.val, 1};
        }
        return new int[]{0, 0, Math.max(left[2], right[2]), 0};
    }

    public static int largestBSTSubtree(TreeNode root) {
        return helper(root)[2];
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(4);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(2);
        root.right.right = new TreeNode(5);
        root.right.right.left = new TreeNode(4);
        root.right.right.right = new TreeNode(6);

        System.out.println(largestBSTSubtree(root)); // Output: 20
    }
}
