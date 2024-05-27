package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.elements.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ElementTest {

    private Element element;

    @BeforeEach
    public void setUp() {
        element = new ConcreteElement(10, 20, true);
    }

    @Test
    public void testDefaultConstructor() {
        Element defaultElement = new ConcreteElement();
        assertEquals(0, defaultElement.getPosX());
        assertEquals(0, defaultElement.getPosY());
        assertTrue(defaultElement.isCanMoveIn());
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals(10, element.getPosX());
        assertEquals(20, element.getPosY());
        assertTrue(element.isCanMoveIn());
    }

    @Test
    public void testSettersAndGetters() {
        element.setPosX(30);
        element.setPosY(40);
        element.setCanMoveIn(false);

        assertEquals(30, element.getPosX());
        assertEquals(40, element.getPosY());
        assertFalse(element.isCanMoveIn());
    }

    @Test
    public void testGetImgLink() {
        assertEquals("dummy_link", element.getImgLink());
    }

    @Test
    public void testToString() {
        String expected = "fr.uphf.sae201_202.maps.elements.ConcreteElement@" + Integer.toHexString(element.hashCode());
        assertEquals(expected, element.toString());
    }
}
