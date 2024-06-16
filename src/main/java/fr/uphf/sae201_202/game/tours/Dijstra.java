package fr.uphf.sae201_202.game.tours;

import fr.uphf.sae201_202.game.Utils;
import fr.uphf.sae201_202.maps.Cell;

import java.util.*;

public class Dijstra {

    /*
    CellDistance permet de d'associer en un objet la distance d'une Cell
    cette même Cell. Elle permet aussi de comparer la distance entre deux Cells
    qui est utile pour la file de priorité (ProrityQueue)
     */
    static class CellDistance implements Comparable<CellDistance> {
        Cell cell;
        int distance;

        CellDistance(Cell cell, int distance) {
            this.cell = cell;
            this.distance = distance;
        }

        @Override
        public int compareTo(CellDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    /*
    Un PathFinder est comme son nom l'indique un moyen de trouver
    un chemin entre un élement a et un élement b.
     */
    public static List<Cell> pathFinder(Cell start, Cell end) {
        // Stockage des différentes données nécessaire à l'algorithme
        PriorityQueue<CellDistance> queue = new PriorityQueue<>();
        Map<Cell, Integer> distances = new HashMap<>();
        Map<Cell, Cell> previous = new HashMap<>();
        Set<Cell> visited = new HashSet<>();

        // intialisation des données pour la Cell de départ
        distances.put(start, 0);
        queue.add(new CellDistance(start, 0));

        while (!queue.isEmpty()) {
            // Récupération de la dernière Cell et sa CellDistance
            CellDistance current = queue.poll();
            Cell currentCell = current.cell;

            // Si la Cell est déjà visitée, on continue car on a déjà travaillé son chemin
            if (visited.contains(currentCell))
                continue;
            visited.add(currentCell);

            // Si la Cell equivaut à la celle de fin, algorithme à terminer son chemin
            if (currentCell.equals(end))
                break;

            // Récupération des voisins de la Cell
            List<Cell> neighbors = Utils.getAdjacentCells(currentCell);

            for (Cell neighbor : neighbors) {
                // Vérification si la Cell est disponible ou non
                if (isBlocked(neighbor) || visited.contains(neighbor))
                    continue;

                int newDist = distances.get(currentCell) + 1;

                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    // Ajout du voisin dans les précédents visités de la Cell
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, currentCell);
                    queue.add(new CellDistance(neighbor, newDist));
                }
            }
        }

        /*
         Création du chemin en récupérant toutes les dernières
         valeurs associées à la Cell finale (end).
         */
        List<Cell> path = new ArrayList<>();
        for (Cell at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        // Vérification si le chemin est vide ou non
        if (path.isEmpty() || !path.getFirst().equals(start)) {
            return Collections.emptyList();
        }

        return path;
    }

    /*
     Fonction qui permet de vérifier si on peut passer
     sur une Cell ou non.
     */
    private static boolean isBlocked(Cell cell) {
        return (cell.getId() != null && cell.getId().equals("bot")) ||
               (cell.getElement() != null && !cell.getElement().isCanMoveIn());
    }

}
