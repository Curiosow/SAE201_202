package fr.uphf.sae201_202.maps;

import fr.uphf.sae201_202.SAE;
import fr.uphf.sae201_202.game.Utils;
import fr.uphf.sae201_202.maps.elements.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Map {

    private final Random random;

    private final int width;
    private final int height;

    private final int lignes;
    private final int colonnes;
    private Grid grid;

    // toutes les listes des différents élements
    private final List<Mine> mines;
    private final List<Water> waters;
    private final List<Storage> storages;

    // nombres totaux des différents élements
    private int minesMax;
    private int watersMax;

    public Map() {
        random = new Random();

        width = 800;
        height = 600;

        lignes = 10;
        colonnes = 10;

        mines = new ArrayList<>();
        waters = new ArrayList<>();
        storages = new ArrayList<>();

        minesMax = random.nextInt(3) + 2;
        watersMax = random.nextInt(11);
    }

    public void initMap(Stage stage) throws MalformedURLException {
        StackPane root = new StackPane();
        grid = new Grid(colonnes, lignes, width, height);
        InputStream bgFile = SAE.get().getClass().getResourceAsStream("/img/gameBackground.png");
        if(bgFile == null) {
            System.out.println("ERROR IN LOADING BACKGROUND IMAGE");
            System.exit(0);
            return;
        }

        // Définition de fond d'écran de la partie
        Background background = new Background(new BackgroundImage(new Image(bgFile, width,height,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT));

        root.setBackground(background);

        // Création des cellules
        for (int row = 0; row < lignes; row++) {
            for (int column = 0; column < colonnes; column++) {
                Cell cell = new Cell(column, row);
                cell.getStyleClass().add("unknowCase");
                grid.add(cell, column, row);
            }
        }

        placeElements();

        root.getChildren().addAll(grid);
        Scene scene = new Scene(root, width, height);
        // Définition du css de la scène
        URL css = getClass().getResource("/application.css");
        scene.getStylesheets().add(css.toExternalForm());
        stage.setTitle("Mineur de l'extrême");
        stage.setScene(scene);
    }

    private void placeElements() {
        System.out.println("------- PLACING ELEMENTS -------");
        // Création des deux premières mines
        createRandomMine(Ores.GOLD);
        createRandomMine(Ores.NICKEL);

        for(int i = 0; i < minesMax; i++) {
            List<Ores> ores = new ArrayList<>(List.of(Ores.values()));
            Collections.shuffle(ores);

            createRandomMine(ores.getFirst());
        }

        for(int i = 0; i < watersMax; i++) {
            createRandomWater();
        }

        // Création des deux premières stockages
        createRandomStorage(Ores.GOLD);
        createRandomStorage(Ores.NICKEL);
        System.out.println("--------------------------------");
    }

    private void createRandomMine(Ores ores) {
        Cell cell = Utils.getRandomCell(grid);
        Mine mine = new Mine(ores);
        // Lancement de l'interface de l'élement quand on clique dessus
        cell.setOnMouseClicked(event -> {
            try {
                new ElementGUI(mine).start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Utils.setBackground(cell, mine);
        mines.add(mine);
        cell.setElement(mine);
        System.out.println("PLACED A MINE OF " + ores.name() + " IN " + cell.getRow() + "/" + cell.getColumn());
    }

    private void createRandomWater() {
        Cell cell = Utils.getRandomCell(grid);
        Water water = new Water();
        Utils.setBackground(cell, water);
        waters.add(water);
        cell.setElement(water);
        System.out.println("PLACED A WATER IN " + cell.getRow() + "/" + cell.getColumn());
    }

    private void createRandomStorage(Ores ores) {
        Cell cell = Utils.getRandomCell(grid);
        Storage storage = new Storage(ores);
        Utils.setBackground(cell, storage);
        cell.setOnMouseClicked(event -> {
            try {
                new ElementGUI(storage).start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        storages.add(storage);
        cell.setElement(storage);
        System.out.println("PLACED A STORAGE IN " + cell.getRow() + "/" + cell.getColumn());
    }


    public List<Mine> getMines() {
        return mines;
    }
    public int getWatersMax() {
        return watersMax;
    }
    public List<Storage> getStorages() {
        return storages;
    }
    public Grid getGrid() {
        return grid;
    }
    public List<Water> getWaters() {
        return waters;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}