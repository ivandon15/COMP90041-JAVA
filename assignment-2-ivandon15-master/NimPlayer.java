/////////////////////////////////////////////////////////
// Author : YiFan Deng
// StudentID: 1150027
// Username: Ivandon15
//
// Class Name: NimPlayer
// To hold the basic attributes of a Nim Player
/////////////////////////////////////////////////////////

import java.io.Serializable;

public abstract class NimPlayer implements Comparable<NimPlayer>, Serializable, Testable {

    // instance variables for recording
    private String userName;
    private String givenName;
    private String familyName;

    private int winTimes;
    private int totalTimes;
    private double ratio;

    // setters and getters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
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

    public double getRatio() {
        return (double) winTimes / totalTimes;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    /**
     * Method: compareTo(NimPlayer p)
     * Override method, compare two objects by their username
     *
     * @param p , the player
     * @return 0 if equals, -1 if current player's name is smaller
     */
    @Override
    public int compareTo(NimPlayer p) {
        if (this.userName.equals(p.getUserName())) {
            return 0;
        }
        return this.userName.compareTo(p.getUserName()) < 0 ? -1 : 1;
    }

    /**
     * Method: toString()
     * Override method, output the player's info
     *
     * @return the info
     */
    @Override
    public String toString() {
        return userName + ", " + givenName + ", " + familyName +
                ", " + totalTimes + " games" + ", " + winTimes + " wins";
    }

    /**
     * Three param Constructor for initialize players' names
     * player's name and the number of games easier
     *
     * @param userName   , the player's username
     * @param givenName  , the player's given name
     * @param familyName , the player's family name
     */
    public NimPlayer(String userName, String givenName, String familyName) {
        this.userName = userName;
        this.givenName = givenName;
        this.familyName = familyName;
        this.totalTimes = 0;
        this.winTimes = 0;
    }

    /**
     * Method: NimPlayer(NimPlayer p)
     * Copy constructor
     *
     * @param p , NimPlayer for copying
     */
    public NimPlayer(NimPlayer p) {
        this.userName = p.getUserName();
        this.givenName = p.getGivenName();
        this.familyName = p.getFamilyName();
        this.winTimes = p.getWinTimes();
        this.totalTimes = p.getTotalTimes();
        this.ratio = p.getRatio();
    }

    /**
     * Method: NimPlayer()
     * Empty Constructor
     */
    public NimPlayer() {
    }

    public void addTotal(){
        this.totalTimes++;
    }

    public void addWin(){
        this.winTimes++;
    }

    /**
     * Method: removeStone(int currentNum, int boundNum)
     * Called when player is removing stones
     *
     * @param currentNum , the current number of stone
     * @param boundNum   , the upper bound of stone removal
     * @return the number of stone the player removed
     */
    public abstract int removeStone(int currentNum, int boundNum);

    /**
     * Method: advancedMove(boolean[] available, String lastMove)
     * Called when player is removing stones in advanced game
     *
     * @param available , the stats of stones
     * @param lastMove , the last move of last player
     * @return the current move of the current player
     */
    public abstract String advancedMove(boolean[] available, String lastMove);
}
