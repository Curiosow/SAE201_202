package fr.uphf.sae201_202;

import fr.uphf.sae201_202.SAE;
import fr.uphf.sae201_202.maps.Map;
import fr.uphf.sae201_202.maps.elements.Mine;
import fr.uphf.sae201_202.maps.elements.Ores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MineTest {

    private Mine mine;
    private Ores ores;

    @BeforeEach
    public void setUp() {
        // Initialisation de l'objet Ores
        ores = Ores.GOLD;

        // Configuration manuelle de l'instance SAE et de la carte
        SAE sae = SAE.get();
        Map map = new Map();
        sae.setMap(map);

        // Initialisation de l'objet Mine
        mine = new Mine(ores);

        // Ajout de la mine à la carte
        sae.getMap().getMines().add(mine);
    }

    @Test
    public void testConstructor() {
        assertNotNull(mine);
        assertNotNull(mine.getOres());
        assertTrue(mine.getOreLess() >= 50 && mine.getOreLess() <= 100);
        assertEquals("mine.png", mine.getImgLink());
        assertEquals("Mine", mine.toString());
    }

    @Test
    public void testSetAndGetOres() {
        assertEquals(ores, mine.getOres());
    }

    @Test
    public void testSetAndGetOreLess() {
        mine.setOreLess(75);
        assertEquals(75, mine.getOreLess());
    }

    @Test
    public void testSetRandomAmount() {
        mine.setRandomAmount();
        int oreLess = mine.getOreLess();
        assertTrue(oreLess >= 50 && oreLess <= 99);
    }

    @Test
    public void testIdAssignment() {
        assertEquals(0, mine.getId());  // Assuming this is the first Mine created
    }
}
