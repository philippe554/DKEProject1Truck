import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Marie on 11/01/2016.
 */
public class Random extends FillEngine{
    public Result run(ArrayList<Parcel> listOfPackets,ArrayList<Parcel>loadedParcels,Setting s) {
        Result result=new Result();
        Container container = new Container(33,8,5);
        boolean stillPossible=true;
        while(listOfPackets.size()>0 && stillPossible)
        {
            int i=(int)Math.random()*listOfPackets.size();
            Parcel p = getAllRotations(listOfPackets.get(i)).get((int) (Math.random()*24));
            listOfPackets.remove(i);
            if(placeRandom(p,container))
            {
                loadedParcels.add(p);
                result.score+=p.getValue();
            }else{
                stillPossible=false;
            }
        }
        result.proccentFilled=(100.0-container.emptyPercent());
        result.combinedScore=cScore*result.score+cFilled*result.proccentFilled;
        return result;
    }
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
                }
            }
            tries++;
        }
        return  placed;
    }
}
