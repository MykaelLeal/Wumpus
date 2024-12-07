package main.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.game.map.Map;
import main.game.map.Monster;
import main.game.map.Point;
import main.game.map.Rock;

public class FewerObstacles implements Strategy {

	@Override
	public Point evaluatePossbileNextStep(List<Point> possibleNextStep, Map map) {
	    // Lista para armazenar os melhores próximos passos
	    List<Point> bestNextSteps = new ArrayList<>();
	    int distance = Integer.MAX_VALUE;  // Valor inicial máximo de distância
	    int lowestObstaculesCount = Integer.MAX_VALUE;  // Contagem mínima de Obstaculos
	    boolean hasValidPath = false;      // Flag para verificar se há um caminho válido
	    
	  
	    Point nextStep = null;
	    for (int i = 0; i < possibleNextStep.size(); i++) {
	        nextStep = possibleNextStep.get(i);

	        // Verifica se a célula tem obstáculos (rochas ou monstros)
	        String space = map.get(nextStep);
	        if (space != null && (space.equals(Rock.CHARACTER) || space.equals(Monster.CHARACTER))) {
	            continue;  // Se tiver obstáculo, ignora esse próximo passo
	        }
	        

	        // Verifica se a posição está dentro dos limites da matriz 8x8
	        if (nextStep.getPositionX() >= 0 && nextStep.getPositionX() < 8 &&
	            nextStep.getPositionY() >= 0 && nextStep.getPositionY() < 8) {

	        	// Verifica se ambos os caminhos (direita, abaixo, esquerda, cima) estão bloqueados
	        	boolean obstacleRight = false;
	        	boolean obstacleLeft = false;
	        	boolean obstacleUp = false;
	        	boolean obstacleDown = false;

	        	// Verifica se há rocha ou monstro à direita
	        	if (nextStep.getPositionX() + 1 < 8) { // Dentro dos limites
	        	    String spaceRight = map.get(new Point(nextStep.getPositionX() + 1, nextStep.getPositionY()));
	        	    if (spaceRight != null && (spaceRight.equals(Rock.CHARACTER) || spaceRight.equals(Monster.CHARACTER))) {
	        	        obstacleRight = true;
	        	    }
	        	} else {
	        	    obstacleRight = true; // Fora dos limites, considera como bloqueado
	        	}

	        	// Verifica se há rocha ou monstro para baixo
	        	if (nextStep.getPositionY() + 1 < 8) { // Dentro dos limites
	        	    String spaceDown = map.get(new Point(nextStep.getPositionX(), nextStep.getPositionY() + 1));
	        	    if (spaceDown != null && (spaceDown.equals(Rock.CHARACTER) || spaceDown.equals(Monster.CHARACTER))) {
	        	        obstacleDown = true;
	        	    }
	        	} else {
	        	    obstacleDown = true; // Fora dos limites, considera como bloqueado
	        	}

	        	// Verifica se há rocha ou monstro para cima
	        	if (nextStep.getPositionY() - 1 >= 0) { // Dentro dos limites
	        	    String spaceUp = map.get(new Point(nextStep.getPositionX(), nextStep.getPositionY() - 1));
	        	    if (spaceUp != null && (spaceUp.equals(Rock.CHARACTER) || spaceUp.equals(Monster.CHARACTER))) {
	        	        obstacleUp = true;
	        	    }
	        	} else {
	        	    obstacleUp = true; // Fora dos limites, considera como bloqueado
	        	}

	        	// Verifica se há rocha ou monstro para a esquerda
	        	if (nextStep.getPositionX() - 1 >= 0) { // Dentro dos limites
	        	    String spaceLeft = map.get(new Point(nextStep.getPositionX() - 1, nextStep.getPositionY()));
	        	    if (spaceLeft != null && (spaceLeft.equals(Rock.CHARACTER) || spaceLeft.equals(Monster.CHARACTER))) {
	        	        obstacleLeft = true;
	        	    }
	        	} else {
	        	    obstacleLeft = true; // Fora dos limites, considera como bloqueado
	        	}

	        	// Se todos os caminhos estão bloqueados
	        	if (obstacleRight && obstacleLeft && obstacleUp && obstacleDown) {
	        	    System.out.println("O robô está preso, não há mais caminhos disponíveis.");
	        	    return null; // Retorna null indicando que não há mais movimento possível
	        	}


	            
	            // Se o número de Obstaculos ao longo do caminho for menor ou o mesmo que o atual melhor, ou se a distância for menor
	            hasValidPath = true;
	         // Caso contrário, o robô pode continuar e analisar a contagem de monstros e a distância
	            int countObstculesAlongPath = countObstculesAlongPath(nextStep, map);
	            int minDistance = calculateDistanceToTreasure(nextStep, map);

	            if (countObstculesAlongPath < lowestObstaculesCount ||
	                (countObstculesAlongPath == lowestObstaculesCount && minDistance < distance)) {
	                lowestObstaculesCount = countObstculesAlongPath;
	                distance = minDistance;
	                bestNextSteps.clear();  // Limpa a lista dos melhores passos
	                bestNextSteps.add(nextStep);  // Adiciona o novo melhor passo
	            } else if (countObstculesAlongPath == lowestObstaculesCount && minDistance == distance) {
	                bestNextSteps.add(nextStep);  // Caso haja empate na quantidade de Obstaculos e na distância, adiciona o próximo passo
	            }
	        }
	    }
	    
	 // Se nenhum caminho válido foi encontrado
	    if (!hasValidPath) {
	        System.out.println("Erro: Nenhum caminho válido encontrado.");
	        return null; // Retorna null caso não existam opções viáveis
	    }


	    // Se nenhum caminho válido foi encontrado
	    if (bestNextSteps.isEmpty()) {
	        System.out.println("Não há saída, o robô ficou preso!");
	        return null; // Retorna null indicando que o movimento não é possível
	    }

	    // Retorna o próximo passo com a menor quantidade de Obstaculos e melhor distância
	    return bestNextSteps.get(0);
	}
	// Conta o número total de Obstaculos ao longo do caminho, considerando um caminho desde a posição atual
	private int countObstculesAlongPath(Point nextStep, Map map) {
	    int obstculesCount = 0;

	    // Verifica os pontos adjacentes (direita, abaixo, esquerda, acima)
	    int[] directionsX = {-1, 1, 0, 0}; // Direções X (esquerda, direita)
	    int[] directionsY = {0, 0, -1, 1}; // Direções Y (acima, abaixo)

	    for (int i = 0; i < 4; i++) {
	        int newX = nextStep.getPositionX() + directionsX[i];
	        int newY = nextStep.getPositionY() + directionsY[i];

	        if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) { // Verifica se está dentro dos limites
	            String adjacentSpace = map.get(new Point(newX, newY));
	            if (adjacentSpace != null && adjacentSpace.equals(Monster.CHARACTER) || adjacentSpace.equals(Rock.CHARACTER)) {
	                obstculesCount++;
	                
	            }
	        }
	    }

	    // Retorna a quantidade de Obstaculos ao longo do caminho, pode ser maior ou igual a 0
	    return obstculesCount;
	}

	// Calcula a menor distância até o tesouro
	private int calculateDistanceToTreasure(Point nextStep, Map map) {
	    Point treasureLocation = map.getTreasureLocation();

	    if (treasureLocation == null) {
	        return Integer.MAX_VALUE;
	    }

	    return Math.abs(nextStep.getPositionX() - treasureLocation.getPositionX()) +
	           Math.abs(nextStep.getPositionY() - treasureLocation.getPositionY());
	}
	}