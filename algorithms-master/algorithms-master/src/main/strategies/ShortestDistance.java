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
	    List<Point> bestNextSteps = new ArrayList<>();
	    int distance = Integer.MAX_VALUE;  // Valor inicial máximo de distância
	    boolean hasValidPath = false;      // Flag para verificar se há um caminho válido

	    Point nextStep = null;
	    for (int i = 0; i < possibleNextStep.size(); i++) {
	        nextStep = possibleNextStep.get(i);
	        
	        // Verifica se a célula não tem obstáculos (rocha ou monstro)
	        String space = map.get(nextStep);
	        if (space != null && (space.equals(Rock.CHARACTER) || space.equals(Monster.CHARACTER))) {
	            continue;
	        }

	        // Verifica se a posição está dentro dos limites da matriz 8x8
	        if (nextStep.getPositionX() >= 0 && nextStep.getPositionX() < 8 && 
	            nextStep.getPositionY() >= 0 && nextStep.getPositionY() < 8) {
	            
	            // Verifica se há obstáculos à direita (x + 1) e para baixo (y + 1)
	            boolean obstacleRight = false;
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

	            // Se ambos os caminhos (direita e abaixo) estiverem bloqueados
	            if (obstacleRight && obstacleDown) {
	                System.out.println("Não há saída, o robô ficou preso!");
	                return null; // Retorna null indicando que o movimento não é possível
	            }

	            // Se há pelo menos um caminho viável
	            hasValidPath = true; // Há pelo menos um caminho válido
	            int minDistance = calculateDistanceToTreasure(nextStep, map);
	            if (minDistance < distance) {
	                distance = minDistance;
	                bestNextSteps.clear();  // Limpa a lista dos melhores passos
	                bestNextSteps.add(nextStep);  // Adiciona o novo melhor passo
	            } else if (minDistance == distance) {
	                bestNextSteps.add(nextStep);  // Caso haja empate na distância, adiciona o próximo passo
	            }
	        }
	    }

	    // Se nenhum caminho válido foi encontrado
	    if (!hasValidPath) {
	        System.out.println("Erro: Nenhum caminho válido encontrado.");
	        return null; // Retorna null caso não existam opções viáveis
	    }

	    // Retorna o próximo passo com a melhor distância ou null se não houver um próximo passo válido
	    return !bestNextSteps.isEmpty() ? bestNextSteps.get(0) : null;
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
