import javafx.geometry.Point3D;

public class Container {

    public int width;
    public int length;
    public int height;
    public int[][][] container;
    Point3D[] vertices;

    public Container(int width,int length,int height ){
        this.width= width;
        this.length = length;
        this.height = height;
        this.container = new int[width][length][height];

        //Make array of 3DPoint objects containing vertices of the container
        vertices = new Point3D[8];
        vertices[7] = new Point3D(0,0,0);
        vertices[5] = new Point3D(0,width,0);
        vertices[4] = new Point3D(0,width,height);
        vertices[6] = new Point3D(0,0,height);
        vertices[3] = new Point3D(length,0,0);
        vertices[1] = new Point3D(length,width,0);
        vertices[0] = new Point3D(length,width,height);
        vertices[2] = new Point3D(length,0,height);

        //empty container
        for (int i = 0; i < container.length; i++){
            for (int j = 0; j < container[i].length; j++){
                for(int k = 0; k < container[i][j].length; k++) {
                    container[i][j][k] = -1;
                }
            }
        }
    }
    public int[][][] getContainer() {
        return container;
            }
     
            
    public int getWidth() {
        return width;
    }
    public int getLength() {
        return length;
    }
    public int getHeight() {
        return height;
    }
    public Point3D[] getVertices() { return vertices; }
    
    public void addParcel(Parcel parcel)
    {
        for(Point3D point : parcel.getBlockLocations())
   	   {
   	    container[(int)point.getX()][(int)point.getY()][(int)point.getZ()] = parcel.getID();
   	   }
    }

    public void removeParcel(int ID)
    {
        for(int i = 0; i < container.length; i++)
        {
            for(int j = 0; j < container[0].length; j++)
            {
                for(int k = 0; k < container[0][0].length; k++)
                {
                    if(container[i][j][k] == ID)
                        container[i][j][k] = -1;
                }
            }
        }
    }
}
