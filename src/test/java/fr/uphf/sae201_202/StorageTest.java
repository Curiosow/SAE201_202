package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.elements.Ores;
import fr.uphf.sae201_202.maps.elements.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {

    private Storage storage;

    @BeforeEach
    public void setUp() {
        storage = new Storage(Ores.NICKEL);
    }

    @Test
    public void testConstructor() {
        assertNotNull(storage);
        assertEquals(10, storage.getPosX());
        assertEquals(20, storage.getPosY());
        assertTrue(storage.isCanMoveIn());
        assertEquals(100, storage.getAmount());
    }

    @Test
    public void testSetAndGetCapacity() {
        storage.setAmount(200);
        assertEquals(200, storage.getAmount());
    }

    @Test
    public void testGetImgLink() {
        assertEquals("storage.png", storage.getImgLink());
    }

    @Test
    public void testToString() {
        String expected = "Storage{id=0, posX=10, posY=20, canMoveIn=true, capacity=100}";
        assertEquals(expected, storage.toString());
    }
}
