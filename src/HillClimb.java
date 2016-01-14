import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by pmmde on 1/13/2016.
 */
public class HillClimb{
    private static Parcel[] parcelPrototype={new ParcelA(),new ParcelB(),new ParcelC(),new ParcelL(),new ParcelP(), new ParcelT()};
    class Setting {
        /*protected double cValue = 1.0;
        protected double cVolumetricValue = 1.0;
        protected double cGaps = -1.0;
        protected double[] cCoordinates = {-1, -1, -1};*/

        protected double[]c={1.0,1.0,-1.0,-1,-1,-1};
    }
    class SettingExt extends Setting{
        private SettingExt(Setting s)
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
        private int amountParcels[] = {0, 0, 0, 0, 0, 0};
        private double parcelShare[]={0,0,0,0,0,0};
        private double maxScore=-9999;
        private Parcel parcel=null;
        private int parcelID=0;
        private int total;
        private int totalScore=0;
    }
    class Result{
        double score=0;
        int proccentFilled=0;
        int scoreID=0;
    }

    public static void main(String[]args){
        int numberOfA= (int) (Math.random()*100);
        int numberOfB= (int) (Math.random()*100);
        int numberOfC=(int) (Math.random()*100);
        int numberOfL=(int) (Math.random()*100);
        int numberOfP=(int) (Math.random()*100);
        int numberOfT=(int) (Math.random()*100);
        System.out.println((numberOfA+numberOfB+numberOfC+numberOfL+numberOfP+numberOfT));
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

        HillClimb hillClimb = new HillClimb(list);
    }
    public HillClimb(ArrayList<Parcel> listOfPackets) {
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

        long lastFPSRecordTime=System.currentTimeMillis();
        int frames=0;

        Setting s=createRandomSetting(10);
        int generation=0;

        while(true) {
            frames++;
            generation++;
            if(lastFPSRecordTime+1000<System.currentTimeMillis())
            {
                tetrisBasedFiller.fps=frames;
                frames=0;
                lastFPSRecordTime+=1000;
            }
            ArrayList<Parcel> loadedParcels = new ArrayList<Parcel>();

            Result result=run(listOfPackets,loadedParcels,s);

            tetrisBasedFiller.emptyParcels();
            tetrisBasedFiller.addParcels(loadedParcels);
            tetrisBasedFiller.values= new double[]{result.score,result.proccentFilled,generation};
            //tetrisBasedFiller.rotate();
            tetrisBasedFiller.renderImage();
            tetrisBasedFiller.repaint();
        }
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
                    for (int i = 0; i < container.getWidth(); i++) {
                        for (int j = 0; j < container.getLength(); j++) {
                            for (int k = 0; k < container.getHeight(); k++) {
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
            if(newResult.score>result.score)
            {
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
        double step=3;
        Setting[] sReturn = new Setting[s.c.length*2+5];
        for(int i=0;i<s.c.length;i++)
        {
            for(int j=0;j<s.c.length;j++)
            {
                sReturn[i*2] = new Setting();
                sReturn[i*2+1] = new Setting();
                if(i==j){
                    sReturn[i*2].c[j]=s.c[j]+step;
                    sReturn[i*2+1].c[j]=s.c[j]-step;
                }else{
                    sReturn[i*2].c[j]=s.c[j];
                    sReturn[i*2+1].c[j]=s.c[j];
                }
            }
        }
        sReturn[sReturn.length-5]=createRandomSetting(10);
        sReturn[sReturn.length-4]=createRandomSetting(10);
        sReturn[sReturn.length-3]=createRandomSetting(10);
        sReturn[sReturn.length-2]=createRandomSetting(10);
        sReturn[sReturn.length-1]= new Setting();
        for(int j=0;j<s.c.length;j++) {
            sReturn[sReturn.length - 1].c[j]=s.c[j];
        }
        return sReturn;
    }
    public Setting createRandomSetting(double range) {
        Setting s=new Setting();
        for(int i=0;i<s.c.length;i++)
        {
            s.c[i]=Math.random()*range*2-range;
        }
        return s;
    }
}
