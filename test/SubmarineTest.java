import edu.colorado.caterpillars.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class SubmarineTest {
    private Ship ship;
    @BeforeEach
    public void createShip(){
        ship = new Submarine();
    }

    @Test
    public void testData(){
        assertEquals("Submarine", ship.getName());
        assertEquals(4, ship.getDimension()[1]);
        assertEquals(2, ship.getDimension()[0]);
        assertTrue(ship.canSubmerge());
    }
}