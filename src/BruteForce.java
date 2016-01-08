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
       Point3D nextFree = new Point3d(0,0,0);
       
    	
    	for(Parcel p : listOfPackets)
       {
           // set locus to next available empty cube
       	   p.setLocation(nextFree);
       	   // attempt to place parcel there, allowing for rotations
       	   // if placed, p is now no longer in listOfPackets
       	   if(placeParcel(p))
       	   	   {break;}
       	   else
       	   {
       	   	   // now try the next 20 open spaces
       	   	   for(int i = 0; i<20; i++)
       	   	   {
       	   	   nextFree = nextOpen(nextFree);
       	   	   p.setLocation(nextFree);
       	   	   if(placeParcel(p))
       	   	   	   {break;}
       	   	     	   	   
       	   	   }
       	   	 
       	   	   // if not able to palce, then move to rejects
       	   	   rejectedParcels.add(p)
       	   	   listOfPackets.remove(p);
       	   }
       	   	   
       	   
       	 
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
   
   
  
   
   /** Finds the next unused block from the supplied parameter
   *
   * @param Point3D the starting point
   * @return Point3D next unused point
   */
   public Point3D nextOpen(Point3D start)
   {
   	   int tmpi = start.getX();
   	   int tmpj = start.getY();
   	   int tmpj = start.getY();
   	   // TODO bounds checking
   	   
   	   	for(int i = tmpi; i< truck.getWidth();i++)
       	   	   {
       	   	   for(int j = tmpj; j<truck.getheight();j++)
       	   	   	{
       	   	   	   for(int k = tmpk; k<truck.getlength(); k++)
       	   	   	   	   {
       	   	   	   	   if(truck.getContainer()[i][j][k] == -1)
       	   	   	   	   {
       	   	   	   	   	   
       	   	   	   	   	   // Can make more exacting choice here. Curently selecting 1x1x1
       	   	   	   	   	   Point3D tmp = new Point3D(i,j,k);
       	   	   	   	   	   // if tmp is not the same as start, return it.
       	   	   	   	   	   if(!tmp.equals(start))
       	   	   	   	   	   return tmp;
       	   	   	   	   }
       	   	   	   	   }
       	   	   	   
       	   	   	   }
       	   	   
       	   	   
       	   	   }
	   
   	   return null;
   }
   
   public void rotateParcel(int rot, Parcel p)
   {
   // rotates parcel to move through all possible iterations.	   
   if(rot > 12) {p.rotateY(); p.rotateY(); rot = rot-12;}
   if(rot > 9) {p.rotateZ();}
   if(rot > 6) {p.rotateZ();}
   if(rot > 3) {p.rotateZ();}
   if(rot%3==0) {p.rotateZ();}
   if(rot%3 == 2){p.rotateX();}
   	   
   	   
   
   }
   
   public boolean placeParcel(Parcel p)
   {
    int rotations = p.getRotations();
       	   // while loop to run through all possible rotations at a point
       	   while(rotations > 0){
       	   
       	   	   if(checkLocation(p))
       	   	   	{
       	   	   		truck.addParcel(p);
       	   	   		loadedParcels.add(p)
       	   	   		listOfPackets.remove(p);
       	   	   		return true;
       	   	   	}
       	   
       	   	   	else
       	   	   		{
       	   	   			rotateParcel(rotations, p);
       	   	   		}
       	     
       	   }
       	   
   return false;
   }
   
   
   
}
