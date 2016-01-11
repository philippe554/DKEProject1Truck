import javafx.geometry.Point3D;

public class ParcelT extends Parcel
{

    public static final int rotations = 12;

    public ParcelT()
    {
        super();
        construct();
        setValue(5);
    }

    public ParcelT(double value)
    {
        super();
        construct();
        setValue(value);
    }

    public ParcelT(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
    }

    private void construct()
    {
        this.add(new Point3D(1,0,1));
        this.add(new Point3D(2,0,1));
        this.add(new Point3D(0,0,0));
        this.add(new Point3D(0,0,1));
        this.add(new Point3D(0,0,2));
    }
    
    public ArrayList<Point3D> getVertices()
    {
        ArrayList<Point3D> part1 = new ArrayList<Point3D>();
    	ArrayList<Point3D> part2 = new ArrayList<Point3D>();
    	ArrayList<Point3D> result = new ArrayList<Point3D>();
    	
    	part1.add(blockLocations.get(0));
    	part1.add(blockLocations.get(1));
    	
    	part2.add(blockLocations.get(2));
    	part2.add(blockLocations.get(3));
    	part2.add(blockLocations.get(4));
    	
    	setVertices(part1);
    	setVertices(part2);
    	
         
         for(Point3D point : vertices)
        {
            result.add(point.add(location));
        }
        
        
        return result;
    }
    
}
