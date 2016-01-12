import javafx.geometry.Point3D;

import java.awt.*;
import java.util.ArrayList;

public class Parcel implements Comparable<Parcel>
{
	//maximum number of times a piece can be rotated
    private int rotations = 1;
    //Locations of the parcel's blocks with respect to the (0,0,0) block
    private ArrayList<Point3D> blockLocations = new ArrayList<Point3D>();
    //Location of the (0,0,0) block with respect to upper container
    private Point3D location = new Point3D(0,0,0);
    //ID to recognise each parcel
    private int ID;
    static int numberOfParcels = 0;
    private double value;

    @Override
    public Parcel clone()
    {
        ArrayList<Point3D> newBlocks = new ArrayList<Point3D>();
        for(Point3D point : blockLocations)
        {
            Point3D newPoint = new Point3D(point.getX(),point.getY(),point.getZ());
            newBlocks.add(newPoint);
        }
        Point3D newLocation = new Point3D(this.location.getX(),this.location.getY(),this.location.getZ());
        Parcel cloneParcel = new Parcel(newBlocks, newLocation);
        cloneParcel.setValue(this.getValue());
        cloneParcel.setRotations(this.getRotations());
        cloneParcel.setID(this.getID());
        return cloneParcel;
    }

    /** Default constructor. Contains a block at (0,0,0) at the location (0,0,0)
     *
     */
    public Parcel()
    {
        ID = numberOfParcels;
        numberOfParcels++;
        blockLocations.add(new Point3D(0,0,0));
    }

    /** Constructor from a set of point locations to a defined location
     *
     * @param blockLocations The locations of the blocks for the parcel
     * @param location The location where the parcel is created to
     */
    public Parcel(ArrayList<Point3D> blockLocations, Point3D location)
    {
        ID = numberOfParcels;
        numberOfParcels++;
        this.blockLocations = (ArrayList<Point3D>) blockLocations.clone();
        this.location = location;
    }

    /** Constructs the parcel from a set of points to location (0,0,0)
     *
     * @param blockLocations Locations of the blocks
     */
    public Parcel(ArrayList<Point3D> blockLocations)
    {
        ID = numberOfParcels;
        numberOfParcels++;
        this.blockLocations = (ArrayList<Point3D>)blockLocations.clone();
    }

    /** Compares two Parcel-class objects by their value/volume ratio
     *
     * @param anotherParcel Object to be compared to
     * @return 0 if parcels are equal, -1 if anotherParcel is larger and 1 if smaller
     */
    @Override
    public int compareTo(Parcel anotherParcel)
    {
        if(anotherParcel.getVolumetricValue() > this.getVolumetricValue())
        {
            return -1;
        }
        else if(anotherParcel.getVolumetricValue() < this.getVolumetricValue())
        {
            return 1;
        }
        return 0;
    }

    /** Gets the value of the parcel divided by its volume
     *
     * @return value/volume as double
     */
    public double getValue()
    {
        return this.value;
    }

    public double getVolumetricValue() {return this.value/blockLocations.size();}

    /** Sets parcel's value to a certain double
     *
     * @param value The value to be set to
     */
    public void setValue(double value) {this.value = value;}

    /** Gets the id of the parcel
     *
     * @return The ID number as an int
     */
    public int getID(){return ID;}

    /** Gets the number of created parcels
     *
     * @return Number of parcels as an int
     */
    public int getNumberOfParcels(){return numberOfParcels;}

    /** Rotates the parcel clockwise along the X-axis
     *
     */
    public void rotateX()
    {
        for(int i = 0; i < blockLocations.size(); i++)
        {
            Point3D point = blockLocations.get(i);
            double tmpY = -point.getZ();
            double tmpZ = point.getY();
            point = new Point3D(point.getX(),tmpY,tmpZ);
            blockLocations.remove(i);
            blockLocations.add(i, point);
        }
    }

    /** Rotates the parcel clockwise along the Y-axis
     *
     */
    public void rotateY()
    {
        for(int i = 0; i < blockLocations.size(); i++)
        {
            Point3D point = blockLocations.get(i);
            double tmpX = point.getZ();
            double tmpZ = -point.getX();
            point = new Point3D(tmpX,point.getY(),tmpZ);
            blockLocations.remove(i);
            blockLocations.add(i, point);
        }
    }

    /** Rotates the parcel clockwise along the Z-axis
     *
     */
    public void rotateZ()
    {
        for(int i = 0; i < blockLocations.size(); i++)
        {
            Point3D point = blockLocations.get(i);
            double tmpY = point.getX();
            double tmpX = -point.getY();
            point = new Point3D(tmpX,tmpY,point.getZ());
            blockLocations.remove(i);
            blockLocations.add(i, point);
        }
    }

    /** Adds a block to the parcel
     *
     * @param block Point3D location of the block with respect to the center block of the parcel
     */
    public void add(Point3D block)
    {
        blockLocations.add(block);
    }

    /** Adds a set of blocks to the parcel
     *
     * @param newLocations A Point3D set of block locations
     */
    public void add(ArrayList<Point3D> newLocations)
    {
        blockLocations.addAll(newLocations);
    }

    /** Gets the list of block locations
     *
     * @return ArrayList<Point3D> of the locations of the blocks
     */
    public ArrayList<Point3D> getLocations()
    {
        return blockLocations;
    }

    /** Gets the location of the center block with respect to a container
     *
     * @return Point3D location
     */
    public Point3D getLocation(){return location;}

    /** Translates the parcel by coodrinates
     *
     * @param x X-coordinate to be translated by
     * @param y Y-coordinate to be translated by
     * @param z Z-coordinate to be translated by
     */
    public void translate(int x, int y, int z)
    {
        location = location.add(x,y,z);
    }

    /** Translates the location of the parcel by a point
     *
     * @param point Point3D by which the location is translated
     */
    public void translate(Point3D point)
    {
        location = location.add(point);
    }

    /** Sets the location of the parcel based coordinates
     *
     * @param x X-coordinate for the location
     * @param y Y-coordinate for the location
     * @param z Z-coordinate for the location
     */
    public void setLocation(int x, int y, int z)
    {
        location = location.add(-location.getX(),-location.getY(),-location.getZ());
        location = location.add(x,y,z);
    }

    /** Sets the location of the parcel to a 3D point
     *
     * @param point Point3D of the location
     */
    public void setLocation(Point3D point)
    {
        location = location.add(-location.getX(),-location.getY(),-location.getZ());
        location = location.add(point);
    }

    /** Returns the locations of the blocks in the parcel with respect to an upper container
     *
     * @return ArrayList<Point3D> containing the points
     */
    public ArrayList<Point3D> getBlockLocations()
    {
        ArrayList<Point3D> coordinates = new ArrayList<Point3D>();
        for(Point3D point : blockLocations)
        {
            coordinates.add(point.add(location));
        }
        return coordinates;
    }

    /** Gets the list of the block locations as a string
     *
     * @return A string of Point3Ds
     */
    @Override
    public String toString()
    {
        String text = "";
        for(Point3D point : blockLocations)
        {
            text += point.toString() + "\n";
        }
        return text;
    }
        
    /** Gets the number of rotations a piece is capable of
     *
     * @return int of possible rotations
     */
    public int getRotations()
    {
    return rotations;
    
    }
    /** Parcel Builder
     *
     */
    protected void construct()
    {
        blockLocations.add(new Point3D(0,0,0));
    }

    /** Rebuilds the parcel to the original state
     *
     */
    public void rebuild()
    {
        blockLocations = new ArrayList<Point3D>();
        this.construct();
    }

    /** Sets the number of rotations for the parcel
     *
     * @param rotations number of possible rotations it can have
     */
    public void setRotations(int rotations) {this.rotations = rotations;}

    /** Sets the ID for the parcel. Meant for cloning
     *
     * @param ID The wanted ID
     */
    public void setID(int ID){this.ID = ID;}

    public static void main(String[] args)
    {
        Parcel parcel = new ParcelA();
        System.out.println(parcel);
        Parcel cloneParcel = parcel.clone();
        System.out.println(cloneParcel);
    }
}
