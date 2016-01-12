import javafx.geometry.Point3D;
import java.util.ArrayList;
import java.util.Collections;

public class BruteForce
{
    //Container where parcels are put in to
    private Container truck;
    //List of parcels that are fitted to the truck
    private ArrayList<Parcel> listOfPackets;
    //List of pacets that will not be fitted to the truck (created by the process)
    private ArrayList<Parcel> rejectedParcels = new ArrayList<Parcel>();
    //List of parcels that have been loaded on truck
    private ArrayList<Parcel> loadedParcels = new ArrayList<Parcel>();
    private ArrayList<Parcel> bestSet = new ArrayList<Parcel>();

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
    }

    public void parcelSolver(Container box, ArrayList<Parcel> pieces)
    {
        if(pieces.size() == 0)
        {
            if(sumList(box.getContainedParcels()) > sumList(bestSet))
            {
                bestSet = new ArrayList<Parcel>();
                for(Parcel p : box.getContainedParcels())
                    bestSet.add(p);
            }
        }
        else
        {
            //Set the first piece as active
            Parcel activeParcel = pieces.get(0);
            //Check if the piece can be used
            //For all locations
            for(int x = 0; x < box.getWidth(); x++)
            {
                for(int z = 0; z < box.getLength(); z++)
                {
                    //Set the location to match coordinates, and above the box
                    activeParcel.setLocation(new Point3D(x,-5,z));
                    //For all rotations ofthe piece
                    int rotations = activeParcel.getRotations();
                    while(rotations > 0)
                    {
                        //Drop it down
                        dropDown(activeParcel,box);
                        //Check if it is in the container in a valid position after dropping
                        if(checkLocation(activeParcel,box))
                        {
                            //If yes, create new box as a copy of the old one
                            Container newBox = box.clone();
                            //Print the parcel to the new box
                            newBox.addParcel(activeParcel);
                            //Create new parcel list which doesn't contain current active parcel
                            ArrayList<Parcel> newPieces = new ArrayList<Parcel>();
                            for(int i = 1; i < pieces.size(); i++)
                                newPieces.add(pieces.get(i));
                            //Move on to new calculation with new box and pieces
                            parcelSolver(newBox,newPieces);
                        }
                        //If not, or if coming from a lower level move on to the next rotation
                        activeParcel.rebuild();
                        rotateParcel(rotations,activeParcel);
                        rotations--;
                    }
                }
            }
            //If we're here it means we've checked all locations and rotations for the piece
            //and we either couldn't fit the piece or we're coming from lower level and have
            //checked all locations

            //Check if we have potential to reach a better solution with the remaining pieces
            if(sumList(pieces) + sumList(box.getContainedParcels()) > sumList(bestSet))
            {
                //If we can we simple move on to the next piece like earlier
                Container newBox = box.clone();
                ArrayList<Parcel> newPieces = new ArrayList<Parcel>();
                for(int i = 1; i < pieces.size(); i++)
                    newPieces.add(pieces.get(i));
                parcelSolver(newBox,newPieces);
            }
            //If we can't we quit the execution of the current branch i.e. prune it
        }
    }

    /**Drops the parcel down to "simulate" gravity in packing
     *
     * @param p Parcel that need to be affected
     * @param box Container containing the parcel p
     */
    public void dropDown(Parcel p, Container box)
    {
        boolean inTheBox = true;
        while(inTheBox)
        {
            p.translate(new Point3D(0, 1, 0));
            for(Point3D point : p.getBlockLocations())
            {
                boolean freeSpot = true;
                if(point.getX() < box.getWidth() && point.getY() < box.getHeight() && point.getZ() < box.getLength() &&
                        point.getX() >= 0 && point.getY() >= 0 && point.getZ() >= 0)
                    freeSpot = (box.getContainer()[(int)point.getX()][(int)point.getY()][(int)point.getZ()] == -1);
                if(!(point.getY() < box.getHeight() && freeSpot))
                    inTheBox = false;
            }
        }
        p.translate(new Point3D(0,-1,0));
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
            if(point.getX() < 0 || point.getY() < 0 || point.getZ() < 0)
                return false;
            else if(point.getX() >= truck.getWidth() || point.getY() >= truck.getHeight() || point.getZ() >= truck.getLength())
                return false;
            //Check that space occupied by the block is free
            else if(truck.getContainer()[(int)point.getX()][(int)point.getY()][(int)point.getZ()] != -1)
                return false;
        }
        //If none of the cases apply we can return true, as the location for whole parcel is valid
        return true;
    
    
   }
   
   public void rotateParcel(int rot, Parcel p)
   {
   // rotates parcel to move through all possible iterations.	   
   if(rot > 12) {p.rotateY(); p.rotateY(); rot = rot-12;}
   if(rot > 9) {p.rotateZ();}
   if(rot > 6) {p.rotateZ();}
   if(rot > 3) {p.rotateZ();}
   if(rot%3==0) {p.rotateY();}
   if(rot%3 == 2){p.rotateX();}
   	   
   	   
   
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

    public ArrayList<Parcel> getBestSet(){return bestSet;}

    /** A method for testing purposes.
     *
     * @param args Not used
     */
   public static void main(String[] args)
   {
       Container container = new Container(5,8,33);
       ArrayList<Parcel> packets = new ArrayList<Parcel>();
       for(int i = 0; i < 264; i++)
           packets.add(new ParcelL());
       BruteForce solver = new BruteForce(container, packets);
       solver.parcelSolver(solver.truck,solver.listOfPackets);

       System.out.println();

       for(Parcel p : solver.getBestSet())
       {
           container.addParcel(p);
           System.out.println(p);
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
   }
   
}
