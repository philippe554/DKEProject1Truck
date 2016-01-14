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
    //OLD CODE!!!!!
    protected Container truck;
    //List of parcels that are fitted to the truck
    protected ArrayList<Parcel> listOfPackets;
    //List of pacets that will not be fitted to the truck (created by the process)
    protected ArrayList<Parcel> rejectedParcels = new ArrayList<Parcel>();
    //List of parcels that have been loaded on truck
    protected ArrayList<Parcel> loadedParcels = new ArrayList<Parcel>();

    public static void main(String[]args){
        int numberOfA=1;
        int numberOfB=1;
        int numberOfC=1;
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
            placeMinimumCoordinates(listOfPackets.get(i));
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
            for (int i = 0; i < truck.getX(); i++) {
                for (int j = 0; j < truck.getY(); j++) {
                    for (int k = 0; k < truck.getZ(); k++) {
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
            rejectedParcels.add(p);
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
            try{
                if(-1 != truck.getContainer()[((int) (pos.get(i).getX() + x))][((int) (pos.get(i).getY() + y))][((int) (pos.get(i).getZ() + z))])
                {
                    possible=false;
                }
            }catch (ArrayIndexOutOfBoundsException error)
            {
                possible=false;
            }
        }
        return possible;
    }

}
