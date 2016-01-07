import javafx.geometry.Point3D;

public class ParcelB extends Parcel
{
    private double value;

    public ParcelB()
    {
        super();
        construct();
        value = 4;
    }

    public ParcelB(double value)
    {
        super();
        construct();
        this.value = value;
    }

    public ParcelB(double value, Point3D location)
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
