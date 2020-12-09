package ethicalengine;

/**
 * Human class derived from Persona to describe human
 * <p>
 * StudentID: 1150027
 * Username: Ivandon15
 *
 * @author YiFan Deng
 */
public class Human extends Persona {

    /**
     * Enum of Profession
     */
    public enum Profession {
        DOCTOR, CEO, SCIENTIST, POLICE, DISABLED,
        CRIMINAL, HOMELESS, UNEMPLOYED, NONE
    }

    /**
     * Enum of age category
     */
    public enum AgeCategory {BABY, CHILD, ADULT, SENIOR}

    // class variables
    private Profession profession = Profession.NONE;
    private AgeCategory ageCategory;
    private boolean isPregnant;
    private boolean isYou;

    /**
     * Empty Constructor
     */
    public Human() {
        super();
    }

    /**
     * Calling parent's constructor and initialize some info
     *
     * @param age        person's age
     * @param profession person's profession
     * @param gender     person's gender
     * @param bodyType   person's body type
     * @param isPregnant true means pregnant
     */
    public Human(int age, Profession profession, Gender gender,
                 BodyType bodyType, boolean isPregnant) {
        super(age, gender, bodyType);
        ageCategory = getAgeCategory();

        // need use setter because there are constrains
        this.setProfession(profession);
        this.setPregnant(isPregnant);
    }

    /**
     * A more general used constructor for myself
     *
     * @param age      person's age
     * @param gender   person's gender
     * @param bodyType person's body type
     */
    public Human(int age, Gender gender, BodyType bodyType) {
        super(age, gender, bodyType);
        ageCategory = getAgeCategory();
        this.profession = Profession.NONE;
        this.isPregnant = false;
    }

    /**
     * Copy constructor
     *
     * @param otherHuman copied human object
     */
    public Human(Human otherHuman) {
        super(otherHuman.getAge(), otherHuman.getGender(),
                otherHuman.getBodyType());
        ageCategory = otherHuman.getAgeCategory();
        this.setAsYou(otherHuman.isYou);
        this.setProfession(otherHuman.profession);
        this.setPregnant(otherHuman.isPregnant);
    }

    /**
     * getter of agecategory
     *
     * @return according to human's age return corresponding
     * age category
     */
    public AgeCategory getAgeCategory() {
        if (this.getAge() >= 0 && this.getAge() <= 4) {
            return AgeCategory.BABY;
        } else if (this.getAge() >= 5 && this.getAge() <= 16) {
            return AgeCategory.CHILD;
        } else if (this.getAge() >= 17 && this.getAge() <= 68) {
            return AgeCategory.ADULT;
        } else {
            return AgeCategory.SENIOR;
        }
    }

    /**
     * getter of Profession
     *
     * @return return none if this human is not an adult
     */
    public Profession getProfession() {
        return profession;
    }

    /**
     * setter of Profession
     *
     * @param profession the profession of adult
     */
    public void setProfession(Profession profession) {
        if (this.ageCategory == AgeCategory.ADULT) {
            this.profession = profession;
        } else {
            this.profession = Profession.NONE;
        }
    }

    /**
     * Check if this human is pregnant
     *
     * @return only female can be pregnant and check
     * if female is pregnant
     */
    public boolean isPregnant() {
        if (this.getGender() != Gender.FEMALE) {
            return false;
        } else {
            return isPregnant;
        }
    }

    /**
     * Set the state of pregnant or not, and take care
     * when this human is male
     *
     * @param pregnant true means pregnant
     */
    public void setPregnant(boolean pregnant) {

        // if we set it to pregnant
        if (pregnant) {

            // check if this human is female
            this.isPregnant = this.getGender() == Gender.FEMALE;
        } else {
            this.isPregnant = false;
        }

    }

    /**
     * Check if user is one of humans in the accident
     *
     * @return true if is you
     */
    public boolean isYou() {
        return isYou;
    }

    /**
     * The setter of isYou
     *
     * @param isYou if the human is the user, set to true
     */
    public void setAsYou(boolean isYou) {
        this.isYou = isYou;
    }

    /**
     * A setter sets all the information from csv
     *
     * @param gender     human's gender
     * @param bodytype   human's body type
     * @param profession human's profession
     * @param isPregnant if pregnant
     * @param lineNum    current row number in csv file
     * @throws InvalidCharacteristicException check invalid characteristic
     */
    public void setHuman(String gender, String bodytype, String profession,
                         boolean isPregnant, int lineNum)
            throws InvalidCharacteristicException {

        // set the information for human
        ageCategory = getAgeCategory();
        boolean hasGender = false, hasBodyType = false, hasPro = false;

        // check if the gender, bodytype and profession are in enum
        for (Gender g : Gender.values()) {
            if (g.name().equalsIgnoreCase(gender)) {
                setGender(g);
                hasGender = true;
                break;
            }
        }

        setPregnant(isPregnant);

        for (BodyType bt : BodyType.values()) {
            if (bt.name().equalsIgnoreCase(bodytype)) {
                setBodyType(bt);
                hasBodyType = true;
                break;
            }
        }
        for (Profession p : Profession.values()) {
            if (p.name().equalsIgnoreCase(profession)) {
                setProfession(p);
                hasPro = true;
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
        if (!hasPro) {
            setProfession(Profession.NONE);
        }

        // throw the exception
        if (!(hasBodyType && hasGender && hasPro)) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in config file in line " + lineNum);
        }
    }

    /**
     * Override and output a new format
     *
     * @return a String type
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        // if the user is the human
        if (isYou) {
            output.append("you ");
        }

        // change enum to lowercase
        output.append(getBodyType().name().toLowerCase())
                .append(" ").append(ageCategory.name().toLowerCase());

        // only ADULT has a profession
        // if an adult's profession is NONE we don't output
        if (ageCategory == AgeCategory.ADULT) {
            if (!(profession == Profession.NONE)) {
                output.append(" ").append(profession.name().toLowerCase());
            }
        }

        output.append(" ").append(getGender().name().toLowerCase());

        // use isPregnant() to keep safety check
        if (isPregnant()) {
            output.append(" pregnant");
        }

        return String.valueOf(output);
    }
}
