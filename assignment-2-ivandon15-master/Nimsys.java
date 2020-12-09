/////////////////////////////////////////////////////////
// Author : YiFan Deng
// StudentID: 1150027
// Username: Ivandon15
//
// Class Name: Nimsys
// To hold different methods and to excute the Nim Game
/////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class Nimsys {

    // a scanner that everyone in this class can be achieved
    public static Scanner sc = new Scanner(System.in);

    private static final int INPUT_SIZE = 4;
    private static final int USERNAME = 0;
    private static final int FAMILYNAME = 1;
    private static final int GIVENNAME = 2;

    private NimPlayer[] players = new NimPlayer[100];

    // current number of players
    private int playerNum = 0;

    // override compare, Descending by the success ratio then their name
    private static Comparator<NimPlayer> desc = new Comparator<NimPlayer>() {
        @Override
        public int compare(NimPlayer p1, NimPlayer p2) {
            if (p1.getRatio() < p2.getRatio()) {
                return 1;
            } else if (p1.getRatio() > p2.getRatio()) {
                return -1;
            }
            return p1.compareTo(p2);
        }
    };

    // override compare, Ascending by the success ratio then their name
    private static Comparator<NimPlayer> asc = new Comparator<NimPlayer>() {
        @Override
        public int compare(NimPlayer p1, NimPlayer p2) {
            if (p1.getRatio() < p2.getRatio()) {
                return -1;
            } else if (p1.getRatio() > p2.getRatio()) {
                return 1;
            }
            return p1.compareTo(p2);
        }
    };


    /**
     * Method: exit():
     * To exit the game normally without using System.exit
     */
    public void exit() {

        // save the players info into a .dat file when normally exiting
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream("players.dat"));
            outputStream.writeObject(players);
            outputStream.close();
        } catch (Exception e) {
            // for testing
            //System.out.println("file saving problems");
        }
    }


    /**
     * Method: help()
     * To give the game instruction
     */
    public void help() {
        System.out.println("Type 'commands' to list all " +
                "available commands\n" +
                "Type 'startgame' to play game\n" +
                "The player that removes the last stone loses!\n");
    }


    /**
     * Method: command()
     * To show the command list
     */
    public void command() {
        LinkedHashMap<String, String> commands = new LinkedHashMap<>();
        addCommand(commands);

        // record the number of the commands
        int i = 1;

        // iterate to output the commands and their content
        for (String command : commands.keySet()) {
            if (i >= 1 && i < 10) {
                System.out.print(" " + i + ": ");
            } else {
                System.out.print(i + ": ");
            }
            System.out.printf("%-18s%s\n", command, commands.get(command));
            i++;
        }
    }


    /**
     * Method: addCommand(LinkedHashMap<String, String> commands)
     * To add command, used by command() method
     *
     * @param commands , a linkedhashmap, key is the command, value
     *                 is the content
     */
    public void addCommand(LinkedHashMap<String, String> commands) {
        commands.put("exit", "(no parameters)");
        commands.put("addplayer", "(username, secondname, firstname)");
        commands.put("addaiplayer", "(username, secondname, firstname)");
        commands.put("removeplayer", "(optional username)");
        commands.put("editplayer", "(username, secondname, firstname)");
        commands.put("resetstats", "(optional username)");
        commands.put("displayplayer", "(optional username)");
        commands.put("rankings", "(optional asc)");
        commands.put("startgame", "(initialstones, upperbound, username1, username2)");
        commands.put("startadvancedgame", "(initialstones, username1, username2)");
        commands.put("commands", "(no parameters)");
        commands.put("help", "(no parameters)");
    }

    /**
     * Method: addPlayer(String userName, String familyName,
     *                           String givenName, boolean isAI)
     * Adding human players and computer players into the game
     *
     * @param userName , players' username
     * @param familyName , players' family name
     * @param givenName , players' given name
     * @param isAI , check if the new added player is a computer player
     */
    public void addPlayer(String userName, String familyName,
                          String givenName, boolean isAI) {

        // first check if has the same username
        if (userExist(userName)) {
            System.out.println("The player already exists.");
            return;
        }

        playerNum++;

        // if there is no such user and it's an AIplayer
        if (isAI) {
            NimAIPlayer aiPlayer = new NimAIPlayer(userName, givenName, familyName);
            for (int i = 0; i < players.length; i++) {
                if (players[i] == null) {
                    players[i] = aiPlayer;
                    break;
                }
            }
        } else {

            // it's a humanPlayer
            NimHumanPlayer humanPlayer = new NimHumanPlayer(userName, givenName, familyName);
            for (int i = 0; i < players.length; i++) {
                if (players[i] == null) {
                    players[i] = humanPlayer;
                    break;
                }
            }
        }
    }

    /**
     * Method: removePlayer(String username)
     * Removing player from the game by using username
     *
     * @param username , players' username
     */
    public void removePlayer(String username) {

        // if there is no such user
        if (!userExist(username)) {
            System.out.println("The player does not exist.");
            return;
        }

        // do loop to find user
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUserName().equals(username)) {
                players[i] = null;
                playerNum--;
                break;
            }
        }
    }

    /**
     * Method: editPlayer(String username, String familyname, String givenname)
     * For changing the user's basic info
     *
     * @param username , player's username
     * @param familyname , player's family name
     * @param givenname , player's given name
     */
    public void editPlayer(String username, String familyname, String givenname) {

        // first to check if the user exists
        if (!userExist(username)) {
            System.out.println("The player does not exist.");
            return;
        }

        // do loop to find the user and edit.
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUserName().equals(username)) {
                players[i].setGivenName(givenname);
                players[i].setFamilyName(familyname);
                break;
            }
        }
    }

    /**
     * Method: resetStats(String username)
     * For reset a specific player's games results
     *
     * @param username , player's username
     */
    public void resetStats(String username) {

        // check if the user exists
        if (!userExist(username)) {
            System.out.println("The player does not exist.");
            return;
        }

        // do loop for find the user then reset
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUserName().equals(username)) {
                players[i].setWinTimes(0);
                players[i].setTotalTimes(0);
                break;
            }
        }
    }

    /**
     * Method: displayPlayer(String username, NimPlayer[] realPlayers)
     * For display a specific player's info and his game result
     *
     * @param username , player's username
     * @param realPlayers , an array all players set in the order
     */
    public void displayPlayer(String username, NimPlayer[] realPlayers) {

        // check if exists
        if (!userExist(username)) {
            System.out.println("The player does not exist.");
            return;
        }

        // output the player's info
        for (NimPlayer realPlayer : realPlayers) {
            if (realPlayer.getUserName().equals(username)) {
                System.out.println(realPlayer);
                break;
            }
        }
    }

    /**
     * Method: rankings(String order, NimPlayer[] realPlayers)
     * For displaying the rankings of all players in specific order
     *
     * @param order , in what order
     * @param realPlayers , an array all players set in the order
     */
    public void rankings(String order, NimPlayer[] realPlayers) {

        // no players
        if (playerNum == 0) {
            return;
        }

        // check in what order
        if (order.equals("desc")) {
            Arrays.sort(realPlayers, desc);
        } else if (order.equals("asc")) {
            Arrays.sort(realPlayers, asc);
        }

        // for output format
        String ratio;
        int tens, ones;

        // do loop to output
        for (int i = 0; i < realPlayers.length; i++) {
            if (realPlayers[i] != null) {
                tens = realPlayers[i].getTotalTimes() / 10;
                ones = realPlayers[i].getTotalTimes() % 10;
                ratio = (int) Math.round(realPlayers[i].getRatio() * 100) + "%";
                System.out.printf("%-5s| %d%d games | %s %s\n", ratio, tens, ones,
                        realPlayers[i].getGivenName(), realPlayers[i].getFamilyName());
            }
        }
    }

    /**
     * Method: startGame(int stoneNum, int bound, String username1, String username2)
     * For starting the easy game by creating NimGame object
     *
     * @param stoneNum , the initial stone nunmber
     * @param bound , the initial stone bound
     * @param username1 , player1
     * @param username2 , player2
     */
    public void startGame(int stoneNum, int bound, String username1, String username2) {

        // find 2 players in the collection of players
        NimPlayer player1 = null, player2 = null;
        for (NimPlayer player : players) {
            if (player != null) {
                if (player.getUserName().equals(username1)) {
                    player1 = player;
                } else if (player.getUserName().equals(username2)) {
                    player2 = player;
                }
            }
        }

        // creating an object by calling four para constructor
        NimGame nimGame = new NimGame(stoneNum, bound, player1, player2);
        nimGame.start();
    }

    /**
     * Method: startAdvancedGame(int stoneNum, String username1, String username2)
     * Similar to startGame(), it's for starting an advanced game
     *
     * @param stoneNum , the initial stone number
     * @param username1 , player1
     * @param username2 , player2
     */
    public void startAdvancedGame(int stoneNum, String username1, String username2){

        // find 2 players in the collection of players
        NimPlayer player1 = null, player2 = null;
        for (NimPlayer player : players) {
            if (player != null) {
                if (player.getUserName().equals(username1)) {
                    player1 = player;
                } else if (player.getUserName().equals(username2)) {
                    player2 = player;
                }
            }
        }

        // creating an object by calling four para constructor
        NimAdvanceGame nimAdvanceGame = new NimAdvanceGame(stoneNum, player1, player2);
        nimAdvanceGame.start();
    }

    /**
     * Method: isCommand(String command)
     * For checking if the command is valid
     *
     * @param command , the command
     * @return true if valid, false if invalid
     */
    public boolean isCommand(String command) {

        return (command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("addplayer") ||
                command.equalsIgnoreCase("addaiplayer") ||
                command.equalsIgnoreCase("removeplayer") ||
                command.equalsIgnoreCase("editplayer") ||
                command.equalsIgnoreCase("resetstats") ||
                command.equalsIgnoreCase("displayplayer") ||
                command.equalsIgnoreCase("rankings") ||
                command.equalsIgnoreCase("startgame") ||
                command.equalsIgnoreCase("startadvancedgame") ||
                command.equalsIgnoreCase("commands") || command.equalsIgnoreCase("help"));
    }


    /**
     * Method: userExist(String username)
     * For checking if the user is existing
     *
     * @param username , the username checked
     * @return true if exist, false if not
     */
    public boolean userExist(String username) {

        if (playerNum == 0) {
            return false;
        }

        for (NimPlayer player : players) {
            if (player != null) {
                if (player.getUserName().equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Method: game()
     * The main game homepage
     */
    public void game() {

        // game welcome instruction
        System.out.println("Welcome to Nim\n\n" +
                "Please enter a command to continue or type 'help' " +
                "for more information\n");

        // for receiving the command and the paras from user
        String command, content;
        String[] input = new String[INPUT_SIZE];

        // check if having a file to hold the previous players' info,
        // if yes, input the info into program
        ObjectInputStream inputStream;
        File file = new File("players.dat");
        if (file.exists()&&file.length()!=0) {
            try {
                inputStream =
                        new ObjectInputStream(new FileInputStream("players.dat"));
                players = (NimPlayer[]) inputStream.readObject();
                for (NimPlayer player : players) {
                    if (player!=null){
                        playerNum++;
                    }
                }
                inputStream.close();
            } catch (Exception e) {
                // for testing
                //System.out.println("file reading problems");
            }
        }


        // main loop
        while (true) {

            // output the symbol for entering
            System.out.print("$ ");

            // every input is using next to avoid getting the content
            // in the keyboard buffer
            command = sc.next();

            // check if the command is valid
            try {
                if (!isCommand(command)) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("'" + command + "' is not a valid command.\n");
            }

            // take the rest input as the content
            content = sc.nextLine();
            input = content.split(",");
            for (int i = 0; i < input.length; i++) {
                input[i] = input[i].trim();
            }

            // command choosing module
            if (command.equalsIgnoreCase("exit")) {

                exit();
                System.out.println();
                break;
            } else if (command.equalsIgnoreCase("addplayer") ||
                    command.equalsIgnoreCase("addaiplayer")) {

                // using isAI para to check if the player created is a
                // computer player
                try {
                    if (input[GIVENNAME].equals("")) {
                        throw new Exception();
                    } else {
                        if (command.equalsIgnoreCase("addaiplayer")) {
                            addPlayer(input[USERNAME], input[FAMILYNAME],
                                    input[GIVENNAME], true);
                        } else {
                            addPlayer(input[USERNAME], input[FAMILYNAME],
                                    input[GIVENNAME], false);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Incorrect number of arguments supplied to command.");
                }
                System.out.println();
            } else if (command.equalsIgnoreCase("removeplayer")) {

                if (input[USERNAME].equals("")) {
                    System.out.println("Are you sure you want to remove all players? (y/n)");
                    String ans = sc.nextLine();

                    // want to remove all the players, using loop
                    if (ans.equals("y")) {
                        for (int i = 0; i < players.length; i++) {
                            if (players[i] != null) {
                                removePlayer(players[i].getUserName());
                            }
                        }
                    }
                } else {
                    removePlayer(input[USERNAME]);
                }
                System.out.println();
            } else if (command.equalsIgnoreCase("editplayer")) {

                try {
                    if (input[GIVENNAME].equals("")) {
                        throw new Exception();
                    } else {
                        editPlayer(input[USERNAME], input[FAMILYNAME], input[GIVENNAME]);
                    }
                } catch (Exception e) {
                    System.out.println("Incorrect number of arguments supplied to command.");

                }
                System.out.println();
            } else if (command.equalsIgnoreCase("resetstats")) {

                if (input[USERNAME].equals("")) {
                    System.out.println("Are you sure you want to reset all player " +
                            "statistics? (y/n)");
                    String ans = sc.nextLine();
                    if (ans.equals("y")) {
                        for (int i = 0; i < players.length; i++) {
                            if (players[i] != null) {
                                resetStats(players[i].getUserName());
                            }
                        }
                    }
                } else {
                    resetStats(input[USERNAME]);
                }
                System.out.println();
            } else if (command.equalsIgnoreCase("displayplayer")) {

                // in that the original array is not sequential, we need a
                // new array to restructure
                NimPlayer[] realPlayers = new NimPlayer[playerNum];
                int j = 0;
                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null) {
                        realPlayers[j] = players[i];
                        j++;
                    }
                }

                // And sort it following ascending order in name
                Arrays.sort(realPlayers);

                // display all the players
                if (input[USERNAME].equals("")) {
                    for (int i = 0; i < realPlayers.length; i++) {
                        if (realPlayers[i] != null) {
                            displayPlayer(realPlayers[i].getUserName(), realPlayers);
                        }
                    }
                } else {

                    // display one player
                    displayPlayer(input[USERNAME], realPlayers);
                }
                System.out.println();
            } else if (command.equalsIgnoreCase("rankings")) {

                // in that the original array is not sequential, we need a
                // new array to restructure
                NimPlayer[] realPlayers = new NimPlayer[playerNum];

                int j = 0;
                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null) {
                        realPlayers[j] = players[i];
                        j++;
                    }
                }

                // the default order is descending
                if (input[0].equals("") || input[0].equals("desc")) {
                    rankings("desc", realPlayers);
                } else if (input[0].equals("asc")) {
                    rankings("asc", realPlayers);
                }
                System.out.println();
            } else if (command.equalsIgnoreCase("startgame")) {

                try {
                    if (input[3].equals("")) {
                        throw new IllegalArgumentException();
                    } else {
                        if (!userExist(input[2]) || !userExist(input[3])) {
                            throw new Exception();
                        } else {
                            startGame(Integer.parseInt(input[0]), Integer.parseInt(input[1]),
                                    input[2], input[3]);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Incorrect number of arguments supplied to command.");
                } catch (Exception e) {
                    System.out.println("One of the players does not exist.");
                }
                System.out.println();
            } else if (command.equalsIgnoreCase("startadvancedgame")) {

                try {
                    if (input[2].equals("")) {
                        throw new IllegalArgumentException();
                    } else {
                        if (!userExist(input[1]) || !userExist(input[2])) {
                            throw new Exception();
                        } else {
                            startAdvancedGame(Integer.parseInt(input[0]), input[1], input[2]);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Incorrect number of arguments supplied to command.");
                } catch (Exception e) {
                    System.out.println("One of the players does not exist.");
                }
                System.out.println();
            } else if (command.equalsIgnoreCase("commands")) {
                command();
                System.out.println();
            } else if (command.equalsIgnoreCase("help")) {
                help();
            }
        }
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
        engine.game();
    }
}
