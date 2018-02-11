package serialArm;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import jssc.SerialPort;
import jssc.SerialPortEvent;            // Die Serial Arm macht auch die Seriell Port Verbindung mit Arduino.
import jssc.SerialPortEventListener;    // Die Seriell Port Verbindung braucht die Importieren  von Seriell Port Packages.
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialArm implements ActionListener, SerialPortEventListener
{
	public static SerialPort serial;
	public static JFrame frame;
	public static JFrame sequencer;
	public static JComboBox<String> comSelect;
	public static JButton goButton;
	public static JLabel label;
	public static ButtonSet base;
	public static ButtonSet shoulder;
	public static ButtonSet elbow;
	public static ButtonSet wrist;
	public static ButtonSet claw;
	static boolean ready;
	
	public static void main(String[] args) throws Exception 
	{
		 try {

         Socket skt = new Socket("192.168.0.30", 1234);

         BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream())); //Die TCP Socket Verbindung mit Arduino

         System.out.print("Received string: '");

         while (!in.ready()) {}

         System.out.println(in.readLine()); // Read one line and 		output it

         System.out.print("'\n");
         in.close();

      }catch(Exception e) {

         System.out.print("Connection Failed\n");
         e.printStackTrace();

      }
               
                SerialArm a = new SerialArm();
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		frame = new JFrame();
		frame.setSize(350, 350);
		frame.setTitle("Arm Controller");
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		base = new ButtonSet("Base", "Left", "Right", "<<", ">>", "o", "p");    //Die Buttons machen die Bewegung des Armes wie sie bewegen z.B links recht oben unten.                                                                           
		shoulder = new ButtonSet("Shoulder", "Up", "Down", "<<", ">>", "q", "w"); //Und nach jede Bewegung es gibt eine Buchstaben als Output in Java.
		elbow = new ButtonSet("Elbow", "Up", "Down", "<<", ">>", "e", "r");
		wrist = new ButtonSet("Wrist", "CCW", "CC", "u", "i");
		claw = new ButtonSet("Claw", "Open", "Close", "y", "t");
		sequencer = new JFrame("Sequencer");
		sequencer.setLocation(550, 50);
		ButtonSet[] set = {base, shoulder, elbow, wrist, claw};
		SequenceOption option =  new SequenceOption(set);
		sequencer.add(option);
		sequencer.setSize(200,100);
		sequencer.getContentPane().setLayout(new BoxLayout(sequencer.getContentPane(), BoxLayout.Y_AXIS));
		sequencer.setVisible(true);
		sequencer.pack();

		String[] portNames = SerialPortList.getPortNames(); //rufen wir die Port Name mit einem String
		frame.setLocation(200, 50);
		if (portNames.length == 0) //ist eine IfAnweisung wo wir die Instanz des Seriellen Ports gefunden 
                                          // wurde oder ob wir den Port finden oder nicht.
		{
			ready = false;
		    label = new JLabel("Could not find any COM ports.");
		    frame.add(label);
		    goButton = new JButton("Refresh");
		    JButton close = new JButton("Close");
		    close.addActionListener(new ActionListener()
		    {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.exit(0);
				}
			});
		    goButton.addActionListener(new ActionListener()
		    {
				@Override
				public void actionPerformed(ActionEvent e)
                    //Diese Methode wird aufgerufen, wenn ein ActionEvent an einer angemeldeten Komponente auftritt. 
                    //Dort muss also das gewünschte Verhalten, was auf das Ereignis erfolgen soll, implementiert werden. 
                    //Als Parameter wird der Methode eine Referenz auf das ActionEvent-Objekt übergeben, über welches 
                   // weitere Informationen über das Ereignis ermittelt werden können.
                    //Es findet die PortName und es macht eine ifAnweisung ob es richtig und ready ist.
				{
					String[] portNames = SerialPortList.getPortNames();
					if(portNames.length != 0)
					{
						ready = true;
					}
					System.out.println(ready);
				}
			});
		    frame.add(goButton);
		    frame.add(close);
		    frame.setVisible(true);
		    while(!ready) { System.out.print("");}
		    portNames = SerialPortList.getPortNames();
		    frame.getContentPane().removeAll();
		    frame.getContentPane().revalidate();
		    frame.getContentPane().repaint();
		}

		for (int i = 0; i < portNames.length; i++){
		    System.out.println(portNames[i]);
		}
		label = new JLabel("Select Arduino COM port: "); //wird ein JComboBox erstellt die comSelect heißt und dann diese selektierten Port rufen.
		frame.add(label);                               //Dann  werden die Port Parameters gesetzt und EventListener hinzugefügt.
		comSelect = new JComboBox<>(portNames);
		comSelect.setMaximumSize(new Dimension(70, 30));
		frame.add(comSelect);
		goButton = new JButton("Go");
		goButton.addActionListener(a);
		frame.add(goButton);
		frame.setVisible(true);

		//frame.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) //Propagates a SerialPortEvent event.Macht die Selektierung von COMPort 
                                                     // und dann addiert die frames (base,elbow,wrist,claw,shoulder).
                                                     // Hat ein Exception fur die SerialPort.	
	{
		if(e.getSource().equals(goButton))
		{
			if(serial == null)
			{
				System.out.println("selected " + comSelect.getSelectedItem());
				serial = new SerialPort((String)comSelect.getSelectedItem());
				try {
					serial.openPort();
					serial.setParams(SerialPort.BAUDRATE_9600,
	                        SerialPort.DATABITS_8,
	                        SerialPort.STOPBITS_1,
	                        SerialPort.PARITY_NONE);
					serial.addEventListener(this);
					serial.writeString("asdf");
					//serial.closePort();
					frame.getContentPane().removeAll();
					frame.revalidate();
					frame.repaint();
					frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
					frame.add(base);
					frame.add(shoulder);
					frame.add(elbow);
					frame.add(wrist);
					frame.add(claw);
					frame.pack();
					frame.validate();
				} catch (SerialPortException e1) {
					
					e1.printStackTrace();
				}
			}
			else
			{
				try {
					serial.writeString("go");
				} catch (SerialPortException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) //SMacht ein Exception die liest den String von Arduino oder macht ein error.
	{
		try {
			System.out.print(serial.readString(arg0.getEventValue()));
		} catch (SerialPortException e) {
			
			e.printStackTrace();
		}
              //"System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");"
		
	}
}