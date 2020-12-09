/////////////////////////////////////////////////////////
// Author : YiFan Deng
// StudentID: 1150027
// Username: Ivandon15
//
// Class Name: Nimsys
// To hold different methods and to excute the Nim Game
/////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Nimsys {

    // a scanner that everyone in this class can be achieved
    public static Scanner sc = new Scanner(System.in);

    /**
     * Method: start():
     * Used as a trigger to launch the game and give some basic instruction,
     * it's the main function of the game
     */
    public void start() {

        // Get the players' names
        System.out.print("Please enter Player 1's name : ");
        String pname1 = sc.next();
        System.out.print("Please enter Player 2's name : ");
        String pname2 = sc.next();

        // totalTimes is to record how many times they played
        int totalTimes = 0;

        // Initialize two players
        NimPlayer player1 = new NimPlayer(pname1);
        NimPlayer player2 = new NimPlayer(pname2);

        // a flag to mark which player is playing, 1 means player1
        int flag = 1;

        // A temp player to hold two players
        NimPlayer playerTemp;

        // create five variables to hold upper bound, current number of
        // stones, the number of stones removal and stone symbol
        int bound;
        int currNum;
        int removeNum;

        // a flag to check if start the game
        boolean play = true;

        while (play) {

            // increment the total playing times
            player1.setTotalTimes(++totalTimes);
            player2.setTotalTimes(totalTimes);

            // enter the upper bound and initial number of stones
            System.out.print("Enter upper bound : ");
            bound = sc.nextInt();
            System.out.print("Enter initial number of stones : ");
            currNum = sc.nextInt();

            while (currNum != 0) {

                System.out.println();

                // show the stones
                System.out.print(currNum + " stones left :");
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

                System.out.println();

                // Player1 starts playing
                removeNum = playerTemp.removeStone(currNum, bound);
                while (invalid(removeNum, bound, currNum)) {
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
            playerTemp.setWinTimes(playerTemp.getWinTimes() + 1);
            System.out.println(playerTemp.getName() + " wins!\n");

            // set flag back to 1, which will still start from player1
            flag = 1;

            // Asking if they want to continue
            System.out.print("Do you want to play again (Y/N): ");

            // if no, end of start() method, back to main method
            if (sc.next().equalsIgnoreCase("n")) {

                play = false;

                // show the game result
                showResult(player1, player2);

                // absorb the \n
                sc.nextLine();
            }
        }
    }

    /**
     * Method: exit():
     * To exit the game normally without using System.exit
     */
    public void exit() {
        System.out.println("Thank you for playing Nim");
    }

    /**
     * Method: help()
     * To give the game instruction
     */
    public void help() {
        System.out.println("Type 'commands' to list all " +
                "available commands");
        System.out.println("Type 'start' to play game");
        System.out.println("Player to remove the last stone loses!");
        System.out.println();
    }

    /**
     * Method: command()
     * To show the command list
     */
    public void command() {
        List<String> commands = new ArrayList<>();
        commands.add(": start");
        commands.add(": exit");
        commands.add(": help");
        commands.add(": commands");

        for (String command : commands) {
            System.out.println(command);
        }
        System.out.println();
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
    public boolean invalid(int removeNum, int bound, int currNum) {

        // remove stones more than the upper bound
        if(removeNum > bound){
            System.out.println("Upper bound limit exceed, upper bound " +
                    "maximum choice is " + bound);
            System.out.println();
            return true;
        }

        // remove stones more than are remaining
        if (removeNum==0||removeNum > currNum){
            System.out.println("Invalid attempt, only "+currNum+" stones" +
                    "remaining! Try again:");
            System.out.println();
            return true;
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

    /**
     * Method: showResult
     * To output the results of game
     *
     * @param player1, player1
     * @param player2, player2
     */
    public void showResult(NimPlayer player1, NimPlayer player2) {
        String g1 = "game";
        String g2 = "game";

        // check the sigular
        if (player1.getWinTimes() >= 2) {
            g1 = "games";
        }
        if (player2.getWinTimes() >= 2) {
            g2 = "games";
        }
        System.out.println(player1.getName() + " won " +
                player1.getWinTimes() + " " + g1 + " out of " +
                player1.getTotalTimes() + " played");
        System.out.println(player2.getName() + " won " +
                player2.getWinTimes() + " " + g2 + " out of " +
                player2.getTotalTimes() + " played");
    }


    /**
     * Method: main():
     * Used as to start the program and call the functions to play game
     *
     * @param args , get the param from console
     */
    public static void main(String[] args) {

        // create an object to call the instance methods
        Nimsys engine = new Nimsys();

        // game welcome instruction
        System.out.println("Welcome to Nim\n");
        System.out.println("Please enter a command to continue\n");

        // command is for receiving the command from user
        String command;


        while (true) {

            // output the symbol for entering
            System.out.print("$ ");

            // every input is using next to avoid getting the content
            // in the keyboard buffer
            command = sc.nextLine();

            if (command.equalsIgnoreCase("start")) {
                System.out.println();
                engine.start();
            } else if (command.equalsIgnoreCase("help")) {
                engine.help();
            } else if (command.equalsIgnoreCase("exit")) {
                System.out.println();
                engine.exit();
                break;
            } else if (command.equalsIgnoreCase("commands")) {
                System.out.println();
                engine.command();
            }
        }
        System.out.println();
    }
}
