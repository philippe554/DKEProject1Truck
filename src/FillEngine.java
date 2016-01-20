import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Parent class of the algorithms except the brute fore
 */
public class FillEngine {
    private static Parcel[] parcelPrototype={new ParcelA(),new ParcelB(),new ParcelC(),new ParcelL(),new ParcelP(), new ParcelT()};

    protected double cScore=1;
    protected double cFilled=1;

    /**
     * The result of a run containing certain information
     */
    public class Result{
        public double score=0;
        public double combinedScore=0;
        public double proccentFilled=0;
        public int scoreID=0;
    }
    /**
     * a prototype for the settings containing the weights
     */
    public class Setting {
        public double[]c={1.0,1.0,-1,-1,-1};
    }
    /**
     * the extended setting containing more information about the run
     */
    public class SettingExt extends Setting{
        /**
         * make a extended setting of a normal setting
         * @param s the normal setting
         */
        public SettingExt(Setting s) {
            for(int i=0;i<s.c.length;i++)
            {
                c[i]=s.c[i];
            }
        }
        public int amountParcels[] = {0, 0, 0, 0, 0, 0};
        public double maxScore=-9999;
        public Parcel parcel=null;
        public int parcelID=0;
        public int total;
        public int totalScore=0;
    }
    /**
     * get a list of all rotations of this parcels, copies not deleted yet
     * @param p the input parcel
     * @return all 24 rotations
     */
    public ArrayList<Parcel> getAllRotations(Parcel p) {
        ArrayList<Parcel> rotations = new ArrayList<Parcel>();
        for(int j=0;j<4;j++) {
            for (int i = 0; i < 4; i++) {
                rotations.add(p.clone());
                p.rotateZ();
            }
            p.rotateX();
        }
        p.rotateY();
        for (int i = 0; i < 4; i++) {
            rotations.add(p.clone());
            p.rotateZ();
        }
        p.rotateY();
        p.rotateY();
        for (int i = 0; i < 4; i++) {
            rotations.add(p.clone());
            p.rotateZ();
        }
        p.rotateY();
        //TODO: check if there are copies
        return rotations;
    }
    /**
     * check if it is possible to place this parcel on this place in a container
     * @param container the container
     * @param p the parcel
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return true if possible to place
     */
    public boolean possibleToPlace(Container container ,Parcel p,int x,int y,int z){
        boolean possible=true;
        ArrayList<Point3D> pos = p.getBlockLocations();
        for(int i=0;i<pos.size();i++){
            try{
                if(-1 != container.getContainer()[((int) (pos.get(i).getX() + x))][((int) (pos.get(i).getY() + y))][((int) (pos.get(i).getZ() + z))])
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
    /**
     * fill in all possible parcels
     * @param listOfPackets list of available parcels
     * @param loadedParcels the pointer to loaded parcels
     * @param container the container, typical empty
     * @param setting the setting
     * @return the result
     */
    public Result fill(ArrayList<Parcel> listOfPackets,ArrayList<Parcel> loadedParcels, Container container,Setting setting){
        SettingExt s = new SettingExt(setting);
        s.total=listOfPackets.size();
        for(int i =0;i<listOfPackets.size();i++)
        {
            if(listOfPackets.get(i) instanceof ParcelA)
            {
                s.amountParcels[0]++;
            }else if(listOfPackets.get(i) instanceof ParcelB)
            {
                s.amountParcels[1]++;
            }else if(listOfPackets.get(i) instanceof ParcelC)
            {
                s.amountParcels[2]++;
            }else if(listOfPackets.get(i) instanceof ParcelL)
            {
                s.amountParcels[3]++;
            }else if(listOfPackets.get(i) instanceof ParcelP)
            {
                s.amountParcels[4]++;
            }else if(listOfPackets.get(i) instanceof ParcelT)
            {
                s.amountParcels[5]++;
            }
        }
        while(fillNext(loadedParcels,container,s)) {}
        Result result=new Result();
        result.score=s.totalScore;
        result.proccentFilled=container.emptyPercent();
        return result;
    }
    /**
     * fill in the next parcel
     * @param loadedParcels list of already loaded parcels
     * @param container the container containing already filled areas
     * @param s the extended setting, containing the setting and the amount af parcels that still needs to be placed
     * @return true if it was still possible to place something
     */
    public boolean fillNext(ArrayList<Parcel> loadedParcels,Container container,SettingExt s) {
        //calcShare(s);
        for(int l=0;l<s.amountParcels.length;l++) {
            if(s.amountParcels[l]>0) {
                ArrayList<Parcel> rotations = getAllRotations(parcelPrototype[l]);
                for (int r = 0; r < rotations.size(); r++) {
                    for (int i = 0; i < container.getX(); i++) {
                        for (int j = 0; j < container.getY(); j++) {
                            for (int k = 0; k < container.getZ(); k++) {
                                if (possibleToPlace(container,rotations.get(r), i, j, k)) {
                                    double score = rotations.get(r).getValue()*s.c[0];
                                    score += rotations.get(r).getVolumetricValue()*s.c[1];
                                    score += i*s.c[2];
                                    score += j*s.c[3];
                                    score += k*s.c[4];
                                    if(score>s.maxScore)
                                    {
                                        s.maxScore=score;
                                        s.parcel=rotations.get(r).clone();
                                        s.parcel.translate(i,j,k);
                                        s.parcelID=l;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(s.maxScore==-9999)
        {
            return false;
        }
        else {
            container.addParcel(s.parcel);
            loadedParcels.add(s.parcel);
            s.amountParcels[s.parcelID]--;
            s.total--;
            s.totalScore+=s.parcel.getValue();
            s.maxScore=-9999;
            return true;
        }
    }
    /**
     * fill a truc with the given parcels and setting
     * @param listOfPackets input parcels
     * @param loadedParcels pointer to output parcels
     * @param s the settings
     * @return the result, containing information about the run
     */
    public Result run(ArrayList<Parcel> listOfPackets,ArrayList<Parcel>loadedParcels,Setting s) {
        Result result=new Result();
        Setting[] settings = mutate(s);
        for(int i=0;i<settings.length;i++)
        {
            ArrayList<Parcel> tLoadedParcels = new ArrayList<Parcel>();
            Result newResult=fill(listOfPackets,tLoadedParcels,new Container(33,8,5),settings[i]);
            newResult.combinedScore=cScore*newResult.score+cFilled*(100.0-newResult.proccentFilled);
            if(newResult.combinedScore>result.combinedScore)
            {
                result.combinedScore=newResult.combinedScore;
                result.scoreID=i;
                result.score=newResult.score;
                result.proccentFilled=newResult.proccentFilled;
                loadedParcels.clear();
                for(int j=0;j<tLoadedParcels.size();j++)
                {
                    loadedParcels.add(tLoadedParcels.get(j));
                }
            }
        }
        for(int i=0;i<s.c.length;i++)
        {
            s.c[i]=settings[result.scoreID].c[i];
        }
        return result;
    }
    /**
     * mutate a setting, could be overwritten to change behavior of engine
     * @param s the start setting
     * @return the list of new settings
     */
    public Setting[] mutate(Setting s) {
        Setting[] sReturn = new Setting[1];
        sReturn[0]=new Setting();
        for(int j=0;j<sReturn[0].c.length;j++)
        {
            sReturn[0].c[j]=s.c[j];
        }
        return sReturn;
    }
    /**
     * Create a random setting
     * @param range the range of the setting
     * @return the setting
     */
    public Setting createRandomSetting(double range) {
        Setting s=new Setting();
        for(int i=0;i<s.c.length;i++)
        {
            s.c[i]=(Math.random()*range*2.0)-range;
        }
        return s;
    }
    /**
     * create a custom setting
     * @param c1 weight of value
     * @param c2 weight of volumetrix value
     * @param c3 weight of x
     * @param c4 weight of y
     * @param c5 weight of z
     * @return the setting
     */
    public Setting createCustomSetting(double c1,double c2, double c3, double c4, double c5) {
        Setting s = new Setting();
        s.c[0]=c1;
        s.c[1]=c2;
        s.c[2]=c3;
        s.c[3]=c4;
        s.c[4]=c5;
        return s;
    }
}
