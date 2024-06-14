module fr.uphf.sae201_202 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens fr.uphf.sae201_202 to javafx.fxml;
    exports fr.uphf.sae201_202;
    exports fr.uphf.sae201_202.maps;
    opens fr.uphf.sae201_202.maps to javafx.fxml;
    exports fr.uphf.sae201_202.maps.elements;
    opens fr.uphf.sae201_202.maps.elements to javafx.fxml;
    exports fr.uphf.sae201_202.game;
    opens fr.uphf.sae201_202.game to javafx.fxml;
    exports fr.uphf.sae201_202.game.tours;
    opens fr.uphf.sae201_202.game.tours to javafx.fxml;
}