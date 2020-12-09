/////////////////////////////////////////////////////////
// Author : YiFan Deng
// StudentID: 1150027
// Username: Ivandon15
//
// Class Name: NimAIPlayer
// To hold the basic attributes of a Computer Player,
// derived from NimPlayer
/////////////////////////////////////////////////////////

import java.util.Random;

public class NimAIPlayer extends NimPlayer {

    /**
     * Method: NimAIPlayer()
     * Empty Constructor
     */
    public NimAIPlayer() {
        super();
    }

    /**
     * Method: NimAIPlayer(String userName, String givenName, String familyName)
     * Three para constructor, calling its parent's constructor
     *
     * @param userName
     * @param givenName
     * @param familyName
     */
    public NimAIPlayer(String userName, String givenName, String familyName) {
        super(userName, givenName, familyName);
    }

    /**
     * Method: NimAIPlayer(NimAIPlayer)
     * Copy Constructor
     *
     * @param p , the player want to be copied
     */
    public NimAIPlayer(NimAIPlayer p) {
        super(p);
    }

    /**
     * Method: removeStone(int currentNum, int boundNum)
     * Override method, to remove stones in easy game
     *
     * @param currentNum , the current number of stone
     * @param boundNum   , the upper bound of stone removal
     * @return the number of the stones removed
     */
    @Override
    public int removeStone(int currentNum, int boundNum) {

        // prepare for random selection if computer can't win
        Random rand = new Random();
        int removeNum = rand.nextInt(boundNum) + 1;

        System.out.println();

        // the number of stone each player wants to remove
        System.out.println(this.getGivenName() + "'s turn - remove how many?");
        int k = (currentNum - 1) / (boundNum + 1);
        if (k * (boundNum + 1) + 1 == currentNum) {
            while (removeNum > boundNum || removeNum > currentNum) {
                removeNum = rand.nextInt(boundNum) + 1;
            }
            return removeNum;
        } else {
            removeNum = currentNum - k * (boundNum + 1) - 1;
        }
        return removeNum;
    }

    /**
     * Method: advancedMove(boolean[] available, String lastMove)
     * Override Method, for removing in advanced game
     *
     * @param available , the stats of stones
     * @param lastMove , the last move of last player
     * @return the current move of computer
     */
    @Override
    public String advancedMove(boolean[] available, String lastMove) {
        String currMove = "";
        int leftNum = 0;
        System.out.println();
        System.out.println(this.getGivenName() + "'s turn - which to remove?");
        for (boolean ava : available) {
            if (ava) {
                leftNum++;
            }
        }


        // AI is the first to remove
        if (lastMove.equals("")) {

            // the number of left stones is even
            if (available.length % 2 == 0) {
                currMove = available.length / 2 + " 2";
            } else {
                currMove = available.length / 2 + 1 + " 1";
            }
        } else {

            // only left one stone
            if (leftNum == 1) {
                for (int i = 0; i < available.length; i++) {
                    if (available[i]) {
                        currMove = i + 1 + " 1";
                    }
                }
            } else if (leftNum == 2) {
                for (int i = 1; i < available.length; i++) {

                    // two stones and they are adjacent
                    if (available[i - 1] && available[i]) {
                        currMove = i + 1 + " 2";
                    }
                }

                // two stones are separate
                if (currMove.equals("")) {
                    for (int i = 0; i < available.length; i++) {
                        if (available[i]) {
                            currMove = i + 1 + " 1";
                        }
                    }
                }
            } else {

                // details in strategy, basically is a symmetrical
                // removing based on the last move
                String[] last = lastMove.split(" ");
                int lastPos = Integer.parseInt(last[0]);
                int lastNum = Integer.parseInt(last[1]);
                if (lastNum == 1) {
                    if (available[available.length - lastPos]) {
                        currMove = available.length - lastPos + 1 + " 1";
                    } else {
                        for (int i = 0; i < available.length; i++) {
                            if (available[i]) {
                                currMove = i + 1 + " 1";
                                break;
                            }
                        }
                    }
                } else if (lastNum == 2) {
                    if (available[available.length - lastPos]) {
                        currMove = available.length - lastPos + " 2";
                    } else {
                        for (int i = 0; i < available.length; i++) {
                            if (available[i]) {
                                currMove = i + 1 + " 1";
                                break;
                            }
                        }
                    }
                }
            }
        }

        System.out.println();
        return currMove;
    }
}
