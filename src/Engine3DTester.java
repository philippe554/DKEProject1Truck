import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by pmmde on 1/15/2016.
 */
public class Engine3DTester extends JPanel{

    private BufferedImage img=null;
    int fps=0;
    public static void main(String[]args) {
        int x=800;
        int y=800;
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
        Engine3DTester engine3DTester = new Engine3DTester();
        window.add(engine3DTester);

        //fit size
        window.pack();

        //make visible
        window.setVisible(true);

        Engine3D engine3D=new Engine3D();
        engine3D.setup();
        engine3D.loadTruck();

        long lastFPSRecordTime=System.currentTimeMillis();
        int frames=0;

        while(true) {
            frames++;
            if(lastFPSRecordTime+1000<System.currentTimeMillis())
            {
                engine3DTester.fps=frames;
                frames=0;
                lastFPSRecordTime+=1000;
            }
            engine3D.rotate(1);
            engine3DTester.img =engine3D.renderImage();
        }
    }
    protected void paintComponent(Graphics g) {
        Graphics2D localGraphics2D = (Graphics2D)g;
        localGraphics2D.drawImage(img,0,0,null);
        localGraphics2D.setColor(Color.red);
        localGraphics2D.drawString("FPS: "+Integer.toString(fps),10,10);
        /*localGraphics2D.drawString("Sides: "+Integer.toString(side.size()),10,30);
        for(int i=0;i<values.length;i++)
        {
            localGraphics2D.drawString("Value "+Integer.toString(i)+": "+Double.toString(values[i]),10,50 + 20*i);
        }*/
    }
}
