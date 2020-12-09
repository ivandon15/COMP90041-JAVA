package ethicalengine;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a scenario to decide on
 * COMP90041, Sem2, 2020: Final Project
 * <p>
 * StudentID: 1150027
 * Username: Ivandon15
 *
 * @author: tilman.dingler@unimelb.edu.au and  YiFan Deng
 */
public class Scenario {

    // class variables
    private ArrayList<Persona> passengers;
    private ArrayList<Persona> pedestrians;
    private boolean isLegalCrossing;

    /**
     * Empty Constructor
     */
    public Scenario() {

    }


    /**
     * Constructor for initializing passengers and pedestrians with
     * arrays and whether is legally crossing
     *
     * @param passengers      passengers' array
     * @param pedestrians     pedestrians' array
     * @param isLegalCrossing whether is legally crossing
     */
    public Scenario(Persona[] passengers, Persona[] pedestrians,
                    boolean isLegalCrossing) {
        this.passengers = new ArrayList<>(Arrays.asList(passengers));
        this.pedestrians = new ArrayList<>(Arrays.asList(pedestrians));
        this.isLegalCrossing = isLegalCrossing;
    }

    // easy for myself
    public Scenario(ArrayList<Persona> passengers, ArrayList<Persona> pedestrians,
                    boolean isLegalCrossing) {
        this.passengers = passengers;
        this.pedestrians = pedestrians;
        this.isLegalCrossing = isLegalCrossing;
    }

    /**
     * Check if you are in the car
     *
     * @return in the car return true
     */
    public boolean hasYouInCar() {

        // iterate all the passengers
        for (Persona passenger : passengers) {

            // check if the passenger is a human
            if (passenger.getClass() == Human.class) {

                // check if this human is you
                if (((Human) passenger).isYou()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if you among pedestrians
     *
     * @return true if you are among pedestrians
     */
    public boolean hasYouInLane() {
        // iterate all the passengers
        for (Persona pedestrian : pedestrians) {

            // check if the passenger is a human
            if (pedestrian.getClass() == Human.class) {

                // check if this human is you
                if (((Human) pedestrian).isYou()) {
                    return true;
                }
            }
        }
        return false;
    }

    // two get lists is easy for myself
    public ArrayList<Persona> getPassengersList() {
        return passengers;
    }

    public ArrayList<Persona> getPedestriansList() {
        return pedestrians;
    }

    // getters and setters
    public Persona[] getPassengers() {
        return passengers.toArray(new Persona[0]);
    }

    public Persona[] getPedestrians() {
        return pedestrians.toArray(new Persona[0]);
    }

    public boolean isLegalCrossing() {
        return isLegalCrossing;
    }

    public void setLegalCrossing(boolean isLegalCrossing) {
        this.isLegalCrossing = isLegalCrossing;
    }

    public int getPassengerCount() {
        return passengers.size();
    }

    public int getPedestrianCount() {
        return pedestrians.size();
    }

    /**
     * Override and output a new format
     *
     * @return all the information
     */
    @Override
    public String toString() {
        StringBuilder output =
                new StringBuilder("======================================\n" +
                        "# Scenario\n" +
                        "======================================\n" +
                        "Legal Crossing: ");

        output.append((isLegalCrossing) ? "yes\n" : "no\n");

        output.append("Passengers (").append(getPassengerCount()).append(")\n");
        for (Persona passenger : passengers) {
            output.append("- ").append(passenger.toString()).append("\n");
        }

        output.append("Pedestrians (").append(getPedestrianCount()).append(")\n");
        for (Persona pedestrian : pedestrians) {
            output.append("- ").append(pedestrian.toString()).append("\n");
        }
        output.deleteCharAt(output.lastIndexOf("\n"));

        return String.valueOf(output);
    }
}