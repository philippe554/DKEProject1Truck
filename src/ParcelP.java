import javafx.geometry.Point3D;

public class ParcelP extends Parcel
{

    public static final int rotations = 24;

    public ParcelP()
    {
        super();
        construct();
        setValue(4);
    }

    public ParcelP(double value)
    {
        super();
        construct();
        setValue(value);
    }

    public ParcelP(double value, Point3D location)
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
        this.add(new Point3D(0,0,1));
        this.add(new Point3D(1,0,1));
        this.add(new Point3D(0,0,2));
    }
}
