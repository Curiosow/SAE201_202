package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.Cell;
import fr.uphf.sae201_202.maps.Map;
import fr.uphf.sae201_202.maps.elements.Ores;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SAE extends Application {

    private static SAE INSTANCE;

    private Map map;
    private List<Bot> bots;
    private Tour tour;

    @Override
    public void start(Stage stage) throws Exception {
        INSTANCE = this;
        map = new Map();

        Pane group = new Pane();
        FileInputStream fis_start = new FileInputStream("libs/img/start.png");
        Image img_start = new Image(fis_start, 256, 256, true, true);
        ImageView start = new ImageView(img_start);
        start.setY(30);
        start.setX(170);

        Background background = new Background(new BackgroundImage(new Image("file:libs/img/backgroundmine.png", 626,316,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT));

        group.setBackground(background);

        start.setOnMouseClicked(event -> {
            Stage gameStage = new Stage();
            gameStage.setResizable(false);
            gameStage.setOnCloseRequest(closeEvent -> System.exit(0));
            try {
                map.initMap(gameStage);

            } catch (MalformedURLException e) {
                System.out.println("CAN'T CREATE GAME.");
            }

            bots = new ArrayList<>();
            try {
                createBots();
            } catch (Exception e) {
                System.out.println("CAN'T CREATE BOTS.");
            }

            gameStage.show();
            tour = new Tour();
            stage.close();
        });

        group.getChildren().add(start);
        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.setHeight(313);
        stage.setWidth(626);
        stage.setResizable(false);
        stage.show();
    }

    private void createBots() throws Exception {
        System.out.println("--------- PLACING BOTS ---------");
        createBot(Ores.GOLD);
        createBot(Ores.NICKEL);
        int supBot = new Random().nextInt(4);
        for(int i = 0; i < supBot; i++) {
            List<Ores> ores = new ArrayList<>(List.of(Ores.values()));
            Collections.shuffle(ores);
            createBot(ores.getFirst());
        }
        System.out.println("--------------------------------");
    }

    private void createBot(Ores ores) throws Exception {
        Cell cell = Utils.getRandomCell(map.getGrid());
        Bot bot = new Bot(ores);
        cell.setBackground(new Background(new BackgroundImage(new Image("file:libs/img/bot.png",64,64,false,true),
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
          BackgroundSize.DEFAULT)));
        bots.add(bot);
        cell.setId("bot");
        bot.setPos(cell.getRow(), cell.getColumn());
        cell.setOnMouseClicked(event -> {
            try {
                bot.start(new Stage());
            } catch (Exception e) {
                System.out.println("THERE WAS A PROBLEM TO CREATE THE BOT GUI");
            }
        });
        System.out.println("PLACED A BOT OF " + cell.getRow() + "/" + cell.getColumn());
    }

    public static void main(String[] args) {
        launch();
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }
    public List<Bot> getBots() {
        return bots;
    }
    public static SAE get() {
        return INSTANCE;
    }
    public Tour getTour() {
        return tour;
    }
}