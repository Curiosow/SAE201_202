package fr.uphf.sae201_202.maps;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Grid extends Pane {

    private final int rows;
    private final int columns;

    // taille de la fenêtre
    private final double width;
    private final double height;

    // toutes les cellules sont stockées dedans
    Cell[][] cells;

    public Grid(int columns, int rows, double width, double height) {
        this.columns = columns;
        this.rows = rows;
        this.width = width;
        this.height = height;

        cells = new Cell[rows][columns];
    }

    public void add(Cell cell, int column, int row) {
        cells[row][column] = cell;

        double w = width / columns;
        double h = height / rows;
        double x = w * column;
        double y = h * row;

        cell.setLayoutX(x);
        cell.setLayoutY(y);
        cell.setPrefWidth(w);
        cell.setPrefHeight(h);

        /*
         grille étend de Pane, donc on peut ajouter des enfants.
         On peut donc ajouter la cellule dedans.
         */
        getChildren().add(cell);
    }

    public Cell getCell(int column, int row) {
        return cells[row][column];
    }

    public List<Cell> getAllCells() {
        // Permet de transformer le tableau de cellules en liste de cellules
        List<Cell> allCells = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                allCells.add(cells[i][j]);
            }
        }
        return allCells;
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
}
