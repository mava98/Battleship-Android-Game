package edu.colorado.caterpillars;

import java.util.Stack;

public class LowerGrid extends Grid {

    private Fleet fleet = new Fleet();
    private int[][] submergedGrid = new int[10][10];
    private Stack<int [][]> undoStack = new Stack<>();
    private Stack<int [][]> undoSubStack = new Stack<>();

    public Fleet getFleet() {
        return fleet;
    }

    public int[][] getSubmergedGrid() {
        return submergedGrid;
    }

    public void addShip(Ship ship, int row, int col, String direction, boolean submerge) {
        int[][] g;
        if (submerge) {
            if (!ship.canSubmerge()) {
                throw new IllegalArgumentException("This ship cannot be submerged!");
            } else {
                g = submergedGrid;
            }

        } else {
            g = grid;
        }
        if (isInvalidPlacement(ship, row, col, direction, g)) // checks for overlap, out of bounds, and invalid direction
            throw new IllegalArgumentException("Ships cannot overlap or hang off of grid!");
        fleet.addShip(ship);
        int id = ship.getID();
        int cid = ship.getCID();
        for (int i = 0; i < ship.getDimension()[0]; i++) {
            for (int j = 0; j < ship.getDimension()[1]; j++) {
                if (direction == "N") {
                    if (ship.getShape()[i][j] == 2) {
                        g[row - j][col + i] = cid;
                    } else if (ship.getShape()[i][j] == 1) {
                        g[row - j][col + i] = id;
                    }
                } else if (direction == "S") { // South means increasing row num (e.g A1 then A2)
                    if (ship.getShape()[i][j] == 2) {
                        g[row + j][col - i] = cid;
                    } else if (ship.getShape()[i][j] == 1) {
                        g[row + j][col - i] = id;
                    }
                } else if (direction == "E") {
                    if (ship.getShape()[i][j] == 2) {
                        g[row + i][col + j] = cid;
                    } else if (ship.getShape()[i][j] == 1) {
                        g[row + i][col + j] = id;
                    }
                } else if (direction == "W") {
                    if (ship.getShape()[i][j] == 2) {
                        g[row - i][col - j] = cid;
                    } else if (ship.getShape()[i][j] == 1) {
                        g[row - i][col - j] = id;
                    }
                }
            }
        }
    }

    private boolean isInvalidPlacement(Ship ship, int row, int col, String direction, int[][] g) {
        for (int i = 0; i < ship.getDimension()[0]; i++) {
            for (int j = 0; j < ship.getDimension()[1]; j++) {
                if (direction == "N") {
                    if (row - j >= 10 || row - j < 0 || col + i >= 10 ||
                            col + i < 0 || g[row - j][col + i] != 0)
                        return true;
                } else if (direction == "S") { // South means increasing row num (e.g A1 then A2)
                    if (row + j >= 10 || row + j < 0 || col - i >= 10 ||
                            col - i < 0 || g[row + j][col - i] != 0)
                        return true;
                } else if (direction == "E") {
                    if (row + i >= 10 || row + i < 0 || col + j >= 10 ||
                            col + j < 0 || g[row + i][col + j] != 0)
                        return true;
                } else if (direction == "W") {
                    if (row - i >= 10 || row - i < 0 || col - j >= 10 ||
                            col - j < 0 || g[row - i][col - j] != 0)
                        return true;
                } else {
                    throw new IllegalArgumentException("Not a valid direction!");
                }
            }
        }
        return false;
    }

    public String receiveAttack(int row, int col) {
        BasicAttack basic = new BasicAttack(this);
        return basic.use(row, col);
    }

    public String receiveAttack(Weapon weapon, int row, int col) {
        return weapon.use(row, col);
    }

    public void undoReceiveAttack(Weapon weapon, int row, int col) {
        weapon.undoUse(row, col);
    }

    public void moveFleet(String dir) {
        int[][] lowerGrid = getGrid();
        int[][] subGrid = getSubmergedGrid();
        int[][] newGrid = new int[10][10];
        int[][] newSubGrid = new int[10][10];

        if (dir == "W") { //a = row b = col
            for (int a = 0; a < 10; a++) {
                if (lowerGrid[a][0] > 0 || lowerGrid[a][0] <= -2 ||  subGrid[a][0] > 0 || subGrid[a][0] <= -2)
                    throw new IllegalArgumentException("Fleet cannot be moved that direction, ship located at North edge.");
            }
            for (int a = 0; a < 10; a++) {
                for (int b = 1; b < 10; b++) {
                    newGrid[a][b - 1] = lowerGrid[a][b];
                    newSubGrid[a][b - 1] = subGrid[a][b];
                }
            }
        } else if (dir == "E") {
            for (int a = 0; a < 10; a++) {
                if (lowerGrid[a][9] > 0 || lowerGrid[a][9] <= -2 || subGrid[a][9] > 0 || subGrid[a][9] <= -2)
                    throw new IllegalArgumentException("Fleet cannot be moved that direction, ship located at East edge.");
            }
            for (int a = 0; a < 10; a++) {
                for (int b = 0; b < 9; b++) {
                    newGrid[a][b + 1] = lowerGrid[a][b];
                    newSubGrid[a][b + 1] = subGrid[a][b];
                }
            }
        } else if (dir == "S") {
            for (int b = 0; b < 10; b++) {
                if (lowerGrid[9][b] > 0 || lowerGrid[9][b] <= -2 || subGrid[9][b] > 0 || subGrid[9][b] <= -2)
                    throw new IllegalArgumentException("Fleet cannot be moved that direction, ship located at South edge.");
            }
            for (int a = 0; a < 9; a++) {
                for (int b = 0; b < 10; b++) {
                    newGrid[a + 1][b] = lowerGrid[a][b];
                    newSubGrid[a + 1][b] = subGrid[a][b];
                }
            }
        } else if (dir == "N") {
            for (int b = 0; b < 10; b++) {
                if (lowerGrid[0][b] > 0 || lowerGrid[0][b] <= -2 || subGrid[0][b] > 0 || subGrid[0][b] <= -2)
                    throw new IllegalArgumentException("Fleet cannot be moved that direction, ship located at North edge.");
            }
            for (int a = 1; a < 10; a++) {
                for (int b = 0; b < 10; b++) {
                    newGrid[a - 1][b] = lowerGrid[a][b];
                    newSubGrid[a - 1][b] = subGrid[a][b];
                }
            }
        } else {
            throw new IllegalArgumentException("Not a valid direction!");
        }
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                lowerGrid[x][y] = newGrid[x][y];
                subGrid[x][y] = newSubGrid[x][y];
            }
        }
    }

    public void addGridsToHistory(){
        int [][] oldGrid = new int [10][10];
        int [][] oldSubGrid = new int [10][10];
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                oldGrid[i][j] = grid[i][j];
                oldSubGrid[i][j] = submergedGrid[i][j];
            }
        }
        undoStack.push(oldGrid);
        undoSubStack.push(oldSubGrid);
    }

    public void undoGrids(){
        grid = undoStack.pop();
        submergedGrid = undoSubStack.pop();
    }

    public void undoAddShip(){
        undoGrids();
        fleet.undoAddShip();
    }
}
