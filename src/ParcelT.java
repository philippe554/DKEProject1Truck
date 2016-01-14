import javafx.geometry.Point3D;

import java.util.ArrayList;

public class ParcelT extends Parcel
{

    public ParcelT()
    {
        super();
        construct();
        setValue(5);
        setRotations(12);
    }

    public ParcelT(double value)
    {
        super();
        construct();
        setValue(value);
        setRotations(12);
    }

    public ParcelT(double value, Point3D location)
    {
        super();
        construct();
        setValue(value);
        this.setLocation(location);
        setRotations(12);
    }

    protected void construct()
    {
        this.add(new Point3D(0,0,0));
        this.add(new Point3D(0,1,0));
        this.add(new Point3D(0,2,0));
        this.add(new Point3D(1,1,0));
        this.add(new Point3D(2,1,0));
    }
    
    @Override
     private void setVertices(ArrayList<Point3D> blocks)
     {
    	int tmp_x, tmp_y, tmp_z;
    	// end face of "top" of T
    	tmp_x = (int)blocks.get(0).getX();
    	tmp_y = (int)blocks.get(0).getY();
    	tmp_z = (int)blocks.get(0).getZ();
    	
    	vertices.add(new Point3D(tmp_x,tmp_y,tmp_z));
    	vertices.add(new Point3D(tmp_x,tmp_y,tmp_z+1));
    	vertices.add(new Point3D(tmp_x,tmp_y+1,tmp_z+1));
        vertices.add(new Point3D(tmp_x,tmp_y+1,tmp_z));
        
        
    	// opposite end face of "top" of T
    	
    	// +1 here is intentional
    	tmp_x = (int)blocks.get(2).getX()+1;
    	tmp_y = (int)blocks.get(2).getY();
    	tmp_z = (int)blocks.get(2).getZ();
    	
    	vertices.add(new Point3D(tmp_x,tmp_y,tmp_z));
    	vertices.add(new Point3D(tmp_x,tmp_y,tmp_z+1));
    	vertices.add(new Point3D(tmp_x,tmp_y+1,tmp_z+1));
        vertices.add(new Point3D(tmp_x,tmp_y+1,tmp_z));
  
    	
    	// joining face
    	tmp_x = (int)blocks.get(3).getX();
    	tmp_y = (int)blocks.get(3).getY();
    	tmp_z = (int)blocks.get(3).getZ();
    	
    	vertices.add(new Point3D(tmp_x+1,tmp_y,tmp_z));
    	vertices.add(new Point3D(tmp_x,tmp_y,tmp_z));
    	vertices.add(new Point3D(tmp_x,tmp_y+1,tmp_z));
        vertices.add(new Point3D(tmp_x+1,tmp_y+1,tmp_z));
    	
    
    	
    	// "bottom" of T
    	
    	tmp_x = (int)blocks.get(4).getX();
    	tmp_y = (int)blocks.get(4).getY();
    	tmp_z = (int)blocks.get(4).getZ()+1;
    	
    	vertices.add(new Point3D(tmp_x+1,tmp_y,tmp_z));
    	vertices.add(new Point3D(tmp_x,tmp_y,tmp_z));
    	vertices.add(new Point3D(tmp_x,tmp_y+1,tmp_z));
        vertices.add(new Point3D(tmp_x+1,tmp_y+1,tmp_z));
    			 
     
     }
    
    
    
    @Override
    public ArrayList<Point3D> getVertices()
    {
       
    	ArrayList<Point3D> result = new ArrayList<Point3D>();
    	
    	setVertices(blockLocations);
    	
         
         for(Point3D point : vertices)
        {
            result.add(point.add(location));
        }
        
        
        return result;
    }
    
    
    @Override
     private void setSides(int nothing)
    {
    	// empty sides list
    	sides = = new ArrayList<Point3D[]>();
    	
    	Point3D[] s1 = {vertices.get(0), vertices.get(1), vertices.get(2)};
    	Point3D[] s2 = {vertices.get(0), vertices.get(2), vertices.get(3)};
    	Point3D[] s3 = {vertices.get(0), vertices.get(1), vertices.get(4)};
    	Point3D[] s4 = {vertices.get(1), vertices.get(4), vertices.get(5)};
    	Point3D[] s5 = {vertices.get(0), vertices.get(3), vertices.get(4)};
    	Point3D[] s6 = {vertices.get(3), vertices.get(4), vertices.get(7)};
    	Point3D[] s7 = {vertices.get(2), vertices.get(3), vertices.get(6)};
    	Point3D[] s8 = {vertices.get(3), vertices.get(6), vertices.get(7)};
    	Point3D[] s9 = {vertices.get(4), vertices.get(6), vertices.get(7)};
    	Point3D[] s10 = {vertices.get(4), vertices.get(5), vertices.get(6)};
    	Point3D[] s11 = {vertices.get(1), vertices.get(2), vertices.get(6)};
    	Point3D[] s12 = {vertices.get(1), vertices.get(5), vertices.get(6)};
    	
    	Point3D[] s13 = {vertices.get(12), vertices.get(13), vertices.get(15)};
    	Point3D[] s14 = {vertices.get(13), vertices.get(14), vertices.get(15)};
    	Point3D[] s15 = {vertices.get(9), vertices.get(12), vertices.get(13)};
    	Point3D[] s16 = {vertices.get(8), vertices.get(9), vertices.get(12)};
    	Point3D[] s17 = {vertices.get(9), vertices.get(10), vertices.get(13)};
    	Point3D[] s18 = {vertices.get(10), vertices.get(13), vertices.get(14)};
    	Point3D[] s19 = {vertices.get(10), vertices.get(14), vertices.get(15)};
    	Point3D[] s20 = {vertices.get(10), vertices.get(11), vertices.get(15)};
    	Point3D[] s21 = {vertices.get(8), vertices.get(12), vertices.get(15)};
    	Point3D[] s22 = {vertices.get(11), vertices.get(12), vertices.get(15)};
    	
    	sides.add(s1);
    	sides.add(s2);
    	sides.add(s3);
    	sides.add(s4);
        sides.add(s5);
        sides.add(s6);
        sides.add(s7);
        sides.add(s8);
        sides.add(s9);
        sides.add(s10);
        sides.add(s11);
        sides.add(s12);
        sides.add(s13);
    	sides.add(s14);
        sides.add(s15);
        sides.add(s16);
        sides.add(s17);
        sides.add(s18);
        sides.add(s19);
        sides.add(s20);
        sides.add(s21);
        sides.add(s22);
    
    
    
    }
    
    
}
