package main.strategies;

import java.util.ArrayList;
import java.util.List;
import main.game.map.Map;
import main.game.map.Monster;
import main.game.map.Point;
import main.game.map.Rock;

public class FewerObstacles implements Strategy {

    @Override
    public Point evaluatePossbileNextStep(List<Point> possibleNextSteps, Map map) {
        List<Point> bestNextSteps = new ArrayList<>();
        double minScore = Double.MAX_VALUE;

        for (Point nextStep : possibleNextSteps) {
            // Skip the current step if it contains an obstacle
            String space = map.get(nextStep);
            if (space != null && (space.equals(Rock.CHARACTER) || space.equals(Monster.CHARACTER))) {
                continue;
            }

            // Calculate the distance to the treasure for scoring purposes
            double distanceToTreasure = calculateDistanceToTreasure(nextStep, map);
            // Calculate the density of obstacles around the current step
            double obstacleDensity = calculateObstacleDensity(nextStep, map);
            double score = distanceToTreasure + obstacleDensity;

            // Update the best next step based on the combined score
            if (score < minScore) {
                minScore = score;
                bestNextSteps.clear();
                bestNextSteps.add(nextStep);
            } else if (score == minScore) {
                bestNextSteps.add(nextStep);
            }
        }

        return bestNextSteps.isEmpty() ? null : bestNextSteps.get(0);
    }

    // Calculate the distance to the treasure
    private double calculateDistanceToTreasure(Point nextStep, Map map) {
        Point treasureLocation = map.getTreasureLocation();
        if (treasureLocation == null) {
            return Double.MAX_VALUE;
        }
        return Math.abs(nextStep.getPositionX() - treasureLocation.getPositionX()) +
               Math.abs(nextStep.getPositionY() - treasureLocation.getPositionY());
    }

    // Calculate the density of obstacles (rocks and monsters) around a point
    private double calculateObstacleDensity(Point step, Map map) {
        int obstacleCount = 0;

        // Assuming the map is a square grid and we know the size (e.g., 8x8)
        final int MAP_SIZE = 8; // Change this to your actual map size

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Skip the current step itself

                int neighborX = step.getPositionX() + dx;
                int neighborY = step.getPositionY() + dy;

                // Check boundaries
                if (neighborX >= 0 && neighborX < MAP_SIZE && neighborY >= 0 && neighborY < MAP_SIZE) {
                    Point neighbor = new Point(neighborX, neighborY);
                    String space = map.get(neighbor);
                    if (space != null && (space.equals(Rock.CHARACTER) || space.equals(Monster.CHARACTER))) {
                        obstacleCount++;
                    }
                }
            }
        }
        return obstacleCount;
    }

}
