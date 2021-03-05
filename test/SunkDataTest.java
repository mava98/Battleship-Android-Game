import edu.colorado.caterpillars.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class SunkDataTest {

    private SunkData observable;

    //Temporary class to mock an observer to use in tests
    static class Observer implements PropertyChangeListener {
        public int numSunk = 0;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            numSunk = (int) evt.getNewValue();
        }
    }

    @BeforeEach
    public void createObservable(){
        observable = new SunkData();

    }

    @Test
    public void testAddingAndRemovingListener(){
        Observer observer = new Observer();
        observable.addListener(observer);
        assertEquals(0, observer.numSunk);
        observable.setNumSunk(1);
        assertEquals(1, observer.numSunk);
        observable.removeListener(observer);
        observable.setNumSunk(2); // unsubscribed, should not get new value
        assertEquals(1, observer.numSunk);
    }

    @Test
    public void testCheckForUpdate(){
        LowerGrid grid = new LowerGrid();
        Ship ship1 = new Minesweeper();
        Ship ship2 = new Destroyer();
        grid.addShip(ship1, 0, 0, "E");
        grid.addShip(ship2, 1, 0, "S");
        observable.setFleet(grid.getFleet());
        Observer observer = new Observer();
        observable.addListener(observer);

        assertEquals(0, observer.numSunk);
        grid.receiveAttack(1, 0); //hit, no sunk
        observable.checkForUpdates(); // This should be called after every turn/move
        assertEquals(0, observer.numSunk);
        grid.receiveAttack(0, 0); // hit and sunk
        observable.checkForUpdates();
        assertEquals(1, observer.numSunk);
        grid.receiveAttack(2, 0); //"miss" due to armor
        observable.checkForUpdates();
        grid.receiveAttack(2, 0); // hit and sunk on CQ
        observable.checkForUpdates();
        assertEquals(2, observer.numSunk);

    }
}
