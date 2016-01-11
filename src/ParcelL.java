import javafx.geometry.Point3D;

public class ParcelL extends Parcel
{

    public static final int rotations = 24;

    public ParcelL()
    {
        super();
        construct();
        setValue(3);
    }

    public ParcelL(double value)
    {
        super();
        construct();
        setValue(value);
    }

    public ParcelL(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
    }

    private void construct()
    {
        this.add(new Point3D(0,0,0));
        this.add(new Point3D(1,0,0));
        this.add(new Point3D(2,0,0));
        this.add(new Point3D(3,0,0));
        this.add(new Point3D(0,0,1));
    }
    
    
    public ArrayList<Point3D> getVertices()
    {
        ArrayList<Point3D> part1 = new ArrayList<Point3D>();
    	ArrayList<Point3D> part2 = new ArrayList<Point3D>();
    	ArrayList<Point3D> result = new ArrayList<Point3D>();
    	
    	part1.add(blockLocations.get(0));
    	part1.add(blockLocations.get(1));
    	part1.add(blockLocations.get(2));
    	part1.add(blockLocations.get(3));
    	
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
