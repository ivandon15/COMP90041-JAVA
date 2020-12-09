/////////////////////////////////////////////////////////
// Author : YiFan Deng
// StudentID: 1150027
// Username: Ivandon15
//
// Class Name: NimAdvanceGame
// The engine of advance game derived from NimGame
/////////////////////////////////////////////////////////

import javax.naming.LimitExceededException;

public class NimAdvanceGame extends NimGame {
    private static final int BOUND = 2;
    private static final int POS = 0;
    private static final int BONUM = 1;
    private boolean[] available;

    /**
     * Method: NimAdvancedGame
     * Empty Constructor
     */
    public NimAdvanceGame() {
        super();
    }

    /**
     * Method: NimAdvanceGame(int currNum, NimPlayer player1, NimPlayer player2)
     * Three para constructor
     *
     * @param currNum
     * @param player1
     * @param player2
     */
    public NimAdvanceGame(int currNum, NimPlayer player1, NimPlayer player2) {
        super(currNum, BOUND, player1, player2);
        available = new boolean[currNum];
        for (int i = 0; i < available.length; i++) {
            available[i] = true;
        }
    }

    /**
     * Method: start():
     * Used as a trigger to launch the game and give some basic instruction,
     * it's the main function of the game
     */
    public void start() {

        // output stones and basic info
        System.out.println("\nInitial stone count: " + this.getCurrNum());
        System.out.print("Stones display:");
        showStone(available);
        System.out.println("\nPlayer 1: " + this.getPlayer1().getGivenName() + " " +
                this.getPlayer1().getFamilyName() + "\n" + "Player 2: " +
                this.getPlayer2().getGivenName() + " " +
                this.getPlayer2().getFamilyName());
        System.out.println();

        // a flag to mark which player is playing, 1 means player1
        int flag = 1;

        // A temp player to hold two players
        NimPlayer playerTemp;

        // create five variables to hold upper bound, current number of
        // stones, the number of stones removal and stone symbol
        String currMove;
        String lastMove = "";


        // increment the total playing times
        this.getPlayer1().addTotal();
        this.getPlayer2().addTotal();

        while (this.getCurrNum() != 0) {

            // show the stones
            System.out.print(this.getCurrNum() + " stones left:");
            showStone(available);

            // player1 starts playing
            if (flag == 1) {
                playerTemp = this.getPlayer1();
                flag = 2;
            } else {

                // player2 starts playing
                playerTemp = this.getPlayer2();
                flag = 1;
            }

            // receive the current player's move
            currMove = playerTemp.advancedMove(available, lastMove);

            // check if the move is invalid
            while (true) {

                try {
                    if (!invalid(available, currMove)) {
                        lastMove = currMove;
                        break;
                    }
                } catch (LimitExceededException e) {
                    System.out.println("Invalid move.\n");
                } catch (IllegalAccessError e){
                    System.out.println("Invalid move.\n");
                }
                System.out.print(this.getCurrNum() + " stones left:");
                showStone(available);
                currMove = playerTemp.advancedMove(available, lastMove);
            }

            lastMove = currMove;

            // decrease the stone number and remove it
            deStone(available,currMove);

        }

        System.out.println("Game Over");

        // playerTemp is stored the last player
        if (flag == 1) {
            playerTemp = this.getPlayer2();
        } else {
            playerTemp = getPlayer1();
        }

        // output the winner info
        playerTemp.addWin();
        System.out.println(playerTemp.getGivenName() + " " + playerTemp.getFamilyName()
                + " wins!");

    }

    /**
     * Method: deStone(boolean[] available, String currMove)
     * Decrease the stone by following the current Move
     *
     * @param available , the stats of stones
     * @param currMove , the current move by the player
     */
    public void deStone(boolean[] available, String currMove){

        String[] s = new String[2];
        s = currMove.split(" ");

        int[] inputs = new int[2];
        for (int i = 0; i < 2; i++) {
            inputs[i] = Integer.parseInt(s[i]);
        }

        this.setCurrNum(this.getCurrNum()-inputs[BONUM]);

        for (int i = 0;i<available.length;i++){
            if (inputs[POS]-1==i){
                available[i]=false;
                if (inputs[BONUM]==2){
                    available[i+1]=false;
                }
            }
        }
    }

    /**
     * Method: showStone(boolean[] available)
     * Output the stones
     *
     * @param available , the stats of the stones
     */
    public void showStone(boolean[] available) {

        String stone = "*";
        String moved = "x";
        for (int i = 0; i < available.length; i++) {
            if (available[i]) {
                System.out.printf(" <%d,%s>", i+1, stone);
            } else {
                System.out.printf(" <%d,%s>", i+1, moved);
            }
        }
    }


    /**
     * Metthod: invalid(boolean[] available, String currMove)
     * To check if the current move is valid under current situations
     * of the stats of stones
     *
     * @param available , the stats of stones
     * @param currMove , the current move
     * @return true if invalid, false if valid
     * @throws LimitExceededException
     * @throws IllegalAccessError
     */
    public boolean invalid(boolean[] available, String currMove)
            throws LimitExceededException,IllegalAccessError {

        String[] s = new String[2];
        s = currMove.split(" ");

        int[] inputs = new int[2];
        for (int i = 0; i < 2; i++) {
            inputs[i] = Integer.parseInt(s[i]);
        }

        // the position of stones is invalid
        // and the number of stones removed is invalid
        if (inputs[POS] > available.length || inputs[POS] == 0 ||
                inputs[BONUM] < 1 || inputs[BONUM] > 2) {
            throw new LimitExceededException();
        } else {

            // the stones have been already removed
            if (inputs[BONUM] == 1) {
                if (!available[inputs[POS]-1]) {
                    throw new IllegalAccessError();
                }
            } else {
                if (!(available[inputs[POS]-1] && available[inputs[POS]])) {
                    throw new IllegalAccessError();
                }
            }
        }
        return false;
    }


}
