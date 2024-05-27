package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.Map;
import fr.uphf.sae201_202.maps.elements.Mine;
import fr.uphf.sae201_202.maps.elements.Ores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {

    private Map map;
    private Mine mine;

    @BeforeEach
    public void setUp() {
        map = new Map();
        mine = new Mine(Ores.GOLD);
    }

    @Test
    public void testConstructor() {
        assertNotNull(map);
        assertNotNull(map.getMines());
        assertTrue(map.getMines().isEmpty());
    }

    @Test
    public void testAddMine() {
        map.getMines().add(mine);
        assertEquals(1, map.getMines().size());
        assertTrue(map.getMines().contains(mine));
    }
}
