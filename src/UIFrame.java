import java.awt.*;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class UIFrame extends JFrame implements ActionListener {
    public JComboBox selection;
    JTextField aField;
    JTextField bField;
    JTextField cField;
    JTextField lField;
    JTextField pField;
    JTextField tField;

    public void actionPerformed(ActionEvent e) {
        ArrayList<Parcel> list = new ArrayList<Parcel>();
        int inputA = Integer.parseInt(aField.getText());
        int inputB = Integer.parseInt(bField.getText());
        int inputC = Integer.parseInt(cField.getText());
        int inputL = Integer.parseInt(lField.getText());
        int inputP = Integer.parseInt(pField.getText());
        int inputT = Integer.parseInt(tField.getText());
        for(int i = 0; i < inputA; i++){
            list.add(new ParcelA());
        }
        for(int i = 0; i < inputB; i++){
            list.add(new ParcelB());
        }
        for(int i = 0; i < inputC; i++){
            list.add(new ParcelC());
        }
        for(int i = 0; i < inputL; i++){
            list.add(new ParcelL());
        }
        for(int i = 0; i < inputP; i++){
            list.add(new ParcelP());
        }
        for(int i = 0; i < inputT; i++){
            list.add(new ParcelT());
        }

        if(this.selection.getSelectedItem().toString() == "Brute Force") {
            System.out.println("Brute Force");
        } else if(this.selection.getSelectedItem().toString() == "Diagonal Fill") {
            System.out.println("Diagonal Fill");
        } else if(this.selection.getSelectedItem().toString() == "Hill Climb") {
            System.out.println("Hill Climb");
        } else if(this.selection.getSelectedItem().toString() == "Genetic") {
            System.out.println("Genetic");
        } else if(this.selection.getSelectedItem().toString() == "Random") {
            System.out.println("Random");
        }

    }

    public UIFrame(String title) {
        super(title);

        this.setLayout(new BorderLayout());

        JButton run = new JButton("Run!");
        run.addActionListener(this);

        this.selection = new JComboBox();
        this.selection.addItem("Brute Force");
        this.selection.addItem("Diagonal Fill");
        this.selection.addItem("Hill Climb");
        this.selection.addItem("Genetic");
        this.selection.addItem("Random");

        JLabel aLabel = new JLabel("#A =");
        JLabel bLabel = new JLabel("#B =");
        JLabel cLabel = new JLabel("#C =");
        JLabel lLabel = new JLabel("#L =");
        JLabel pLabel = new JLabel("#P =");
        JLabel tLabel = new JLabel("#T =");

        aField = new JTextField("0",2);
        bField = new JTextField("0",2);
        cField = new JTextField("0",2);
        lField = new JTextField("0",2);
        pField = new JTextField("0",2);
        tField = new JTextField("0",2);


        Container container = new Container();
        container.setLayout(new FlowLayout());
        container.add(this.selection);
        container.add(run);

        Container container2 = new Container();
        container2.setLayout(new FlowLayout());
        container2.add(aLabel);container2.add(aField);
        container2.add(bLabel);container2.add(bField);
        container2.add(cLabel);container2.add(cField);
        container2.add(lLabel);container2.add(lField);
        container2.add(pLabel);container2.add(pField);
        container2.add(tLabel);container2.add(tField);

        Container container3 = new Container();
        container3.setLayout(new BorderLayout());
        container3.add(container2, BorderLayout.NORTH);
        container3.add(container, BorderLayout.CENTER);
        this.add(container3,BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        UIFrame truckFrame = new UIFrame("Knapsack Truck");
        truckFrame.setSize(400, 400);
        truckFrame.setVisible(true);
        truckFrame.setDefaultCloseOperation(3);
    }
}