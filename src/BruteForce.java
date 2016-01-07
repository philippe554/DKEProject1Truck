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
    private ArrayList<Parcel> rejectedPackets = new ArrayList<Parcel>();

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

    public void parcelSolver()
    {
       for(Parcel p : listOfPackets)
       {
            //TODO:
           //Write this method and comment it
       }
    }

    /** Checks if the location of the parcel is valid within the container
     *
     * @param parcel The parcel to be checked.
     * @return If the parcel can be fitted to the location in the container or not.
     */
    public boolean checkLocation(Parcel parcel)
    {
        //Check for each block in the parcel
        for(Point3D point : parcel.getBlockLocations())
        {
            //Check that space occupied by the block is free
            if(truck.getContainer()[(int)point.getX()][(int)point.getY()][(int)point.getZ()] != -1)
                return false;
            //Check that the block is not outside of the container
            else if(point.getX() < 0 || point.getY() < 0 || point.getZ() < 0)
                return false;
            else if(point.getX() >= truck.getWidth() || point.getY() >= truck.getheight() || point.getZ() >= truck.getlength())
                return false;
        }
        //If none of the cases apply we can return true, as the location for whole parcel is valid
        return true;
    }
}
