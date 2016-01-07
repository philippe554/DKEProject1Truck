import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

class Point3D {
    public int x, y, z;
    public Point3D( int X, int Y, int Z ) {
        x = X;  y = Y;  z = Z;
    }
}

class Edge {
    public int a, b;
    public Edge( int A, int B ) {
        a = A;  b = B;
    }
}

class Side{
    public int a,b,c;
    public Side(int A,int B,int C) {
        a=A;b=B;c=C;
    }
}

public class WireframeViewer extends Applet
        implements MouseListener, MouseMotionListener, MouseWheelListener {

    int width, height;
    int mx, my;  // the most recently recorded mouse coordinates

    Image backbuffer;
    Graphics backg;

    int azimuth = 35, elevation = 30;
    float nearToObj = 6f;  // distance from near plane to center of object

    Point3D[] vertices;
    Edge[] edges;
    Side[] side;

    Point3D sun;

    public void init() {
        width = getSize().width;
        height = getSize().height;

        sun = new Point3D(3,3,3);

        vertices = new Point3D[ 8 ];
        vertices[0] = new Point3D( -2, -2, -2 );
        vertices[1] = new Point3D( -2, -2,  2 );
        vertices[2] = new Point3D( -2,  2, -2 );
        vertices[3] = new Point3D( -2,  2,  2 );
        vertices[4] = new Point3D(  2, -2, -2 );
        vertices[5] = new Point3D(  2, -2,  2 );
        vertices[6] = new Point3D(  2,  2, -2 );
        vertices[7] = new Point3D(  2,  2,  2 );

        edges = new Edge[ 12 ];
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

        side = new Side[12];
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


        backbuffer = createImage( width, height );
        backg = backbuffer.getGraphics();
        drawWireframe( backg );

        addMouseListener( this );
        addMouseMotionListener( this );
        addMouseWheelListener(this);
    }

    void drawWireframe( Graphics g ) {

        // compute coefficients for the projection
        double theta = Math.PI * 1 / 180.0;
        double phi = Math.PI * 1 / 180.0;
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
            int x0 = vertices[j].x;
            int y0 = vertices[j].y;
            int z0 = vertices[j].z;

            // compute an orthographic projection
            float x1 = cosT*x0 + sinT*z0;
            float y1 = -sinTsinP*x0 + cosP*y0 + cosTsinP*z0;

            // now adjust things to get a perspective projection
            float z1 = cosTcosP*z0 - sinTcosP*x0 - sinP*y0;
            x1 = x1*near/(z1+near+nearToObj);
            y1 = y1*near/(z1+near+nearToObj);

            // the 0.5 is to round off when converting to int
            points[j] = new Point(
                    (int)(width/2 + scaleFactor*x1 + 0.5),
                    (int)(height/2 - scaleFactor*y1 + 0.5)
            );
        }

        // draw the wireframe
        g.setColor( Color.black );
        g.fillRect( 0, 0, width, height );

        for(int j=0;j<side.length;j++)
        {
            Point3D vector = new Point3D((vertices[side[j].a].x+vertices[side[j].b].x+vertices[side[j].c].x)/3,
            (vertices[side[j].a].y+vertices[side[j].b].y+vertices[side[j].c].y)/3,
            (vertices[side[j].a].z+vertices[side[j].b].z+vertices[side[j].c].z)/3);

            double dotProduct = vector.x*sun.x+vector.y*sun.y+vector.z*sun.z;

            double lenthSun=Math.sqrt(sun.x*sun.x + sun.y*sun.y +sun.z*sun.z);

            double lenthVector=Math.sqrt(vector.x*vector.x + vector.y*vector.y +vector.z*vector.z);

            int angle = (int)(Math.acos(dotProduct/ (lenthSun*lenthVector))*180/Math.PI);

            System.out.println(angle);

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

        g.setColor( Color.white );
        for (int j = 0; j < edges.length; ++j ) {
            g.drawLine(
                    points[ edges[j].a ].x, points[ edges[j].a ].y,
                    points[ edges[j].b ].x, points[ edges[j].b ].y);
        }

    }

    public void mouseWheelMoved(MouseWheelEvent e){
        nearToObj = nearToObj + (float) e.getPreciseWheelRotation();
        // update the backbuffer
        drawWireframe( backg );
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

        // adjust angles according to the distance travelled by the mouse
        // since the last event
        azimuth -= new_mx - mx;
        elevation += new_my - my;

        double[][] m=new double[3][3];
        m[0][0]=Math.cos(azimuth);


        // update the backbuffer
        drawWireframe( backg );

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