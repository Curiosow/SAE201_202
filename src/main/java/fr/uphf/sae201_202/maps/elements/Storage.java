package fr.uphf.sae201_202.maps.elements;

import fr.uphf.sae201_202.SAE;

public class Storage extends Element {

    private final Ores ores;
    private int amount;

    public Storage(Ores ores) {
        this.ores = ores;
        this.amount = 0;
        this.id = SAE.get().getMap().getStorages().size();
    }

    @Override
    public String getImgLink() {
        return "storage.png";
    }

    public Ores getOres() {
        return ores;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Storage";
    }
}
