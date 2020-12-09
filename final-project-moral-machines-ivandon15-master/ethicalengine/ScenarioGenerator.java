package ethicalengine;

import java.util.ArrayList;
import java.util.Random;

import ethicalengine.Persona.*;
import ethicalengine.Human.*;
import ethicalengine.Animal.*;

/**
 * To create generator object and generate random/specified scenarios
 * <p>
 * StudentID: 1150027
 * Username: Ivandon15
 *
 * @author YiFan Deng
 */
public class ScenarioGenerator {

    // use current time as seed to generate
    // totally different number
    private long seedTime = System.currentTimeMillis();
    private Random rd = new Random();

    // the bound number of passengers and pedestrians
    private int passengerCountMin = 1, passengerCountMax = 5,
            pedestrianCountMin = 1, pedestrianCountMax = 5;

    // put all enum into arrays
    private Gender[] genders = Gender.values();
    private BodyType[] bodyTypes = BodyType.values();
    private Profession[] professions = Profession.values();
    private Species[] speciess = Species.values();


    /**
     * Empty constructor to generate a truly random number
     */
    public ScenarioGenerator() {

        // a truly random
        rd.setSeed(seedTime);
    }

    /**
     * Constructor sets the seed with a predefined value
     *
     * @param seed predefined value
     */
    public ScenarioGenerator(long seed) {
        rd.setSeed(seed);
    }

    /**
     * Constructor set seed and four bounds
     *
     * @param seed                   predefined value
     * @param passengerCountMinimum  minimum number of passengers
     * @param passengerCountMaximum  maximum number of passengers
     * @param pedestrianCountMinimum minimum number of pedestrians
     * @param pedestrianCountMaximum maximum number of pedestrians
     */
    public ScenarioGenerator(long seed, int passengerCountMinimum,
                             int passengerCountMaximum, int pedestrianCountMinimum,
                             int pedestrianCountMaximum)
            throws NumberFormatException {
        rd.setSeed(seed);
        if (passengerCountMaximum >= passengerCountMinimum &&
                pedestrianCountMaximum >= pedestrianCountMinimum) {
            passengerCountMin = passengerCountMinimum;
            passengerCountMax = passengerCountMaximum;
            pedestrianCountMin = pedestrianCountMinimum;
            pedestrianCountMax = pedestrianCountMaximum;
        } else {

            // one exception handling by myself
            throw new NumberFormatException("WARNING: maximum must bigger than" +
                    "or equal to minimum!");
        }
    }

    // setters of four bounds
    public void setPassengerCountMin(int min) {
        passengerCountMin = min;
    }

    public void setPassengerCountMax(int max) {
        passengerCountMax = max;
    }

    public void setPedestrianCountMin(int min) {
        pedestrianCountMin = min;
    }

    public void setPedestrianCountMax(int max) {
        pedestrianCountMax = max;
    }

    /**
     * To get a random human
     *
     * @return a human object
     */
    public Human getRandomHuman() {

        // 100 is enough
        int age = rd.nextInt(101);
        Gender gender = genders[rd.nextInt(genders.length-1)];
        BodyType bodyType = bodyTypes[rd.nextInt(bodyTypes.length-1)];
        Profession profession = professions[rd.nextInt(professions.length-1)];

        // assume that 68>=age>=17, female can be pregnant
        // the percentage of pregnant female is 1/20
        boolean isPregnant = (age >= 17) && (age <= 68) &&
                (gender == Gender.FEMALE) && (rd.nextInt(20) == 0);
        return new Human(age, profession, gender, bodyType, isPregnant);
    }

    /**
     * To get a random animal
     *
     * @return a animal object
     */
    public Animal getRandomAnimal() {

        // 21 is enough for an animal ?
        int age = rd.nextInt(21);
        Gender gender = genders[rd.nextInt(genders.length)];
        BodyType bodyType = bodyTypes[rd.nextInt(bodyTypes.length)];
        String species = speciess[rd.nextInt(speciess.length)].toString();

        // give 1/10 not a pet
        boolean isPet = !(rd.nextInt(10) == 0);

        return new Animal(age, species, gender, bodyType, isPet);
    }

    /**
     * Based on the bounds to randomly generate a scenario
     *
     * @return a scenario
     */
    public Scenario generate() {

        int passCount = rd.nextInt(passengerCountMax - passengerCountMin
                + 1) + passengerCountMin;
        int pedeCount = rd.nextInt(pedestrianCountMax - pedestrianCountMin
                + 1) + pedestrianCountMin;
        ArrayList<Persona> passengers = new ArrayList<>();
        ArrayList<Persona> pedestrians = new ArrayList<>();

        // 0-> in car, 1-> on street, 2-> absent
        int posYou = rd.nextInt(3);

        // 0-> green(legal), 1-> red
        boolean legalCross = rd.nextInt(2) == 0;

        // always create You for preparation
        Human you = getRandomHuman();
        you.setAsYou(true);

        if (posYou == 0) {
            passengers.add(you);
            passCount--;
        } else if (posYou == 1) {
            pedestrians.add(you);
            pedeCount--;
        }
        for (int i = 0; i < pedeCount; i++) {

            // give 1/6 is a animal
            pedestrians.add(rd.nextInt(6) == 0 ?
                    getRandomAnimal() : getRandomHuman());
        }
        for (int i = 0; i < passCount; i++) {

            // give 1/6 is a animal
            passengers.add(rd.nextInt(6) == 0 ?
                    getRandomAnimal() : getRandomHuman());
        }


        return new Scenario(passengers, pedestrians, legalCross);
    }

    /**
     * Use separate row and separate tuples in csv file to create a list of scenarios
     *
     * @param separateLine each line represent one row in file
     * @return a list of scenario objects created from the file
     */
    public ArrayList<Scenario> csvScenario(ArrayList<String[]> separateLine) {

        // the first line is header, data always start at 2nd line
        int lineNum = 2;
        boolean isLegalCrossing = false;
        final int GENDER = 1, AGE = 2, BODYTYPE = 3, PRO = 4, PREGN = 5, ISYOU = 6,
                SPEC = 7, ISPET = 8, ROLE = 9;

        // two arraylist to store passengers and pedestrians
        ArrayList<Persona> passengers = new ArrayList<>();
        ArrayList<Persona> pedestrians = new ArrayList<>();
        ArrayList<Scenario> scenarios = new ArrayList<>();

        for (String[] row : separateLine) {

            // check if it's the scenarios' row
            if (row[0].toLowerCase().startsWith("scenario:")) {

                // it's not the first scenarios
                // need to create the scenario before
                if (lineNum != 2) {
                    Scenario scenario = new Scenario(passengers, pedestrians, isLegalCrossing);
                    scenarios.add(scenario);

                    // start a new scenario
                    passengers = new ArrayList<>();
                    pedestrians = new ArrayList<>();
                }

                // separate "scenario:red", index 1 means red/green
                isLegalCrossing = row[0].split(":")[1].equals("green");
            } else if (row[0].equalsIgnoreCase("human")) {
                try {
                    Human human = new Human();

                    // if it's not true and false in pregnant/isYou column
                    // need to throws NumberFormatException
                    if (!(row[PREGN].equalsIgnoreCase("false") ||
                            row[PREGN].equalsIgnoreCase("true"))) {
                        row[PREGN] = "false";
                        throw new NumberFormatException();
                    }
                    if (!(row[ISYOU].equalsIgnoreCase("false") ||
                            row[ISYOU].equalsIgnoreCase("true"))) {
                        row[ISYOU] = "false";
                        throw new NumberFormatException();
                    }

                    human.setAsYou(Boolean.parseBoolean(row[ISYOU]));
                    human.setAge(Integer.parseInt(row[AGE]));
                    // if a string is not contained in a enum, then it'll throw exception
                    human.setHuman(row[GENDER], row[BODYTYPE], row[PRO],
                            Boolean.parseBoolean(row[PREGN]), lineNum);

                    // check which side this human belongs
                    if (row[ROLE].equalsIgnoreCase("passenger")) {
                        passengers.add(human);
                    } else {
                        pedestrians.add(human);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("WARNING: invalid number " +
                            "format in config file in line " + lineNum);
                } catch (RuntimeException e) {

                    // catch invalid characteristic
                    System.out.println(e.getMessage());
                }
            } else if (row[0].equalsIgnoreCase("animal")) {
                try {
                    Animal animal = new Animal();

                    // if it's not true and false in isPet column
                    // need to throws NumberFormatException
                    if (!(row[ISPET].equalsIgnoreCase("false") ||
                            row[ISPET].equalsIgnoreCase("true"))) {
                        row[ISYOU] = "false";
                        throw new NumberFormatException();
                    }

                    animal.setAge(Integer.parseInt(row[AGE]));
                    animal.setPet(Boolean.parseBoolean(row[ISPET]));
                    // set all the information to animal
                    animal.setAnimal(row[GENDER], row[BODYTYPE], row[SPEC], lineNum);

                    // check which side this animal belongs
                    if (row[ROLE].equalsIgnoreCase("passenger")) {
                        passengers.add(animal);
                    } else {
                        pedestrians.add(animal);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("WARNING: invalid number " +
                            "format in config file in line " + lineNum);
                } catch (RuntimeException e) {

                    // catch invalid characteristic
                    System.out.println(e.getMessage());
                }
            }
            lineNum++;
        }

        // the last scenario need to be created
        Scenario scenario = new Scenario(passengers, pedestrians, isLegalCrossing);
        scenarios.add(scenario);
        return scenarios;
    }
}
