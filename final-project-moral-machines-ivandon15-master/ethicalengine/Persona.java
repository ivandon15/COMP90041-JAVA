package ethicalengine;

/**
 * A basic class to describe the personality of live object
 * <p>
 * StudentID: 1150027
 * Username: Ivandon15
 *
 * @author YiFan Deng
 */
public abstract class Persona {

    /**
     * Enum of gender
     */
    public enum Gender {FEMALE, MALE, UNKNOWN}

    /**
     * Enum of body typr
     */
    public enum BodyType {AVERAGE, ATHLETIC, OVERWEIGHT, UNSPECIFIED}

    // class invariants
    private int age;

    // set to default value
    private Gender gender = Gender.UNKNOWN;
    private BodyType bodyType = BodyType.UNSPECIFIED;

    /**
     * Empty constructor
     */
    public Persona() {
    }

    /**
     * Default constructor that can initialize some info
     *
     * @param age      person's age
     * @param gender   person's gender
     * @param bodyType person's body type
     */
    public Persona(int age, Gender gender, BodyType bodyType) {
        this.age = age;
        this.gender = gender;
        this.bodyType = bodyType;
    }

    /**
     * Copy constructor
     *
     * @param otherPersona another Persona Object
     */
    public Persona(Persona otherPersona) {
        this.age = otherPersona.age;
        this.gender = otherPersona.gender;
        this.bodyType = otherPersona.bodyType;
    }

    // a set of getters and setters
    public int getAge() {
        return age;
    }

    /**
     * Setter of age
     * @param age the age of the object
     * @throws NumberFormatException deal with when age is less than 0
     */
    public void setAge(int age) throws NumberFormatException{
        if (age >= 0) {
            this.age = age;
        }else {
            throw new NumberFormatException(
                    "WARNING: Age should be 0 or a positive number.");
        }
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }
}
