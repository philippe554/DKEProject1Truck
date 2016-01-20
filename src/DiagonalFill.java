import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * An extension on the the fill engine: hill climbing
 */
public class DiagonalFill extends FillEngine{
    /**
     * Mutates the setting to constant values needed for the hillclimbing
     * @param s the start setting
     * @return the list of new settigns (here size = 1)
     */
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
