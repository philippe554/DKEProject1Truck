import javafx.geometry.Point3D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Parcel implements Comparable<Parcel>
{
    //maximum number of times a piece can be rotated
    private int rotations = 1;
    //Locations of the parcel's blocks with respect to the (0,0,0) block
    private ArrayList<Point3D> blockLocations = new ArrayList<Point3D>();
    //Locations of the vertices of the parcel
    protected ArrayList<Point3D> vertices = new ArrayList<Point3D>();
    //Locations of the sides of the parcel
    private ArrayList<Point3D[]> sides = new ArrayList<Point3D[]>();
    //Location of the (0,0,0) block with respect to upper container
    protected Point3D location = new Point3D(0,0,0);
    //ID to recognise each parcel
    private int ID;
    static int numberOfParcels = 0;
    private double value;
    //To identify the parcel when it loses it's class casting
    protected int parcelType;

    /** Getter for parcel type
     *
     * @return int value of the type the parcel is assigned to
     */
    public int getParcelType(){return parcelType;}

    /** Deep clone for the Parcel.
     *
     * @return Another Parcel object with exactly same parameters and values as the original one
     */
    @Override
    public Parcel clone()
    {
        //Create new blocks and copy the blocks
        ArrayList<Point3D> newBlocks = new ArrayList<Point3D>();
        for(Point3D point : blockLocations)
        {
            Point3D newPoint = new Point3D(point.getX(),point.getY(),point.getZ());
            newBlocks.add(newPoint);
        }
        //Copy the location
        Point3D newLocation = new Point3D(this.location.getX(),this.location.getY(),this.location.getZ());
        //Create the parcel and copy the values
        Parcel cloneParcel = new Parcel(newBlocks, newLocation);
        cloneParcel.setValue(this.getValue());
        cloneParcel.setRotations(this.getRotations());
        cloneParcel.setID(this.getID());
        cloneParcel.parcelType = this.parcelType;
        return cloneParcel;
    }

    /** Default constructor. Contains a block at (0,0,0) at the location (0,0,0)
     *
     */
    public Parcel()
    {
        ID = numberOfParcels;
        numberOfParcels++;
        //blockLocations.add(new Point3D(0,0,0));
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

    /** Gets the value of the
     *
     * @return value as double
     */
    public double getValue()
    {
        return this.value;
    }

    /** Gets the value of the parcel divided by its volume
     *
     * @return value/volume as double
     */
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

    /** Translates the block locations by given coordinates
     *
     * @param x amount to be translated by on x axis
     * @param y amount to be translated by on y axis
     * @param z amount to be translated by on z axis
     */
    public void translateBlocks(int x, int y, int z)
    {
        ArrayList<Point3D> newBlockLocations = new ArrayList<Point3D>();
        for(Point3D point : blockLocations)
        {
            newBlockLocations.add(point.add(x,y,z));
        }
        this.blockLocations = newBlockLocations;
    }

    /** Translates the block locations by a given point
     *
     * @param point point to be translated by
     */
    public void translateBlocks(Point3D point)
    {
        ArrayList<Point3D> newBlockLocations = new ArrayList<Point3D>();
        for(Point3D block : blockLocations)
        {
            newBlockLocations.add(block.add(point));
        }
        this.blockLocations = newBlockLocations;
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

    /** Generates the vertices of a cuboid parcel
     *
     * @param blocks of Point3D
     */
    protected void setVertices(ArrayList<Point3D> blocks) {

        int max_x = 0;
        int max_y = 0;
        int max_z = 0;
        int min_x = 999;
        int min_y = 999;
        int min_z = 999;

        for(Point3D point : blocks)
        {
            if((point.getX()) > max_x) max_x = (int)point.getX();
            if((point.getY()) > max_y) max_y = (int)point.getY();
            if((point.getZ()) > max_z) max_z = (int)point.getZ();

            if((point.getX()) < min_x) min_x = (int)point.getX();
            if((point.getY()) < min_y) min_y = (int)point.getY();
            if((point.getZ()) < min_z) min_z = (int)point.getZ();
        }

        for(int i = 0; i<2; i++)
        {
            for(int j = 0; j<2; j++)
            {
                for(int k = 0; k<2; k++)
                {
                    int tmp_x;
                    if(k==0){
                        tmp_x=min_x;
                    }else{
                        tmp_x = max_x+1;
                    }
                    int tmp_y;
                    if(j==0){
                        tmp_y=min_y;
                    }else{
                        tmp_y = max_y+1;
                    }
                    int tmp_z;
                    if(i==0){
                        tmp_z=min_z;
                    }else{
                        tmp_z = max_z+1;
                    }
                    vertices.add(new Point3D(tmp_x,tmp_y,tmp_z));

                }
            }
        }


    }

    /** Gets the vertices of a cuboid parcel relevent to a higher container
     *
     * @return Arraylist of Point3D of the vertices
     */
    public ArrayList<Point3D> getVertices()
    {
        vertices.clear();
        setVertices(blockLocations);
        ArrayList<Point3D> result = new ArrayList<Point3D>();

        for(Point3D point : vertices)
        {
            result.add(point.add(location));
        }
        vertices.clear();
        for(Point3D point:result)
        {
            vertices.add(point);
        }

        System.out.println(vertices.get(0));
        System.out.println(vertices.get(1));
        System.out.println(vertices.get(2));
        System.out.println(vertices.get(3));
        System.out.println(vertices.get(4));
        System.out.println(vertices.get(5));
        System.out.println(vertices.get(6));
        System.out.println(vertices.get(7));

        return result;
    }

    /** Gets the sides of a cuboid parcel, relevent to a higher container
     *
     * @param temp 0 for vertices 0-7, 8 for vertices 8-15
     */
    private void setSides(int temp)
    {
        // empty sides list

        Point3D[] s1 = {vertices.get(temp), vertices.get(temp+1), vertices.get(temp+2)};
        Point3D[] s2 = {vertices.get(temp), vertices.get(temp+1), vertices.get(temp+4)};
        Point3D[] s3 = {vertices.get(temp), vertices.get(temp+2), vertices.get(temp+4)};
        Point3D[] s4 = {vertices.get(temp+1), vertices.get(temp+2), vertices.get(temp+3)};
        Point3D[] s5 = {vertices.get(temp+1), vertices.get(temp+3), vertices.get(temp+7)};
        Point3D[] s6 = {vertices.get(temp+2), vertices.get(temp+3), vertices.get(temp+7)};
        Point3D[] s7 = {vertices.get(temp+1), vertices.get(temp+4), vertices.get(temp+5)};
        Point3D[] s8 = {vertices.get(temp+1), vertices.get(temp+5), vertices.get(temp+7)};
        Point3D[] s9 = {vertices.get(temp+4), vertices.get(temp+5), vertices.get(temp+7)};
        Point3D[] s10 = {vertices.get(temp+2), vertices.get(temp+4), vertices.get(temp+6)};
        Point3D[] s11 = {vertices.get(temp+4), vertices.get(temp+6), vertices.get(temp+7)};
        Point3D[] s12 = {vertices.get(temp+2), vertices.get(temp+6), vertices.get(temp+7)};

        sides.add(s1);
        sides.add(s2);
        sides.add(s3);
        sides.add(s4);
        sides.add(s5);
        sides.add(s6);
        sides.add(s7);
        sides.add(s8);
        sides.add(s9);
        sides.add(s10);
        sides.add(s11);
        sides.add(s12);



    }

    /** Gets the sides of a cuboid parcel, relevent to a higher container
     *
     * @return An arraylist of arrays of Point3D objects
     */
    public ArrayList<Point3D[]> getSides()
    {
        if(parcelType<=3) {
            getVertices();
        }else if(parcelType==4)
        {
            getVerticesL();
        }
        else if(parcelType==5)
        {
            getVerticesP();
        }
        else if(parcelType==6)
        {
            getVerticesT();
        }
        int tmp = vertices.size();
        sides = new ArrayList<Point3D[]>();

        if(tmp > 9){
            setSides(8);
        }
        setSides(0);

        return sides;
    }
    public ArrayList<Point3D> getVerticesL(){
        ArrayList<Point3D> part1 = new ArrayList<Point3D>();
        ArrayList<Point3D> part2 = new ArrayList<Point3D>();
        ArrayList<Point3D> result = new ArrayList<Point3D>();

        part1.add(this.getBlockLocations().get(0));
        part1.add(this.getBlockLocations().get(1));
        part1.add(this.getBlockLocations().get(2));
        part1.add(this.getBlockLocations().get(3));

        part2.add(this.getBlockLocations().get(4));

        vertices.clear();
        setVertices(part1);
        setVertices(part2);

        return result;
    }
    public ArrayList<Point3D> getVerticesP(){
        ArrayList<Point3D> part1 = new ArrayList<Point3D>();
        ArrayList<Point3D> part2 = new ArrayList<Point3D>();
        ArrayList<Point3D> result = new ArrayList<Point3D>();

        part1.add(this.getBlockLocations().get(0));
        part1.add(this.getBlockLocations().get(1));
        part1.add(this.getBlockLocations().get(2));
        part1.add(this.getBlockLocations().get(3));

        part2.add(this.getBlockLocations().get(4));

        vertices.clear();
        setVertices(part1);
        setVertices(part2);
        return result;
    }
    public ArrayList<Point3D> getVerticesT() {
        ArrayList<Point3D> part1 = new ArrayList<Point3D>();
        ArrayList<Point3D> part2 = new ArrayList<Point3D>();
        ArrayList<Point3D> result = new ArrayList<Point3D>();

        part1.add(this.getBlockLocations().get(0));
        part1.add(this.getBlockLocations().get(1));

        part2.add(this.getBlockLocations().get(2));
        part2.add(this.getBlockLocations().get(3));
        part2.add(this.getBlockLocations().get(4));

        vertices.clear();
        setVertices(part1);
        setVertices(part2);

        return result;
    }

    /** Sets the ID for the parcel. Meant for cloning
     *
     * @param ID The wanted ID
     */
    public void setID(int ID){this.ID = ID;}

    public static void main(String[] args)
    {
        Parcel test = new Parcel();
        test.add(new Point3D(1,1,1));
        test.add(new Point3D(1,1,0));
        test.add(new Point3D(0,1,1));
        test.add(new Point3D(0,1,0));
        System.out.println(test.getLocation());
        System.out.println("\n" + test.getBlockLocations() + "\n");
        test.translate(new Point3D(1,1,1));
        System.out.println(test.getLocation());
        test.setLocation(-1,-1,-1);
        System.out.println(test.getLocation());
        System.out.println("\n" + test.getBlockLocations() + "\n");
    }
}