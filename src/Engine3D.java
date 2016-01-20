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

/**
 * The 3D engine
 */
class Engine3D {

    /**
     * stores the information for one side
     */
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
        /**
         * pre calculate some values to shorten the time of renderImage
         */
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
        /**
         * constuctor, sets the points
         * @param A point a
         * @param B point b
         * @param C point c
         */
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
                        img.setRGB(i,j,((255 << 16) | (255 << 8) | 255));
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

    private BufferedImage img;
    private double hViewAngle;
    private double vViewAngle;
    private double viewDesity;
    private double hPixels;
    private double vPixels;

    private int amountOfThreads=4;

    /**
     * setup the engine to begin rendering
     */
    public void setup(){
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
        side = new ArrayList<Side>();
    }
    /**
     * add a list of parcels
     * @param parcels the list of parcels
     */
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
                    vertices.add(new Point3D( newSides.get(j)[k].getX()-16.5,newSides.get(j)[k].getY()-2.5,newSides.get(j)[k].getZ()));
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
    /**
     * Empty the all sides
     */
    public void emptyParcels() {
        vertices.clear();
        side.clear();
    }
    /**
     * rotate the setup, doesn't render it
     * @param degree the degree
     */
    public void rotate(int degree){
        double angle =degree/180.0*Math.PI;

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
    /**
     * Render and return the image that is stored
     * @return the image
     */
    public BufferedImage renderImage(){
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
        return img;
    }
    /**
     * add a box to the arraylist
     * @param minX minx
     * @param maxX maxx
     * @param minY miny
     * @param maxY maxy
     * @param minZ minz
     * @param maxZ maxz
     * @param colorR red
     * @param colorG green
     * @param colorB blue
     */
    public void addBox(double minX,double maxX,double minY,double maxY,double minZ,double maxZ, int colorR, int colorG, int colorB) {
        vertices.add(new Point3D(  minX,  minY,  minZ ));
        vertices.add(new Point3D(  minX,  maxY,  minZ ));
        vertices.add(new Point3D(  minX,  maxY,  maxZ ));
        vertices.add(new Point3D(  minX,  minY,  maxZ ));
        side.add(new Side(vertices.size()-4,vertices.size()-3,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;
        side.add(new Side(vertices.size()-4,vertices.size()-1,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;

        vertices.add(new Point3D(  maxX,  minY,  minZ ));
        vertices.add(new Point3D(  maxX,  maxY,  minZ ));
        vertices.add(new Point3D(  maxX,  maxY,  maxZ ));
        vertices.add(new Point3D(  maxX,  minY,  maxZ ));
        side.add(new Side(vertices.size()-4,vertices.size()-3,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;
        side.add(new Side(vertices.size()-4,vertices.size()-1,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;

        vertices.add(new Point3D(  minX,  minY,  minZ ));
        vertices.add(new Point3D(  maxX,  minY,  minZ ));
        vertices.add(new Point3D(  maxX,  minY,  maxZ ));
        vertices.add(new Point3D(  minX,  minY,  maxZ ));
        side.add(new Side(vertices.size()-4,vertices.size()-3,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;
        side.add(new Side(vertices.size()-4,vertices.size()-1,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;

        vertices.add(new Point3D(  minX,  maxY,  minZ ));
        vertices.add(new Point3D(  maxX,  maxY,  minZ ));
        vertices.add(new Point3D(  maxX,  maxY,  maxZ ));
        vertices.add(new Point3D(  minX,  maxY,  maxZ ));
        side.add(new Side(vertices.size()-4,vertices.size()-3,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;
        side.add(new Side(vertices.size()-4,vertices.size()-1,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;

        vertices.add(new Point3D(  minX,  minY,  minZ ));
        vertices.add(new Point3D(  maxX,  minY,  minZ ));
        vertices.add(new Point3D(  maxX,  maxY,  minZ ));
        vertices.add(new Point3D(  minX,  maxY,  minZ ));
        side.add(new Side(vertices.size()-4,vertices.size()-3,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;
        side.add(new Side(vertices.size()-4,vertices.size()-1,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;

        vertices.add(new Point3D(  minX,  minY,  maxZ ));
        vertices.add(new Point3D(  maxX,  minY,  maxZ ));
        vertices.add(new Point3D(  maxX,  maxY,  maxZ ));
        vertices.add(new Point3D(  minX,  maxY,  maxZ ));
        side.add(new Side(vertices.size()-4,vertices.size()-3,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;
        side.add(new Side(vertices.size()-4,vertices.size()-1,vertices.size()-2));
        side.get(side.size()-1).colorR=colorR;
        side.get(side.size()-1).colorG=colorG;
        side.get(side.size()-1).colorB=colorB;

    }
    /**
     * clear everything and add a truck
     */
    public void loadTruck() {
        side.clear();
        vertices.clear();

        vertices.add(new Point3D(  -100,  -0.5,  -100 ));//0
        vertices.add(new Point3D(  -100,  -0.5,  100 ));//1
        vertices.add(new Point3D(  100,  -0.5,  100 ));//2
        vertices.add(new Point3D(  100,  -0.5,  -100 ));//3
        side.add(new Side(0,1,2));//0
        side.add(new Side(0,3,2));//1
        side.get(0).colorR=0;
        side.get(1).colorR=0;
        side.get(0).colorG=150;
        side.get(1).colorG=150;
        side.get(0).colorB=0;
        side.get(1).colorB=0;

        addBox(-100,100,-0.5,0,-4,4   ,50,50,50);

        addBox(-16.5,16.5,1,9,-2.5,2.5   ,150,0,0);
        addBox(16.5,24,1,3.5,-2.5,2.5   ,20,20,20);
        addBox(20,24,3.5,8,-2.5,2.5   ,150,150,150);

        addBox(-14,-11,0,3,-2.5,-3.5  ,0,0,0);
        addBox(-10,-7,0,3,-2.5,-3.5  ,0,0,0);
        addBox(-6,-3,0,3,-2.5,-3.5  ,0,0,0);
        addBox(-14,-11,0,3,2.5,3.5  ,0,0,0);
        addBox(-10,-7,0,3,2.5,3.5  ,0,0,0);
        addBox(-6,-3,0,3,2.5,3.5  ,0,0,0);

        addBox(13,16,0,3,-2.5,-3.5  ,0,0,0);
        addBox(13,16,0,3,2.5,3.5  ,0,0,0);
        addBox(20,23,0,3,-2.5,-3.5  ,0,0,0);
        addBox(20,23,0,3,2.5,3.5  ,0,0,0);

        addBox(24,24.1,5,7.5,-2,2  ,150,150,0);
        addBox(22,23.5,5,7.5,2.5,2.6  ,150,150,0);
        addBox(22,23.5,5,7.5,-2.6,2.6  ,150,150,0);

        addBox(27,27.5,-0.5,15,-5,-5.5  ,20,20,20);
        addBox(26.7,27.8,15,16,-2,-5.8  ,20,20,20);
        sun = new Point3D(27.25,13.5,-2.3);
    }
    /**
     * Subtract 2 Point3D of each other
     * @param p1 first point
     * @param p2 second point
     * @return first point - second point
     */
    private Point3D subtract3D(Point3D p1,Point3D p2) {
        return new Point3D(p1.getX()-p2.getX(),p1.getY()-p2.getY(),p1.getZ()-p2.getZ());
    }
    /**
     * Check if a point is in a triangle in 3D
     * @param p the point
     * @param a the index of the trianglecorner 1
     * @param b the index of the trianglecorner 1
     * @param c the index of the trianglecorner 1
     * @return True if its in, false if its outside
     */
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
}