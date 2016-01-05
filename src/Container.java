import javafx.geometry.Point3D;
/**
 * Created by asus on 04.01.2016.
 */

public class Container {

    /*test
   public static void main(String[] args){


       Container b = new Container(4,3,5);

       System.out.println(b.getContainer()[0][0][0]);
   }*/

    public int width;
    public int length;
    public int height;
    public int[][][] container;

    public Container(int width,int length,int height ){
        this.width= width;
        this.length = length;
        this.height = height;
        this.container = new int[width][length][height];

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
    public int getlength() {
        return length;
    }
    public int getheight() {
        return height;
    }

    public Point3D[] getVerticles(){
        Point3D[] verticles = new Point3D[8];
        verticles[0] = new Point3D(0,0,0);
        verticles[1] = new Point3D(0,width,0);
        verticles[2] = new Point3D(0,width,height);
        verticles[3] = new Point3D(0,0,height);
        verticles[4] = new Point3D(length,0,0);
        verticles[5] = new Point3D(length,width,0);
        verticles[6] = new Point3D(length,width,height);
        verticles[7] = new Point3D(length,0,height);

        return verticles;
    }
}
