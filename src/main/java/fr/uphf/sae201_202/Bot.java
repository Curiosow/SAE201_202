package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.elements.Element;
import fr.uphf.sae201_202.maps.elements.Mine;
import fr.uphf.sae201_202.maps.elements.Ores;
import fr.uphf.sae201_202.maps.elements.Storage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot extends Application {

    private final int id;
    private int posX;
    private int posY;
    private Element lastElement;

    private Button actionButton = null;

    private final int storageMax;
    private final Ores ores;
    private int harvest;

    public Bot(Ores ores) {
        this(0, 0, ores, ((new Random().nextInt(5)) + 5), 0);
    }

    public Bot(int posX, int posY, Ores ores, int storageMax, int harvest) {
        this.ores = ores;
        this.storageMax = storageMax;
        this.harvest = harvest;
        this.posX = posX;
        this.posY = posY;
        this.lastElement = null;
        this.id = SAE.get().getBots().size();
    }

    public int getId() {
        return id;
    }
    public int getStorageMax() {
        return storageMax;
    }
    public Ores getOres() {
        return ores;
    }
    public int getHarvest() {
        return harvest;
    }
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public Element getLastElement() {
        return lastElement;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }
    public void setPos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
    public void setHarvest(int harvest) {
        this.harvest = harvest;
    }
    public void setLastElement(Element lastElement) {
        this.lastElement = lastElement;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group group = new Group();

        Label idLabel = new Label("ID : " + id);
        Label oreLabel = new Label("Minerai : " + ores.getDisplay());
        oreLabel.setLayoutY(20);
        Label harvestLabel = new Label("Stockage : " + harvest + "/" + storageMax);
        harvestLabel.setLayoutY(40);

        Label canMine = new Label("Peut miner : ");
        canMine.setLayoutY(300);

        Label canStore = new Label("Peut ranger : ");
        canStore.setLayoutY(320);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> harvestLabel.setText("Stockage : " + harvest + "/" + storageMax)), 5, 5, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {
            boolean mining = lastElement != null && lastElement instanceof Mine;
            canMine.setText("Peut miner : " + (mining ? "Oui" : "Non"));

            boolean storing = lastElement != null && lastElement instanceof Storage;
            canStore.setText("Peut ranger : " + (storing ? "Oui" : "Non"));

            group.getChildren().remove(actionButton);
            actionButton = null;
            if(mining) {
                 actionButton = new Button("Miner les ressources");
                 actionButton.setOnMouseClicked(event -> Utils.elementRunning(this, SAE.get().getMap().getGrid().getCell(posY, posX)));
            }
            if(storing) {
                 actionButton = new Button("Ranger vos ressources");
                 actionButton.setOnMouseClicked(event -> Utils.elementRunning(this, SAE.get().getMap().getGrid().getCell(posY, posX)));
            }

            if(actionButton != null) {
                actionButton.setLayoutY(350);
                group.getChildren().add(actionButton);
            }
        }), 1, 1, TimeUnit.SECONDS);

        group.getChildren().addAll(idLabel, oreLabel, harvestLabel,
                Utils.createArrow(this, false, "arrow-left.png", "Y/-", 125, 20),
                Utils.createArrow(this, false, "arrow-right.png", "Y/+", 120, 175),
                Utils.createArrow(this, false, "arrow-up.png", "X/-", 50, 100),
                Utils.createArrow(this, false, "arrow-down.png", "X/+", 200, 100),
                canMine, canStore);
        Scene scene = new Scene(group, 300, 400);
        stage.setTitle("Bot");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
