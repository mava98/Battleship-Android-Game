import edu.colorado.caterpillars.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void createObjects(){
        game = new Game();
        player1 = game.getPlayer(1);
        player2 = game.getPlayer(2);
        Ship mine1 = new Minesweeper();
        Ship mine2 = new Minesweeper();
        game.addShip(mine1,0, 0, "E", false);
        game.endTurn();
        game.addShip(mine2,0, 0, "E", false);
        game.endTurn();
    }

    @Test
    public void invalidPlayer(){
        assertThrows(Exception.class, () -> { game.getPlayer(3); });
    }

    @Test
    public void testEndGame(){
        assertTrue(game.isRunning());
        game.attack(0, 0);
        assertFalse(game.isRunning());
    }

    @Test
    public void testTurnChange(){
        assertSame(player1, game.getActivePlayer());
        assertSame(player2, game.getWaitingPlayer());
        game.endTurn();
        assertSame(player2, game.getActivePlayer());
        assertSame(player1, game.getWaitingPlayer());
    }

    @Test
    public void testMovesThenUndos(){
        int [][] testGridStart = new int[10][10];
        testGridStart[0][0] = 3;
        testGridStart[0][1] = 2;
        assertArrayEquals(testGridStart, game.getActivePlayer().getLower().getGrid());

        game.move("E");
        int [][] testGridE = new int[10][10];
        testGridE[0][1] = 3;
        testGridE[0][2] = 2;
        assertArrayEquals(testGridE, game.getActivePlayer().getLower().getGrid());

        game.move("S");
        int [][] testGridS = new int[10][10];
        testGridS[1][1] = 3;
        testGridS[1][2] = 2;
        assertArrayEquals(testGridS, game.getActivePlayer().getLower().getGrid());

        game.move("E");
        int [][] testGridESE = new int[10][10];
        testGridESE[1][2] = 3;
        testGridESE[1][3] = 2;
        assertArrayEquals(testGridESE, game.getActivePlayer().getLower().getGrid());

        game.undo();
        assertArrayEquals(testGridS, game.getActivePlayer().getLower().getGrid());

        game.undo();
        assertArrayEquals(testGridE, game.getActivePlayer().getLower().getGrid());

        game.undo();
        assertArrayEquals(testGridStart, game.getActivePlayer().getLower().getGrid());
    }

    @Test
    public void testMovesThenUndosThenRedos(){
        int [][] testGridStart = new int[10][10];
        testGridStart[0][0] = 3;
        testGridStart[0][1] = 2;
        assertArrayEquals(testGridStart, game.getActivePlayer().getLower().getGrid());

        game.move("E");
        int [][] testGridE = new int[10][10];
        testGridE[0][1] = 3;
        testGridE[0][2] = 2;
        assertArrayEquals(testGridE, game.getActivePlayer().getLower().getGrid());

        game.move("S");
        int [][] testGridS = new int[10][10];
        testGridS[1][1] = 3;
        testGridS[1][2] = 2;
        assertArrayEquals(testGridS, game.getActivePlayer().getLower().getGrid());

        game.move("E");
        int [][] testGridESE = new int[10][10];
        testGridESE[1][2] = 3;
        testGridESE[1][3] = 2;
        assertArrayEquals(testGridESE, game.getActivePlayer().getLower().getGrid());

        game.undo();
        assertArrayEquals(testGridS, game.getActivePlayer().getLower().getGrid());

        game.undo();
        assertArrayEquals(testGridE, game.getActivePlayer().getLower().getGrid());

        game.undo();
        assertArrayEquals(testGridStart, game.getActivePlayer().getLower().getGrid());

        game.redo();
        assertArrayEquals(testGridE, game.getActivePlayer().getLower().getGrid());

        game.redo();
        assertArrayEquals(testGridS, game.getActivePlayer().getLower().getGrid());

        game.redo();
        assertArrayEquals(testGridESE, game.getActivePlayer().getLower().getGrid());


    }

    @Test void testAddShip(){
        Ship ship = new Battleship();
        game.addShip(ship,5,5,"E",false);
        Ship ship1 = game.getActivePlayer().getLower().getFleet().getShipById(4);
        assertEquals(ship,ship1);
    }

    @Test void undoMoveAndSwapAndAddShip(){
        int [][] testGridStart = new int[10][10];
        testGridStart[0][0] = 3;
        testGridStart[0][1] = 2;
        game.move("E");
        int [][] testGridE = new int[10][10];
        testGridE[0][1] = 3;
        testGridE[0][2] = 2;
        assertArrayEquals(testGridE, game.getActivePlayer().getLower().getGrid());

        game.undo(); // move
        assertArrayEquals(testGridStart, game.getActivePlayer().getLower().getGrid());

        assertEquals(player1,game.getActivePlayer());
        game.undo(); // swap players
        assertEquals(player2,game.getActivePlayer());

        game.undo(); // add ship
        assertEquals(0,player2.getLower().getFleet().getTotalNumShips());
        game.redo();
        assertEquals(1,player2.getLower().getFleet().getTotalNumShips());
        game.redo();
        assertEquals(player1,game.getActivePlayer());
        game.redo();
        assertArrayEquals(testGridE, game.getActivePlayer().getLower().getGrid());

    }

    @Test
    public void testInvalidRedo(){
        game.move("E");
        game.undo();
        game.move("E");
        assertThrows(Exception.class, () ->game.redo());

    }


    @Test
    public void testInvalidUndo() {
        game.undo();
        game.undo();
        game.undo();
        game.undo();
        assertThrows(Exception.class, () -> game.undo());
    }



}
