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
			double minDistance = calculateDistanceToTreasure(nextStep, map);
			
			
			String space = map.get(nextStep);
            if (space != null && (space.equals("R") || space.equals("M"))) {
                continue; 
            }
			
			if (minDistance < distance) {
				distance = minDistance;
				bestNextSteps.clear();
				bestNextSteps.add(nextStep);
			
			} else if (minDistance == distance) {
	            bestNextSteps.add(nextStep);
	        	
		}
		}
		  
		return bestNextSteps.isEmpty() ? null : bestNextSteps.get(0);
    }
	
		
		
	private double calculateDistanceToTreasure(Point nextStep, Map map) {
	    Point treasureLocation = map.getTreasureLocation();
	    
	    if (treasureLocation == null) {
	        return Double.MAX_VALUE;
	    }

	    return Math.abs(nextStep.getPositionX() - treasureLocation.getPositionX()) +
	           Math.abs(nextStep.getPositionY() - treasureLocation.getPositionY());
	}



		
		
		
	}

		
		  
	
	
	   

	


	
	