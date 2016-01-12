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
        int numberOfA=0;
        int numberOfB=0;
        int numberOfC=100;
        int numberOfL=0;
        int numberOfP=0;
        int numberOfT=0;
        ArrayList<Parcel> list = new ArrayList<Parcel>();
        for(int i=0;i<numberOfA;i++)
        {
            list.add(new ParcelA());
        }
        for(int i=0;i<numberOfB;i++)
        {
            list.add(new ParcelB());
        }
        for(int i=0;i<numberOfC;i++)
        {
            list.add(new ParcelC());
        }
        for(int i=0;i<numberOfL;i++)
        {
            list.add(new ParcelL());
        }
        for(int i=0;i<numberOfP;i++)
        {
            list.add(new ParcelP());
        }
        for(int i=0;i<numberOfT;i++)
        {
            list.add(new ParcelT());
        }

        Greedy greedy = new Greedy(new Container(33,8,5),list);
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
                //loadedParcels.add(listOfPackets.get(i));
            }else
            {
                rejectedParcels.add(listOfPackets.get(i));
            }
        }

        double values[] = {truck.emptyPercent(),loadedParcels.size()};
        tetrisBasedFiller.values=values;
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
    public ArrayList<Parcel> getAllRotations(Parcel p) {
        ArrayList<Parcel> rotations = new ArrayList<Parcel>();
        for(int j=0;j<4;j++) {
            for (int i = 0; i < 4; i++) {
                rotations.add(new Parcel(p.getBlockLocations()));
                p.rotateZ();
            }
            p.rotateX();
        }
        p.rotateY();
        for (int i = 0; i < 4; i++) {
            rotations.add(new Parcel(p.getBlockLocations()));
            p.rotateZ();
        }
        p.rotateY();
        p.rotateY();
        for (int i = 0; i < 4; i++) {
            rotations.add(new Parcel(p.getBlockLocations()));
            p.rotateZ();
        }
        p.rotateY();
        //TODO: check if there are copies
        return rotations;
    }
    public boolean placeMinimumCoordinates(Parcel p ) {
        int minX=9999;
        int minY=9999;
        int minZ=9999;
        int bestRotation=0;
        ArrayList<Parcel> rotations = getAllRotations(p);
        for(int r=0;r<rotations.size();r++) {
            for (int i = 0; i < truck.getWidth(); i++) {
                for (int j = 0; j < truck.getLength(); j++) {
                    for (int k = 0; k < truck.getHeight(); k++) {
                        if (possibleToPlace(rotations.get(r), i, j, k)) {
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
            p=rotations.get(bestRotation);
            p.translate(minX,minY,minZ);
            truck.addParcel(p);
            loadedParcels.add(p);
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
