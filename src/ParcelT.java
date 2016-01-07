import javafx.geometry.Point3D;

public class ParcelT extends Parcel
{
    private double value;

    public ParcelT()
    {
        super();
        construct();
        value = 5;
    }

    public ParcelT(double value)
    {
        super();
        construct();
        this.value = value;
    }

    public ParcelT(double value, Point3D location)
    {
        super();
        construct();
        this.value = value;
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
}
