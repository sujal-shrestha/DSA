import java.util.Arrays;
import java.util.Collections;

public class TravelingSalesman {
    public static int calculateDistance(int[] tour, int[][] distanceMatrix) {
        int totalDistance = 0;
        for (int i = 0; i < tour.length; i++) {
            totalDistance += distanceMatrix[tour[i]][tour[(i + 1) % tour.length]];
        }
        return totalDistance;
    }

    public static int[] hillClimbing(int[][] distanceMatrix) {
        int numCities = distanceMatrix.length;
        int[] currentTour = new int[numCities];
        for (int i = 0; i < numCities; i++) currentTour[i] = i;
        Collections.shuffle(Arrays.asList(currentTour));

        int currentDistance = calculateDistance(currentTour, distanceMatrix);

        while (true) {
            boolean foundBetterTour = false;
            for (int i = 0; i < numCities; i++) {
                for (int j = i + 1; j < numCities; j++) {
                    int[] newTour = currentTour.clone();
                    newTour[i] = currentTour[j];
                    newTour[j] = currentTour[i];
                    int newDistance = calculateDistance(newTour, distanceMatrix);
                    if (newDistance < currentDistance) {
                        currentTour = newTour;
                        currentDistance = newDistance;
                        foundBetterTour = true;
                    }
                }
            }
            if (!foundBetterTour) break;
        }

        return currentTour;
    }

    public static void main(String[] args) {
        int[][] distanceMatrix = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        int[] bestTour = hillClimbing(distanceMatrix);
        System.out.println("Best tour: " + Arrays.toString(bestTour));
        System.out.println("Best distance: " + calculateDistance(bestTour, distanceMatrix));
    }
}
