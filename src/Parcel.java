import javafx.geometry.Point3D;
import java.util.ArrayList;

public class Parcel
{

    ArrayList<Point3D> blockLocations = new ArrayList<Point3D>();

    /** Default constructor. Contains a block at (0,0,0)
     *
     */
    public Parcel()
    {
        blockLocations.add(new Point3D(0,0,0));
    }

    /** Constructor from a set of point locations
     *
     * @param blockLocations The locations of the blocks for the parcel
     */
    public Parcel(ArrayList<Point3D> blockLocations)
    {
        this.blockLocations = (ArrayList<Point3D>) blockLocations.clone();
    }

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

    /** Test method
     *
     * @param args Not used
     */
    public static void main(String[] args)
    {
        Parcel test = new Parcel();
        test.add(new Point3D(1,1,1));
        test.add(new Point3D(1,1,0));
        test.add(new Point3D(0,1,1));
        test.add(new Point3D(0,1,0));
        System.out.println(test);
        //These rotations "reflect" the parcel
        test.rotateX();
        test.rotateY();
        test.rotateZ();
        test.rotateZ();
        test.rotateZ();
        System.out.println(test);
    }
}
