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
}
