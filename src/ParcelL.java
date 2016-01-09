import javafx.geometry.Point3D;

public class ParcelL extends Parcel
{
    private double value;

    public ParcelL()
    {
        super();
        construct();
        value = 3;
    }

    public ParcelL(double value)
    {
        super();
        construct();
        this.value = value;
    }

    public ParcelL(double value, Point3D location)
    {
        super();
        construct();
        this.value = value;
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
