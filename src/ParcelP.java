import javafx.geometry.Point3D;

public class ParcelP extends Parcel
{
    private double value;

    public ParcelP()
    {
        super();
        construct();
        value = 4;
    }

    public ParcelP(double value)
    {
        super();
        construct();
        this.value = value;
    }

    public ParcelP(double value, Point3D location)
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
        this.add(new Point3D(0,0,1));
        this.add(new Point3D(1,0,1));
        this.add(new Point3D(0,0,2));
    }
}
