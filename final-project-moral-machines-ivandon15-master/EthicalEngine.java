import ethicalengine.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static ethicalengine.Human.AgeCategory.*;
import static ethicalengine.Human.Profession.*;
import static ethicalengine.Persona.BodyType.*;

/**
 * COMP90041, Sem2, 2020: Final Project: A skeleton code for you to update
 * <p>
 * StudentID: 1150027
 * Username: Ivandon15
 *
 * @author: tilman.dingler@unimelb.edu.au and YiFan Deng
 */
public class EthicalEngine {

    public enum Decision {PASSENGERS, PEDESTRIANS}
    
    /**
     * Decides whether to save the passengers or the pedestrians
     *
     * @param scenario: the ethical dilemma
     * @return Decision: which group to save
     */
    public static Decision decide(Scenario scenario) {

        EthicalEngine engine = new EthicalEngine();
        // two flags to store which side contains user
        boolean pedYou, passYou = false;
        final double  NOT_LEGAL = 0.7;
        double passengerWeight = 1.0, pedestrianWeight = 1.0;

        // calculate the sum of passengers' weights
        for (Persona passenger : scenario.getPassengers()) {
            passengerWeight += engine.calWeight(passenger);
        }

        // calculate the sum of pedestrians' weights
        for (Persona pedestrian : scenario.getPedestrians()) {
            pedestrianWeight += engine.calWeight(pedestrian);
        }

        // check pedestrians if legally crossing
        pedestrianWeight *= (scenario.isLegalCrossing() ? 1.0 : NOT_LEGAL);

        // check which side has You
        for (Persona pedestrian : scenario.getPedestrians()) {
            if (pedestrian instanceof Human) {
                if (((Human) pedestrian).isYou()) {
                    pedYou = true;
                }
            }
        }

        // assume isYou only appear at one side
        for (Persona passenger : scenario.getPassengers()) {
            if (passenger instanceof Human) {
                if (((Human) passenger).isYou()) {
                    passYou = true;
                }
            }
        }

        // if both sides' weight are same, choose the user side to survive
        // if no user in the scenario, then choose pedestrians
        if (pedestrianWeight == passengerWeight) {
            return (passYou ? Decision.PASSENGERS : Decision.PEDESTRIANS);
        }
        return (passengerWeight > pedestrianWeight ?
                Decision.PASSENGERS : Decision.PEDESTRIANS);
    }

    /**
     * To calculate one object's weight
     *
     * @param object object may be Human, may be Animal
     * @return the total weight
     */
    public double calWeight(Persona object) {
        
        // three dimension weights of human, two dimension weights of pet
        final double ANIMAL_WEIGHT = 0.05, PET_WEIGHT = 10.0,
                YOU_WEIGHT = 2.0, PREGNANT_WEIGHT = 2.0;

        // age weight
        final HashMap<Human.AgeCategory, Double> AGE_WEIGHT =
                new HashMap<Human.AgeCategory, Double>() {
                    {
                        put(BABY, 1.6);
                        put(CHILD, 2.2);
                        put(ADULT, 2.0);
                        put(SENIOR, 0.9);
                    }
                };

        // profession weight
        final HashMap<Human.Profession, Double> PROF_WEIGHT =
                new HashMap<Human.Profession, Double>() {
                    {
                        put(DOCTOR, 1.8);
                        put(CEO, 1.3);
                        put(SCIENTIST, 1.6);
                        put(POLICE, 1.2);
                        put(DISABLED, 0.85);
                        put(CRIMINAL, 0.5);
                        put(HOMELESS, 0.7);
                        put(UNEMPLOYED, 0.9);
                        put(NONE, 1.0);
                    }
                };

        // body type weight
        final HashMap<Persona.BodyType, Double> BODY_WEIGHT =
                new HashMap<Persona.BodyType, Double>() {
                    {
                        put(AVERAGE, 1.0);
                        put(ATHLETIC, 1.6);
                        put(OVERWEIGHT, 0.8);
                        put(UNSPECIFIED, 1.0);
                    }
                };

        double weight = 0.0;

        // object is huamn
        if (object instanceof Human) {

            // times all the weight of human
            weight = AGE_WEIGHT.get(((Human) object).getAgeCategory()) *
                    PROF_WEIGHT.get(((Human) object).getProfession()) *
                    BODY_WEIGHT.get(object.getBodyType()) *
                    (((Human) object).isYou() ? YOU_WEIGHT : 1.0) *
                    (((Human) object).isPregnant() ? PREGNANT_WEIGHT : 1.0);

        } else if (object instanceof Animal) {

            // ANIMAL_WEIGHT is compared with human weight
            // PET_WEIGHT is compared with a non-pet animal
            weight = ANIMAL_WEIGHT * (((Animal) object).isPet() ?
                    PET_WEIGHT : 1.0);
        }

        return weight;
    }

    /**
     * Output the help information and exit
     */
    public void help() {
        System.out.println("EthicalEngine - COMP90041 - Final Project");
        System.out.println("Usage: java EthicalEngine [arguments]\n");
        System.out.println("Arguments:");
        System.out.println("-c or --config\tOptional: path to config file");
        System.out.println("-h or --help\tPrint Help (this message) and exit");
        System.out.println("-r or --results\tOptional: path to results log file");
        System.out.println("-i or --interactive\tOptional: launches interactive mode");
        System.exit(0);
    }

    /**
     * A inner class to provide a exception class
     */
    private static class InvalidDataFormatException extends RuntimeException {
        public InvalidDataFormatException(String message) {
            super(message);
        }

        public InvalidDataFormatException() {
            super();
        }
    }

    /**
     * Main method, choose the mode and initial each run() method
     *
     * @param args args from command line
     */
    public static void main(String[] args) {

        boolean isInteractive = false;
        boolean isConfig = false;
        int index = 0;
        ArrayList<Scenario> scenarioList = new ArrayList<>();
        String resultpath = "result.log";
        EthicalEngine engine = new EthicalEngine();

        // check if command line has arguments
        while (index < args.length) {

            // config mode
            if (args[index].equals("--config") || args[index].equals("-c")) {

                // no argument after -c
                if (index + 1 >= args.length) {
                    engine.help();
                } else {

                    BufferedReader br;

                    // a list to store every tuple in one line
                    ArrayList<String[]> separatedLine = new ArrayList<>();

                    // a string to store current row of data
                    String line;
                    try {

                        // current index is command, next index is tha path
                        br = new BufferedReader(new FileReader(args[index + 1]));

                        // the header of the csv file which is not the data
                        line = br.readLine();
                        int lineNum = 1;

                        while (line != null) {

                            // skip the header
                            if (lineNum != 1) {

                                // -1 here is to store all the content in scenario line
                                // cuz we need catch exception if the number of values
                                // of one row is not equal to 10
                                if (line.split(",", -1).length != 10) {

                                    // skip the wrong data row
                                    line = br.readLine();
                                    lineNum++;
                                    throw new InvalidDataFormatException(
                                            "WARNING: invalid data format in config " +
                                                    "file in line " + (lineNum - 1));
                                }

                                // light row has only one index
                                separatedLine.add(line.split(","));
                            }
                            line = br.readLine();
                            lineNum++;
                        }

                        // close the stream
                        br.close();

                        // generate a list of scenarios
                        ScenarioGenerator generator = new ScenarioGenerator();
                        scenarioList = generator.csvScenario(separatedLine);

                        isConfig = true;
                    } catch (InvalidDataFormatException e) {
                        System.out.println(e.getMessage());
                    } catch (FileNotFoundException e) {

                        // terminate with the following error message
                        System.out.println("ERROR: could not find config file.");
                        System.exit(0);
                    } catch (IOException e) {
                        // check nextline
                    }
                }
            } else if (args[index].equals("--interactive") || args[index].equals("-i")) {

                // interactive mode is on
                isInteractive = true;
            } else if (args[index].equals("--help") || args[index].equals("-h")) {
                engine.help();
            } else if (args[index].equals("--results") || args[index].equals("-r")) {
                try {

                    // check if the file exists
                    new FileReader(args[index + 1]);
                } catch (FileNotFoundException e) {
                    System.out.println(
                            "ERROR: could not print results. Target directory does not exist.");
                    System.exit(0);
                }

                // change the path to user's input
                resultpath = args[index + 1];
            }
            index++;
        }

        // go through which modes are on
        if (isInteractive) {

            // initialize an interactive mode
            InteractiveScene iScene = new InteractiveScene(scenarioList, isConfig);
            iScene.run();
        } else {

            // if it not interactive mode
            Audit audit;
            if (isConfig) {
                audit = new Audit(scenarioList.toArray(new Scenario[0]));
                audit.run();
                audit.printToFile(resultpath);
                audit.printStatistic();
                System.exit(1);
            }

            // if it's not -i and -c, run 100 times as default behavior
            else {
                audit = new Audit();
                audit.run(100);
                audit.printToFile(resultpath);
            }
        }
    }
}