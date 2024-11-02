package main.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import main.game.map.Map;
import main.game.map.Monster;
import main.game.map.Point;
import main.game.map.Rock;

public class FewerObstacles implements Strategy {

    private Set<Point> visitedPoints = new HashSet<>();
    private LinkedList<Point> visitedPointsHistory = new LinkedList<>();
    private static final int HISTORY_LIMIT = 10;

    @Override
    public Point evaluatePossbileNextStep(List<Point> possibleNextSteps, Map map) {
        List<Point> bestNextSteps = new ArrayList<>();
        double minScore = Double.MAX_VALUE;

        for (Point nextStep : possibleNextSteps) {
            if (visitedPoints.contains(nextStep) || visitedPointsHistory.contains(nextStep)) {
                continue;
            }

            if (isTreasureNearby(nextStep, map)) {
                return nextStep; // Escolha imediata se o tesouro está próximo
            }

            double distanceToTreasure = calculateDistanceToTreasure(nextStep, map) * 0.3;
            double obstacleDensity = calculateObstacleDensity(nextStep, map) * 1.2;
            double score = distanceToTreasure + obstacleDensity;

            if (score < minScore) {
                minScore = score;
                bestNextSteps.clear();
                bestNextSteps.add(nextStep);
            } else if (score == minScore) {
                bestNextSteps.add(nextStep);
            }
        }

        if (!bestNextSteps.isEmpty()) {
            Point chosenStep = bestNextSteps.get(0);
            visitedPoints.add(chosenStep);
            visitedPointsHistory.add(chosenStep);
            if (visitedPointsHistory.size() > HISTORY_LIMIT) {
                visitedPointsHistory.removeFirst();
            }
            return chosenStep;
        }

        return possibleNextSteps.isEmpty() ? null : possibleNextSteps.get(0); // Evita parada ao escolher o primeiro passo disponível
    }

    private boolean isTreasureNearby(Point point, Map map) {
        Point treasureLocation = map.getTreasureLocation();
        if (treasureLocation == null) {
            return false;
        }
        return calculateDistanceToTreasure(point, map) == 1;
    }

    private boolean isObstacle(Point point, Map map) {
        String space = map.get(point);
        return space != null && (space.equals(Rock.CHARACTER) || space.equals(Monster.CHARACTER));
    }

    private double calculateDistanceToTreasure(Point nextStep, Map map) {
        Point treasureLocation = map.getTreasureLocation();
        if (treasureLocation == null) {
            return Double.MAX_VALUE;
        }
        return Math.abs(nextStep.getPositionX() - treasureLocation.getPositionX()) +
               Math.abs(nextStep.getPositionY() - treasureLocation.getPositionY());
    }

    private double calculateObstacleDensity(Point step, Map map) {
        int obstacleCount = 0;
        int[] mapSize = map.getScenarioSize();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int neighborX = step.getPositionX() + dx;
                int neighborY = step.getPositionY() + dy;

                if (neighborX >= 0 && neighborX < mapSize[0] && neighborY >= 0 && neighborY < mapSize[1]) {
                    Point neighbor = new Point(neighborX, neighborY);
                    if (isObstacle(neighbor, map)) {
                        obstacleCount++;
                    }
                }
            }
        }
        return obstacleCount * 0.8; // Peso ajustado para penalizar áreas de obstáculos próximos sem bloquear completamente
    }
}
