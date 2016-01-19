import javafx.geometry.Point3D;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pmmde on 1/17/2016.
 */
public class experiments {
    private static int amountDatabase[][]={
            /*{100,0,0,0,0,0},
            {0,100,0,0,0,0},
            {0,0,100,0,0,0},
            {0,0,0,300,0,0},
            {0,0,0,0,300,0},
            {0,0,0,0,0,300},
            {100,100,100,0,0,0},
            {50,50,50,0,0,0},
            {0,0,0,100,100,100},
            {0,0,0,300,300,300},
            {50,50,50,50,50,50},
            {25,25,25,25,25,25},
            {-1,-1,-1,-1,-1,-1,50},
            {-1,-1,-1,0,0,0,75},
            {0,0,0,-1,-1,-1,200},*/
            {100,100,100,300,300,300}};
    private static Point3D containerSize=new Point3D(33,8,5);

    private static ArrayList<Parcel> makeList(int j) {
        int numberOfParcels[]={0,0,0,0,0,0};
        for(int i=0;i<numberOfParcels.length;i++)
        {
            if(amountDatabase[j][i]==-1)
            {
                numberOfParcels[i]=(int)(Math.random()*amountDatabase[j][6]);
            }
            else{
                numberOfParcels[i]=amountDatabase[j][i];
            }
        }
        ArrayList<Parcel> list = new ArrayList<Parcel>();
        for(int i=0;i<numberOfParcels[0];i++)
        {
            list.add(new ParcelA());
        }
        for(int i=0;i<numberOfParcels[1];i++)
        {
            list.add(new ParcelB());
        }
        for(int i=0;i<numberOfParcels[2];i++)
        {
            list.add(new ParcelC());
        }
        for(int i=0;i<numberOfParcels[3];i++)
        {
            list.add(new ParcelL());
        }
        for(int i=0;i<numberOfParcels[4];i++)
        {
            list.add(new ParcelP());
        }
        for(int i=0;i<numberOfParcels[5];i++)
        {
            list.add(new ParcelT());
        }
        int volume=0;
        for(int i=0;i<list.size();i++){
            volume+=list.get(i).getBlockLocations().size();
        }
        if(volume<(containerSize.getX()*containerSize.getY()*containerSize.getZ())){
            return makeList(j);
        }

        return list;
    }
    public static void main(String[]args) {
        startOutputResult();
        //run(new DiagonalFill(),1,"DiagonalFill");
        run(new Genetic(),20,"Genetic");
        //run(new HillClimb(),10,"HillClimb");
        //run(new Random(),1,"Random");
    }
    private static void run(FillEngine fillEngine,int amountOfGenerations,String type) {
        for (int i = 0; i < amountDatabase.length; i++) {
            int generation = 0;
            FillEngine.Setting setting = fillEngine.createRandomSetting(10);
            while (generation < amountOfGenerations) {
                generation++;
                long startTime = System.currentTimeMillis();
                double[] settingCopy = new double[setting.c.length];
                for (int j = 0; j < settingCopy.length; j++) {
                    settingCopy[j] = setting.c[j];
                }
                ArrayList<Parcel> listOfPackets = makeList(i);
                ArrayList<Parcel> loadedParcels = new ArrayList<Parcel>();
                FillEngine.Result result = fillEngine.run(listOfPackets, loadedParcels, setting);
                outputResult(type,i,generation, listOfPackets, loadedParcels, result, System.currentTimeMillis() - startTime, settingCopy, setting);
                System.out.println(type+": Database: " + i+" Generation: "+generation);
            }
        }
    }
    private static int[] countList(ArrayList<Parcel> list) {
        int[] numberOfParcels={0,0,0,0,0,0};
        for(int i=0;i<list.size();i++)
        {
            numberOfParcels[list.get(i).parcelType-1]++;
        }
        return numberOfParcels;
    }
    private static void startOutputResult() {
        try
        {
            String filename= "data.csv";
            FileWriter fw = new FileWriter(filename,true);
            fw.write("type,databaseID,generation,time,A,B,C,L,P,T,A loaded,B loaded,C loaded,L loaded,P loaded,T loaded,combined score,score,filled,C1,C2,C3,C4,C5,C6,start C1,start C2,start C3,start C4,start C5,start C6\n");
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
    private static void outputResult(String type,int database,int generation,ArrayList<Parcel> listOfPackets,ArrayList<Parcel> loadedParcels,FillEngine.Result result,long time, double[]settingCopy,FillEngine.Setting setting) {
        int[]available=countList(listOfPackets);
        int[]loaded=countList(loadedParcels);
        try
        {
            String filename= "data.csv";
            FileWriter fw = new FileWriter(filename,true);
            fw.write(type+","+database+","+generation+","+time+",");
            for(int i=0;i<available.length;i++)
            {
                fw.write(available[i]+",");
            }
            for(int i=0;i<loaded.length;i++)
            {
                fw.write(loaded[i]+",");
            }
            fw.write(result.combinedScore+","+result.score+","+(100-result.proccentFilled)+",");
            for(int i=0;i<setting.c.length;i++)
            {
                fw.write(setting.c[i]+",");
            }
            for(int i=0;i<settingCopy.length;i++)
            {
                fw.write(settingCopy[i]+",");
            }
            fw.write("\n");
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
