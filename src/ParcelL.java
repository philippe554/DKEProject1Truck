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
}
