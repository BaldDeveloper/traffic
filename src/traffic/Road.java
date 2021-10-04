package traffic;

import java.util.*;

/**
 *  {@code Road} is class that allows vehicular traffic.
 *  @version 2020092700
 *  @author Richard Barton
 */
public class Road {
    private final int           identifier;
    private static int          nextIdentifier;
    private final String        name;
    private final int           carCount;
    private final String        flow;
    private final int           hashCode;

    private Lane        lane;

    /*
     *  This class manages individual lanes of a road.
     */
    private class       Lane {
        private final boolean   reverseFlow;
        private final Vehicle   lane[];

        /*
         *  flow will determine the order in which Vehicles are
         *  accessed in accessor methods.
         */
        public Lane(boolean reverseFlow) {
            this.reverseFlow = reverseFlow;
            lane = new Vehicle[carCount];
        }

        public Vehicle vehicleAt(int which)
        {
            if ((which < 0) || (which >= carCount)) {
                return(null);
            }

            if (reverseFlow == true) {
                /*
                 *  Reverse the order of Vehicles.
                 */
                which = carCount - which - 1;
            }

            return(lane[which]);
        }

        /*
         *  Cause the instance to adjust the position of its vehicles.
         */
        public void tick()
        {
            int             i;
            int             j;

            /*
             *  We'll use the high indices as the outgoing end
             *  of the road and index 0 as the incoming end of
             *  the road.  For a vehicle to advance, the "slot" in
             *  front of it must be empty.  So, we'll start at the
             *  outgoing end.
             */
            for (i = carCount - 1, j = i - 1; (i > 0); --i, --j) {
                if (lane[i] != null) {
                    /*
                     *  This "slot" in the road is not empty.  So, we
                     *  can't advance the vehicle behind us into this
                     *  slot.
                     */
                    continue;
                }

                /*
                 *  This slot is empty so advance the next Vehicle.
                 *  Advance the next vehicle to this slot and empty
                 *  its former slot.
                 */
                lane[i] = lane[j];
                lane[j] = null;
            }
            if ((carCount > 0) && (lane[0] == null)) {
                /*
                 *  The incoming slot is empty so get a Vehicle for it.
                 */
                lane[0] = Vehicle.nextRandom();
            }
        }
    }

    /**
     *  @param name required name of road
     *  @param carCount required number of cars in one direction
     *  @param reverseFlow describes whether vehicles should travel the
     *  other way.  The convention will be that reverseFlow will be
     *  false for vehicles traveling north or east while reverseFlow
     *  will be true for vehicles traveling south or west.
     */
    public Road(String name, int carCount, boolean reverseFlow)
    {
        int     i;

        identifier = nextIdentifier++;
        this.name = name;
        if (carCount < 0) {
            carCount = 0;
        }
        this.carCount = carCount;
        if (reverseFlow == true) {
            this.flow = "SW";
        } else {
            this.flow = "NE";
        }
        hashCode = Objects.hash(identifier, name, carCount, flow);

        this.lane = new Lane(reverseFlow);
    }

    /**
     *  @return the unique identifier of the road
     */
    public int  getIdentifier()
    {
        return(identifier);
    }

    /**
     *  @return the name of the road
     */
    public String       getName()
    {
        return(name);
    }

    /**
     *  @return a {@code String} representation of the
     *  vehicles on this instance of the road.
     */
    public String snapshot()
    {
        int     i;
        int     whichVehicle;
        char    charsOfVehicles[];
        Vehicle thisVehicle;

        /*
         *  Need to build a string of characters.
         */
        charsOfVehicles = new char[carCount];

        for (whichVehicle = 0; (whichVehicle < carCount);
             ++whichVehicle) {
            /*
             *  Assume this "slot" in the road is empty.
             */
            charsOfVehicles[whichVehicle] = '_';
            thisVehicle = lane.vehicleAt(whichVehicle);
            if (thisVehicle != null) {
                /*
                 *  There's a vehicle in this slot in the road.
                 *  Get the first character of its type to use
                 *  to represent the vehicle on the road.
                 */
                charsOfVehicles[whichVehicle] =
                        thisVehicle.getType().charAt(0);
            }
        }

        /*
         *  Instantiate a String of our characters.
         */
        return(new String(charsOfVehicles));
    }

    /**
     *  Cause the {@code Road} instance to adjust the
     *  position of its vehicles.
     */
    public void tick()
    {
        /*
         *  The lane manages its own ticks.
         */
        lane.tick();
    }

    /**
     *  Compare two {@code Road} objects
     *  @param otherObject the object to compare to this object
     *  @return equal or not
     */
    public boolean equals(Object otherObject)
    {
        Road    otherMe;

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
        otherMe = (Road)otherObject;

        /*
         *  We're going to trust identifier to be unique.
         */
        if (identifier != otherMe.identifier) {
            /*
             *  They're different identifiers.
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
                ",name=" + name +
                ",carCount=" + carCount +
                ",flow=" + flow + "]");
    }

    /*
     *  Run some ticks with car configurations and make sure the road
     *  is behaving.
     */
    private static int testRoadTicks(int carCount,
                                     boolean reverseFlow)
    {
        int     errors;
        int     i;
        int     maxTicks;
        char    lastSnapshot[];
        String  roadName;
        Road    roadToTest;

        errors = 0;
        roadName = "Road with " + carCount + " cars, " +
                ((reverseFlow == false) ? "NE" : "SW") + " flow";

        roadToTest = new Road(roadName, carCount, reverseFlow);

        /*
         *  We'll run some number of ticks of the clock on the given
         *  road configuration to output what happens on the road.
         *  Then we'll compare each snapshot with the last to see if
         *  vehicles advanced as they should.
         */
        lastSnapshot = null;
        maxTicks = (3 * carCount) + 2;
        System.out.println("    " + roadToTest.getName());
        for (i = 0; (i < maxTicks); ++i) {
            int         j;
            int         k;
            String      snapshot;
            char        thisSnapshot[];

            roadToTest.tick();
            snapshot = roadToTest.snapshot();
            System.out.printf("%2d: %s\n", i, snapshot);

            thisSnapshot = snapshot.toCharArray();
            if (reverseFlow == true) {
                /*
                 *  To avoid having two different loops below that
                 *  simulate the tick by using the character array,
                 *  we'll just reverse the array when the flow is
                 *  reversed.
                 */
                for (j = 0, k = thisSnapshot.length - 1; (j < k);
                     ++j, --k) {
                    char        temp;

                    temp = thisSnapshot[j];
                    thisSnapshot[j] = thisSnapshot[k];
                    thisSnapshot[k] = temp;
                }
            }

            if (lastSnapshot != null) {
                /*
                 *  Advance the vehicles in the last snapshot to see if
                 *  this snapshot looks correct.
                 */
                for (j = lastSnapshot.length - 1, k = j - 1;
                     (k >= 0); --j, --k) {
                    if (lastSnapshot[j] != '_') {
                        /*
                         *  This slot isn't empty so we can't advance
                         *  anything to here.
                         */
                        continue;
                    }

                    /*
                     *  This slot must be empty.  Move the following
                     *  vehicle here.
                     */
                    lastSnapshot[j] = lastSnapshot[k];
                    lastSnapshot[k] = '_';
                }
                if (lastSnapshot.length > 0) {
                    lastSnapshot[0] = thisSnapshot[0];
                }

                /*
                 *  Now compare them to make sure tick() did the
                 *  right thing.
                 */
                for (j = lastSnapshot.length - 1; (j >= 0); --j) {
                    if (thisSnapshot[j] == lastSnapshot[j]) {
                        /*
                         *  This one's correct.
                         */
                        continue;
                    }

                    /*
                     *  Something's wrong.
                     */
                    k = j;
                    if (reverseFlow == true) {
                        /*
                         *  In order to provide the correct position
                         *  when the flow is reversed, we need to
                         *  reverse the position number.
                         */
                        k = carCount - j - 1;
                    }
                    System.out.println("**** ERROR:  Position " +
                            k + " should be '" +
                            lastSnapshot[j] + "'");
                    ++errors;
                }
            }
            lastSnapshot = thisSnapshot;
        }

        return(errors);
    }

    /**
     *  Performs a unit test on the {@code Road} class
     *  by instantiating several different roads
     *  and testing the methods.
     *  @param args arguments to the unit test
     */
    public static void main(String[] args)
    {
        int     errors;
        int     i;
        int     roadIndex;
        Road    road[];
        BitSet  usedIdentifiers;
        int     fakeCarCount;
        String  fakeRoadName;
        /*
         *  A corresponding index is used with each of the following
         *  arrays to instantiate a Road.
         */
        String  roadName[]      = {"Wattling Road NE",
                "Wattling Road SW",
                "Wattling Road NE",
                "Wattling Road SW",
                "Wattling Roae NE",
                "",
                "",
                "Wattling Road NE",
                "Wattling Road SW"};
        int     roadCarCount[]  = {10,
                10,
                9,
                9,
                10,
                10,
                10,
                0,
                0};
        boolean roadReverse[]   = {false,
                true,
                false,
                true,
                false,
                false,
                true,
                false,
                true};

        /*
         *  Make sure the arrays are all the same length.
         */
        errors = 0;
        roadIndex = roadName.length;
        if (roadCarCount.length != roadIndex) {
            System.out.println("*** ERROR *** roadName.length (" +
                    roadIndex + ") != roadCarCount.length (" +
                    roadCarCount.length + ")");
            ++errors;
        }
        if (roadReverse.length != roadIndex) {
            System.out.println("*** ERROR *** roadName.length (" +
                    roadIndex + ") != roadReverse.length (" +
                    roadReverse.length + ")");
            ++errors;
        }
        if (errors > 0) {
            System.exit(1);
        }

        /*
         *  Instantiate our roads.
         */
        road = new Road[roadIndex];
        for (i = 0; (i < roadIndex); ++i) {
            road[i] = new Road(roadName[i], roadCarCount[i],
                    roadReverse[i]);
        }

        for (--roadIndex; (roadIndex >= 0); --roadIndex) {
            String      toString;
            String      name;
            int         identifier;
            String      direction;
            Road        which;

            /*
             *  For each road, output its toString() and its
             *  name and identifier.
             */
            which = road[roadIndex];
            toString = "" + which;
            name = which.getName();
            identifier = which.getIdentifier();
            direction = "NE";
            if (roadReverse[roadIndex] == true) {
                direction = "SW";
            }
            System.out.println(toString);
            System.out.println(name + ": " + identifier);

            if (name.equals(roadName[roadIndex]) == false) {
                /*
                 *  The road has the wrong name.
                 */
                System.out.println("*** ERROR *** name should be \"" +
                        roadName[roadIndex] + "\"");
                ++errors;
            }

            /*
             *  See if the toString() return value is formatted
             *  correctly.  Look for the name preceded by an = .
             */
            if ((i = toString.indexOf("=" + roadName[roadIndex])) < 0) {
                System.out.println("*** ERROR *** Didn't find name (" +
                        roadName[roadIndex] +
                        ") in toString() value");
                ++errors;
            } else {
                int     j;

                /*
                 *  Found the name with a = in front.
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
                System.out.println("name must be stored in " +
                        toString.substring(j, i));
            }

            /*
             *  Look for the car count.
             */
            if ((i = toString.indexOf("=" +
                    roadCarCount[roadIndex])) < 0) {
                System.out.println("*** ERROR *** Didn't find" +
                        " car count (" +
                        roadCarCount[roadIndex] +
                        ") in toString() value");
                ++errors;
            } else {
                int     j;

                /*
                 *  Found the car count with a = in front.
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
                System.out.println("car count must be stored in " +
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

            /*
             *  Look for the flow.
             */
            if ((i = toString.indexOf("=" + direction)) < 0) {
                System.out.println("*** ERROR *** Didn't find" +
                        " direction (" + direction +
                        ") in toString() value");
                ++errors;
            } else {
                int     j;

                /*
                 *  Found the direction with a = in front.
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
                System.out.println("direction must be stored in " +
                        toString.substring(j, i));
            }

            for (Road other : road) {
                boolean compareIdentifiers;
                boolean compareEquals;

                /*
                 *  For every other road in the array, including
                 *  this one, make sure the equals() and
                 *  hashCode() methods do what they're supposed
                 *  to do.
                 *
                 *  We're counting on identifier to be unique.
                 */
                compareIdentifiers = which.getIdentifier() ==
                        other.getIdentifier();
                compareEquals = which.equals(other);

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
        for (Road which : road) {
            /*
             *  Add the instantances we've already created to our
             *  database.
             */
            usedIdentifiers.set(which.getIdentifier());
        }
        fakeRoadName = "Fake Road 0";
        fakeCarCount = 1;
        for (; (i > 0); --i) {
            int      identifier;
            Road     which;

            if ((i % 100) == 0) {
                /*
                 *  Every hundred roads, change the name.
                 */
                fakeRoadName = "Fake Road " + (i / 100);
                fakeCarCount = 1;
            } else if ((i & 0x1) == 0) {
                /*
                 *  Every other road, increment the number of cars.
                 */
                ++fakeCarCount;
            }
            which = new Road(fakeRoadName, fakeCarCount,
                    ((i & 0x1) == 0));

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
         *  Make sure wild combinations of cars and flow works.
         */
        for (i = -1; (i <= 2); ++i) {
            errors += testRoadTicks(i, false);
            errors += testRoadTicks(i, true);
        }
        /*
         *  Do a more reasonable couple to look at.
         */
        errors += testRoadTicks(10, false);
        errors += testRoadTicks(10, true);

        if (errors > 0) {
            /*
             *  We found a problem during unit test.
             */
            System.out.println("\n UNIT TEST FAILED with " + errors +
                    " errors");
            System.exit(1);
        }
    }
}
