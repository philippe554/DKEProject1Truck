import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by pmmde on 1/14/2016.
 */
public class DiagonalFill extends FillEngine{
    @Override
    public Setting[] mutate(Setting s) {
        Setting[] sReturn = new Setting[1];
        sReturn[0]=new Setting();
        for(int j=0;j<3;j++)
        {
            sReturn[0].c[j]=0;
        }
        sReturn[0].c[0]=1;
        for(int j=3;j<6;j++)
        {
            sReturn[0].c[j]=-1;
        }
        return sReturn;
    }
}
