import javafx.geometry.Point3D;

public class ParcelA extends Parcel
{
    private double value;


    /*
            This class along with other parcel subclasses construct a parcel with certain blocks in them.
            They also add value for the parcel types.
            They are ment to make managing parcels easier.
     */
    public ParcelA()
    {
        super();
        construct();
        value = 3;
    }

    public ParcelA(double value)
    {
        super();
        construct();
        this.value = value;
    }

    public ParcelA(double value, Point3D location)
    {
        super();
        construct();
        this.value = value;
        this.setLocation(location);
    }

    private void construct()
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
