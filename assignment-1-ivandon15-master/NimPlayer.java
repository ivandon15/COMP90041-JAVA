/////////////////////////////////////////////////////////
// Author : YiFan Deng
// StudentID: 1150027
// Username: Ivandon15
//
// Class Name: NimPlayer
// To hold the basic attributes of a Nim Player
/////////////////////////////////////////////////////////

public class NimPlayer {

    // instance variables for recording
    private String name;
    private int winTimes;
    private int totalTimes;

    // setters and getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWinTimes() {
        return winTimes;
    }

    public void setWinTimes(int winTimes) {
        this.winTimes = winTimes;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    /**
     * One param Constructor for initialize
     * player's name and the number of games easier
     * @param name , the player's name
     */
    public NimPlayer(String name) {
        this.name = name;
    }

    /**
     * Empty Constructor
     */
    public NimPlayer() {}

    /**
     * Called when player is removing stones
     * @param currentNum , the current number of stone
     * @param boundNum , the upper bound of stone removal
     * @return the number of stone the player removed
     */
    public int removeStone(int currentNum, int boundNum){

        // the number of stone each player wants to remove
        System.out.print(name+"'s turn. Enter stones to remove : ");
        int removeNum = Nimsys.sc.nextInt();

        // absorb the \n
        Nimsys.sc.nextLine();
        return removeNum;
    }
}
