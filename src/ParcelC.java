import javafx.geometry.Point3D;

public class ParcelC extends Parcel
{

    public static final int rotations = 1;

    public ParcelC()
    {
        super();
        construct();
        setValue(5);
    }

    public ParcelC(double value)
    {
        super();
        construct();
        setValue(value);
    }

    public ParcelC(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
    }

    @Override
    protected void construct()
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                for(int k = 0; k < 3; k++)
                {
                    this.add(new Point3D(i,j,k));
                }
            }
        }
    }
}
