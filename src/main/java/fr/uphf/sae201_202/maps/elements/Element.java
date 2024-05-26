package fr.uphf.sae201_202.maps.elements;

public abstract class Element {

    protected int id;
    private int posX;
    private int posY;
    private boolean canMoveIn;

    public Element() {
        this(0, 0, true);
    }

    public Element(int posX, int posY, boolean canMoveIn) {
        this.posX = posX;
        this.posY = posY;
        this.canMoveIn = canMoveIn;
    }

    public String getImgLink() {
        return null;
    }

    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public boolean isCanMoveIn() {
        return canMoveIn;
    }
    public int getId() {
        return id;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }
    public void setCanMoveIn(boolean canMoveIn) {
        this.canMoveIn = canMoveIn;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
