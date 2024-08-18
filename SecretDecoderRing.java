public class SecretDecoderRing {
    public static String rotateChar(char c, int direction) {
        if (direction == 1) {
            return Character.toString((char) ((c - 'a' + 1) % 26 + 'a'));
        } else {
            return Character.toString((char) ((c - 'a' - 1 + 26) % 26 + 'a'));
        }
    }

    public static String decipherMessage(String s, int[][] shifts) {
        StringBuilder result = new StringBuilder(s);

        for (int[] shift : shifts) {
            int start = shift[0];
            int end = shift[1];
            int direction = shift[2];

            for (int i = start; i <= end; i++) {
                result.setCharAt(i, rotateChar(result.charAt(i), direction).charAt(0));
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String s = "hello";
        int[][] shifts = {{0, 1, 1}, {2, 3, 0}, {0, 2, 1}};
        System.out.println(decipherMessage(s, shifts)); // Output: jglko
    }
}
