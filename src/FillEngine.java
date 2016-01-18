import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by pmmde on 1/14/2016.
 */
public abstract class FillEngine {
    private static Parcel[] parcelPrototype={new ParcelA(),new ParcelB(),new ParcelC(),new ParcelL(),new ParcelP(), new ParcelT()};

    protected double cScore=1;
    protected double cFilled=5;

    public class Result{
        public double score=0;
        public double combinedScore=0;
        public int proccentFilled=0;
        public int scoreID=0;
    }
    public class Setting {
        /*protected double cValue = 1.0;
        protected double cVolumetricValue = 1.0;
        protected double cGaps = -1.0;
        protected double[] cCoordinates = {-1, -1, -1};*/
    /*
     * value
     * volumentric value
     * gaps
     * * * coordinates
     */
        public double[]c={1.0,1.0,-1.0,-1,-1,-1};
    }
    public class SettingExt extends Setting{
        public SettingExt(Setting s)
        {
            for(int i=0;i<s.c.length;i++)
            {
                c[i]=s.c[i];
            }
            /*cValue =s.cValue;
            cVolumetricValue =s.cVolumetricValue;
            cGaps = s.cGaps;
            cCoordinates = new double[]{s.cCoordinates[0], s.cCoordinates[1], s.cCoordinates[2]};
            cParcelShare = new double[]{s.cParcelShare[0], s.cParcelShare[1], s.cParcelShare[2], s.cParcelShare[3], s.cParcelShare[4], s.cParcelShare[5]};*/
        }
        public int amountParcels[] = {0, 0, 0, 0, 0, 0};
        public double parcelShare[]={0,0,0,0,0,0};
        public double maxScore=-9999;
        public Parcel parcel=null;
        public int parcelID=0;
        public int total;
        public int totalScore=0;
    }

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
                                    score += 0*s.c[2]; //TODO: make fucntion that counts gaps
                                    score += i*s.c[3];
                                    score += j*s.c[4];
                                    score += k*s.c[5];
                                    /*for(int a=0;a<s.amountParcels.length;a++){
                                        score+= s.parcelShare[a]*s.c[6+a];
                                    }*/
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
    public void calcShare(SettingExt s) {
        double amountOfParcels=0;
        for(int i=0;i<s.amountParcels.length;i++)
        {
            amountOfParcels+=s.amountParcels[i];
        }
        for(int i=0;i<s.parcelShare.length;i++)
        {
            s.parcelShare[i]/=amountOfParcels;
        }
    }
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
    public Setting[] mutate(Setting s) {
        return null;
    }
    public Setting createRandomSetting(double range) {
        Setting s=new Setting();
        for(int i=0;i<s.c.length;i++)
        {
            s.c[i]=(Math.random()*range*2.0)-range;
        }
        return s;
    }
}
