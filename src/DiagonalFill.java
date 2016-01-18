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
        sReturn[0].c[0]=1;
        sReturn[0].c[1]=1;
        for(int j=2;j<5;j++)
        {
            sReturn[0].c[j]=-1;
        }
        return sReturn;
    }
}
