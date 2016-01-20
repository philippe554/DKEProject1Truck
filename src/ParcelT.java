import javafx.geometry.Point3D;

import java.util.ArrayList;

/**
 * specialization for parcel T
 */
public class ParcelT extends Parcel
{

    public ParcelT()
    {
        super();
        construct();
        setValue(5);
        setRotations(12);
        this.parcelType = 6;
    }

    public ParcelT(double value)
    {
        super();
        construct();
        setValue(value);
        setRotations(12);
        this.parcelType = 6;
    }

    public ParcelT(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
        setRotations(12);
        this.parcelType = 6;
    }

    protected void construct()
    {
        this.add(new Point3D(1,0,1));
        this.add(new Point3D(2,0,1));
        this.add(new Point3D(0,0,0));
        this.add(new Point3D(0,0,1));
        this.add(new Point3D(0,0,2));
    }
}