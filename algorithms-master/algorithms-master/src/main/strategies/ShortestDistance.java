package main.strategies;

import java.util.ArrayList;
import java.util.List;

import main.game.map.Map;
import main.game.map.Point;

public class ShortestDistance implements Strategy{


	@Override
	public Point evaluatePossbileNextStep(List<Point> possibleNextStep, Map map) {
		
		
		List<Point> bestNextSteps = new ArrayList<>();
		double distance = Double.MAX_VALUE;
		
		for (Point nextStep : possibleNextStep) {
			double minDistance = calculateDistanceToTreasure(nextStep, nextStep);
			
			if (minDistance < distance) {
				distance = minDistance;
				bestNextSteps.clear();
				bestNextSteps.add(nextStep);
			
			} else if (minDistance == distance) {
	            bestNextSteps.add(nextStep);
	        	
		}
		}
		return null;
	}
		
		
		private double calculateDistanceToTreasure(Point start, Point treasure) {
			    return Math.abs(start.getPositionX() - treasure.getPositionX()) + Math.abs(start.getPositionY() - treasure.getPositionY());
			
	
		}
		
	}

		
		  
	
	
	   

	


	
	