import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by pmmde on 1/14/2016.
 */
public class Genetic extends FillEngine {
    @Override
    public Setting[] mutate(Setting s) {
        double mutationRate=0.30;
        Setting[] sReturn = new Setting[21];
        for(int i=0;i<20;i++)
        {
            sReturn[i]=new Setting();
            for(int j=0;j<s.c.length;j++)
            {
                if(Math.random()<mutationRate)
                {
                    sReturn[i].c[j]=Math.random()*10*2-10;
                }else
                {
                    sReturn[i].c[j]=s.c[j];
                }
            }
        }
        sReturn[20]=new Setting();
        for(int j=0;j<s.c.length;j++)
        {
            sReturn[20].c[j]=s.c[j];
        }
        return sReturn;
    }
}
