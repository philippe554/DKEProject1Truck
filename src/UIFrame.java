import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

/**
 * UI
 */
public class UIFrame extends JFrame implements ActionListener {

    //Declare objects
    public JComboBox selection;
    JTextField aField;
    JTextField bField;
    JTextField cField;
    JTextField lField;
    JTextField pField;
    JTextField tField;
    JTextField valueField;
    JTextField volumetricValueField;
    JTextField xField;
    JTextField yField;
    JTextField zField;

    static ArrayList<Parcel> list;
    Engine3D Engine;
    private BufferedImage img = null;
    static ArrayList<Parcel> loadedParcels = new ArrayList<Parcel>();

    static double percentEmpty;
    static double score;
    static long startTime;
    static long endTime;

    /**
     * What happens when Run button is pressed. Gathers inputs from every text field, constructs parcel list and starts selected algorithm.
     * @param e the event
     */
    public void actionPerformed(ActionEvent e) {
        //Fill the parcel list with input from the frame
        list = new ArrayList<Parcel>();
        int inputA = Integer.parseInt(aField.getText());
        int inputB = Integer.parseInt(bField.getText());
        int inputC = Integer.parseInt(cField.getText());
        int inputL = Integer.parseInt(lField.getText());
        int inputP = Integer.parseInt(pField.getText());
        int inputT = Integer.parseInt(tField.getText());
        for (int i = 0; i < inputA; i++) {
            list.add(new ParcelA());
        }
        for (int i = 0; i < inputB; i++) {
            list.add(new ParcelB());
        }
        for (int i = 0; i < inputC; i++) {
            list.add(new ParcelC());
        }
        for (int i = 0; i < inputL; i++) {
            list.add(new ParcelL());
        }
        for (int i = 0; i < inputP; i++) {
            list.add(new ParcelP());
        }
        for (int i = 0; i < inputT; i++) {
            list.add(new ParcelT());
        }
        //Record starting time;
        startTime = System.currentTimeMillis();

        //Run chosen algorithm
        if (this.selection.getSelectedItem().toString() == "Brute Force") {
            Engine.emptyParcels();
            //Brute force needs a container made seperately
            Container truck = new Container(33,8,5);
            BruteForce bruteForce = new BruteForce(truck, list);
            bruteForce.parcelSolver(truck,list);
            for(Parcel p : bruteForce.getBestSet())
            {
                truck.addParcel(p);
            }
            loadedParcels = truck.getContainedParcels();

            Engine.addParcels(loadedParcels);
            img = Engine.renderImage();
            repaint();


        } else if (this.selection.getSelectedItem().toString() == "Diagonal Fill") {
            run(new DiagonalFill(), 1, "DiagonalFill");
            Engine.emptyParcels();
            Engine.addParcels(loadedParcels);
            img = Engine.renderImage();
            repaint();
        } else if (this.selection.getSelectedItem().toString() == "Hill Climb") {
            run(new HillClimb(), 10, "HillClimb");
            Engine.emptyParcels();
            Engine.addParcels(loadedParcels);
            img = Engine.renderImage();
            repaint();
        } else if (this.selection.getSelectedItem().toString() == "Genetic") {
            run(new Genetic(), 10, "Genetic");
            Engine.emptyParcels();
            Engine.addParcels(loadedParcels);
            img = Engine.renderImage();
            repaint();
        } else if (this.selection.getSelectedItem().toString() == "Random") {
            run(new Random(), 1, "Random");
            Engine.emptyParcels();
            Engine.addParcels(loadedParcels);
            img = Engine.renderImage();
            repaint();
        }
        else if (this.selection.getSelectedItem().toString() == "Custom") {

            //Custom setting for fillengine
            FillEngine custom = new FillEngine();
            custom.run(list,loadedParcels,custom.createCustomSetting(Integer.parseInt(valueField.getText()),Integer.parseInt(volumetricValueField.getText()),Integer.parseInt(xField.getText()),Integer.parseInt(yField.getText()),Integer.parseInt(zField.getText())));
            Engine.emptyParcels();
            Engine.addParcels(loadedParcels);
            img = Engine.renderImage();
            repaint();
        }
        //Record end time
        endTime = System.currentTimeMillis();

    }

    /**
     * Creates the frame, all elements of the UI and adds them to frame.
     * @param title title of the frame
     */
    public UIFrame(String title) {

        super(title);
        this.setLayout(new BorderLayout());
        //Create run button and add the main actionlistener
        JButton run = new JButton("Run");
        run.addActionListener(this);
        //Create rotate button with actionlistener for rotation
        JButton rotate = new JButton("Rotate");
        rotate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Engine.rotate(20);
                img = Engine.renderImage();
                repaint();
            }
        });


        //Make a Combobox and add elements to it
        this.selection = new JComboBox();
        this.selection.addItem("Brute Force");
        this.selection.addItem("Diagonal Fill");
        this.selection.addItem("Hill Climb");
        this.selection.addItem("Genetic");
        this.selection.addItem("Random");
        this.selection.addItem("Custom");

        //Make labels to explain textfields
        JLabel aLabel = new JLabel("#A= ");
        JLabel bLabel = new JLabel("#B= ");
        JLabel cLabel = new JLabel("#C= ");
        JLabel lLabel = new JLabel("#L= ");
        JLabel pLabel = new JLabel("#P= ");
        JLabel tLabel = new JLabel("#T= ");

        JLabel customSettings = new JLabel("Custom settings for FillEngine:");
        JLabel valueLabel = new JLabel("C1=");
        JLabel volumetricValueLabel = new JLabel("C2=");
        JLabel xLabel = new JLabel("X=");
        JLabel yLabel = new JLabel("Y=");
        JLabel zLabel = new JLabel("Z=");

        //Make textfields for parcel numbers
        aField = new JTextField("0", 2);
        bField = new JTextField("0", 2);
        cField = new JTextField("0", 2);
        lField = new JTextField("0", 2);
        pField = new JTextField("0", 2);
        tField = new JTextField("0", 2);

        //Textfields with parameters for custom FillEngine
        valueField = new JTextField("1", 2);
        volumetricValueField = new JTextField("1", 2);
        xField = new JTextField("-1", 2);
        yField = new JTextField("-1", 2);
        zField = new JTextField("-1", 2);

        //Add everything to containers to pack it nicely in the frame
        java.awt.Container container = new java.awt.Container();
        container.setLayout(new FlowLayout());
        container.add(this.selection);
        container.add(run);
        container.add(rotate);

        java.awt.Container container2 = new java.awt.Container();
        container2.setLayout(new FlowLayout());
        container2.add(aLabel);
        container2.add(aField);
        container2.add(bLabel);
        container2.add(bField);
        container2.add(cLabel);
        container2.add(cField);
        container2.add(lLabel);
        container2.add(lField);
        container2.add(pLabel);
        container2.add(pField);
        container2.add(tLabel);
        container2.add(tField);

        java.awt.Container container3= new java.awt.Container();
        container3.setLayout(new FlowLayout());
        container3.add(customSettings);
        container3.add(valueLabel);
        container3.add(valueField);
        container3.add(volumetricValueLabel);
        container3.add(volumetricValueField);
        container3.add(xLabel);
        container3.add(xField);
        container3.add(yLabel);
        container3.add(yField);
        container3.add(zLabel);
        container3.add(zField);

        java.awt.Container container4 = new java.awt.Container();

        container4.setLayout(new BorderLayout());
        container4.add(container2, BorderLayout.NORTH);
        container4.add(container3, BorderLayout.CENTER);
        container4.add(container, BorderLayout.SOUTH);

        //Add containers and image generated by 3Dengine to the frame
        this.add(container4, BorderLayout.SOUTH);
        this.add(new DrawPane(), BorderLayout.CENTER);

    }

    /**
     *
     * @param fillEngine fillEngine used (Random, HillClimb, DiagonalFill or Genetic)
     * @param amountOfGenerations For how many generations should it run. 1 for everything but genetic And HillClimb, which should take more generations.
     * @param type String of the fillEngine used.
     */
    private static void run(FillEngine fillEngine, int amountOfGenerations, String type) {
        int generation = 0;
        FillEngine.Setting setting = fillEngine.createRandomSetting(10);
        while (generation < amountOfGenerations) {
            generation++;
            long startTime = System.currentTimeMillis();
            double[] settingCopy = new double[setting.c.length];
            for (int j = 0; j < settingCopy.length; j++) {
                settingCopy[j] = setting.c[j];
            }
            ArrayList<Parcel> listOfPackets = list;
            loadedParcels = new ArrayList<Parcel>();
            FillEngine.Result result = fillEngine.run(listOfPackets, loadedParcels, setting);
            percentEmpty = result.proccentFilled;
            score = result.score;
        }


    }

    /**
     * Creates the Frame objects, displays it, creates 3DEngine and sets it up. Initializes some variables.
     * @param args not used
     */
    public static void main(String[] args) {
        UIFrame truckFrame = new UIFrame("Knapsack Truck");
        truckFrame.setSize(700, 700);
        truckFrame.setVisible(true);
        truckFrame.setResizable(false);
        truckFrame.setDefaultCloseOperation(3);
        truckFrame.getContentPane().setBackground(Color.white);

        truckFrame.Engine = new Engine3D();
        truckFrame.Engine.setup();
        truckFrame.img = truckFrame.Engine.renderImage();
        percentEmpty = 100;
        score = 0;
        truckFrame.repaint();
    }


    class DrawPane extends JPanel {
        /**
         * Scales and draws image from 3D engine, draws information on the screen too.
         * @param g
         */
        protected void paintComponent(Graphics g) {
            Graphics2D localGraphics2D = (Graphics2D) g;
            localGraphics2D.drawImage(img,0,0,600,600,0,0,300,300,null);
            localGraphics2D.setFont(new Font("Arial", Font.PLAIN, 26));
            localGraphics2D.drawString("Percent filled: " + String.valueOf(100.00 -(double) Math.round(percentEmpty * 100) / 100) + "%",10,30);
            localGraphics2D.drawString("Score: " + String.valueOf(score),10,60);
            localGraphics2D.drawString("Algorithm runtime (in seconds): " + String.valueOf((double)(endTime-startTime)/1000),10,90);
        }
    }
}
