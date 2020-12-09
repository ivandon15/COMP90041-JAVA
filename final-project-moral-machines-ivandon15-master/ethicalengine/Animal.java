package ethicalengine;

/**
 * Animal class derived from Persona to describe animal
 * <p>
 * StudentID: 1150027
 * Username: Ivandon15
 *
 * @author YiFan Deng
 */
public class Animal extends Persona {

    /**
     * Enum of species
     */
    public enum Species {CAT, DOG, BIRD, DEER, BEAR, FERRET}

    // class variables
    private String species;
    private boolean isPet;

    /**
     * Empty constructor
     */
    public Animal() {
        super();
    }

    /**
     * Constructor initializing species of animals
     *
     * @param species the species of animal
     */
    public Animal(String species) {
        this.species = species;
    }

    /**
     * Constructor initializing all the information of animals
     *
     * @param age      its age
     * @param species  its species
     * @param gender   its gender
     * @param bodytype its body type
     * @param isPet    whether a pet
     */
    public Animal(int age, String species, Gender gender,
                  BodyType bodytype, boolean isPet) {
        super(age, gender, bodytype);
        this.species = species;
        this.isPet = isPet;
    }

    /**
     * Copy constructor
     *
     * @param otherAnimal copied animal
     */
    public Animal(Animal otherAnimal) {
        super(otherAnimal.getAge(), otherAnimal.getGender(),
                otherAnimal.getBodyType());
        this.species = otherAnimal.species;
        this.isPet = otherAnimal.isPet;
    }

    // getters and setters
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isPet() {
        return isPet;
    }

    public void setPet(boolean isPet) {
        this.isPet = isPet;
    }

    /**
     * a setter sets all the information from csv
     *
     * @param gender   animal's gender
     * @param bodytype animal's bodytype
     * @param species  animal's species
     * @param lineNum  current row number in csv file
     * @throws InvalidCharacteristicException check invalid characteristic
     */
    public void setAnimal(String gender,String bodytype, String species, int lineNum)
            throws InvalidCharacteristicException {

        // set the information for animal
        boolean hasGender = false, hasBodyType = false, hasSpecies = false;

        // check if the gender, bodytype and species are in enum
        for (Gender g : Gender.values()) {
            if (g.name().equalsIgnoreCase(gender)) {
                setGender(g);
                hasGender = true;
                break;
            }
        }
        for (BodyType bt : BodyType.values()) {
            if (bt.name().equalsIgnoreCase(bodytype)) {
                setBodyType(bt);
                hasBodyType = true;
                break;
            }
        }
        for (Species s : Species.values()) {
            if (s.name().equalsIgnoreCase(species)) {
                setSpecies(species);
                hasSpecies = true;
                break;
            }
        }

        // if they are not in enum, set them to default value
        if (!hasGender) {
            setGender(Gender.UNKNOWN);
        }
        if (!hasBodyType) {
            setBodyType(BodyType.UNSPECIFIED);
        }
        if (!hasSpecies) {
            setSpecies("cat");
        }

        // throw the exception
        if (!(hasBodyType && hasGender && hasSpecies)) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in config file in line " + lineNum);
        }
    }

    /**
     * Override and output new format
     *
     * @return the output
     */
    @Override
    public String toString() {
        String output = species.toLowerCase();
        if (isPet) {
            output += " is pet";
        }
        return output;
    }
}
