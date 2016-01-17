import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by pmmde on 1/17/2016.
 */
public class FillEngineTester extends JPanel {
    private int fps=0;
    private int generation=0;
    private int amountOfGenerations=0;
    private FillEngine fillEngine;
    private Engine3D engine3D;
    FillEngine.Result result;
    ArrayList<Parcel> loadedParcels;

    private BufferedImage img=null;
    public static void main(String[]args){
        FillEngineTester fillEngineTester = new FillEngineTester();
    }
    public FillEngineTester() {
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

        //fit size
        window.pack();

        //make visible
        window.setVisible(true);

        window.add(this);

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

        engine3D=new Engine3D();
        engine3D.setup();

        //fillEngine = new HillClimb();
        //fillEngine = new Genetic();
        fillEngine = new DiagonalFill();
        amountOfGenerations=1;
        this.run(list);
    }
    public void run(ArrayList<Parcel> listOfPackets){

        long lastFPSRecordTime=System.currentTimeMillis();
        int frames=0;

        FillEngine.Setting setting=fillEngine.createRandomSetting(10);

        while(true) {
            frames++;
            generation++;
            if(lastFPSRecordTime+1000<System.currentTimeMillis())
            {
                fps=frames;
                frames=0;
                lastFPSRecordTime+=1000;
            }
            if(generation>amountOfGenerations){
                engine3D.rotate(1);
                img =engine3D.renderImage();
                repaint();
            }else {
                loadedParcels = new ArrayList<Parcel>();

                result = fillEngine.run(listOfPackets, loadedParcels, setting);

                engine3D.emptyParcels();
                engine3D.addParcels(loadedParcels);
                img = engine3D.renderImage();
                repaint();
            }
        }
    }
    protected void paintComponent(Graphics g) {
        Graphics2D localGraphics2D = (Graphics2D)g;
        if(img!=null) {
            localGraphics2D.drawImage(img, 0, 0, null);
            localGraphics2D.drawString("FPS: "+Integer.toString(loadedParcels.size()),10,30);
        }
        localGraphics2D.setColor(Color.red);
        localGraphics2D.drawString("FPS: "+Integer.toString(fps),10,10);
        /*localGraphics2D.drawString("Sides: "+Integer.toString(side.size()),10,30);
        for(int i=0;i<values.length;i++)
        {
            localGraphics2D.drawString("Value "+Integer.toString(i)+": "+Double.toString(values[i]),10,50 + 20*i);
        }*/
    }
}
