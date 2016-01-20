import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * An extension on the the fill engine: genetic
 */
public class HillClimb extends FillEngine{
    /**
     * Changes the setting with hill climbing rules + copy of the original
     * @param s the start setting
     * @return the list of new settigns (here size = 13)
     */
    @Override
    public Setting[] mutate(Setting s) {
        double step=1;
        Setting[] sReturn = new Setting[s.c.length*2+3];
        for(int i=0;i<s.c.length;i++)
        {
            sReturn[i*2] = new Setting();
            sReturn[i*2+1] = new Setting();
            for(int j=0;j<s.c.length;j++)
            {
                if(i==j){
                    sReturn[i*2].c[j]=s.c[j]+step;
                    sReturn[i*2+1].c[j]=s.c[j]-step;
                }else{
                    sReturn[i*2].c[j]=s.c[j];
                    sReturn[i*2+1].c[j]=s.c[j];
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
