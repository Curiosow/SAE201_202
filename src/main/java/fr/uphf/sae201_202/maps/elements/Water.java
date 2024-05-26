package fr.uphf.sae201_202.maps.elements;

import fr.uphf.sae201_202.SAE;

public class Water extends Element {

    public Water() {
        setCanMoveIn(false);
        this.id = SAE.get().getMap().getWaters().size();
    }

    @Override
    public String getImgLink() {
        return "water.png";
    }

    @Override
    public String toString() {
        return "Water";
    }
}
