import javafx.geometry.Point3D;

import java.util.ArrayList;

/**
 * specialization for parcel P
 */
public class ParcelP extends Parcel
{

    public ParcelP()
    {
        super();
        construct();
        setValue(4);
        setRotations(24);
        this.parcelType = 5;
    }

    public ParcelP(double value)
    {
        super();
        construct();
        setValue(value);
        setRotations(24);
        this.parcelType = 5;
    }

    public ParcelP(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
        setRotations(24);
        this.parcelType = 5;
    }

    protected void construct()
    {
        this.add(new Point3D(0,0,0));
        this.add(new Point3D(1,0,0));
        this.add(new Point3D(0,0,1));
        this.add(new Point3D(1,0,1));
        this.add(new Point3D(0,0,2));
    }
}