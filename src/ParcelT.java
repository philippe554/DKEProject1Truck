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

    @Override
    protected void construct()
    {
        this.add(new Point3D(1,0,1));
        this.add(new Point3D(2,0,1));
        this.add(new Point3D(0,0,0));
        this.add(new Point3D(0,0,1));
        this.add(new Point3D(0,0,2));
    }
}
