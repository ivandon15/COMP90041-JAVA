/////////////////////////////////////////////////////////
// Author : YiFan Deng
// StudentID: 1150027
// Username: Ivandon15
//
// Class Name: NimHumanPlayer
// To hold the basic attributes of a Nim Human Player,
// derived from NimPlayer
/////////////////////////////////////////////////////////

public class NimHumanPlayer extends NimPlayer {

    /**
     * Method: NimHumanPlayer()
     * Empty constructor
     */
    public NimHumanPlayer() {
        super();
    }

    /**
     * Method: NimHumanPlayer(String userName, String givenName, String familyName)
     * Three para constructor, call the parents class
     *
     * @param userName , player's username
     * @param givenName , player's given name
     * @param familyName , player's family name
     */
    public NimHumanPlayer(String userName, String givenName, String familyName) {
        super(userName, givenName, familyName);
    }

    /**
     * Method: NimHumanPlayer(NimHumanPlayer p)
     * Copy Constructor, but I didn't use it at last
     *
     * @param p
     */
    public NimHumanPlayer(NimHumanPlayer p) {
        super(p);
    }

    /**
     * Method: removeStone(int currentNum, int boundNum)
     * An override method, for removing stones in easy game
     *
     * @param currentNum , the current number of stone
     * @param boundNum   , the upper bound of stone removal
     * @return the number of stones removed
     */
    @Override
    public int removeStone(int currentNum, int boundNum) {

        System.out.println();
        // the number of stone each player wants to remove
        System.out.println(this.getGivenName() + "'s turn - remove how many?");
        int removeNum = Nimsys.sc.nextInt();

        // absorb the \n
        Nimsys.sc.nextLine();
        return removeNum;
    }

    /**
     * Method: advancedMove(boolean[] available, String lastMove)
     * An override method, for removing stones in advanced game
     *
     * @param available , the stats of stones
     * @param lastMove , the last move of last player
     * @return the current move of the current player
     */
    @Override
    public String advancedMove(boolean[] available, String lastMove) {
        System.out.println();
        System.out.println(this.getGivenName() + "'s turn - which to remove?\n");
        String currMove = Nimsys.sc.nextLine();
        return currMove;
    }
}
