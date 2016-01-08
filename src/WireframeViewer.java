import com.sun.javafx.geom.Point2D;
import javafx.geometry.Point2DBuilder;
import javafx.geometry.Point3D;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.Math;
import java.util.Arrays;

class Edge {
    public int a, b;
    public Edge( int A, int B ) {
        a = A;  b = B;
    }
}

public class WireframeViewer extends Applet
        implements MouseListener, MouseMotionListener, MouseWheelListener {

    class Side implements Comparable<Side>{
        public int a,b,c;
        double distance;
        public double t;
        public Point3D dir;
        double planeA;
        double planeB;
        double planeC;
        double planeD;
        public void calcPlane(){
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
        }
        public Side(int A,int B,int C) {
            a=A;b=B;c=C;
            calcPlane();
        }

        //@Override
        public int compareTo(Side side) {
            return -Double.compare(distance,side.distance);
            /*Point3D g = new Point3D((vertices[a].getX()+vertices[b].getX()+vertices[c].getX())/3.0, //gravity point triangle
                    (vertices[a].getY()+vertices[b].getY()+vertices[c].getY())/3.0,
                    (vertices[a].getZ()+vertices[b].getZ()+vertices[c].getZ())/3.0);
            Point3D l = new Point3D(g.getX()-camera.getX(),g.getY()-camera.getY(),g.getZ()-camera.getZ());//line from camera to g
            Point3D d1 = new Point3D(vertices[side.a].getX()-vertices[side.c].getX(), //start calc plane second side
                    vertices[side.a].getY()-vertices[side.c].getY(),
                    vertices[side.a].getZ()-vertices[side.c].getZ());
            Point3D d2 = new Point3D(vertices[side.b].getX()-vertices[side.c].getX(),
                    vertices[side.b].getY()-vertices[side.c].getY(),
                    vertices[side.b].getZ()-vertices[side.c].getZ());
            Point3D cp = new Point3D(d1.getY()*d2.getZ()-d1.getZ()*d2.getY(),
                    d1.getZ()*d2.getX()-d1.getX()*d2.getZ()
                    ,d1.getX()*d2.getY()-d1.getY()*d2.getX());
            double A=cp.getX();
            double B=cp.getY();
            double C=cp.getZ();
            double D=A*vertices[side.c].getX() +
                    B*vertices[side.c].getY()+
                    C*vertices[side.c].getZ();

            double Nv = A*l.getX() + B*l.getY() + C*l.getZ();
            double Nr0 = A*camera.getX() + B*camera.getY() + C*camera.getZ();

            if (Nv != 0)       // line and plane are not parallel and must intersect
            {
                t = (D - Nr0)/Nv;
                return t;
                //Point3D i = new Point3D(g1.getX() + l.getX()*t,g1.getY() + l.getY()*t,g1.getZ() + l.getZ()*t);
            }
            else               // line is parallel to plane
            {
                t=0;
                if (Nr0 == D) {
                    return 0;//line is in plane
                }
                else{
                    return 0;//line is parallel to plane
                }
            }*/
        }
    }

    int width, height;
    int mx, my;  // the most recently recorded mouse coordinates

    Image backbuffer;
    Graphics backg;

    int azimuth = 0, elevation = 45;
    float nearToObj = 6f;  // distance from near plane to center of object

    Point3D[] vertices;
    Edge[] edges;
    Side[] side;

    Point3D sun;
    Point3D camera;

    public void init() {
        width = getSize().width;
        height = getSize().height;

        sun = new Point3D(Math.sqrt(0.5),Math.sqrt(0.5),0);
        camera = new Point3D(0,10,-10);

        vertices = new Point3D[ 16 ];
        vertices[0] = new Point3D( -2, -2, -2 );
        vertices[1] = new Point3D( -2, -2,  2 );
        vertices[2] = new Point3D( -2,  2, -2 );
        vertices[3] = new Point3D( -2,  2,  2 );
        vertices[4] = new Point3D(  2, -2, -2 );
        vertices[5] = new Point3D(  2, -2,  2 );
        vertices[6] = new Point3D(  2,  2, -2 );
        vertices[7] = new Point3D(  2,  2,  2 );

        vertices[8] = new Point3D( -2+5, -2, -2 );
        vertices[9] = new Point3D( -2+5, -2,  2 );
        vertices[10] = new Point3D( -2+5,  2, -2 );
        vertices[11] = new Point3D( -2+5,  2,  2 );
        vertices[12] = new Point3D(  2+5, -2, -2 );
        vertices[13] = new Point3D(  2+5, -2,  2 );
        vertices[14] = new Point3D(  2+5,  2, -2 );
        vertices[15] = new Point3D(  2+5,  2,  2 );

        edges = new Edge[ 24 ];
        edges[ 0] = new Edge( 0, 1 );
        edges[ 1] = new Edge( 0, 2 );
        edges[ 2] = new Edge( 0, 4 );
        edges[ 3] = new Edge( 1, 3 );
        edges[ 4] = new Edge( 1, 5 );
        edges[ 5] = new Edge( 2, 3 );
        edges[ 6] = new Edge( 2, 6 );
        edges[ 7] = new Edge( 3, 7 );
        edges[ 8] = new Edge( 4, 5 );
        edges[ 9] = new Edge( 4, 6 );
        edges[10] = new Edge( 5, 7 );
        edges[11] = new Edge( 6, 7 );

        edges[ 0+12] = new Edge( 0+8, 1+8 );
        edges[ 1+12] = new Edge( 0+8, 2+8 );
        edges[ 2+12] = new Edge( 0+8, 4+8 );
        edges[ 3+12] = new Edge( 1+8, 3+8 );
        edges[ 4+12] = new Edge( 1+8, 5+8 );
        edges[ 5+12] = new Edge( 2+8, 3+8 );
        edges[ 6+12] = new Edge( 2+8, 6+8 );
        edges[ 7+12] = new Edge( 3+8, 7+8 );
        edges[ 8+12] = new Edge( 4+8, 5+8 );
        edges[ 9+12] = new Edge( 4+8, 6+8 );
        edges[10+12] = new Edge( 5+8, 7+8 );
        edges[11+12] = new Edge( 6+8, 7+8 );

        side = new Side[24];
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
        side[0].dir=new Point3D(0,0,-1);
        side[1].dir=new Point3D(0,0,-1);
        side[2].dir=new Point3D(-1,0,0);
        side[3].dir=new Point3D(-1,0,0);
        side[4].dir=new Point3D(0,1,0);
        side[5].dir=new Point3D(0,1,0);
        side[6].dir=new Point3D(0,-1,0);
        side[7].dir=new Point3D(0,-1,0);
        side[8].dir=new Point3D(1,0,0);
        side[9].dir=new Point3D(1,0,0);
        side[10].dir=new Point3D(0,0,1);
        side[11].dir=new Point3D(0,0,1);

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
        side[0+12].dir=new Point3D(0,0,-1);
        side[1+12].dir=new Point3D(0,0,-1);
        side[2+12].dir=new Point3D(-1,0,0);
        side[3+12].dir=new Point3D(-1,0,0);
        side[4+12].dir=new Point3D(0,1,0);
        side[5+12].dir=new Point3D(0,1,0);
        side[6+12].dir=new Point3D(0,-1,0);
        side[7+12].dir=new Point3D(0,-1,0);
        side[8+12].dir=new Point3D(1,0,0);
        side[9+12].dir=new Point3D(1,0,0);
        side[10+12].dir=new Point3D(0,0,1);
        side[11+12].dir=new Point3D(0,0,1);

        /*vertices = new Point3D[ 4 ];
        vertices[0] = new Point3D( -2, -2, 0 );
        vertices[1] = new Point3D( 2, -2,  0 );
        vertices[2] = new Point3D( 2,  2, 0 );
        vertices[3] = new Point3D( -2,  2,  0 );

        side = new Side[2];
        side[0] = new Side(0,1,2);//front
        side[1] = new Side(0,3,2);
        side[0].dir=new Point3D(0,0,1);
        side[1].dir=new Point3D(0,0,1);*/


        backbuffer = createImage( width, height );
        backg = backbuffer.getGraphics();
        //drawWireframe( backg );
        drawWindow(backg);

        addMouseListener( this );
        addMouseMotionListener( this );
        addMouseWheelListener(this);

        while(true)
        {
            double angle =5/180.0*Math.PI;

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
                side[i].dir = new Point3D(m[0][0]*side[i].dir.getX() + m[0][1]*side[i].dir.getY() + m[0][2]*side[i].dir.getZ()
                        ,m[1][0]*side[i].dir.getX() + m[1][1]*side[i].dir.getY() + m[1][2]*side[i].dir.getZ()
                        ,m[2][0]*side[i].dir.getX() + m[2][1]*side[i].dir.getY() + m[2][2]*side[i].dir.getZ());
                side[i].calcPlane();
            }

            sun = new Point3D(m[0][0]*sun.getX() + m[0][1]*sun.getY() + m[0][2]*sun.getZ()
                    ,m[1][0]*sun.getX() + m[1][1]*sun.getY() + m[1][2]*sun.getZ()
                    ,m[2][0]*sun.getX() + m[2][1]*sun.getY() + m[2][2]*sun.getZ());

            // update the backbuffer
            //drawWireframe( backg );
            drawWindow(backg);
            repaint();
        }
    }

    void drawWireframe( Graphics g ) {

        // draw the wireframe
        g.setColor( Color.black );
        g.fillRect( 0, 0, width, height );

        // compute coefficients for the projection
        double theta = Math.PI * azimuth / 180.0;
        double phi = Math.PI * elevation / 180.0;
        float cosT = (float)Math.cos( theta ), sinT = (float)Math.sin( theta );
        float cosP = (float)Math.cos( phi ), sinP = (float)Math.sin( phi );
        float cosTcosP = cosT*cosP, cosTsinP = cosT*sinP,
                sinTcosP = sinT*cosP, sinTsinP = sinT*sinP;

        // project vertices onto the 2D viewport
        Point[] points;

        points = new Point[ vertices.length ];

        int scaleFactor = width/4;

        float near = 3;  // distance from eye to near plane

        for ( int j = 0; j < vertices.length; j++ ) {
            double x0 = vertices[j].getX();
            double y0 = vertices[j].getY();
            double z0 = vertices[j].getZ();

            // compute an orthographic projection
            double x1 = cosT*x0 + sinT*z0;
            double y1 = -sinTsinP*x0 + cosP*y0 + cosTsinP*z0;

            // now adjust things to get a perspective projection
            double z1 = cosTcosP*z0 - sinTcosP*x0 - sinP*y0;
            x1 = x1*near/(z1+near+nearToObj);
            y1 = y1*near/(z1+near+nearToObj);

            // the 0.5 is to round off when converting to int
            points[j] = new Point(
                    (int)(width/2 + scaleFactor*x1 + 0.5),
                    (int)(height/2 - scaleFactor*y1 + 0.5)
            );
        }

        for(int j=0;j<side.length;j++)
        {
            double d1 = Math.sqrt(Math.pow(vertices[side[j].a].getX()-camera.getX() ,2)+ Math.pow(vertices[side[j].a].getY()-camera.getY() ,2)+ Math.pow(vertices[side[j].a].getZ()-camera.getZ() ,2));
            double d2 = Math.sqrt(Math.pow(vertices[side[j].b].getX()-camera.getX() ,2)+ Math.pow(vertices[side[j].b].getY()-camera.getY() ,2)+ Math.pow(vertices[side[j].b].getZ()-camera.getZ() ,2));
            double d3 = Math.sqrt(Math.pow(vertices[side[j].c].getX()-camera.getX() ,2)+ Math.pow(vertices[side[j].c].getY()-camera.getY() ,2)+ Math.pow(vertices[side[j].c].getZ()-camera.getZ() ,2));

            side[j].distance=Math.sqrt(
                    Math.pow((vertices[side[j].a].getX()+vertices[side[j].b].getX()+vertices[side[j].c].getX())/3-camera.getX() ,2)+
                            Math.pow((vertices[side[j].a].getY()+vertices[side[j].b].getY()+vertices[side[j].c].getY())/3-camera.getY() ,2)+
                            Math.pow((vertices[side[j].a].getZ()+vertices[side[j].b].getZ()+vertices[side[j].c].getZ())/3-camera.getZ() ,2)
            );

            //side[j].distance=Math.min(d1,d2);
            //side[j].distance=Math.min(side[j].distance,d3);
        }

        Arrays.sort(side);
        /*int errorFound=10;
        while(errorFound>8)
        {
            errorFound=0;
            for(int i=0;i<(side.length-1);i++)
            {
                double t = side[i].compareTo(side[i+1]);
                if(t>1.0)
                {
                    errorFound++;
                    Side tSide=side[i];
                    side[i]=side[i+1];
                    side[i+1]=tSide;
                }
            }
            System.out.println(errorFound);
        }*/

        for(int j=0;j<side.length;j++)
        {
            /*Point3D d1 = new Point3D(vertices[side[j].a].getX()-vertices[side[j].c].getX(),
                    vertices[side[j].a].getY()-vertices[side[j].c].getY(),
                    vertices[side[j].a].getZ()-vertices[side[j].c].getZ());
            Point3D d2 = new Point3D(vertices[side[j].b].getX()-vertices[side[j].c].getX(),
                    vertices[side[j].b].getY()-vertices[side[j].c].getY(),
                    vertices[side[j].b].getZ()-vertices[side[j].c].getZ());
            Point3D cp = new Point3D(d1.getY()*d2.getZ()-d1.getZ()*d2.getY(),
                    d1.getZ()*d2.getX()-d1.getX()*d2.getZ()
                    ,d1.getX()*d2.getY()-d1.getY()*d2.getX());
            double A=cp.getX();
            double B=cp.getY();
            double C=cp.getZ();
            double D=-(A*vertices[side[j].c].getX() +
                    B*vertices[side[j].c].getY()+
                    C*vertices[side[j].c].getZ());

            double angle = Math.asin(Math.abs(A*sun.getX()+B*sun.getY()+B*sun.getZ())/
                    (Math.sqrt(A*A+B*B+C*C)* Math.sqrt(sun.getX()*sun.getX()+sun.getY()*sun.getY()+sun.getZ()*sun.getZ())))*
                    180/Math.PI;

            angle*=4;*/

            int angle = 255-(int)(Math.abs(Math.acos( sun.getX()*side[j].dir.getX()+sun.getY()*side[j].dir.getY()+sun.getZ()*side[j].dir.getZ()))/Math.PI*255);

            if(angle<0){angle=0;}
            else if(angle>255){angle=255;}

            int pointX[]=new int[3];
            pointX[0]=points[side[j].a].x;
            pointX[1]=points[side[j].b].x;
            pointX[2]=points[side[j].c].x;
            int pointY[]=new int[3];
            pointY[0]=points[side[j].a].y;
            pointY[1]=points[side[j].b].y;
            pointY[2]=points[side[j].c].y;
            g.setColor(new Color(100,100,angle));
            g.fillPolygon(pointX, pointY , 3);
        }

        /*g.setColor( Color.white );
        for (int j = 0; j < edges.length; ++j ) {
            g.drawLine(
                    points[ edges[j].a ].x, points[ edges[j].a ].y,
                    points[ edges[j].b ].x, points[ edges[j].b ].y);
        }*/

    }

    void drawWindow(Graphics g)
    {
        camera = new Point3D(0,20,-20);
        //Point3D windowStart=new Point3D(-3,3,-3);
        Point3D dir = new Point3D(0,-1,1);
        g.setColor( Color.BLACK );
        g.fillRect( 0, 0, width, height );

        int hViewAngle=60;
        int vViewAngle=hViewAngle;
        int viewDesity=5;
        BufferedImage img = new BufferedImage(hViewAngle*viewDesity, vViewAngle*viewDesity, BufferedImage.TYPE_INT_RGB);
        double debtData[][] = new double[hViewAngle*viewDesity][vViewAngle*viewDesity];
        Color colorData[][] = new Color[hViewAngle*viewDesity][vViewAngle*viewDesity];

        //get vecors in all directions
        for(int i=0;i<debtData.length;i++)
        {
            for(int j=0;j<debtData[0].length;j++)
            {
                //Point3D angle = new Point3D(windowStart.getX()+(double)i/30.0,windowStart.getY()-(double)j/30.0,windowStart.getZ()-camera.getZ());

                double[][] m=new double[3][3];
                double hAngle = (-(double)hViewAngle/2.0 + (double)i/(double)debtData.length*(double)hViewAngle)/180.0*Math.PI;
                m[0][0]=Math.cos(hAngle);m[0][1]=0;m[0][2]=Math.sin(hAngle);
                m[1][0]=0;m[1][1]=1;m[1][2]=0;
                m[2][0]=-Math.sin(hAngle);m[2][1]=0;m[2][2]=Math.cos(hAngle);
                Point3D angle = new Point3D(m[0][0]*dir.getX() + m[0][1]*dir.getY() + m[0][2]*dir.getZ()
                        ,m[1][0]*dir.getX() + m[1][1]*dir.getY() + m[1][2]*dir.getZ()
                        ,m[2][0]*dir.getX() + m[2][1]*dir.getY() + m[2][2]*dir.getZ());

                double vAngle = (-(double)vViewAngle/2.0 + (double)j/(double)debtData[0].length*(double)vViewAngle)/180.0*Math.PI;
                m[0][0]=1;m[0][1]=0;m[0][2]=0;
                m[1][0]=0;m[1][1]=Math.cos(vAngle);m[1][2]=-Math.sin(vAngle);
                m[2][0]=0;m[2][1]=Math.sin(vAngle);m[2][2]=Math.cos(vAngle);
                angle = new Point3D(m[0][0]*angle.getX() + m[0][1]*angle.getY() + m[0][2]*angle.getZ()
                        ,m[1][0]*angle.getX() + m[1][1]*angle.getY() + m[1][2]*angle.getZ()
                        ,m[2][0]*angle.getX() + m[2][1]*angle.getY() + m[2][2]*angle.getZ());

                double minDebt=-1;
                Color color=null;
                for(int k=0;k<side.length;k++)
                {
                    double Nv = side[k].planeA*angle.getX() + side[k].planeB*angle.getY() + side[k].planeC*angle.getZ();
                    double Nr0 = side[k].planeA*camera.getX() + side[k].planeB*camera.getY() + side[k].planeC*camera.getZ();

                    if (Nv != 0)       // line and plane are not parallel and must intersect
                    {
                        double t = (side[k].planeD - Nr0)/Nv;
                        if(t>=1) {
                            Point3D col = new Point3D(camera.getX() + t * angle.getX(), camera.getY() + t * angle.getY(), camera.getZ() + t * angle.getZ());
                            if (PointInTriangle(col, vertices[side[k].a], vertices[side[k].b], vertices[side[k].c])) {
                                double distance = distancePointPoint3D(col, camera);
                                if (minDebt == -1 || minDebt > distance) {
                                    minDebt = distance;
                                    int shadow = 255 - (int) (Math.abs(Math.acos(sun.getX() * side[k].dir.getX() + sun.getY() * side[k].dir.getY() + sun.getZ() * side[k].dir.getZ())) / Math.PI * 255);

                                    if (shadow < 0) {
                                        shadow = 0;
                                    } else if (shadow > 255) {
                                        shadow = 255;
                                    }

                                    color = new Color(100, 100, shadow);
                                }
                            }
                        }
                    }
                }
                if(minDebt!=-1)
                {
                    debtData[i][j]=minDebt;
                    colorData[i][j]=color;
                    //g.setColor( colorData[i][j] );
                    //g.drawLine(i, j, i, j);
                    img.setRGB(i,j,((100 << 16) | (100 << 8) | colorData[i][j].getBlue()));
                }
            }
        }
        g.drawImage(img,0,0,null);
    }
    public Point3D subtract3D(Point3D p1,Point3D p2)
    {
        return new Point3D(p1.getX()-p2.getX(),p1.getY()-p2.getY(),p1.getZ()-p2.getZ());
    }
    public double distancePointPoint3D(Point3D p1,Point3D p2)
    {
        return Math.sqrt(Math.pow(p1.getX()-p2.getX(),2)+Math.pow(p1.getY()-p2.getY(),2)+Math.pow(p1.getZ()-p2.getZ(),2));
    }
    public boolean PointInTriangle(Point3D p,Point3D a,Point3D b,Point3D c) {
        Point3D v0=subtract3D(c,a);
        Point3D v1=subtract3D(b,a);
        Point3D v2=subtract3D(p,a);
        double u = (dotProduct3D(v1,v1)*dotProduct3D(v2,v0)-dotProduct3D(v1,v0)*dotProduct3D(v2,v1))/(dotProduct3D(v0,v0)*dotProduct3D(v1,v1)-dotProduct3D(v0,v1)*dotProduct3D(v1,v0));
        double v = (dotProduct3D(v0,v0)*dotProduct3D(v2,v1)-dotProduct3D(v0,v1)*dotProduct3D(v2,v0))/(dotProduct3D(v0,v0)*dotProduct3D(v1,v1)-dotProduct3D(v0,v1)*dotProduct3D(v1,v0));
        if(u>=0 && v>=0 && u<=1 && v<=1 && (u+v)<=1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean sameSide3D(Point3D p1,Point3D p2,Point3D a,Point3D b){
        Point3D cp1 = crossProduct(new Point3D(b.getX()-a.getX(),b.getY()-a.getY(),b.getZ()-a.getZ()),new Point3D(p1.getX()-a.getX(),p1.getY()-a.getY(),p1.getZ()-a.getZ()));
        Point3D cp2 = crossProduct(new Point3D(b.getX()-a.getX(),b.getY()-a.getY(),b.getZ()-a.getZ()),new Point3D(p2.getX()-a.getX(),p2.getY()-a.getY(),p2.getZ()-a.getZ()));
        if(dotProduct3D(cp1,cp2)>=0){
            return true;
        }
        else
        {
            return false;
        }
    }
    public double dotProduct3D(Point3D p1, Point3D p2)
    {
        return p1.getX()*p2.getX()+p1.getY()*p2.getY()+p1.getZ()*p2.getZ();
    }
    public Point3D crossProduct(Point3D p1, Point3D p2)
    {
        return new Point3D(p1.getY()*p2.getZ()-p1.getZ()*p2.getY(),p1.getZ()*p2.getX()-p1.getX()*p2.getZ(),p1.getX()*p2.getY()-p1.getY()*p2.getZ());
    }
    public boolean triangleTriangleIntersectino2D(int sideID1, int sideID2)
    {
        return true;
    }
    public boolean lineLineIntersection2D(Point2D p1,Point2D p2,Point2D p3,Point2D p4)
    {
        double x1=p1.x,x2=p2.x,x3=p3.x,x4=p4.x;
        double y1=p1.y,y2=p2.y,y3=p3.y,y4=p4.y;

        double ua = (((x4-x3)*(y1-y3))-((y4-y3)*(x1-x3)))/(((y4-y3)*(x2-x1))-((x4-x3)*(y2-y1)));
        double ub = (((x2-x1)*(y1-y3))-((y2-y1)*(x1-x3)))/(((y4-y3)*(x2-x1))-((x4-x3)*(y2-y1)));

        if(ua>=0 && ua<=1 && ub>=0 && ub<=1)
        {
            return true;
        }
        return false;
    }


    public void mouseWheelMoved(MouseWheelEvent e){
        nearToObj = nearToObj + (float) e.getPreciseWheelRotation();
        // update the backbuffer
        //drawWireframe( backg );
        drawWindow(backg);
        repaint();
        e.consume();
    }

    public void mouseEntered( MouseEvent e ) { }
    public void mouseExited( MouseEvent e ) { }
    public void mouseClicked( MouseEvent e ) { }
    public void mousePressed( MouseEvent e ) {
        mx = e.getX();
        my = e.getY();
        e.consume();
    }
    public void mouseReleased( MouseEvent e ) { }
    public void mouseMoved( MouseEvent e ) { }
    public void mouseDragged( MouseEvent e ) {
        // get the latest mouse position
        int new_mx = e.getX();
        int new_my = e.getY();

        double angle =(double)(mx-new_mx)/180.0*Math.PI;

        mx=new_mx;
        my=new_my;

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
            side[i].dir = new Point3D(m[0][0]*side[i].dir.getX() + m[0][1]*side[i].dir.getY() + m[0][2]*side[i].dir.getZ()
                    ,m[1][0]*side[i].dir.getX() + m[1][1]*side[i].dir.getY() + m[1][2]*side[i].dir.getZ()
                    ,m[2][0]*side[i].dir.getX() + m[2][1]*side[i].dir.getY() + m[2][2]*side[i].dir.getZ());
            side[i].calcPlane();
        }

        sun = new Point3D(m[0][0]*sun.getX() + m[0][1]*sun.getY() + m[0][2]*sun.getZ()
                ,m[1][0]*sun.getX() + m[1][1]*sun.getY() + m[1][2]*sun.getZ()
                ,m[2][0]*sun.getX() + m[2][1]*sun.getY() + m[2][2]*sun.getZ());

        // update the backbuffer
        //drawWireframe( backg );
        drawWindow(backg);

        // update our data
        mx = new_mx;
        my = new_my;

        repaint();
        e.consume();
    }

    public void update( Graphics g ) {
        g.drawImage( backbuffer, 0, 0, this );
        showStatus("Elev: "+elevation+" deg, Azim: "+azimuth+" deg");
    }

    public void paint( Graphics g ) {
        update( g );
    }
}