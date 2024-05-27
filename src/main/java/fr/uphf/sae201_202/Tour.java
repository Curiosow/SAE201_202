package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.Cell;
import fr.uphf.sae201_202.maps.elements.Mine;
import fr.uphf.sae201_202.maps.elements.Storage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Tour extends Application {

    private int nbrTour;
    private boolean inTour;
    private List<Bot> botInTour;
    private boolean hasDoneAction;

    private Button actionButton = null;

    public Tour() {
        this.nbrTour = 0;
        this.hasDoneAction = false;
        this.inTour = false;
        this.botInTour = null;
        this.start(new Stage());
        System.out.println("CREATED TOUR SYSTEM");
    }

    private void makeNewTour() {
        this.nbrTour++;
        this.botInTour = new ArrayList<>(SAE.get().getBots());
        this.inTour = true;
        System.out.println("MAKING A NEW TOUR");
    }

    @Override
    public void start(Stage stage) {
        stage.setOnCloseRequest(event -> {
            this.start(new Stage());
        });

        Group group = new Group();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {
            group.getChildren().clear();
            Label tourLabel = new Label("Tour n° " + nbrTour);
            tourLabel.setLayoutY(380);
            group.getChildren().add(tourLabel);
            hasDoneAction = false;

            if(!inTour) {
                Label noTourLabel = new Label("Aucun tour en cours.");
                Button createATour = new Button("Faire un tour");
                createATour.setLayoutY(20);
                createATour.setOnMouseClicked(event -> makeNewTour());
                group.getChildren().addAll(noTourLabel, createATour);
            } else {
                if(botInTour.isEmpty()) {
                    inTour = false;
                    System.out.println("TOUR FINISH");
                    for(Cell cell : SAE.get().getMap().getGrid().getAllCells()) {
                        cell.getStyleClass().remove("flash");
                    }
                    return;
                }
                Bot bot = botInTour.getFirst();
                Cell cell = SAE.get().getMap().getGrid().getCell(bot.getPosY(), bot.getPosX());
                if(cell.getStyleClass().contains("flash"))
                    cell.getStyleClass().remove("flash");
                else
                    cell.getStyleClass().add("flash");

                Label idLabel = new Label("ID : " + bot.getId());
                Label oreLabel = new Label("Minerai : " + bot.getOres().getDisplay());
                oreLabel.setLayoutY(20);
                Label harvestLabel = new Label("Stockage : " + bot.getHarvest() + "/" + bot.getStorageMax());
                harvestLabel.setLayoutY(40);

                boolean mining = bot.getLastElement() != null && bot.getLastElement() instanceof Mine;
                Label canMine = new Label("Peut miner : " + (mining ? "Oui" : "Non"));
                canMine.setLayoutY(300);

                boolean storing = bot.getLastElement() != null && bot.getLastElement() instanceof Storage;
                Label canStore = new Label("Peut ranger : " + (storing ? "Oui" : "Non"));
                canStore.setLayoutY(320);

                group.getChildren().remove(actionButton);
                actionButton = null;
                if(mining) {
                    actionButton = new Button("Miner les ressources");
                    actionButton.setOnMouseClicked(event -> {
                        if(hasDoneAction)
                            return;

                        Utils.elementRunning(bot, SAE.get().getMap().getGrid().getCell(bot.getPosY(), bot.getPosX()));
                        System.out.println("ACTION DONE : MINED\nGO TO NEXT BOT");
                        botInTour.remove(bot);
                        hasDoneAction = true;
                        new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    Platform.runLater(() -> cell.getStyleClass().remove("flash"));
                                }
                            },
                            TimeUnit.SECONDS.toMillis(1)
                        );
                    });
                }
                if(storing) {
                    actionButton = new Button("Ranger vos ressources");
                    actionButton.setOnMouseClicked(event -> {
                        if(hasDoneAction)
                            return;

                        Utils.elementRunning(bot, SAE.get().getMap().getGrid().getCell(bot.getPosY(), bot.getPosX()));
                        System.out.println("ACTION DONE : STORED\nGO TO NEXT BOT");
                        botInTour.remove(bot);
                        hasDoneAction = true;
                        new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    Platform.runLater(() -> cell.getStyleClass().remove("flash"));
                                }
                            },
                            TimeUnit.SECONDS.toMillis(1)
                        );
                    });
                }

                if(actionButton != null) {
                    actionButton.setLayoutY(350);
                    group.getChildren().add(actionButton);
                }

                try {
                    group.getChildren().addAll(idLabel, oreLabel, harvestLabel,
                            Utils.createArrow(bot, true, "arrow-left.png", "Y/-", 125, 20),
                            Utils.createArrow(bot, true, "arrow-right.png", "Y/+", 120, 175),
                            Utils.createArrow(bot, true, "arrow-up.png", "X/-", 50, 100),
                            Utils.createArrow(bot, true, "arrow-down.png", "X/+", 200, 100),
                            canMine, canStore
                    );
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }), 1, 1, TimeUnit.SECONDS);

        Scene scene = new Scene(group, 300, 400);
        stage.setTitle("Tour");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public int getNbrTour() {
        return nbrTour;
    }
    public boolean isInTour() {
        return inTour;
    }
    public List<Bot> getBotInTour() {
        return botInTour;
    }
    public void setHasDoneAction(boolean hasDoneAction) {
        this.hasDoneAction = hasDoneAction;
    }
    public boolean isHasDoneAction() {
        return hasDoneAction;
    }
}
