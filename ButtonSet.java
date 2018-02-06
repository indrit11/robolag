package serialArm;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

@SuppressWarnings("serial")
public class ButtonSet extends JPanel implements ActionListener, SerialPortEventListener
{
	SerialPort s;
	JPanel top;
	JPanel bottom;
	JLabel lbl;
	JButton left;
	JButton right;
	JButton veryRight;
	JButton veryLeft;
	String lAction;
	String rAction;
	
	public ButtonSet(String label, String leftButton, String rightButton, String leftAction, String rightAction) 
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		top = new JPanel();
		top.setLayout(new FlowLayout());
		bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		add(top);
		add(bottom);
		lbl = new JLabel(label);
		top.add(lbl);
		left = new JButton(leftButton);
		left.addActionListener(this);
		right = new JButton(rightButton);
		right.addActionListener(this);
		bottom.add(left);
		bottom.add(right);
		lAction = leftAction;
		rAction = rightAction;
	}
	
	public ButtonSet(String label, String leftButton, String rightButton,String vLeft, String vRight, String leftAction, String rightAction) 
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		top = new JPanel();
		top.setLayout(new FlowLayout());
		bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		add(top);
		add(bottom);
		lbl = new JLabel(label);
		top.add(lbl);
		veryLeft = new JButton(vLeft);
		veryLeft.addActionListener(this);
		left = new JButton(leftButton);
		left.addActionListener(this);
		right = new JButton(rightButton);
		right.addActionListener(this);
		veryRight = new JButton(vRight);
		veryRight.addActionListener(this);
		bottom.add(veryLeft);
		bottom.add(left);
		bottom.add(right);
		bottom.add(veryRight);
		lAction = leftAction;
		rAction = rightAction;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String action;
		if(e.getSource().equals(left))
		{
			action = lAction;
		}
		else if (e.getSource().equals(right))
		{
			action = rAction;
		}
		else if (e.getSource().equals(veryLeft))
		{
			action = lAction.toUpperCase();
		}
		else
		{
			action = rAction.toUpperCase();
		}
		
		try 
		{
			SerialArm.serial.writeString(action);
			System.out.println("Sent: " + action);
		} 
		catch (SerialPortException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	@Override
	public void serialEvent(SerialPortEvent arg0) 
	{
		try {
			System.out.println(s.readString(arg0.getEventValue()));
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
