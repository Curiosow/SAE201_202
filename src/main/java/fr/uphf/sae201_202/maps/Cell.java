package fr.uphf.sae201_202.maps;

import fr.uphf.sae201_202.maps.elements.Element;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane {

    private int column;
    private int row;
    // Element présent sur la cellule
    private Element element;

    public Cell(int column, int row) {
        this.column = column;
        this.row = row;
        this.element = null;

        /*
         Permet de faire une grille entre chaque cellule s'il n'y
         a pas de fond
         */
        getStyleClass().add("cell");
        setOpacity(0.9);
    }

    public void setElement(Element element) {
        this.element = element;
    }
    public Element getElement() {
        return element;
    }


    public String toString() {
        return this.column + "/" + this.row;
    }

    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    public void setRow(int row) {
        this.row = row;
    }
}
