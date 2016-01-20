import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * extension on FillEngine: random
 * Is not using the main functions of the fill engine, but uses the elementary functions
 */
public class Random extends FillEngine{
    /**
     * Fill all the parcels
     * @param listOfPackets input parcels
     * @param loadedParcels pointer to output parcels
     * @param s the settings
     * @return the result
     */
    public Result run(ArrayList<Parcel> listOfPackets,ArrayList<Parcel>loadedParcels,Setting s) {
        Result result=new Result();
        Container container = new Container(33,8,5);
        boolean stillPossible=true;
        while(listOfPackets.size()>0 && stillPossible)
        {
            int i=(int)Math.random()*listOfPackets.size();
            Parcel p = getAllRotations(listOfPackets.get(i)).get((int) (Math.random()*24));
            if(placeRandom(p,container))
            {
                listOfPackets.remove(i);
                loadedParcels.add(p);
                result.score+=p.getValue();
            }else{
                stillPossible=false;
            }
        }
        result.proccentFilled=container.emptyPercent();
        result.combinedScore=cScore*result.score+cFilled*result.proccentFilled;
        return result;
    }
    /**
     * place the parcel on a random place in the container
     * Gives is a limited amount of tries, before interupting
     * @param p the parcel
     * @param c the container
     * @return true if it was possible to place a parcel
     */
    public boolean placeRandom(Parcel p,Container c) {
        int tries= 0;
        boolean placed=false;
        while(tries < (c.getX()*c.getY()) && !placed) {
            int i = (int) (Math.random() * c.getX());
            int j = (int) (Math.random() * c.getY());
            for (int k = 0; k < c.getZ(); k++) {
                if (possibleToPlace(c,p, i, j, k)) {
                    p.translate(i,j,k);
                    c.addParcel(p);
                    placed=true;
                    k=c.getZ();
                }
            }
            tries++;
        }
        return  placed;
    }
}
