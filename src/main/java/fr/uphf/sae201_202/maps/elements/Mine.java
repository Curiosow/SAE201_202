package fr.uphf.sae201_202.maps.elements;

import fr.uphf.sae201_202.SAE;

import java.util.Random;

public class Mine extends Element {

    // nombre de minerais restants dans la mine
    private int oreLess;
    private final Ores ores;

    public Mine(Ores ores) {
        this.oreLess = new Random().nextInt(51) + 50;
        this.ores = ores;
        this.id = SAE.get().getMap().getMines().size();
    }

    @Override
    public String getImgLink() {
        // image de référence à la mine
        return "mine.png";
    }

    public Ores getOres() {
        return ores;
    }
    public int getOreLess() {
        return oreLess;
    }

    public void setOreLess(int oreLess) {
        this.oreLess = oreLess;
    }
    public void setRandomAmount() {
        // définition du nombre de minerais entre 50 et 100
        Random random = new Random();
        int amount = random.nextInt(50);
        oreLess = amount + 50;
    }

    @Override
    public String toString() {
        return "Mine";
    }
}
