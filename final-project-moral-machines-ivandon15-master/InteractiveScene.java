import ethicalengine.Scenario;
import ethicalengine.ScenarioGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A new added class to deal with the test under interactive mode
 * <p>
 * StudentID: 1150027
 * Username: Ivandon15
 *
 * @author YiFan Deng
 */
public class InteractiveScene {
    public static Scanner sc = new Scanner(System.in);
    private boolean isValid = false;
    private boolean isConfig;
    private String consent;
    private ArrayList<Scenario> scenarioList = new ArrayList<>();

    public InteractiveScene() {
    }

    /**
     * Constructor to initialize an interactive mode, if isConfig is false, then it
     * means this is only an interactive mode not with configuration
     *
     * @param scenarioList scenarios imported from csv file
     * @param isConfig     if is through configuration
     */
    public InteractiveScene(ArrayList<Scenario> scenarioList, boolean isConfig) {
        this.scenarioList = scenarioList;
        this.isConfig = isConfig;
    }

    /**
     * To run the interactive mode
     */
    public void run() {

        // output welcome and consent message
        welcomeMessage();
        while (!isValid) {
            try {
                consent = consentMessage();
            } catch (InvalidInputException e) {
                System.out.print("Invalid response. ");
            }
        }

        Audit audit = new Audit();
        audit.setAuditType("User");
        boolean isContinue = true;

        // in case it is not -i -c mode
        ScenarioGenerator generator = new ScenarioGenerator();

        // check if we meet the end of the config
        boolean meetEnd = false;

        // there is no config file
        if (!isConfig) {

            // generate 3 scenarios
            for (int i = 0; i < 3; i++) {
                scenarioList.add(generator.generate());
            }

            // if it's randomly generating, we can have infinite
            // scenarios if users want to play
            while (isContinue) {
                audit.setScenarios(scenarioList.toArray(new Scenario[0]));
                audit.interactiveRun(consent);
                System.out.println("Would you like to continue? (yes/no)");
                String ans;

                // this ans is to answer if continuing
                ans = sc.nextLine();

                if (ans.equalsIgnoreCase("yes")) {

                    // create a brand new list of scenarios
                    scenarioList = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        scenarioList.add(generator.generate());
                    }
                } else {
                    isContinue = false;
                }

            }
        }

        // it's a config file
        else {

            // check if the file only contains no more than 3 scenarios
            // then we just need one round of choice
            if (scenarioList.size() <= 3) {
                audit.setScenarios(scenarioList.toArray(new Scenario[0]));
                audit.interactiveRun(consent);

            } else {

                // a pointer to point current index of the first scenarios
                int point = 0;
                while (point < scenarioList.size() && isContinue) {
                    ArrayList<Scenario> subset = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        if (i + point < scenarioList.size()) {
                            subset.add(scenarioList.get(i + point));
                        } else {

                            // there is no more scenarios in config file
                            meetEnd = true;
                            break;
                        }
                    }
                    audit.setScenarios(subset.toArray(new Scenario[0]));
                    audit.interactiveRun(consent);
                    System.out.println("Would you like to continue? (yes/no)");

                    // this ans is to answer if continuing
                    String ans = sc.nextLine();

                    if (ans.equalsIgnoreCase("no")) {
                        isContinue = false;
                    }
                    point += 3;

                    // if there are 3n scenarios in config file
                    if (point == scenarioList.size()) {
                        meetEnd = true;
                    }
                }
            }
        }


        // If the config file does not contain any more scenarios, the final
        // statistic is shown followed by the exit prompt
        if (meetEnd) {
            audit.printStatistic();
        }
        System.out.println("That's all. Press Enter to quit.");
        sc.nextLine();
        System.exit(0);
    }

    /**
     * To show the welcome messages
     */
    public void welcomeMessage() {
        try {

            // if I need test using command line I should add
            // 'final-project-moral-machines-ivandon15' before welcome.ascii
            BufferedReader br = new BufferedReader(new FileReader("welcome.ascii"));

            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Output consent message and ask for answer
     *
     * @return a String, "yes" means that user agrees with save data
     * @throws InvalidInputException deal with unexpected answer
     */
    public String consentMessage() throws InvalidInputException {
        System.out.println(
                "Do you consent to have your decisions saved to a file? (yes/no)");
        String ans = sc.nextLine();
        if (!(ans.equalsIgnoreCase("yes") ||
                ans.equalsIgnoreCase("no"))) {
            throw new InvalidInputException();
        } else {
            isValid = true;
            return ans;
        }
    }

    /**
     * A exception class to deal with unexpected answer
     */
    private class InvalidInputException extends Throwable {

        public InvalidInputException(String message) {
            super(message);
        }

        public InvalidInputException() {
            super();
        }
    }
}
