import java.io.*;                        
import java.util.*;                      
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class RequiredFieldException extends Exception
{
	 RequiredFieldException(String message)
	 {
		 super(message);
	 }
}
class Customer implements Serializable
{
	 String CustomerName;
	 int CustomerNumber;
	 String city;
	 String State;
	 String Pincode;
	 Customer(String name,int number,String City,String State,String Pincode)
	 {
		 CustomerName=name;
		 CustomerNumber=number;
		 city=City;
		 this.State=State;
		 this.Pincode=Pincode;
	 }
	 public String toString()            // to display in text area
	 {
		  return "CustomerName: "+CustomerName+"\n"+"CustomerNumber:  "+CustomerNumber+"\n"+"City: "+city+"\n"+"State:  "+State+"\n"+"Pincode:  "+Pincode+"\n";
	 }
}
class CustomerManagement
{
	  ArrayList<Customer> customers;
	  File customerfile;
	  ObjectOutputStream out;
	  ObjectInputStream in;
	  CustomerManagement()
	  {
		   customers=new ArrayList<>();
		   try
		   {
			   customerfile=new File("Customerfile.ser");
			   customerfile.createNewFile();
		   }
		   catch(Exception e)
		   {
			   JOptionPane.showMessageDialog(null,e,"File Error",JOptionPane.ERROR_MESSAGE);
		   }
	  }
	  public void Addcustomer(Customer obj)
	  {
		  customers.add(obj);
	  }
	  public void closestream()
	  {
		  try
		  {
		   if(out!=null)
		   {
			   out.close();
		  }
		 }
		 catch(Exception e)
		 {
		 }
	  }
	  public Customer searchCustomer(int Customernumber)     // return customer class object and we can display in textarea
	  {
		    for(Customer obj: customers)
			{
				if(obj.CustomerNumber==Customernumber)
				{
					try
					{
					  out=new ObjectOutputStream(new FileOutputStream(customerfile,true));  // here true appends the data to the file follow this method correctly
					 out.writeObject(obj);
					 out.close();
					}
					catch(Exception e)
					{
						 
						 JOptionPane.showMessageDialog(null,e,"FILE_INPUT",JOptionPane.ERROR_MESSAGE);
					}
					
					return obj;
				}
			}
			return null;
	  }
	  public ArrayList<Customer> display()           // returning whole customer object array lists
	  {
	 	 return customers;
	  }
}
class GUI extends JFrame implements ActionListener, Runnable {
    JLabel namelabel, numberlabel, citylabel, statelabel, pincodelabel, movingtitle;
    CustomerManagement obj;
    JTextField namefield, numberfield, cityfield, statefield, pincodefield;
    JTextArea displayarea;
    JButton add, search, display;
    JPanel panel1, panel2;
    int titleX;

    GUI() {
        titleX = 0;
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();

        // Panel Colors
        panel1 = new JPanel();
        panel1.setBackground(new Color(240, 248, 255)); // Alice Blue
        panel1.setLayout(new GridLayout(9, 2, 10, 10));
        panel1.setPreferredSize(new Dimension(350, 500));

        panel2 = new JPanel(null);  // Using null layout for moving title
        panel2.setBackground(new Color(70, 130, 180));  // Steel Blue
        panel2.setPreferredSize(new Dimension(700, 75));

        movingtitle = new JLabel("SHOP CUSTOMERS MANAGEMENT");
        movingtitle.setFont(new Font("Serif", Font.BOLD, 22));
        movingtitle.setForeground(Color.WHITE);
        movingtitle.setSize(getPreferredSize());
        movingtitle.setBounds(titleX, 20, 400, 30);
        panel2.add(movingtitle);

        Thread t1 = new Thread(this);
        t1.start();

        namelabel = new JLabel("Customer Name:");
        namefield = new JTextField();
        panel1.add(namelabel);
        panel1.add(namefield);

        numberlabel = new JLabel("Customer Number:");
        numberfield = new JTextField();
        panel1.add(numberlabel);
        panel1.add(numberfield);

        citylabel = new JLabel("City:");
        cityfield = new JTextField();
        panel1.add(citylabel);
        panel1.add(cityfield);

        statelabel = new JLabel("State:");
        statefield = new JTextField();
        panel1.add(statelabel);
        panel1.add(statefield);

        pincodelabel = new JLabel("Pincode:");
        pincodefield = new JTextField();
        panel1.add(pincodelabel);
        panel1.add(pincodefield);

        add = new JButton("Add Customer");
        search = new JButton("Search");
        display = new JButton("Display");

        // Improved Button Colors
        add.setBackground(new Color(60, 179, 113));  // Medium Sea Green
        search.setBackground(new Color(65, 105, 225)); // Royal Blue
        display.setBackground(new Color(255, 140, 0)); // Dark Orange
        add.setForeground(Color.WHITE);
        search.setForeground(Color.WHITE);
        display.setForeground(Color.WHITE);

        panel1.add(add);
        panel1.add(search);
        panel1.add(display);

        add.addActionListener(this);
        search.addActionListener(this);
        display.addActionListener(this);

        obj = new CustomerManagement();

        displayarea = new JTextArea();
        displayarea.setPreferredSize(new Dimension(350, 500));
        displayarea.setBorder(BorderFactory.createEtchedBorder());
        displayarea.setEditable(false);
        JScrollPane scroll = new JScrollPane(displayarea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        setLayout(new BorderLayout());
        setTitle("SHOP CUSTOMERS MANAGEMENT");
        c.add(panel2, BorderLayout.NORTH);
        c.add(panel1, BorderLayout.WEST);
        c.add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    public void run() {
        while (true) {
            titleX += 5;
            if (titleX > getWidth()) {
                titleX = -movingtitle.getWidth();
            }
            movingtitle.setLocation(titleX, 20);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == add) {
                String name = namefield.getText();
                String number = numberfield.getText();
                String city = cityfield.getText();
                String state = statefield.getText();
                String pin = pincodefield.getText();

                if (name.isEmpty() || number.isEmpty() || city.isEmpty() || state.isEmpty() || pin.isEmpty()) {
                    throw new RequiredFieldException("All Fields Are Required");
                }

                int num = Integer.parseInt(number);
                int pinc = Integer.parseInt(pin);

                Customer customer = new Customer(name, num, city, state, pin);
                obj.Addcustomer(customer);

                namefield.setText("");
                numberfield.setText("");
                cityfield.setText("");
                statefield.setText("");
                pincodefield.setText("");
            }

            if (e.getSource() == search) {
                int num = Integer.parseInt(numberfield.getText());
                Customer searched = obj.searchCustomer(num);

                if (searched == null) {
                    displayarea.setText("CUSTOMER NOT FOUND");
                } else {
                    displayarea.setText("CUSTOMER FOUND\n" + searched);
                }

                namefield.setText("");
                numberfield.setText("");
                cityfield.setText("");
                statefield.setText("");
                pincodefield.setText("");
            }

            if (e.getSource() == display) {
                displayarea.setText("");
                ArrayList<Customer> custom = obj.display();
                for (Customer objects : custom) {
                    displayarea.append(objects + "\n");
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid Number Format", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
public class ShopManagement 
{
	 public static void main(String[] args)
	 {
		 new GUI();
	 }
}