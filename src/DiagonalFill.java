import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by pmmde on 1/14/2016.
 */
public class DiagonalFill extends FillEngine{
    public static void main(String[]args){
        int numberOfA= (int) (Math.random()*100);
        int numberOfB= (int) (Math.random()*100);
        int numberOfC=(int) (Math.random()*100);
        int numberOfL=0;//=(int) (Math.random()*0);
        int numberOfP=0;//(int) (Math.random()*0);
        int numberOfT=0;//(int) (Math.random()*0);
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

        DiagonalFill diagonalFill= new DiagonalFill(list);
    }
    public DiagonalFill(ArrayList<Parcel> listOfPackets) {
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
        ArrayList<Parcel> loadedParcels = new ArrayList<Parcel>();
        Result result=run(listOfPackets,loadedParcels,s);
        tetrisBasedFiller.addParcels(loadedParcels);
        tetrisBasedFiller.values= new double[]{result.score,result.proccentFilled};
        while(true) {
            frames++;
            if(lastFPSRecordTime+1000<System.currentTimeMillis()) {
                tetrisBasedFiller.fps = frames;
                frames = 0;
                lastFPSRecordTime += 1000;
            }
            tetrisBasedFiller.rotate();
            tetrisBasedFiller.renderImage();
            tetrisBasedFiller.repaint();
        }
    }
    @Override
    public Setting[] mutate(Setting s) {
        Setting[] sReturn = new Setting[1];
        sReturn[0]=new Setting();
        for(int j=0;j<3;j++)
        {
            sReturn[0].c[j]=0;
        }
        sReturn[0].c[0]=1;
        for(int j=3;j<6;j++)
        {
            sReturn[0].c[j]=-1;
        }
        return sReturn;
    }
}
