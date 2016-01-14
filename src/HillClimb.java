import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by pmmde on 1/13/2016.
 */
public class HillClimb extends FillEngine{

    public static void main(String[]args){
        int numberOfA= (int) (Math.random()*200);
        int numberOfB= (int) (Math.random()*200);
        int numberOfC=(int) (Math.random()*200);
        int numberOfL=(int) (Math.random()*0);
        int numberOfP=(int) (Math.random()*0);
        int numberOfT=(int) (Math.random()*0);
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
            tetrisBasedFiller.values= new double[]{result.score,result.proccentFilled,generation,99999.0,s.c[0],s.c[1],s.c[2],s.c[3],s.c[4],s.c[5]};
            //tetrisBasedFiller.rotate();
            tetrisBasedFiller.renderImage();
            tetrisBasedFiller.repaint();
        }
    }
    @Override
    public Setting[] mutate(Setting s) {
        double step=3;
        Setting[] sReturn = new Setting[s.c.length*2+5];
        for(int i=0;i<s.c.length;i++)
        {
            sReturn[i*2] = new Setting();
            sReturn[i*2+1] = new Setting();
            for(int j=0;j<s.c.length;j++)
            {
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
}
