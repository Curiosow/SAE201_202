package fr.uphf.sae201_202.game.tours;

import fr.uphf.sae201_202.game.Utils;
import fr.uphf.sae201_202.maps.Cell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {

    /*
    Un PathFinder est comme son nom l'indique un moyen de trouver
    un chemin entre un élement a et un élement b.
     */
    public static List<Cell> pathFinder(Cell start, Cell end) {
        List<Cell> chemin = new ArrayList<>();
        PriorityQueue<Cell> filePrio = new PriorityQueue<>(
                ((o1, o2) -> (int) (
                        Utils.calculateDistance(start.getColumn(), start.getRow(), o2.getColumn(), o2.getRow()) -
                        Utils.calculateDistance(start.getColumn(), start.getRow(), o1.getColumn(), o1.getRow())
                )));

        filePrio.add(start);
        while (!filePrio.isEmpty()) {
            Cell actual = filePrio.poll();
            if(actual.equals(start)) {
                while(actual != null) {

                }
                break;
            }
        }
        return chemin;
    }

}
