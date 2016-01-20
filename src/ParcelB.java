import javafx.geometry.Point3D;
/**
 * specialization for parcel B
 */
public class ParcelB extends Parcel
{

    public ParcelB()
    {
        super();
        construct();
        setValue(4);
        setRotations(6);
        this.parcelType = 2;
    }

    public ParcelB(double value)
    {
        super();
        construct();
        setValue(value);
        setRotations(6);
        this.parcelType = 2;
    }

    public ParcelB(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
        setRotations(6);
        this.parcelType = 2;
    }

    @Override
    protected void construct()
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                for(int k = 0; k < 2; k++)
                {
                    this.add(new Point3D(i,j,k));
                }
            }
        }
    }
}