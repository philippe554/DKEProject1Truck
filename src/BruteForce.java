import javafx.geometry.Point3D;
import java.util.ArrayList;
import java.util.Collections;

/**
 * algorithm for the brute force
 */
public class BruteForce
{
    //Container where parcels are put in to
    private Container truck;
    //List of parcels that are fitted to the truck
    private ArrayList<Parcel> listOfPackets;
    //The current best solution
    private ArrayList<Parcel> bestSet = new ArrayList<Parcel>();
    //List corresponding to the rotations of the parcels
    ArrayList<ArrayList<Parcel>> listOfRotations = new ArrayList<ArrayList<Parcel>>();

    /** Constructs a new search for a given container and a set of parcels
     *
     * @param truck Container where parcels are fitted to
     * @param listOfPackets List of parcels that should be fitted to the truck
     */
    public BruteForce(Container truck, ArrayList<Parcel> listOfPackets)
    {
        //Initialization of variables
        this.truck = truck;
        this.listOfPackets = listOfPackets;
        //Sorting the list from largest value to smallest
        Collections.sort(listOfPackets);
        Collections.reverse(listOfPackets);
        //Complete the list of rotations
        listOfRotations.add(getAllRotations(new ParcelA()));
        listOfRotations.add(getAllRotations(new ParcelB()));
        listOfRotations.add(getAllRotations(new ParcelC()));
        listOfRotations.add(getAllRotations(new ParcelL()));
        listOfRotations.add(getAllRotations(new ParcelP()));
        listOfRotations.add(getAllRotations(new ParcelT()));
    }

    /** Solves the best possible packing by value of the container with given parcels.
     *
     * @param box The container to be filled
     * @param pieces Parcels to be packed
     */
    public void parcelSolver(Container box, ArrayList<Parcel> pieces)
    {
        //We're done if we don't have any more parcels to place in the list
        if(pieces.size() == 0)
        {
            //If our container has better value than ever before, we save it as the new best
            if(sumList(box.getContainedParcels()) > sumList(bestSet))
            {
                bestSet = new ArrayList<Parcel>();
                for(Parcel p : box.getContainedParcels())
                    bestSet.add(p);
            }
        }
        else
        {
            //Set the first piece as active and get the rotation list
            Parcel activeParcel = pieces.get(0);
            ArrayList<Parcel> rotationList = listOfRotations.get(activeParcel.getParcelType()-1);
            //Check if the piece can be used
            //For all locations
            for(int x = 0; x < box.getX(); x++)
            {
                for(int y = 0; y < box.getY(); y++)
                {
                    //For all rotations of the piece
                    for(Parcel p : rotationList)
                    {
                        //Set the location to match coordinates, and above the box
                        p.setLocation(new Point3D(x,y,-6));
                        //Drop it down
                        dropDown(p,box);
                        //Check if it is in the container in a valid position after dropping
                        if(checkLocation(p,box))
                        {
                            //Set the rotation list id's so the pieces can be drawn separately
                            for(Parcel parcel : rotationList)
                            {
                                parcel.setID(activeParcel.getID());
                            }
                            //If yes, create new box as a copy of the old one
                            Container newBox = box.clone();
                            //Print the parcel to the new box
                            newBox.addParcel(p);
                            //Create new parcel list which doesn't contain current active parcel
                            ArrayList<Parcel> newPieces = new ArrayList<Parcel>();
                            for(int i = 1; i < pieces.size(); i++)
                                newPieces.add(pieces.get(i));
                            //Move on to new calculation with new box and pieces
                            parcelSolver(newBox,newPieces);
                        }

                    }
                }
            }
            //If we're here it means we've checked all locations and rotations for the piece
            //and we either couldn't fit the piece or we're coming from lower level and have
            //checked all locations

            //Check if we have potential to reach a better solution with the remaining pieces
            if(sumList(pieces) + sumList(box.getContainedParcels()) > sumList(bestSet))
            {
                //If we can, we simple move on to the next piece like earlier
                Container newBox = box.clone();
                ArrayList<Parcel> newPieces = new ArrayList<Parcel>();
                for(int i = 1; i < pieces.size(); i++)
                    //We also exclude all pieces of the same type from this list
                    //as we know none of them will fit the container
                    if(pieces.get(i).getParcelType() != activeParcel.getParcelType())
                        newPieces.add(pieces.get(i));
                parcelSolver(newBox,newPieces);
            }
            //If we can't we quit the execution of the current branch i.e. prune it
        }
    }

    /**Drops the parcel down to "simulate" gravity in packing
     *
     * @param p Parcel that needs to be affected
     * @param box Container containing the parcel p
     */
    public void dropDown(Parcel p, Container box)
    {
        boolean inTheBox = true;
        while(inTheBox)
        {
            p.translate(new Point3D(0, 0, 1));
            for(Point3D point : p.getBlockLocations())
            {
                boolean freeSpot = true;
                if(point.getX() < box.getX() && point.getY() < box.getY() && point.getZ() < box.getZ() &&
                        point.getX() >= 0 && point.getY() >= 0 && point.getZ() >= 0)
                    freeSpot = (box.getContainer()[(int)point.getX()][(int)point.getY()][(int)point.getZ()] == -1);
                if(!(point.getZ() < box.getZ() && freeSpot))
                    inTheBox = false;
            }
        }
        p.translate(new Point3D(0,0,-1));
    }

    /** Checks if the location of the parcel is valid within a container
     *
     * @param parcel The parcel to be checked.
     * @param truck The container we're checking the location for
     * @return If the parcel can be fitted to the location in the container or not.
     */
    public boolean checkLocation(Parcel parcel, Container truck)
    {
        //Check for each block in the parcel
        for(Point3D point : parcel.getBlockLocations())
        {
            //Check that the block is not outside of the container
            if (point.getX() < 0 || point.getY() < 0 || point.getZ() < 0)
                return false;
            else if (point.getX() >= truck.getX() || point.getY() >= truck.getZ() || point.getZ() >= truck.getY())
                return false;
                //Check that space occupied by the block is free
            else if (truck.getContainer()[(int) point.getX()][(int) point.getY()][(int) point.getZ()] != -1)
                return false;
        }
        //If none of the cases apply we can return true, as the location for whole parcel is valid
        return true;
    }

    /** Gets the sum of the parcels' values in the list
     *
     * @param list List of parcels to be calculated
     * @return Integer of the sum of the values
     */
    public double sumList(ArrayList<Parcel> list)
    {
        double sum = 0;
        for(Parcel p : list)
            sum += p.getValue();
        return sum;
    }

    /** Gets the best set of parcels that can be fitted to the container
     *
     * @return List of the parcels with highest value
     */
    public ArrayList<Parcel> getBestSet(){return bestSet;}

    /** Gets all the possible rotations of the given parcel without repetition
     *
     * @param p parcel to be rotated
     * @return ArrayList of rotations
     */
    public ArrayList<Parcel> getAllRotations(Parcel p) {
        ArrayList<Parcel> rotations = new ArrayList<Parcel>();
        for(int j=0;j<4;j++) {
            for (int i = 0; i < 4; i++) {
                rotations.add(p.clone());
                p.rotateZ();
            }
            p.rotateX();
        }
        p.rotateY();
        for (int i = 0; i < 4; i++) {
            rotations.add(p.clone());
            p.rotateZ();
        }
        p.rotateY();
        p.rotateY();
        for (int i = 0; i < 4; i++) {
            rotations.add(p.clone());
            p.rotateZ();
        }
        p.rotateY();

        for(Parcel parcel : rotations)
        {
            //Translate the parcel to be entirely "positive"
            for(int i = 0; i < parcel.getBlockLocations().size(); i++)
            {
                Point3D point = parcel.getBlockLocations().get(i);
                if(point.getX() < 0) parcel.translateBlocks(-(int)point.getX(),0,0);
                if(point.getY() < 0) parcel.translateBlocks(0,-(int)point.getY(),0);
                if(point.getZ() < 0) parcel.translateBlocks(0,0,-(int)point.getZ());
            }
        }
        for(int i = 0; i < rotations.size(); i++)
        {
            for(int j = i+1; j < rotations.size(); j++)
            {
                if(rotations.get(i).getBlockLocations().containsAll(
                        rotations.get(j).getBlockLocations()))
                {
                    rotations.remove(j);
                    j--;
                }
            }
        }
        return rotations;
    }

    /** A method for testing purposes.
     *
     * @param args Not used
     */
    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
        System.out.println("Starting calculation at " + startTime);
        Container container = new Container(33,8,5);
        ArrayList<Parcel> packets = new ArrayList<Parcel>();
        for(int i = 0; i < 83; i++)
        {
            packets.add(new ParcelA());
            packets.add(new ParcelB());
            packets.add(new ParcelC());
        }
        BruteForce solver = new BruteForce(container, packets);
        solver.parcelSolver(solver.truck,solver.listOfPackets);

        System.out.println();

        for(Parcel p : solver.getBestSet())
        {
            container.addParcel(p);
        }

        for(int i = 0; i < container.getContainer().length; i++)
        {
            for(int j = 0; j < container.getContainer()[0].length; j++)
            {
                for(int k = 0; k < container.getContainer()[0][0].length; k++)
                {
                    System.out.print(container.getContainer()[i][j][k]);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
            System.out.println();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Ending calculation at " + endTime);
        System.out.println("Completed in " + (endTime-startTime) + " milliseconds.");
        int value = 0;
        for(Parcel p : solver.getBestSet())
        {
            value += p.getValue();
            System.out.print(p.getParcelType() + ", ");
        }
        System.out.println("\nThe container's total value is: " + value +
            "\nThe container was filled " + (100 - container.emptyPercent()) + "% full.");
    }

}