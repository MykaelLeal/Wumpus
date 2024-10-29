package main.strategies;

import java.util.ArrayList;
import java.util.List;
import main.game.map.Map;
import main.game.map.Monster;
import main.game.map.Point;
import main.game.map.Rock;

public class ShortestDistance implements Strategy{


	@Override
	public Point evaluatePossbileNextStep(List<Point> possibleNextStep, Map map) {
		
		//Lista criada para armazenar os melhores passos
		List<Point> bestNextSteps = new ArrayList<>();
		//A distancia fica como valor maximo
		double distance = Double.MAX_VALUE;
		
		//Nesse for vai pegar os proximos passos da lista possibleNextStep
		for (Point nextStep : possibleNextStep) {
			//Pegando, faz o calculo de menor distancia
			double minDistance = calculateDistanceToTreasure(nextStep, map);
			
			//Verifica o espaco proximo para ignorar rochas e monstros
			String space = map.get(nextStep);
            if (space != null && (space.equals(Rock.CHARACTER) || space.equals(Monster.CHARACTER))) {
                continue; 
            }
			
            //Se a distancia calculada for menor que a distancia maxima
			if (minDistance < distance) {
				distance = minDistance;    //A distancia recebe calculada
				bestNextSteps.add(nextStep);// adiciona a lista de melhores passos
			
			} else if (minDistance == distance) {
	            bestNextSteps.add(nextStep);
	        	
		}
		}
		  
		return bestNextSteps.isEmpty() ? null : bestNextSteps.get(0);
    }
	
		
	// Calcula a menor distancia até o tesouro	
	private double calculateDistanceToTreasure(Point nextStep, Map map) {
	    Point treasureLocation = map.getTreasureLocation();
	    
	    if (treasureLocation == null) {
	        return Double.MAX_VALUE;
	    }

	    return Math.abs(nextStep.getPositionX() - treasureLocation.getPositionX()) +
	           Math.abs(nextStep.getPositionY() - treasureLocation.getPositionY());
	}



		
		
		
	}

		