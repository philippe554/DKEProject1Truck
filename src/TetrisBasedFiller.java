import com.sun.javafx.geom.Point2D;
import javafx.geometry.Point3D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;

class TetrisBasedFiller extends JPanel {

    //OLD CODE!!!
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
            Point3D d1 = new Point3D(vertices.get(a).getX()-vertices.get(c).getX(), //start calc plane second side
                    vertices.get(a).getY()-vertices.get(c).getY(),
                    vertices.get(a).getZ()-vertices.get(c).getZ());
            Point3D d2 = new Point3D(vertices.get(b).getX()-vertices.get(c).getX(),
                    vertices.get(b).getY()-vertices.get(c).getY(),
                    vertices.get(b).getZ()-vertices.get(c).getZ());
            Point3D cp = new Point3D(d1.getY()*d2.getZ()-d1.getZ()*d2.getY(),
                    d1.getZ()*d2.getX()-d1.getX()*d2.getZ()
                    ,d1.getX()*d2.getY()-d1.getY()*d2.getX());
            planeA=cp.getX();
            planeB=cp.getY();
            planeC=cp.getZ();
            planeD=planeA*vertices.get(c).getX() +
                    planeB*vertices.get(c).getY()+
                    planeC*vertices.get(c).getZ();
            center=new Point3D((vertices.get(a).getX()+vertices.get(b).getX()+vertices.get(c).getX())/3,
                    (vertices.get(a).getY()+vertices.get(b).getY()+vertices.get(c).getY())/3,
                    (vertices.get(a).getZ()+vertices.get(b).getZ()+vertices.get(c).getZ())/3);
            minColDistance=Math.max(Math.max(center.distance(vertices.get(a)),center.distance(vertices.get(b))),center.distance(vertices.get(c)));
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
                    for(int k=0;k<side.size();k++)
                    {
                        double Nv = side.get(k).planeA*camAngle.getX() + side.get(k).planeB*camAngle.getY() + side.get(k).planeC*camAngle.getZ();
                        double Nr0 = side.get(k).planeA*camera.getX() + side.get(k).planeB*camera.getY() + side.get(k).planeC*camera.getZ();

                        if (Nv != 0)       // line and plane are not parallel and must intersect
                        {
                            double t = (side.get(k).planeD - Nr0)/Nv;
                            if(t>=1) {
                                Point3D col = new Point3D(camera.getX() + t * camAngle.getX(), camera.getY() + t * camAngle.getY(), camera.getZ() + t * camAngle.getZ());
                                double distance = col.distance(camera);
                                if (minDebt == -1 || minDebt > distance) {
                                    if(col.distance(side.get(k).center)<=side.get(k).minColDistance) {//if it is not close to the triangle, dont claculate it preciese
                                        if (PointInTriangle(col, vertices.get(side.get(k).a), vertices.get(side.get(k).b), vertices.get(side.get(k).c))) {
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
            for(int k=0;k<side.size();k++)
            {
                if(k!=sideID){
                    double Nv = side.get(k).planeA*sunAngle.getX() + side.get(k).planeB*sunAngle.getY() + side.get(k).planeC*sunAngle.getZ();
                    double Nr0 = side.get(k).planeA*p.getX() + side.get(k).planeB*p.getY() + side.get(k).planeC*p.getZ();

                    if (Nv != 0)       // line and plane are not parallel and must intersect
                    {
                        double t = (side.get(k).planeD - Nr0)/Nv;
                        if(t>=0) {
                            Point3D col = new Point3D(p.getX() + t * sunAngle.getX()
                                    , p.getY() + t * sunAngle.getY()
                                    , p.getZ() + t * sunAngle.getZ());
                            if(col.distance(side.get(k).center)<=side.get(k).minColDistance) {//if it is not close to the triangle, dont claculate it preciese
                                if (PointInTriangle(col, vertices.get(side.get(k).a), vertices.get(side.get(k).b), vertices.get(side.get(k).c))) {
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
                Point3D sideNormal = subtract3D(vertices.get(side.get(sideID).a),vertices.get(side.get(sideID).b)).crossProduct(
                        subtract3D(vertices.get(side.get(sideID).a),vertices.get(side.get(sideID).c))).normalize();
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
            return ((side.get(sideID).colorR+(int)(100.0*shadow) << 16) | (side.get(sideID).colorG+(int)(100.0*shadow) << 8) | side.get(sideID).colorB+(int)(100.0*shadow));
        }
    }

    private ArrayList<Point3D> vertices;
    private ArrayList<Side> side;

    private Point3D sun;
    private Point3D camera;

    private Point3D camDirection;
    private Point3D camHVector;
    private Point3D camVVector;

    public int fps=0;

    private BufferedImage img;
    private double hViewAngle;
    private double vViewAngle;
    private double viewDesity;
    private double hPixels;
    private double vPixels;

    private int amountOfThreads=4;

    public double[]values = {0};

    public TetrisBasedFiller() {
        hViewAngle=60.0;
        vViewAngle=hViewAngle;
        viewDesity=5.0;
        hPixels=viewDesity*hViewAngle;
        vPixels=viewDesity*vViewAngle;

        sun = new Point3D(20,50,0);

        camera = new Point3D(30,30,30);

        camDirection = new Point3D(-8,-8,-8);
        camHVector = new Point3D(-0.2,0,0.2);
        camVVector = new Point3D(0,-0.3,0);

        img = new BufferedImage((int)(hViewAngle*viewDesity), (int)(vViewAngle*viewDesity), BufferedImage.TYPE_INT_RGB);

        vertices = new ArrayList<Point3D>();
        /*vertices.add(new Point3D(  -100,  -3,  -100 ));
        vertices.add(new Point3D(  -100,  -3,  100 ));
        vertices.add(new Point3D(  100,  -3,  100 ));
        vertices.add(new Point3D(  100,  -3,  -100 ));*/

        side = new ArrayList<Side>();
        /*side.add(new Side(0,1,2));
        side.add(new Side(0,3,2));
        side.get(0).colorR=0;
        side.get(1).colorR=0;
        side.get(0).colorG=150;
        side.get(1).colorG=150;
        side.get(0).colorB=0;
        side.get(1).colorB=0;*/

        /*LinkedList<Parcel> parcels = new LinkedList<Parcel>();
        parcels.add(new ParcelA(3,new Point3D(0,5,0)));
        addParcels(parcels);
*/
        /*
        vertices.add(new Point3D( -2, -2, -2 ));
        vertices.add(new Point3D( -2, -2,  2 ));
        vertices.add(new Point3D( -2,  10, -2 ));
        vertices.add(new Point3D( -2,  10,  2 ));
        vertices.add(new Point3D(  2, -2, -2 ));
        vertices.add(new Point3D(  2, -2,  2 ));
        vertices.add(new Point3D(  2,  10, -2 ));
        vertices.add(new Point3D(  2,  10,  2 ));

        vertices.add(new Point3D( -2+5, -2, -2 ));
        vertices.add(new Point3D( -2+5, -2,  2 ));
        vertices.add(new Point3D( -2+5,  2, -2 ));
        vertices.add(new Point3D( -2+5,  2,  2 ));
        vertices.add(new Point3D(  2+5, -2, -2 ));
        vertices.add(new Point3D(  2+5, -2,  2 ));
        vertices.add(new Point3D(  2+5,  2, -2 ));
        vertices.add(new Point3D(  2+5,  2,  2 ));

        side.add(new Side(0,2,4));//front
        side.add(new Side(2,4,6));
        side.add(new Side(0,1,3));//left
        side.add(new Side(0,2,3));
        side.add(new Side(2,3,7));//top
        side.add(new Side(2,6,7));
        side.add(new Side(0,1,5));//bottom
        side.add(new Side(0,4,5));
        side.add(new Side(4,6,7));//right
        side.add(new Side(4,5,7));
        side.add(new Side(1,3,7));//back
        side.add(new Side(1,5,7));

        side.add( new Side(0+8,2+8,4+8));//front
        side.add( new Side(2+8,4+8,6+8));
        side.add( new Side(0+8,1+8,3+8));//left
        side.add( new Side(0+8,2+8,3+8));
        side.add( new Side(2+8,3+8,7+8));//top
        side.add( new Side(2+8,6+8,7+8));
        side.add( new Side(0+8,1+8,5+8));//bottom
        side.add( new Side(0+8,4+8,5+8));
        side.add( new Side(4+8,6+8,7+8));//right
        side.add( new Side(4+8,5+8,7+8));
        side.add( new Side(1+8,3+8,7+8));//back
        side.add( new Side(1+8,5+8,7+8));*/
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

    public void addParcels(ArrayList<Parcel> parcels) {
        for(int i=0;i<parcels.size();i++) {
            int colorR = (int) (Math.random() * 150);
            int colorG = (int) (Math.random() * 150);
            int colorB = (int) (Math.random() * 150);
            ArrayList<Point3D[]> newSides=parcels.get(i).getSides();
            for(int j=0;j<newSides.size();j++)
            {
                for(int k=0;k<newSides.get(j).length;k++)
                {
                    vertices.add(newSides.get(j)[k]);
                }
                side.add(new Side(vertices.size()-3,vertices.size()-2,vertices.size()-1));//front
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
            }
        }/*
        for(int i=0;i<parcels.size();i++)
        {
            int colorR= (int) (Math.random()*150);
            int colorG= (int) (Math.random()*150);
            int colorB= (int) (Math.random()*150);
            for(int j=0;j<parcels.get(i).getBlockLocations().size();j++) {
                int begin = vertices.size();
                vertices.add(new Point3D(-0.5, -0.5, -0.5).add(parcels.get(i).getBlockLocations().get(j)));
                vertices.add(new Point3D(-0.5, -0.5, 0.5).add(parcels.get(i).getBlockLocations().get(j)));
                vertices.add(new Point3D(-0.5, 0.5, -0.5).add(parcels.get(i).getBlockLocations().get(j)));
                vertices.add(new Point3D(-0.5, 0.5, 0.5).add(parcels.get(i).getBlockLocations().get(j)));
                vertices.add(new Point3D(0.5, -0.5, -0.5).add(parcels.get(i).getBlockLocations().get(j)));
                vertices.add(new Point3D(0.5, -0.5, 0.5).add(parcels.get(i).getBlockLocations().get(j)));
                vertices.add(new Point3D(0.5, 0.5, -0.5).add(parcels.get(i).getBlockLocations().get(j)));
                vertices.add(new Point3D(0.5, 0.5, 0.5).add(parcels.get(i).getBlockLocations().get(j)));

                side.add(new Side(begin+0,begin+2,begin+4));//front
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+2,begin+4,begin+6));
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+0,begin+1,begin+3));//left
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+0,begin+2,begin+3));
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+2,begin+3,begin+7));//top
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+2,begin+6,begin+7));
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+0,begin+1,begin+5));//bottom
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+0,begin+4,begin+5));
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+4,begin+6,begin+7));//right
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+4,begin+5,begin+7));
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+1,begin+3,begin+7));//back
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
                side.add(new Side(begin+1,begin+5,begin+7));
                side.get(side.size()-1).colorR=colorR;
                side.get(side.size()-1).colorG=colorG;
                side.get(side.size()-1).colorB=colorB;
            }
        }*/
    }
    public void emptyParcels() {
        vertices.clear();
        side.clear();
    }
    protected void paintComponent(Graphics g) {
        Graphics2D localGraphics2D = (Graphics2D)g;
        localGraphics2D.drawImage(img,0,0,null);
        localGraphics2D.setColor(Color.red);
        localGraphics2D.drawString("FPS: "+Integer.toString(fps),10,10);
        localGraphics2D.drawString("Sides: "+Integer.toString(side.size()),10,30);
        for(int i=0;i<values.length;i++)
        {
            localGraphics2D.drawString("Value "+Integer.toString(i)+": "+Double.toString(values[i]),10,50 + 20*i);
        }
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
    public void rotate(){
        double angle =1/180.0*Math.PI;

        double[][] m=new double[3][3];
        m[0][0]=Math.cos(angle);m[0][1]=0;m[0][2]=Math.sin(angle);
        m[1][0]=0;m[1][1]=1;m[1][2]=0;
        m[2][0]=-Math.sin(angle);m[2][1]=0;m[2][2]=Math.cos(angle);

        for(int i=0;i<vertices.size();i++)
        {
            vertices.set(i, new Point3D(m[0][0]*vertices.get(i).getX() + m[0][1]*vertices.get(i).getY() + m[0][2]*vertices.get(i).getZ()
                    ,m[1][0]*vertices.get(i).getX() + m[1][1]*vertices.get(i).getY() + m[1][2]*vertices.get(i).getZ()
                    ,m[2][0]*vertices.get(i).getX() + m[2][1]*vertices.get(i).getY() + m[2][2]*vertices.get(i).getZ()));
        }
        for(int i=0;i<side.size();i++)
        {
            side.get(i).calcPlane();
        }

        /*sun = new Point3D(m[0][0]*sun.getX() + m[0][1]*sun.getY() + m[0][2]*sun.getZ()
                ,m[1][0]*sun.getX() + m[1][1]*sun.getY() + m[1][2]*sun.getZ()
                ,m[2][0]*sun.getX() + m[2][1]*sun.getY() + m[2][2]*sun.getZ());*/
    }
    public void renderImage(){
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
    public BufferedImage getImage(){return img;}
}