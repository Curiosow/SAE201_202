package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.Cell;
import fr.uphf.sae201_202.maps.Grid;
import fr.uphf.sae201_202.maps.elements.Element;
import fr.uphf.sae201_202.maps.elements.Mine;
import fr.uphf.sae201_202.maps.elements.Storage;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class Utils {

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

    public static void setBackground(Cell cell, Element element) {
        cell.setBackground(new Background(new BackgroundImage(new Image("file:libs/img/" + element.getImgLink(),64,64,false,true),
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
          BackgroundSize.DEFAULT)));
    }

    public static ImageView createArrow(Bot bot, boolean inTour, String imgName, String action, int layoutY, int layoutX) throws FileNotFoundException {
        FileInputStream fis_arrow = new FileInputStream("libs/img/" + imgName);
        Image img_arrow = new Image(fis_arrow);
        ImageView arrow = new ImageView(img_arrow);
        arrow.setFitHeight(75);
        arrow.setFitWidth(75);
        arrow.setLayoutY(layoutY);
        arrow.setLayoutX(layoutX);
        arrow.setOnMouseClicked(event -> {
            actionMoving(bot, action);
            if(inTour) {
                System.out.println("ACTION DONE : MOVED\nGO TO NEXT BOT");
                SAE.get().getTour().getBotInTour().remove(bot);
                Cell cell = SAE.get().getMap().getGrid().getCell(bot.getPosY(), bot.getPosX());
                Platform.runLater(() -> cell.getStyleClass().remove("flash"));
            }
        });

        return arrow;
    }

    public static void actionMoving(Bot bot, String action) {
        Grid grid = SAE.get().getMap().getGrid();
        Cell cell = grid.getCell(bot.getPosY(), bot.getPosX());
        cell.setId(null);

        String[] actionParts = action.split("/");
        String axis = actionParts[0];
        String direction = actionParts[1];

        Cell newCell = move(bot, axis, direction, cell, grid);
        if(newCell == null) {
            System.out.println("CELL NOT EXIST!");
            return;
        }

        if((newCell.getElement() != null && !newCell.getElement().isCanMoveIn()) || (newCell.getId() != null && newCell.getId().equals("bot"))) {
            System.out.println("IMPOSSIBLE MOVING");
            return;
        }

        cell.setBackground(null);
        cell.setId(null);

        if(bot.getLastElement() != null) {
            cell.setBackground(new Background(new BackgroundImage(new Image("file:libs/img/" + bot.getLastElement().getImgLink(),64,64,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));
            bot.setLastElement(null);
        }
        if(newCell.getElement() != null)
            bot.setLastElement(newCell.getElement());

        newCell.setBackground(new Background(new BackgroundImage(new Image("file:libs/img/bot.png",64,64,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));
        newCell.setId("bot");
        bot.setPosY(newCell.getColumn());
        bot.setPosX(newCell.getRow());
    }

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

}
