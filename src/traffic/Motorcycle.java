package traffic;

/**
 *  This object implements a {@code Vehicle} of type {@code Motorcycle}.
 *  @version 20200907
 *  @author Richard Barton
 */
public class Motorcycle extends Vehicle {
    private static final String type    = "motorcycle";

    /**
     *  Performs a unit test on the {@code Motorcycle} class
     *  @param args arguments to the unit test
     */
    public static void main(String[] args)
    {
        int             errors;
        Vehicle[]       motorcycle;

        motorcycle = new Motorcycle[2];

        /*
         *  Instantiate two Motorcycles to test with.
         */
        motorcycle[0] = new Motorcycle();
        motorcycle[1] = new Motorcycle();

        errors = 0;
        for (Vehicle which : motorcycle) {
            int         whichIdentifier;
            String      whichType;

            /*
             *  For each Motorcycle, get some interesting return values
             *  from accessor methods.  Then, show the user what we're
             *  testing.
             */
            whichType = which.getType();
            whichIdentifier = which.getIdentifier();
            System.out.println(whichType + ": " + whichIdentifier);

            if (whichType.equals(type) == false) {
                /*
                 *  The type is wrong.
                 */
                System.out.println("** ERROR type '" + whichType +
                        "' != '" + type + "'");
                ++errors;
            }

            for (Vehicle other : motorcycle) {
                int     otherIdentifier;

                /*
                 *  Go through all the Motorcycles, again, and compare
                 *  them with which.  First, get the identifier.
                 */
                otherIdentifier = other.getIdentifier();

                if (other == which) {
                    /*
                     *  We're looking at the same instance.
                     */
                    if (whichIdentifier != otherIdentifier) {
                        /*
                         *  But, the identifier is different.
                         */
                        System.out.println("** ERROR " + which +
                                ".getIdentifier() != " +
                                other +
                                ".getIdentifier() and" +
                                " should");
                        ++errors;
                    }

                    if (which.equals(other) != true) {
                        /*
                         *  equals() says they're different.
                         */
                        System.out.println("** ERROR " + which +
                                ".equals(" + other +
                                ") != true and should");
                        ++errors;
                    }
                } else {
                    /*
                     *  We're looking at different instances.
                     */
                    if (whichIdentifier == otherIdentifier) {
                        /*
                         *  But, the identifier is the same.
                         */
                        System.out.println("** ERROR " + which +
                                ".getIdentifier() == " +
                                other +
                                ".getIdentifier() and" +
                                " shouldn't");
                        ++errors;
                    }

                    if (which.equals(other) == true) {
                        /*
                         *  equals() says they're the same.
                         */
                        System.out.println("** ERROR " + which +
                                ".equals(" + other +
                                ") == true and shouldn't");
                        ++errors;
                    }
                }
            }
        }

        if (errors > 0) {
            System.out.println("** UNIT TEST FAILED with " + errors +
                    " errors");
            System.exit(1);
        }
    }

    /**
     *  Construct an instance of a {@code Motorcycle}.
     */
    public Motorcycle()
    {
        super(type);
    }
}
