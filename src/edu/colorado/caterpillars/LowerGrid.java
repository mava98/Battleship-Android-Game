package edu.colorado.caterpillars;

public class LowerGrid extends Grid{

    private Fleet fleet = new Fleet();

    public Fleet getFleet(){
        return fleet;
    }

    public void addShip(Ship ship, int row, int col, String direction){
        if(isInvalidPlacement(ship, row, col, direction)) // checks for overlap, out of bounds, and invalid direction
            throw new IllegalArgumentException("Ships cannot overlap!");
        fleet.addShip(ship);
        int id = ship.getID();
        int cid = ship.getCID();
        for(int i = 0; i < ship.getSize(); i++){
            if (direction == "N"){
                if(i == ship.getcIndex()){
                    grid[row--][col] = cid;
                }else {
                    grid[row--][col] = id;
                }
            }
            else if (direction == "S"){ // South means increasing row num (e.g A1 then A2)
                if(i == ship.getcIndex()){
                    grid[row++][col] = cid;
                }else {
                    grid[row++][col] = id;
                }
            }
            else if (direction == "E"){
                if(i == ship.getcIndex()){
                    grid[row][col++] = cid;
                }else {
                    grid[row][col++] = id;
                }
            }
            else if (direction == "W"){
                if(i == ship.getcIndex()){
                    grid[row][col--] = cid;
                }else {
                    grid[row][col--] = id;
                }
            }
        }

    }

    private boolean isInvalidPlacement(Ship ship, int row, int col, String direction){
        for(int i = 0; i < ship.getSize(); i++){
            if (direction == "N"){
                if(grid[row--][col] != 0)
                    return true;
            }
            else if (direction == "S"){ // South means increasing row num (e.g A1 then A2)
                if(grid[row++][col] != 0)
                    return true;
            }
            else if (direction == "E"){
                if(grid[row][col++] != 0)
                    return true;
            }
            else if (direction == "W"){
                if(grid[row][col--] != 0)
                    return true;
            }
            else{
                throw new IllegalArgumentException("Not a valid direction!");
            }
        }
        return false;
    }

    public String receiveAttack(int row, int col){
        BasicAttack basic = new BasicAttack(this);
        return basic.use(row, col);
    }
    public String receiveAttack(Weapon weapon, int row, int col){
        return weapon.use(row, col);

//        if(grid[row][col] > 0){
//            int id = grid[row][col];
//            String result = fleet.hitShipById(id);
//            if(!result.equals("MISS")){
//                grid[row][col] = -id; // -ship.id represents a ship coord that has been hit (should be <= -2)
//            }
//            if(result.contains("SUNK") || result.equals("SURRENDER")){
//                Ship ship = fleet.getShipById(id);
//                int sid = ship.getID();
////                int cid = ship.getCID();
//                int h = grid.length;
//                int w = grid[0].length;
//                for(int i = 0; i < h; i++){
//                    for(int j = 0; j < w; j++){
//                        if(grid[i][j] == sid) {
//                            grid[i][j] = -sid;
//                        }
////                        else if(grid[i][j] == cid){
////                            grid[i][j] = -cid;
////                        }
//                    }
//                }
//
//            }
//            return result;
//        }
//        else{
//            grid[row][col] = -1; // -1 should indicate miss
//            return "MISS";
//        }
    }

}
