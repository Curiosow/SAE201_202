package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.elements.Element;

public class ConcreteElement extends Element {
    public ConcreteElement() {
        super();
    }

    public ConcreteElement(int posX, int posY, boolean canMoveIn) {
        super(posX, posY, canMoveIn);
    }

    @Override
    public String getImgLink() {
        return "dummy_link";
    }
}
