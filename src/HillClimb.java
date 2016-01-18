import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by pmmde on 1/13/2016.
 */
public class HillClimb extends FillEngine{
    @Override
    public Setting[] mutate(Setting s) {
        double bigStep=3;
        double smallStep=0.5;
        Setting[] sReturn = new Setting[s.c.length*4+3];
        for(int i=0;i<s.c.length;i++)
        {
            sReturn[i*4] = new Setting();
            sReturn[i*4+1] = new Setting();
            sReturn[i*4+2] = new Setting();
            sReturn[i*4+3] = new Setting();
            for(int j=0;j<s.c.length;j++)
            {
                if(i==j){
                    sReturn[i*4].c[j]=s.c[j]+bigStep;
                    sReturn[i*4+1].c[j]=s.c[j]-smallStep;
                    sReturn[i*4+2].c[j]=s.c[j]+smallStep;
                    sReturn[i*4+3].c[j]=s.c[j]-smallStep;
                }else{
                    sReturn[i*4].c[j]=s.c[j];
                    sReturn[i*4+1].c[j]=s.c[j];
                    sReturn[i*4+2].c[j]=s.c[j];
                    sReturn[i*4+3].c[j]=s.c[j];
                }
            }
        }
        sReturn[sReturn.length-3]=createRandomSetting(10);
        sReturn[sReturn.length-2]=createRandomSetting(10);
        sReturn[sReturn.length-1]= new Setting();
        for(int j=0;j<s.c.length;j++) {
            sReturn[sReturn.length - 1].c[j]=s.c[j];
        }
        return sReturn;
    }
}
