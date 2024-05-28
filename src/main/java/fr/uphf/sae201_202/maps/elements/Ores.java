package fr.uphf.sae201_202.maps.elements;

public enum Ores {

    // énumération avec la liste des minerais et leur texte d'affichage
    NICKEL("Nickel"),
    GOLD("Or"),
    ;

    private final String display;

    Ores(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
