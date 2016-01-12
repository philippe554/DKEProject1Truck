import javafx.geometry.Point3D;

public class ParcelA extends Parcel
{
    /*
            This class along with other parcel subclasses construct a parcel with certain blocks in them.
            They also add value for the parcel types.
            They are ment to make managing parcels easier.
     */

    public static final int rotations = 3;

    public ParcelA()
    {
        super();
        construct();
        setValue(3);
    }

    public ParcelA(double value)
    {
        super();
        construct();
        setValue(value);
    }

    public ParcelA(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
    }

    @Override
    protected void construct()
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 2; j++)
            {
                for(int k = 0; k < 2; k++)
                {
                    this.add(new Point3D(i,j,k));
                }
            }
        }
    }
}
