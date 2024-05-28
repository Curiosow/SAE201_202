package fr.uphf.sae201_202.maps.elements;

import fr.uphf.sae201_202.SAE;

public class Water extends Element {

    public Water() {
        setCanMoveIn(false);
        /*
        pour avoir un id qui fait +1 à chaque nouveau élement,
        on récupère la taille de la liste et ça devient son id
         */
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
