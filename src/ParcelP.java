import javafx.geometry.Point3D;

import java.util.ArrayList;

public class ParcelP extends Parcel
{

    public ParcelP()
    {
        super();
        construct();
        setValue(4);
        setRotations(24);
    }

    public ParcelP(double value)
    {
        super();
        construct();
        setValue(value);
        setRotations(24);
    }

    public ParcelP(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
        setRotations(24);
    }

    protected void construct()
    {
        this.add(new Point3D(0,0,0));
        this.add(new Point3D(1,0,0));
        this.add(new Point3D(0,0,1));
        this.add(new Point3D(1,0,1));
        this.add(new Point3D(0,0,2));
    }
    
    public ArrayList<Point3D> getVertices()
    {
        ArrayList<Point3D> part1 = new ArrayList<Point3D>();
    	ArrayList<Point3D> part2 = new ArrayList<Point3D>();
    	ArrayList<Point3D> result = new ArrayList<Point3D>();
    	
    	part1.add(this.getBlockLocations().get(0));
    	part1.add(this.getBlockLocations().get(1));
    	part1.add(this.getBlockLocations().get(2));
    	part1.add(this.getBlockLocations().get(3));
    	
    	part2.add(this.getBlockLocations().get(4));
    	
    	setVertices(part1);
    	setVertices(part2);
    	
         
         for(Point3D point : this.getVertices())
        {
            result.add(point.add(this.getLocation()));
        }
        
        
        return result;
    }
}
