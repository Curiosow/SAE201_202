package fr.uphf.sae201_202.game.tours;

import fr.uphf.sae201_202.SAE;
import fr.uphf.sae201_202.game.Utils;
import fr.uphf.sae201_202.maps.Cell;

import java.util.*;

public class Dijstra {

    public static List<Cell> pathFinder(Cell start, Cell end) {
        Map<Cell, Cell> previousCells = new HashMap<>();
        Map<Cell, Double> shortestDistances = new HashMap<>();
        PriorityQueue<Cell> queue = new PriorityQueue<>(Comparator.comparing(shortestDistances::get));
        List<Cell> knownCells = SAE.get().getBotKnowsCells();

        for(Cell cell : Utils.getAdjacentCells(start)) {
            if(cell.equals(end))
                return new ArrayList<>(Collections.singleton(cell));
        }

        for (Cell cell : knownCells) {
            if ((cell.getId() != null && cell.getId().equals("bot")) || (cell.getElement() != null && !cell.getElement().isCanMoveIn())) {
                shortestDistances.put(cell, Double.MAX_VALUE);
            } else {
                shortestDistances.put(cell, cell.equals(start) ? 0 : Double.MAX_VALUE);
                queue.add(cell);
            }
        }

        while (!queue.isEmpty()) {
            Cell currentCell = queue.poll();

            for (Cell neighbor : Utils.getAdjacentCells(currentCell)) {
                if(!shortestDistances.containsKey(neighbor)) {
                    previousCells.put(neighbor, currentCell);
                    continue;
                }

                if (shortestDistances.get(currentCell) + Utils.calculateDistance(currentCell, neighbor) < shortestDistances.get(neighbor)) {
                    queue.remove(neighbor);
                    shortestDistances.put(neighbor, shortestDistances.get(currentCell) + Utils.calculateDistance(currentCell, neighbor));
                    previousCells.put(neighbor, currentCell);
                    queue.add(neighbor);
                }
            }
        }
        List<Cell> path = new ArrayList<>();
        for (Cell cell = end; cell != null; cell = previousCells.get(cell)) {
            path.add(cell);
        }
        Collections.reverse(path);

        return path;
    }
}
