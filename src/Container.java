import javafx.geometry.Point3D;

import java.util.ArrayList;

public class Container {

    public int width;
    public int length;
    public int height;
    public int[][][] container;
    Point3D[] vertices;
    ArrayList<Parcel> containedParcels = new ArrayList<Parcel>();

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


    public int getX() {
        return width;
    }
    public int getY() {
        return length;
    }
    public int getZ() {
        return height;
    }
    public Point3D[] getVertices() { return vertices; }

    /** Prints the given parcel to the container by its ID
     *
     * @param parcel
     */
    public void addParcel(Parcel parcel)
    {
        for(Point3D point : parcel.getBlockLocations())
        {
            container[(int)point.getX()][(int)point.getY()][(int)point.getZ()] = parcel.getID();
        }
        containedParcels.add(parcel.clone());
    }

    public void removeParcel(Parcel parcel)
    {
        int ID = parcel.getID();
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
        containedParcels.remove(parcel);
    }

    /* Check how much of the truck is empty (in %)
     */
    public int emptyPercent(){
        int emptyCounter = 0;
        int totalCounter = 0;
        for (int i = 0; i < container.length; i++) {
            for (int j = 0; j < container[i].length; j++) {
                for (int k = 0; k < container[i][j].length; k++) {
                    totalCounter++;
                    if (container[i][j][k] == -1) {
                        emptyCounter++;
                    }
                }
            }
        }

        return ((emptyCounter * 100)/totalCounter);
    }

    /** Clones the current container
     *
     * @return A deep clone of current container
     */
    @Override
    public Container clone()
    {
        Container newContainer = new Container(this.width,this.length,this.height);
        ArrayList<Parcel> newContained = new ArrayList<Parcel>();
        for(Parcel p : containedParcels)
        {
            newContained.add(p.clone());
        }
        for(Parcel p : newContained)
        {
            newContainer.addParcel(p);
        }
        return newContainer;
    }

    public ArrayList<Parcel> getContainedParcels() {return containedParcels;}

    public static void main(String[] args)
    {
        Container container = new Container(5,5,5);
        Parcel t = new ParcelT();
        container.addParcel(t);
        for(int[][] k : container.getContainer())
        {
            for(int[] j : k)
            {
                for(int i : j)
                {
                    System.out.print(i);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
        Container newContainer = container.clone();
        for(int[][] k : newContainer.getContainer())
        {
            for(int[] j : k)
            {
                for(int i : j)
                {
                    System.out.print(i);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
    }
}