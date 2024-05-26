package fr.uphf.sae201_202.maps;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Grid extends Pane {

    private final int rows;
    private final int columns;

    private final double width;
    private final double height;

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

        getChildren().add(cell);
    }

    public Cell getCell(int column, int row) {
        return cells[row][column];
    }

    public List<Cell> getAllCells() {
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
