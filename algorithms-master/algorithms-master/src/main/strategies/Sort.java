package main.strategies;

import java.util.List;
import java.util.Random;

import main.game.Player;
import main.game.map.Map;
import main.game.map.Point;
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
		return null;
	}	
}
