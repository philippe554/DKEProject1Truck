import javafx.geometry.Point3D;

public class ParcelC extends Parcel
{

    public ParcelC()
    {
        super();
        construct();
        setValue(5);
        setRotations(1);
        this.parcelType = 3;
    }

    public ParcelC(double value)
    {
        super();
        construct();
        setValue(value);
        setRotations(1);
        this.parcelType = 3;
    }

    public ParcelC(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
        setRotations(1);
        this.parcelType = 3;
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