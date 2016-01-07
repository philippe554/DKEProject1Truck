import javafx.geometry.Point3D;

public class ParcelC extends Parcel
{
    private double value;

    public ParcelC()
    {
        super();
        construct();
        value = 5;
    }

    public ParcelC(double value)
    {
        super();
        construct();
        this.value = value;
    }

    public ParcelC(double value, Point3D location)
    {
        super();
        construct();
        this.value = value;
        this.setLocation(location);
    }

    private void construct()
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
