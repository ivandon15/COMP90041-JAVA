/////////////////////////////////////////////////////////
// Author : YiFan Deng
// StudentID: 1150027
// Username: Ivandon15
//
// Class Name: NimGame
// The engine of the game, for starting game
/////////////////////////////////////////////////////////

import javax.naming.LimitExceededException;

public class NimGame {

    // basic info in one game
    private int currNum;
    private int bound;
    private NimPlayer player1, player2;

    /**
     * Method: NimGame()
     * Empty Constructor
     */
    public NimGame() {
    }

    /**
     * Method: NimGame(int currNum, int bound, NimPlayer player1, NimPlayer player2)
     * Initial the game
     *
     * @param currNum , initial number of stones
     * @param bound , the upper bound of this game
     * @param player1 , player1
     * @param player2 , player2
     */
    public NimGame(int currNum, int bound, NimPlayer player1, NimPlayer player2) {
        this.currNum = currNum;
        this.bound = bound;
        this.player1 = player1;
        this.player2 = player2;
    }

    // getters and setters
    public int getCurrNum() {
        return currNum;
    }

    public void setCurrNum(int currNum) {
        this.currNum = currNum;
    }

    public int getBound() {
        return bound;
    }

    public void setBound(int bound) {
        this.bound = bound;
    }

    public NimPlayer getPlayer1() {
        return player1;
    }

    public void setPlayer1(NimPlayer player1) {
        this.player1 = player1;
    }

    public NimPlayer getPlayer2() {
        return player2;
    }

    public void setPlayer2(NimPlayer player2) {
        this.player2 = player2;
    }


    /**
     * Method: start()
     * Used as a trigger to launch the game and give some basic instruction,
     * it's the main function of the game
     */
    public void start() {

        System.out.println("\nInitial stone count: " + currNum + "\n" +
                "Maximum stone removal: " + bound + "\n" +
                "Player 1: " + player1.getGivenName() + " " + player1.getFamilyName() +
                "\n" + "Player 2: " + player2.getGivenName() + " " +
                player2.getFamilyName());

        // a flag to mark which player is playing, 1 means player1
        int flag = 1;

        // A temp player to hold two players
        NimPlayer playerTemp;

        // create five variables to hold upper bound, current number of
        // stones, the number of stones removal and stone symbol
        int removeNum;


        // increment the total playing times
        player1.addTotal();
        player2.addTotal();

        while (currNum != 0) {

            System.out.println();

            // show the stones
            System.out.print(currNum + " stones left:");
            showStone(currNum);

            // player1 starts playing
            if (flag == 1) {
                playerTemp = player1;
                flag = 2;
            } else {

                // player2 starts playing
                playerTemp = player2;
                flag = 1;
            }


            removeNum = playerTemp.removeStone(currNum, bound);

            // check if the move is invalid
            while (true) {
                try {
                    if (!invalid(removeNum, bound, currNum)) break;
                } catch (LimitExceededException e) {
                    System.out.println("\nInvalid move. You must remove between 1 and "
                            + Math.min(bound, currNum) + " stones.\n");
                }
                System.out.print(currNum + " stones left:");
                showStone(currNum);
                removeNum = playerTemp.removeStone(currNum, bound);
            }

            // update the current number after one round playing
            currNum -= removeNum;
        }

        System.out.println();
        System.out.println("Game Over");

        // playerTemp is stored the last player
        if (flag == 1) {
            playerTemp = player1;
        } else {
            playerTemp = player2;
        }

        // output the winner info
        playerTemp.addWin();
        System.out.println(playerTemp.getGivenName() + " " + playerTemp.getFamilyName()
                + " wins!");

    }


    /**
     * Method: invalid(int)
     * To check if the number received is invalid for remove
     *
     * @param removeNum, the number of stones removal
     * @param bound,     the upper bound
     * @param currNum,   the current number of stones
     * @return is invalid or not.
     */
    public boolean invalid(int removeNum, int bound, int currNum) throws LimitExceededException {

        // remove stones more than the upper bound
        if (removeNum > bound || removeNum == 0 || removeNum > currNum) {
            throw new LimitExceededException();
        }
        return false;
    }


    /**
     * Method: showStone(int)
     * To output the stones' images
     *
     * @param currNum, the current number of stones
     */
    public void showStone(int currNum) {
        String stone = " *";
        for (int i = 0; i < currNum; i++) {
            System.out.print(stone);
        }
    }
}
