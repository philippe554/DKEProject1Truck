import com.sun.javafx.geom.Point2D;
import javafx.geometry.Point3D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

class TetrisBasedFiller extends JPanel {

    class Side{
        private int a,b,c;
        private double planeA;
        private double planeB;
        private double planeC;
        private double planeD;
        private int colorR=100;
        private int colorB=100;
        private int colorG=100;
        private double minColDistance=0;
        private Point3D center;
        private void calcPlane(){
            Point3D d1 = new Point3D(vertices[a].getX()-vertices[c].getX(), //start calc plane second side
                    vertices[a].getY()-vertices[c].getY(),
                    vertices[a].getZ()-vertices[c].getZ());
            Point3D d2 = new Point3D(vertices[b].getX()-vertices[c].getX(),
                    vertices[b].getY()-vertices[c].getY(),
                    vertices[b].getZ()-vertices[c].getZ());
            Point3D cp = new Point3D(d1.getY()*d2.getZ()-d1.getZ()*d2.getY(),
                    d1.getZ()*d2.getX()-d1.getX()*d2.getZ()
                    ,d1.getX()*d2.getY()-d1.getY()*d2.getX());
            planeA=cp.getX();
            planeB=cp.getY();
            planeC=cp.getZ();
            planeD=planeA*vertices[c].getX() +
                    planeB*vertices[c].getY()+
                    planeC*vertices[c].getZ();
            center=new Point3D((vertices[a].getX()+vertices[b].getX()+vertices[c].getX())/3,
                    (vertices[a].getY()+vertices[b].getY()+vertices[c].getY())/3,
                    (vertices[a].getZ()+vertices[b].getZ()+vertices[c].getZ())/3);
            minColDistance=Math.max(Math.max(center.distance(vertices[a]),center.distance(vertices[b])),center.distance(vertices[c]));
        }
        private Side(int A,int B,int C) {
            a=A;b=B;c=C;
            calcPlane();
        }
    }
    class RenderThread extends Thread{
        private int xl,xr,yt,yb;
        public RenderThread(int xLeft,int xRight, int yTop,int yBottom) {
            xl=xLeft;
            xr=xRight;
            yt=yTop;
            yb=yBottom;
        }
        @Override
        public void run() {
            for(int i=xl;i<xr;i++) {
                for (int j = yt; j < yb; j++) {
                    Point3D camAngle = new Point3D(camDirection.getX()+((hPixels /2 - i)*camHVector.getX()+(j-vPixels /2)*camVVector.getX())/viewDesity,
                            camDirection.getY()+((hPixels /2 - i)*camHVector.getY()+(j-vPixels /2)*camVVector.getY())/viewDesity,
                            camDirection.getZ()+((hPixels /2 - i)*camHVector.getZ()+(j-vPixels /2 )*camVVector.getZ())/viewDesity);
                    camAngle=camAngle.normalize();

                    double minDebt=-1;
                    int clossedSideID = 0;
                    Point3D clossedCol = null;
                    for(int k=0;k<side.length;k++)
                    {
                        double Nv = side[k].planeA*camAngle.getX() + side[k].planeB*camAngle.getY() + side[k].planeC*camAngle.getZ();
                        double Nr0 = side[k].planeA*camera.getX() + side[k].planeB*camera.getY() + side[k].planeC*camera.getZ();

                        if (Nv != 0)       // line and plane are not parallel and must intersect
                        {
                            double t = (side[k].planeD - Nr0)/Nv;
                            if(t>=1) {
                                Point3D col = new Point3D(camera.getX() + t * camAngle.getX(), camera.getY() + t * camAngle.getY(), camera.getZ() + t * camAngle.getZ());
                                double distance = col.distance(camera);
                                if (minDebt == -1 || minDebt > distance) {
                                    if(col.distance(side[k].center)<=side[k].minColDistance) {//if it is not close to the triangle, dont claculate it preciese
                                        if (PointInTriangle(col, vertices[side[k].a], vertices[side[k].b], vertices[side[k].c])) {
                                            minDebt = distance;
                                            clossedSideID = k;
                                            clossedCol = col;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(minDebt!=-1) {
                        img.setRGB(i, j, calcColor(clossedSideID, clossedCol, camAngle));
                    }else{
                        img.setRGB(i,j,0);
                    }
                }
            }
        }
        private int calcColor(int sideID,Point3D p,Point3D camAngle){
            boolean blocked=false;
            Point3D sunAngle = subtract3D(sun,p);
            for(int k=0;k<side.length;k++)
            {
                if(k!=sideID){
                    double Nv = side[k].planeA*sunAngle.getX() + side[k].planeB*sunAngle.getY() + side[k].planeC*sunAngle.getZ();
                    double Nr0 = side[k].planeA*p.getX() + side[k].planeB*p.getY() + side[k].planeC*p.getZ();

                    if (Nv != 0)       // line and plane are not parallel and must intersect
                    {
                        double t = (side[k].planeD - Nr0)/Nv;
                        if(t>=0) {
                            Point3D col = new Point3D(p.getX() + t * sunAngle.getX()
                                    , p.getY() + t * sunAngle.getY()
                                    , p.getZ() + t * sunAngle.getZ());
                            if(col.distance(side[k].center)<=side[k].minColDistance) {//if it is not close to the triangle, dont claculate it preciese
                                if (PointInTriangle(col, vertices[side[k].a], vertices[side[k].b], vertices[side[k].c])) {
                                    blocked = true;
                                }
                            }
                        }
                    }
                }
            }

            //calc normal vector (required for shadow)
            double shadow=0.5;
            if(!blocked)
            {
                Point3D sideNormal = subtract3D(vertices[side[sideID].a],vertices[side[sideID].b]).crossProduct(
                        subtract3D(vertices[side[sideID].a],vertices[side[sideID].c])).normalize();
                if((Math.abs(Math.acos(camAngle.dotProduct(sideNormal)))/Math.PI*180.0)<90) //face it in the diraction of the camera
                {
                    sideNormal=new Point3D(-sideNormal.getX(),-sideNormal.getY(),-sideNormal.getZ());
                }
                sideNormal=sideNormal.normalize();

                sunAngle=sunAngle.normalize();

                shadow =  1.0-(Math.abs(Math.acos(sideNormal.dotProduct(sunAngle)))/Math.PI);
                if(shadow<0.5){
                    shadow=0.5;
                }
            }
            return ((side[sideID].colorR+(int)(100.0*shadow) << 16) | (side[sideID].colorG+(int)(100.0*shadow) << 8) | side[sideID].colorB+(int)(100.0*shadow));
        }
    }

    private Point3D[] vertices;
    private Side[] side;

    private Point3D sun;
    private Point3D camera;

    private Point3D camDirection;
    private Point3D camHVector;
    private Point3D camVVector;

    private int fps=0;

    private BufferedImage img;
    private double hViewAngle;
    private double vViewAngle;
    private double viewDesity;
    private double hPixels;
    private double vPixels;

    private int amountOfThreads=4;

    public TetrisBasedFiller() {
        hViewAngle=60.0;
        vViewAngle=hViewAngle;
        viewDesity=5.0;
        hPixels=viewDesity*hViewAngle;
        vPixels=viewDesity*vViewAngle;

        sun = new Point3D(20,50,0);

        camera = new Point3D(0,15,-15);

        camDirection = new Point3D(0,-8,8);
        camHVector = new Point3D(0.3,0,0);
        camVVector = new Point3D(0,-0.3,0);

        img = new BufferedImage((int)(hViewAngle*viewDesity), (int)(vViewAngle*viewDesity), BufferedImage.TYPE_INT_RGB);

        vertices = new Point3D[ 20 ];
        vertices[0] = new Point3D( -2, -2, -2 );
        vertices[1] = new Point3D( -2, -2,  2 );
        vertices[2] = new Point3D( -2,  10, -2 );
        vertices[3] = new Point3D( -2,  10,  2 );
        vertices[4] = new Point3D(  2, -2, -2 );
        vertices[5] = new Point3D(  2, -2,  2 );
        vertices[6] = new Point3D(  2,  10, -2 );
        vertices[7] = new Point3D(  2,  10,  2 );

        vertices[8] = new Point3D( -2+5, -2, -2 );
        vertices[9] = new Point3D( -2+5, -2,  2 );
        vertices[10] = new Point3D( -2+5,  2, -2 );
        vertices[11] = new Point3D( -2+5,  2,  2 );
        vertices[12] = new Point3D(  2+5, -2, -2 );
        vertices[13] = new Point3D(  2+5, -2,  2 );
        vertices[14] = new Point3D(  2+5,  2, -2 );
        vertices[15] = new Point3D(  2+5,  2,  2 );

        vertices[16] = new Point3D(  -10,  0,  -10 );
        vertices[17] = new Point3D(  -10,  0,  10 );
        vertices[18] = new Point3D(  10,  0,  10 );
        vertices[19] = new Point3D(  10,  0,  -10 );


        side = new Side[26];
        side[0] = new Side(0,2,4);//front
        side[1] = new Side(2,4,6);
        side[2] = new Side(0,1,3);//left
        side[3] = new Side(0,2,3);
        side[4] = new Side(2,3,7);//top
        side[5] = new Side(2,6,7);
        side[6] = new Side(0,1,5);//bottom
        side[7] = new Side(0,4,5);
        side[8] = new Side(4,6,7);//right
        side[9] = new Side(4,5,7);
        side[10] = new Side(1,3,7);//back
        side[11] = new Side(1,5,7);
        side[24] = new Side(16,17,18);
        side[25] = new Side(16,19,18);
        side[24].colorR=0;
        side[25].colorR=0;
        side[24].colorG=150;
        side[25].colorG=150;
        side[24].colorB=0;
        side[25].colorB=0;

        side[0+12] = new Side(0+8,2+8,4+8);//front
        side[1+12] = new Side(2+8,4+8,6+8);
        side[2+12] = new Side(0+8,1+8,3+8);//left
        side[3+12] = new Side(0+8,2+8,3+8);
        side[4+12] = new Side(2+8,3+8,7+8);//top
        side[5+12] = new Side(2+8,6+8,7+8);
        side[6+12] = new Side(0+8,1+8,5+8);//bottom
        side[7+12] = new Side(0+8,4+8,5+8);
        side[8+12] = new Side(4+8,6+8,7+8);//right
        side[9+12] = new Side(4+8,5+8,7+8);
        side[10+12] = new Side(1+8,3+8,7+8);//back
        side[11+12] = new Side(1+8,5+8,7+8);
    }
    public static void main(String[]args) {
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

        while(true) {
            frames++;
            if(lastFPSRecordTime+1000<System.currentTimeMillis())
            {
                tetrisBasedFiller.fps=frames;
                frames=0;
                lastFPSRecordTime+=1000;
            }
            tetrisBasedFiller.rotate();
            tetrisBasedFiller.renderImage();
            tetrisBasedFiller.repaint();
            /*try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    protected void paintComponent(Graphics g) {
        Graphics2D localGraphics2D = (Graphics2D)g;
        localGraphics2D.drawImage(img,0,0,null);
        localGraphics2D.setColor(Color.red);
        localGraphics2D.drawString(Integer.toString(fps),10,10);
    }
    private Point3D subtract3D(Point3D p1,Point3D p2) {
        return new Point3D(p1.getX()-p2.getX(),p1.getY()-p2.getY(),p1.getZ()-p2.getZ());
    }
    private boolean PointInTriangle(Point3D p,Point3D a,Point3D b,Point3D c) {
        Point3D v0=subtract3D(c,a);
        Point3D v1=subtract3D(b,a);
        Point3D v2=subtract3D(p,a);

        double v0v0 = v0.dotProduct(v0);
        double v0v1 = v0.dotProduct(v1);
        double v0v2 = v0.dotProduct(v2);
        double v1v1 = v1.dotProduct(v1);
        double v1v2 = v1.dotProduct(v2);

        double u = (v1v1*v0v2-v0v1*v1v2)/(v0v0*v1v1-v0v1*v0v1);
        double v = (v0v0*v1v2-v0v1*v0v2)/(v0v0*v1v1-v0v1*v0v1);
        if(u>=0 && v>=0 && u<=1 && v<=1 && (u+v)<=1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private void rotate(){
        double angle =1/180.0*Math.PI;

        double[][] m=new double[3][3];
        m[0][0]=Math.cos(angle);m[0][1]=0;m[0][2]=Math.sin(angle);
        m[1][0]=0;m[1][1]=1;m[1][2]=0;
        m[2][0]=-Math.sin(angle);m[2][1]=0;m[2][2]=Math.cos(angle);

        for(int i=0;i<vertices.length;i++)
        {
            vertices[i] = new Point3D(m[0][0]*vertices[i].getX() + m[0][1]*vertices[i].getY() + m[0][2]*vertices[i].getZ()
                    ,m[1][0]*vertices[i].getX() + m[1][1]*vertices[i].getY() + m[1][2]*vertices[i].getZ()
                    ,m[2][0]*vertices[i].getX() + m[2][1]*vertices[i].getY() + m[2][2]*vertices[i].getZ());
        }
        for(int i=0;i<side.length;i++)
        {
            side[i].calcPlane();
        }

        /*sun = new Point3D(m[0][0]*sun.getX() + m[0][1]*sun.getY() + m[0][2]*sun.getZ()
                ,m[1][0]*sun.getX() + m[1][1]*sun.getY() + m[1][2]*sun.getZ()
                ,m[2][0]*sun.getX() + m[2][1]*sun.getY() + m[2][2]*sun.getZ());*/
    }
    private void renderImage(){
        RenderThread RT[]= new RenderThread[amountOfThreads];
        for(int i=0;i<amountOfThreads;i++) {
            RT[i] = new RenderThread((int)(hPixels*i / amountOfThreads), (int) (hPixels* (i+1) / amountOfThreads), 0, (int) (vPixels));
            RT[i].start();
        }
        for(int i=0;i<amountOfThreads;i++) {
            try {
                RT[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}