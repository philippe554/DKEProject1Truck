/**
 * An extension on the the fill engine: genetic
 */
public class Genetic extends FillEngine {
    /**
     * Mutates the setting in a genetic way + copy of the original
     * @param s the start setting
     * @return the list of new settigns (here size = 21)
     */
    @Override
    public Setting[] mutate(Setting s) {
        double mutationRate=0.50;
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
