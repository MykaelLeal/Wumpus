package main.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.game.Player;
import main.game.map.Map;
import main.game.map.Monster;
import main.game.map.Point;
import main.game.map.Rock;
import main.game.map.TreasureChest;

public class Sort implements Strategy{
	/**
	 * N is the next location 
	 * p1 p2 p3
	 * p4 N p5
	 * p6 p7 p8
	 */
	@Override
	public Point evaluatePossbileNextStep(List<Point> possibleNextSteps, Map map) {
	       List<Point> validNextSteps = new ArrayList<>();

		    // Verifica passos válidos (ignora rochas e monstros)
		    for (Point nextStep : possibleNextSteps) {
		        String space = map.get(nextStep);
		        if (space != null && (space.equals(Rock.CHARACTER) || space.equals(Monster.CHARACTER))) {
		            continue;
		        }
		        validNextSteps.add(nextStep);
		    }

		    // Escolhe aleatoriamente um ponto da lista de passos válidos
		    Random random = new Random();
		    return validNextSteps.get(random.nextInt(validNextSteps.size()));
		    
		    
		}

		
	}	

