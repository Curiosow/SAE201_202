package fr.uphf.sae201_202.maps.elements;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ElementGUI extends Application {

    private final Element element;

    public ElementGUI(Element element) {
        this.element = element;
    }

    @Override
    public void start(Stage stage) {
        Group group = new Group();

        Label id = new Label("ID : " + element.getId());
        group.getChildren().add(id);

        if(element instanceof Mine) {
            Mine mine = (Mine) this.element;
            Label ore = new Label("Minerai : " + mine.getOres().getDisplay());
            ore.setLayoutY(20);
            Label ores = new Label("Quantité : " + mine.getOreLess());
            ores.setLayoutY(40);
            // Actualisateur automatique des textes toutes les secondes
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> ores.setText("Quantité : " + mine.getOreLess())), 5, 5, TimeUnit.SECONDS);
            group.getChildren().addAll(ore, ores);
        }

        if(element instanceof Storage) {
            Storage storage = (Storage) this.element;
            Label ore = new Label("Minerai : " + storage.getOres().getDisplay());
            ore.setLayoutY(20);
            Label ores = new Label("Quantité : " + storage.getAmount());
            ores.setLayoutY(40);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> ores.setText("Quantité : " + storage.getAmount())), 5, 5, TimeUnit.SECONDS);
            group.getChildren().addAll(ore, ores);
        }

        Scene scene = new Scene(group, 300, 300);
        stage.setTitle(this.element.toString());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
