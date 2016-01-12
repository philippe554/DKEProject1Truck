import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by pmmde on 1/11/2016.
 */
public class Greedy {
    private Container truck;
    //List of parcels that are fitted to the truck
    private ArrayList<Parcel> listOfPackets;
    //List of pacets that will not be fitted to the truck (created by the process)
    private ArrayList<Parcel> rejectedParcels = new ArrayList<Parcel>();
    //List of parcels that have been loaded on truck
    private ArrayList<Parcel> loadedParcels = new ArrayList<Parcel>();

    public static void main(String[]args){
        ArrayList<Parcel> list = new ArrayList<Parcel>();
        list.add(new ParcelC());list.add(new ParcelC());list.add(new ParcelC());list.add(new ParcelC());
        list.add(new ParcelB());list.add(new ParcelB());list.add(new ParcelB());list.add(new ParcelB());
        list.add(new ParcelA());list.add(new ParcelA());list.add(new ParcelA());list.add(new ParcelA());
        list.add(new ParcelL());list.add(new ParcelL());list.add(new ParcelL());list.add(new ParcelC());
        list.add(new ParcelP());list.add(new ParcelP());list.add(new ParcelP());list.add(new ParcelB());
        list.add(new ParcelT());list.add(new ParcelT());list.add(new ParcelT());list.add(new ParcelA());

        Greedy greedy = new Greedy(new Container(33,5,15),list);
    }
    public Greedy(Container tTruck, ArrayList<Parcel> tListOfPackets) {
        int x=400;
        int y=400;
        //make new frame
        JFrame window = new JFrame("Pentomino");
        window.setPreferredSize(new Dimension(x,y));

        //set some parameters
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        //place in the center of the screen
        Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((localDimension.width - x) / 2, (localDimension.height - y) / 2);

        //add the PaintComponent to the window
        TetrisBasedFiller tetrisBasedFiller = new TetrisBasedFiller();
        window.add(tetrisBasedFiller);

        //fit size
        window.pack();

        //make visible
        window.setVisible(true);

        //Initialization of variables
        truck = tTruck;
        listOfPackets = tListOfPackets;
        //Sorting the list from largest value to smallest
        Collections.sort(listOfPackets);

        for(int i=0;i<listOfPackets.size();i++)
        {
            if(placeMinimumCoordinates(listOfPackets.get(i)))
            {
                loadedParcels.add(listOfPackets.get(i));
            }else
            {
                rejectedParcels.add(listOfPackets.get(i));
            }
        }

        tetrisBasedFiller.addParcels(loadedParcels);

        long lastFPSRecordTime=System.currentTimeMillis();
        int frames=0;

        while(true) {
            frames++;
            if(lastFPSRecordTime+1000<System.currentTimeMillis())
            {
                tetrisBasedFiller.fps=frames;
                frames=0;
                lastFPSRecordTime+=1000;
            }
            tetrisBasedFiller.rotate();
            tetrisBasedFiller.renderImage();
            tetrisBasedFiller.repaint();
            /*try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
    public void rotateParcel(int rot, Parcel p) {
        // rotates parcel to move through all possible iterations.
        if(rot > 12) {p.rotateY(); p.rotateY(); rot = rot-12;}
        if(rot > 9) {p.rotateZ();}
        if(rot > 6) {p.rotateZ();}
        if(rot > 3) {p.rotateZ();}
        if(rot%3==0) {p.rotateY();}
        if(rot%3 == 2){p.rotateX();}
    }
    public boolean placeMinimumCoordinates(Parcel p ) {
        int minX=9999;
        int minY=9999;
        int minZ=9999;
        int bestRotation=0;
        for(int r=0;r<p.getRotations();r++) {
            Parcel tParcel = new Parcel(p.getBlockLocations());
            rotateParcel(r, tParcel);
            for (int i = 0; i < truck.getWidth(); i++) {
                for (int j = 0; j < truck.getLength(); j++) {
                    for (int k = 0; k < truck.getHeight(); k++) {
                        if (possibleToPlace(tParcel, i, j, k)) {
                            if ((i + j + k) < (minX + minY + minZ)) {
                                minX = i;
                                minY = j;
                                minZ = k;
                                bestRotation=r;
                            }
                        }
                    }
                }
            }
        }
        if(minX==9999&&minY==9999&&minZ==9999)
        {
            return false;
        }
        else {
            rotateParcel(bestRotation, p);
            p.translate(minX,minY,minZ);
            truck.addParcel(p);
            return true;
        }
    }
    public boolean possibleToPlace(Parcel p,int x,int y,int z){
        boolean possible=true;
        ArrayList<Point3D> pos = p.getBlockLocations();
        for(int i=0;i<pos.size();i++){
            if(pos.get(i).getX()+x>=0 && pos.get(i).getX()+x<truck.getWidth())
            {
                if(pos.get(i).getY()+y>=0 && pos.get(i).getY()+y<truck.getLength())
                {
                    if(pos.get(i).getZ()+z>=0 && pos.get(i).getZ()+z<truck.getHeight())
                    {
                        if(-1 != truck.getContainer()[((int) (pos.get(i).getX() + x))][((int) (pos.get(i).getY() + y))][((int) (pos.get(i).getZ() + z))])
                        {
                            possible=false;
                        }
                    }
                    else
                    {
                        possible=false;
                    }
                }
                else
                {
                    possible=false;
                }
            }
            else
            {
                possible=false;
            }
        }
        return possible;
    }

}
