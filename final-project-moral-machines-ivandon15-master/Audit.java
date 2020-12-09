import ethicalengine.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Final statistics on the results of each scenario
 * <p>
 * StudentID: 1150027
 * Username: Ivandon15
 *
 * @author YiFan Deng
 */
public class Audit {

    private String auditType = "Unspecified";

    // Here String means characteristics,
    // int[0] to store the number of survive,
    // int[1] to store the total number
    private HashMap<String, int[]> map = new HashMap<>();

    // total sum of survivors' ages, and the number of survivors
    private double sumAge = 0.0;
    private int survivorCount = 0;

    private Scenario[] scenarios = null;

    private int runs = 0;

    /**
     * Empty Constructor
     */
    public Audit() {
    }

    /**
     * Constructor to receive a bunch of scenarios from csv
     *
     * @param scenarios a list of scenarios
     */
    public Audit(Scenario[] scenarios) {
        this.scenarios = scenarios;
    }

    /**
     * Run the scenario
     */
    public void run() {
        for (Scenario scenario : scenarios) {
            EthicalEngine.Decision decision = EthicalEngine.decide(scenario);
            surviveClassify(scenario, decision);
            runs++;
        }
    }

    /**
     * Used when under interactive mode, mainly ask user for deciding,
     * and then conduct statistic
     *
     * @param consent if user agrees with save their result
     */
    public void interactiveRun(String consent) {
        String ans;

        // iterate <=3 scenarios
        for (Scenario scenario : scenarios) {

            // output current scenario
            System.out.println(scenario.toString());
            System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
            ans = InteractiveScene.sc.nextLine();

            // check user's decision
            if (ans.equalsIgnoreCase("passenger") ||
                    ans.equalsIgnoreCase("passengers") ||
                    ans.equalsIgnoreCase("1")) {
                surviveClassify(scenario, EthicalEngine.Decision.PASSENGERS);
            } else if (ans.equalsIgnoreCase("pedestrian") ||
                    ans.equalsIgnoreCase("pedestrians") ||
                    ans.equalsIgnoreCase("2")) {
                surviveClassify(scenario, EthicalEngine.Decision.PEDESTRIANS);
            }

            // for appending on the previous number if under the same audit
            runs++;
        }

        // output the statistics
        printStatistic();
        if (consent.equalsIgnoreCase("yes")) {
            printToFile("user.log");
        }
    }

    /**
     * Run specific number of scenarios in one audit
     *
     * @param runs the number of scenarios
     */
    public void run(int runs) {

        // create a generator for randomly generate the scenario
        ScenarioGenerator generator = new ScenarioGenerator();

        // record the running times of scenarios
        this.runs = runs;

        // iterate to generate runs scenarios
        for (int i = 0; i < runs; i++) {

            // a random scenario
            Scenario scenario = generator.generate();

            // decision is to decide which group to save
            EthicalEngine.Decision decision = EthicalEngine.decide(scenario);

            // add the decision of current scenario to the total results
            surviveClassify(scenario, decision);
        }
    }

    /**
     * Separate which side are survivors and which are victims in order to separate
     * their characteristics
     *
     * @param scenario current scenario
     * @param decision the group survivor
     */
    private void surviveClassify(Scenario scenario, EthicalEngine.Decision decision) {

        // get the survivors and victims
        ArrayList<Persona> survivors = (decision == EthicalEngine.Decision.PASSENGERS ?
                scenario.getPassengersList() : scenario.getPedestriansList());
        ArrayList<Persona> victims = (decision == EthicalEngine.Decision.PASSENGERS ?
                scenario.getPedestriansList() : scenario.getPassengersList());

        // 1->means is survive
        for (Persona survivor : survivors) {
            charClassify(survivor, 1, scenario.isLegalCrossing());
        }

        // 0->means not survive
        for (Persona victim : victims) {
            charClassify(victim, 0, scenario.isLegalCrossing());
        }

        // if passengers survive
        if (decision == EthicalEngine.Decision.PASSENGERS) {

            // update the number of passengers
            for (Persona survivor : survivors) {
                updateRatio("passengers", 1);
            }

            // update the number of victims
            for (Persona victim : victims) {
                updateRatio("pedestrians", 0);
            }
        } else if (decision == EthicalEngine.Decision.PEDESTRIANS) {
            // if pedestrians survive

            for (Persona survivor : survivors) {
                updateRatio("pedestrians", 1);
            }
            for (Persona victim : victims) {
                updateRatio("passengers", 0);
            }
        }
    }

    /**
     * Classify each objects' characteristics in order to calculate the ratio
     * in the future
     *
     * @param object       human or animal
     * @param isSurvive    1 means survive, 0 means not survive
     * @param isLegalCross true for green light
     */
    private void charClassify(Persona object, int isSurvive, boolean isLegalCross) {

        updateRatio(isLegalCross ? "green" : "red", isSurvive);

        if (object instanceof Human) {

            // only count for survivor's ages
            if (isSurvive == 1) {
                sumAge += object.getAge();

                // count for survivors to calculate the average of age
                survivorCount++;
            }

            // update all the characteristic
            updateRatio(((Human) object).getAgeCategory().name(), isSurvive);
            updateRatio(object.getGender().name(), isSurvive);
            updateRatio(object.getBodyType().name(), isSurvive);
            updateRatio(((Human) object).getProfession().name(), isSurvive);

            updateRatio("human", isSurvive);

            if (((Human) object).isYou()) {
                updateRatio("you", isSurvive);
            }
            if (((Human) object).isPregnant()) {
                updateRatio("pregnant", isSurvive);
            }
        } else if (object instanceof Animal) {

            // object is Animal
            updateRatio("animal", isSurvive);
            updateRatio(((Animal) object).getSpecies(), isSurvive);
            if (((Animal) object).isPet()) {
                updateRatio("pet", isSurvive);
            }

        }
    }


    /**
     * For updating every characteristic's number of surviving
     * and total number of each characteristic
     *
     * @param character object's characteristic
     * @param isSurvive whether survives
     */
    private void updateRatio(String character, int isSurvive) {

        // we need to check if String is in {unknown,unspecified,none},
        // they need to be excluded
        if (!character.equals("UNKNOWN") && !character.equals("UNSPECIFIED")
                && !character.equals("NONE")) {

            // int[0] -> number of survivors
            // int[1] -> number of total
            // put if absent
            map.putIfAbsent(character, new int[]{0, 0});

            // get the last data of this characteristic
            int[] count = map.get(character);

            // if the object survives, increase the number of survivors
            // increase the total number
            map.replace(character, new int[]{count[0] + isSurvive, count[1] + 1});
        }
    }


    /**
     * Setter of array of scenarios, used for interactive mode
     *
     * @param scenarios outside scenarios
     */
    public void setScenarios(Scenario[] scenarios) {
        this.scenarios = scenarios;
    }

    /**
     * Set the name of audit
     *
     * @param name the name of the audit
     */
    public void setAuditType(String name) {
        auditType = name;
    }

    /**
     * Getter of audit type
     *
     * @return auditType
     */
    public String getAuditType() {
        return auditType;
    }

    /**
     * Override the output format
     *
     * @return the result of current decision (survive ratio of objects)
     */
    @Override
    public String toString() {
        if (runs == 0) {
            return "no audit available";
        } else {
            StringBuilder output = new StringBuilder();
            output.append("======================================\n");
            output.append("# ").append(getAuditType()).append(" Audit\n");
            output.append("======================================\n");
            output.append("- % SAVED AFTER ").append(runs).append(" RUNS\n");

            List<Map.Entry<String, String>> list = sortMap(map);
            for (Map.Entry<String, String> entry : list) {
                output.append(entry.getKey().toLowerCase()).append(": ").append(
                        entry.getValue()).append("\n");
            }
            output.append("--\n");
            output.append("average age: ").append(
                    String.format("%.2f", sumAge / survivorCount));

            return String.valueOf(output);
        }
    }

    /**
     * For sorting the output result from highest ratio to lowest,
     * and then the name of charateristics from smallest to biggest
     *
     * @param map the orignal result
     * @return a sorted list, each item contains one map entry (character, ratio)
     */
    public List<Map.Entry<String, String>> sortMap(HashMap<String, int[]> map) {

        // create a modified map to store the result of division
        Map<String, String> modifiedMap = new HashMap<>();
        for (String k : map.keySet()) {

            // if third number after dot is not 0, then we round up
            modifiedMap.putIfAbsent(k,new BigDecimal(String.format("%.3f",
                    (double) map.get(k)[0] / map.get(k)[1])).
                    setScale(2, RoundingMode.CEILING).toString());

            //new BigDecimal((double) map.get(k)[0] / map.get(k)[1]).
            // setScale(2, RoundingMode.CEILING).toString()
            //String.format("%.2f", (double) map.get(k)[0] / map.get(k)[1]));
        }

        // create a list to sort the modified map
        List<Map.Entry<String, String>> list =
                new ArrayList<>(modifiedMap.entrySet());
        list.sort((o1, o2) -> {

            // if ratio is the same, then compare the characteristic
            if (o2.getValue().compareTo(o1.getValue()) == 0) {
                return o1.getKey().toLowerCase().compareTo(o2.getKey().toLowerCase());
            }
            return o2.getValue().compareTo(o1.getValue());
        });

        return list;
    }

    /**
     * Prints the summary returned by the toString() to the command-line
     */
    public void printStatistic() {
        System.out.println(toString());
    }


    /**
     * Save the result to the file
     *
     * @param filepath the path to store the result
     */
    public void printToFile(String filepath) {

        try {
            PrintWriter outputStream =
                    new PrintWriter(new FileOutputStream(filepath, true));
            outputStream.println(toString());
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: could not print results. " +
                    "Target directory does not exist.");
        }
    }
}
