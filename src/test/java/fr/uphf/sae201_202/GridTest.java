package fr.uphf.sae201_202;

import fr.uphf.sae201_202.maps.Cell;
import fr.uphf.sae201_202.maps.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {

    private Grid grid;

    @BeforeEach
    public void setUp() {
        grid = new Grid(5, 5,10,10);
    }

    @Test
    public void testConstructor() {
        assertNotNull(grid);
        assertEquals(5, grid.getWidth());
        assertEquals(5, grid.getHeight());
        assertNotNull(grid.getAllCells());
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grid{width=5, height=5, cells=");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                sb.append("null,");
            }
        }
        sb.append("}");
        String expected = sb.toString();
        assertEquals(expected, grid.toString());
    }
}
