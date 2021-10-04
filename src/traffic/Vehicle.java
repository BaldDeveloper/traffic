package traffic;

import java.util.*;

/**
 *  {@code Vehicle} is an abstract class defining things
 *  that travel on our roads.
 *  @version 2020091800
 *  @author Richard Barton
 */
public abstract class Vehicle {
    private final int           identifier;
    private static int          nextIdentifier;
    private final String        type;
    private final int           hashCode;
    private static final Random ourRandom       = new Random();

    /**
     *  @param type required type of vehicle
     */
    public Vehicle(String type)
    {
        identifier = nextIdentifier++;
        this.type = type;
        hashCode = Objects.hash(identifier, type);
    }

    /**
     *  @return the unique identifier of the vehicle
     */
    public int  getIdentifier()
    {
        return(identifier);
    }

    /**
     *  @return the type of the vehicle
     */
    public String       getType()
    {
        return(type);
    }

    /**
     *  @return an instance of a randomly selected subclass
     *  of {@code Vehicle} or {@code null}.
     *  Roughly 20% of the time we'll return a {@code Car},
     *  5% of the time we'll return a {@code Motorcycle}
     *  and the rest of the time we'll return {@code null}.
     */
    static public Vehicle      nextRandom()
    {
        int             nextRandom;

        /*
         *  Get a random number between 0 and 99, inclusive.
         */
        nextRandom = ourRandom.nextInt(100);
        if (nextRandom < 20) {
            /*
             *  20% cars
             */
            return(new Car());
        }

        if (nextRandom < 25) {
            /*
             *  5% motorcycles
             */
            return(new Motorcycle());
        }

        return(null);
    }

    /**
     *  Compare two {@code Vehicle} objects
     *  @param otherObject the object to compare to this object
     *  @return equal or not
     */
    public boolean equals(Object otherObject)
    {
        Vehicle otherMe;

        if (otherObject == null) {
            /*
             *  The caller didn't give us anything.
             */
            return(false);
        }

        if (this == otherObject) {
            /*
             *  They're at the same address!
             */
            return(true);
        }

        if (getClass() != otherObject.getClass()) {
            /*
             *  They're different classes.
             */
            return(false);
        }

        /*
         *  We know the otherObject must be a non-null reference to
         *  this class.
         */
        otherMe = (Vehicle)otherObject;

        /*
         *  We're going to trust identifier to be unique.
         */
        if (identifier != otherMe.identifier) {
            /*
             *  They're different types.
             */
            return(false);
        }

        return(true);
    }

    /**
     *  @return object's hash code
     */
    public int hashCode()
    {
        return(hashCode);
    }

    /**
     *  @return {@code String} representation of object
     */
    public String toString()
    {
        return(getClass().getName() +
                "[identifier=" + identifier +
                ",type=" + type + "]");
    }

    /**
     *  Performs a unit test on the {@code Vehicle} class
     *  by instantiating all subclasses and testing the
     *  methods.
     *  @param args arguments to the unit test
     */
    public static void main(String[] args)
    {
        int     errors;
        Vehicle vehicle[]  = {new Car(),
                new Motorcycle(),
                new Car(),
                new Motorcycle()};
        int     i;
        BitSet  usedIdentifiers;
        String  typeName[]      = {"car", "motorcycle", "null"};
        int     typeCount[]     = {0, 0, 0};
        int     typePercent[]   = {20, 5, 75};
        int     randomCount;

        errors = 0;
        for (Vehicle which : vehicle) {
            String      toString;
            String      type;
            int         identifier;

            /*
             *  For each vehicle, output its toString() and
             *  its type and identifier.
             */
            toString = "" + which;
            type = which.getType();
            identifier = which.getIdentifier();
            System.out.println(toString);
            System.out.println(type + ": " + identifier);

            /*
             *  See if the toString() return value is
             *  formatted correctly.  Look for the type
             *  preceded by an =.
             */
            if ((i = toString.indexOf("=" + type)) < 0) {
                System.out.println("*** ERROR *** Didn't find type (" +
                        type + ") in toString() value");
                ++errors;
            } else {
                int     j;

                /*
                 *  Found the type with a = in front.
                 *  See what the variable name is.
                 */
                for (j = i - 1; (j >= 0); --j) {
                    char    thisChar;

                    thisChar = toString.charAt(j);
                    if (Character.isJavaIdentifierPart(thisChar) ==
                            false) {
                        for (++j; (j < i); ++j) {
                            thisChar = toString.charAt(j);
                            if (Character.
                                    isJavaIdentifierStart(thisChar) ==
                                    true) {
                                break;
                            }
                        }
                        break;
                    }
                }
                System.out.println("type must be stored in " +
                        toString.substring(j, i));
            }

            /*
             *  Look for the identifier.
             */
            if ((i = toString.indexOf("=" + identifier)) < 0) {
                System.out.println("*** ERROR *** Didn't find" +
                        " identifier (" + identifier +
                        ") in toString() value");
                ++errors;
            } else {
                int     j;

                /*
                 *  Found the identifier with a = in front.
                 *  See what the variable name is.
                 */
                for (j = i - 1; (j >= 0); --j) {
                    char    thisChar;

                    thisChar = toString.charAt(j);
                    if (Character.isJavaIdentifierPart(thisChar) ==
                            false) {
                        for (++j; (j < i); ++j) {
                            thisChar = toString.charAt(j);
                            if (Character.
                                    isJavaIdentifierStart(thisChar) ==
                                    true) {
                                break;
                            }
                        }
                        break;
                    }
                }
                System.out.println("identifier must be stored in " +
                        toString.substring(j, i));
            }

            for (Vehicle other : vehicle) {
                boolean compareIdentifiers;
                boolean compareEquals;
                boolean compareTypes;

                /*
                 *  For every other vehicle in the array, including
                 *  this one, make sure the equals() and hashCode()
                 *  methods do what they're supposed to.
                 *
                 *  We're counting on the identifier to be unique.
                 */
                compareIdentifiers = which.getIdentifier() ==
                        other.getIdentifier();
                compareEquals = which.equals(other);
                compareTypes = which.getType().equals(other.getType());

                /*
                 *  Be verbose.
                 */
                System.out.print(which + ".equals(" + other +
                        ") : ");
                if (compareEquals == true) {
                    /*
                     *  So, equals() says they're equal.
                     */
                    System.out.println("true");
                    if (compareIdentifiers != true) {
                        /*
                         *  But their identifiers are different.
                         */
                        System.out.println("*** ERROR *** but" +
                                " identifiers are" +
                                " different: " +
                                which.getIdentifier() +
                                " != " +
                                other.getIdentifier());
                        ++errors;
                    }
                    if (compareTypes != true) {
                        /*
                         *  But their types are different.
                         */
                        System.out.println("*** ERROR *** but" +
                                " types are" +
                                " different: " +
                                which.getType() +
                                " != " +
                                other.getType());
                        ++errors;
                    }
                } else {
                    /*
                     *  So, equals() says they aren't.
                     */
                    System.out.println("false");
                    if (compareIdentifiers != false) {
                        /*
                         *  But their identifiers are the same.
                         */
                        System.out.println("*** ERROR *** but" +
                                " identifiers are" +
                                " the same: " +
                                which.getIdentifier() +
                                " == " +
                                other.getIdentifier());
                        ++errors;
                    }
                }

                System.out.print(which + ".hashCode() == " + other +
                        ".hashCode() : ");
                if (which.hashCode() == other.hashCode()) {
                    /*
                     *  They have the same hash code.
                     */
                    System.out.println("true");
                } else {
                    /*
                     *  They have different hash codes.
                     */
                    System.out.println("false");
                    if (compareEquals != false) {
                        /*
                         *  But equals() says they're the same.
                         */
                        System.out.println("*** ERROR *** but" +
                                " equals() is true");
                        ++errors;
                    }
                }
            }
        }

        /*
         *  We're going to instantiate a lot of instantances
         *  and make sure the identifiers don't get duplicated.
         */
        i = 1024 * 1024;
        usedIdentifiers = new BitSet(i);
        for (Vehicle which : vehicle) {
            /*
             *  Add the instantances we've already created to our
             *  database.
             */
            usedIdentifiers.set(which.getIdentifier());
        }
        for (; (i > 0); --i) {
            int         identifier;
            Vehicle     which;

            /*
             *  Take turns making cars and motorcycles.
             */
            if ((i & 0x1) == 0) {
                which = new Car();
            } else {
                which = new Motorcycle();
            }

            /*
             *  Get this instance's identifier and see if it's
             *  already in our database.
             */
            identifier = which.getIdentifier();
            if (usedIdentifiers.get(identifier) == true) {
                /*
                 *  We've seen this identifier before.
                 */
                System.out.println(which + ": duplicate identifier(" +
                        identifier + ")");
                ++errors;
                continue;
            }

            /*
             *  Add this identifier to our database.
             */
            usedIdentifiers.set(identifier);
        }

        /*
         *  Count the number and calculate the ratios of
         *  the vehicle types returned by nextRandom().
         */
        for (randomCount = 0; (randomCount < 1000000); ++randomCount) {
            Vehicle     nextRandomVehicle;
            String      nextRandomVehicleType;

            /*
             *  Instantiate another random vehicle.  Assume its type
             *  is null.
             */
            nextRandomVehicle = Vehicle.nextRandom();
            nextRandomVehicleType = "null";
            if (nextRandomVehicle != null) {
                /*
                 *  Get the vehicle's type.
                 */
                nextRandomVehicleType = nextRandomVehicle.getType();
            }

            /*
             *  Let's find the vehicle in our array.
             */
            for (i = 0; (i < typeName.length); ++i) {
                if (typeName[i].equals(nextRandomVehicleType) ==
                        true) {
                    /*
                     *  Found it.  Exit the loop.
                     */
                    break;
                }
            }

            if (i < typeName.length) {
                /*
                 *  Found it.  Increment its count.
                 */
                ++typeCount[i];
            } else {
                /*
                 *  We went through the whole array and didn't find an
                 *  entry for this type.  We need a bigger array.
                 */
                System.out.println("*****  type arrays" +
                        " too small  *****");
                ++errors;
                break;
            }
        }

        /*
         *  Display the types, counts and ratios.
         */
        for (i = 0; (i < typeName.length); ++i) {
            int         difference;
            double      percent;

            /*
             *  Calculate the percentage of the total for this type and
             *  display the results.
             */
            percent = ((double)typeCount[i] / randomCount) * 100;
            System.out.println(typeName[i] + ": " + typeCount[i] +
                    "/" + randomCount + "(" +
                    percent + "%)");

            /*
             *  Calculate a positive difference between the actual
             *  percentage and what's expected.
             */
            difference = (int)(percent + 0.5) - typePercent[i];
            if (percent < typePercent[i]) {
                difference = typePercent[i] - (int)(percent + 0.5);
            }

            if (difference > 5) {
                /*
                 *  Complain about too big a difference.
                 */
                System.out.println("**** ERROR: percentage not close" +
                        " enough to " + typePercent[i] +
                        "%");
                ++errors;
            }
        }

        if (errors > 0) {
            /*
             *  We found a problem during unit test.
             */
            System.out.println("\nUNIT TEST FAILED with " + errors +
                    " errors");
            System.exit(1);
        }
    }
}
