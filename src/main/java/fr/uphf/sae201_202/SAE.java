package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.Cell;
import fr.uphf.sae201_202.maps.Map;
import fr.uphf.sae201_202.maps.elements.Ores;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SAE extends Application {

    // Permet de récupérer l'instance du main (SAE) qui va nous servir dans les différentes classes
    private static SAE INSTANCE;

    private Map map;
    private List<Bot> bots;
    private Tour tour;

    @Override
    public void start(Stage stage) throws Exception {
        INSTANCE = this;
        map = new Map();

        Pane group = new Pane();

        // Bouton de démarrage du jeu
        InputStream is_start = getClass().getResourceAsStream("/img/Start.png");
        if(is_start == null) {
            System.out.println("ERROR IN LOADING START BUTTON IMAGE");
            System.exit(0);
            return;
        }
        Image img_start = new Image(is_start, 256, 256, true, true);
        ImageView start = new ImageView(img_start);
        start.setY(30);
        start.setX(170);

        // Fond d'écran de l'écran de démarrage
        InputStream bgFile = getClass().getResourceAsStream("/img/backgroundmine.png");
        if(bgFile == null) {
            System.out.println("ERROR IN LOADING BACKGROUND IMAGE");
            System.exit(0);
            return;
        }
        Background background = new Background(new BackgroundImage(new Image(bgFile, 626,316,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT));

        group.setBackground(background);

        // Définit l'initilisation de la carte, la création des robots et le début de la partie
        start.setOnMouseClicked(event -> {
            Stage gameStage = new Stage();
            // Fait en sorte qu'on ne puisse pas redimensionner la fenêtre pour éviter d'avoir du vide
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

        // Définition de l'image du robot
        InputStream file = SAE.get().getClass().getResourceAsStream("/img/bot.png");
        if(file == null) {
            System.out.println("ERROR IN LOADING BOT IMAGE");
            System.exit(0);
            return;
        }
        cell.setBackground(new Background(new BackgroundImage(new Image(file,64,64,false,true),
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
          BackgroundSize.DEFAULT)));
        bots.add(bot);

        // L'id de la cellule permet d'identifier celle-ci comme un robot
        cell.setId("bot");
        bot.setPos(cell.getRow(), cell.getColumn());
        cell.setOnMouseClicked(event -> {
            try {
                // Démarre l'interface de contrôle du robot en mode manuel
                bot.start(new Stage());
            } catch (Exception e) {
                System.out.println("THERE WAS A PROBLEM TO CREATE THE BOT GUI");
                e.printStackTrace();
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