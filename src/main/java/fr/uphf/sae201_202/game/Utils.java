package fr.uphf.sae201_202.game;

import fr.uphf.sae201_202.SAE;
import fr.uphf.sae201_202.maps.Cell;
import fr.uphf.sae201_202.maps.Grid;
import fr.uphf.sae201_202.maps.elements.Element;
import fr.uphf.sae201_202.maps.elements.ElementGUI;
import fr.uphf.sae201_202.maps.elements.Mine;
import fr.uphf.sae201_202.maps.elements.Storage;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {

    // Fonction permettant d'obtenir une cellule aléatoire vide
    public static Cell getRandomCell(Grid grid) {
        Random random = new Random();
        int tryed = 0;

        while (true) {
            if(tryed == 20) {
                System.out.println("Sorry, I can't find a place!");
                System.exit(0);
                return null;
            }
            int x = random.nextInt(grid.getColumns());
            int y = random.nextInt(grid.getRows());
            Cell cell = grid.getCell(x, y);
            if(cell.getElement() == null) {
                return cell;
            }

            tryed++;
        }
    }

    // Définit le fond d'un élement
    public static void setBackground(Cell cell, Element element) {
        InputStream file = SAE.get().getClass().getResourceAsStream("/img/" + element.getImgLink());
        if(file == null) {
            System.out.println("ERROR IN LOADING BG IMAGE");
            System.exit(0);
            return;
        }
        cell.setBackground(new Background(new BackgroundImage(new Image(file,64,64,false,true),
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
          BackgroundSize.DEFAULT)));
    }

    // Créer un objet ImageView d'une flèche prête à être ajoutée dans une interface
    public static ImageView createArrow(Bot bot, boolean inTour, String imgName, String action, int layoutY, int layoutX) throws FileNotFoundException {
        InputStream fis_arrow = SAE.get().getClass().getResourceAsStream("/img/" + imgName);
        if(fis_arrow == null) {
            System.out.println("ERROR IN LOADING ARROW IMAGE");
            System.exit(0);
            return null;
        }
        Image img_arrow = new Image(fis_arrow);
        ImageView arrow = new ImageView(img_arrow);
        arrow.setFitHeight(75);
        arrow.setFitWidth(75);
        arrow.setLayoutY(layoutY);
        arrow.setLayoutX(layoutX);
        arrow.setOnMouseClicked(event -> {
            if(inTour) {
                if(SAE.get().getTour().isHasDoneAction())
                    return;

                System.out.println("ACTION DONE : MOVED\nGO TO NEXT BOT");
                SAE.get().getTour().getBotInTour().remove(bot);
                SAE.get().getTour().setHasDoneAction(true);
                Cell cell = SAE.get().getMap().getGrid().getCell(bot.getPosY(), bot.getPosX());
                new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> cell.getStyleClass().remove("flash"));
                        }
                    },
                    TimeUnit.SECONDS.toMillis(1)
                );
            }
            actionMoving(bot, action);
        });

        return arrow;
    }

    public static void moveToCell(Bot bot, Cell cell, Cell newCell) {
        // L'ancienne cellule est vidée
        cell.setBackground(null);
        cell.setId(null);
        cell.setOnMouseClicked(null);

        // Ré-application de l'ancien élement sur la case
        if(bot.getLastElement() != null) {
            InputStream fileArrow = SAE.get().getClass().getResourceAsStream("/img/" + bot.getLastElement().getImgLink());
            if(fileArrow == null) {
                System.out.println("ERROR IN LOADING ARROW IMAGE");
                System.exit(0);
                return;
            }
            cell.setBackground(new Background(new BackgroundImage(new Image(fileArrow,64,64,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));
            cell.setElement(bot.getLastElement());
            cell.setOnMouseClicked(event -> {
                try {
                    new ElementGUI(cell.getElement()).start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            bot.setLastElement(null);
        }
        if(newCell.getElement() != null)
            bot.setLastElement(newCell.getElement());

        InputStream file = SAE.get().getClass().getResourceAsStream("/img/bot.png");
        if(file == null) {
            System.out.println("ERROR IN LOADING BOT IMAGE");
            System.exit(0);
            return;
        }
        newCell.setBackground(new Background(new BackgroundImage(new Image(file,64,64,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));

        newCell.setOnMouseClicked(event -> {
            try {
                bot.start(new Stage());
            } catch (Exception e) {
                System.out.println("THERE WAS A PROBLEM TO CREATE THE BOT GUI");
            }
        });
        newCell.setId("bot");
        bot.setPos(newCell.getRow(), newCell.getColumn());
        for(Cell cells : getAdjacentCells(bot)) {
            if(SAE.get().getBotKnowsCells().contains(cells))
                continue;

            SAE.get().getBotKnowsCells().add(cells);
            cells.getStyleClass().remove("unknowCase");
        }
    }

    // Fonction permettant d'effectuer l'action de déplacement
    public static void actionMoving(Bot bot, String action) {
        Grid grid = SAE.get().getMap().getGrid();
        Cell cell = grid.getCell(bot.getPosY(), bot.getPosX());

        String[] actionParts = action.split("/");
        String axis = actionParts[0];
        String direction = actionParts[1];

        Cell newCell = move(bot, axis, direction, cell, grid);
        if(newCell == null) {
            System.out.println("CELL NOT EXIST!");
            return;
        }

        // Si l'élement n'est pas nul et qu'on ne peut pas se déplacer dessus ou que c'est un bot qui est présent sur la cellule
        if((newCell.getElement() != null && !newCell.getElement().isCanMoveIn()) || (newCell.getId() != null && newCell.getId().equals("bot"))) {
            System.out.println("IMPOSSIBLE MOVING");
            return;
        }

        moveToCell(bot, cell, newCell);
    }

    // Fonction permettant de récupérer la cellule par rapport à une cellule, l'axe, la direction et la grille
    public static Cell move(Bot bot, String axis, String direction, Cell cell, Grid grid) {
        int newPos;
        if(direction.equals("-")) {
            newPos = (axis.equals("Y") ? bot.getPosY() : bot.getPosX()) - 1;
        } else {
            newPos = (axis.equals("Y") ? bot.getPosY() : bot.getPosX()) + 1;
        }

        if(newPos < 0 || newPos == (axis.equals("Y") ? grid.getColumns() : grid.getRows())) {
            System.out.println("IMPOSSIBLE MOVING");
            return null;
        }

        return grid.getCell(axis.equals("Y") ? newPos : bot.getPosY(), axis.equals("Y") ? bot.getPosX() : newPos);
    }

    // Fonction lançant le runner d'un élement
    public static void elementRunning(Bot bot, Cell cell) {
        if(cell.getElement() == null)
            return;

        Element element = cell.getElement();
        if(element instanceof Mine mine) {
            handleMining(bot, mine);
        } else if (element instanceof Storage storage) {
            handleStoring(bot, storage);
        }
    }

    // Fonction de l'action de miner
    public static void handleMining(Bot bot, Mine mine) {
        if(!mine.getOres().equals(bot.getOres())) {
            System.out.println("MINING: IMPOSSIBLE! DIFFERENT TYPE.");
            return;
        }

        int amount = mine.getOreLess();
        if(amount == 0) {
            System.out.println("MINING: IMPOSSIBLE! MINE IS EMPTY.");
            return;
        }
        for(int i = 0; i < amount; i++) {
            if(mine.getOreLess() == 0 || bot.getHarvest() == bot.getStorageMax()) {
                System.out.println("MINING: STOPPED!");
                return;
            }

            mine.setOreLess(mine.getOreLess() - 1);
            bot.setHarvest(bot.getHarvest() + 1);
            System.out.println("MINING: MINE A " + bot.getOres().name());
        }
        System.out.println("MINING: DONE!");
    }

    // Fonction de l'action d'enregistrer dans l'entrepôt
    public static void handleStoring(Bot bot, Storage storage) {
        if(!storage.getOres().equals(bot.getOres()) || bot.getHarvest() == 0) {
            System.out.println("STORAGING: IMPOSSIBLE!");
            return;
        }

        storage.setAmount(storage.getAmount() + bot.getHarvest());
        int amount = bot.getHarvest();
        bot.setHarvest(0);
        System.out.println("STORAGING: STORE x" + amount + " " + bot.getOres().name());
        System.out.println("STORAGING: DONE!");
    }

    public static List<Cell> getAdjacentCells(Cell cell) {
        List<Cell> adjacentCells = new ArrayList<>();

        Grid grid = SAE.get().getMap().getGrid();
        int posX = cell.getRow();
        int posY = cell.getColumn();

        // Cellule à gauche
        if (posX > 0) {
            adjacentCells.add(grid.getCell(posY, posX - 1));
        }

        // Cellule à droite
        if (posX < grid.getColumns() - 1) {
            adjacentCells.add(grid.getCell(posY, posX + 1));
        }

        // Cellule en haut
        if (posY > 0) {
            adjacentCells.add(grid.getCell(posY - 1, posX));
        }

        // Cellule en bas
        if (posY < grid.getRows() - 1) {
            adjacentCells.add(grid.getCell(posY + 1, posX));
        }

        return adjacentCells;
    }

    // Fonction pour avoir les 4 cases autour du bot
    public static List<Cell> getAdjacentCells(Bot bot) {
        return getAdjacentCells(SAE.get().getMap().getGrid().getCell(bot.getPosY(), bot.getPosX()));
    }

    // Calcule la distance estimée entre deux cases
    public static double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static double calculateDistance(Cell cell1, Cell cell2) {
        return calculateDistance(cell1.getColumn(), cell1.getRow(), cell2.getColumn(), cell2.getRow());
    }

}
