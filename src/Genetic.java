import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by pmmde on 1/14/2016.
 */
public class Genetic extends FillEngine {
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

        Genetic genetic=new Genetic(list);
    }
    public Genetic(ArrayList<Parcel> listOfPackets) {
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
        double mutationRate=0.1;
        Setting[] sReturn = new Setting[21];
        for(int i=0;i<20;i++)
        {
            sReturn[i]=new Setting();
            for(int j=0;j<s.c.length;j++)
            {
                if(Math.random()<mutationRate)
                {
                    sReturn[i].c[j]=Math.random()*10*2-10;
                }else
                {
                    sReturn[i].c[j]=s.c[j];
                }
            }
        }
        sReturn[20]=new Setting();
        for(int j=0;j<s.c.length;j++)
        {
            sReturn[20].c[j]=s.c[j];
        }
        return sReturn;
    }
}
