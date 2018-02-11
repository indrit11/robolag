package serialArm;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jssc.SerialPortException;

@SuppressWarnings("serial")
public class SequenceOption extends JPanel implements ActionListener 
{
	JComboBox<String> options;
	JButton add;
	JButton left;
	JButton right;
	JLabel label;
	String[] names;
	String[] lDirections;
	String[] rDirections;
	String[] lLabels;
	String[] rLabels;
	static ArrayList<String> commands;
	boolean[] verys;
	JCheckBox very;
	JPanel top;
	JPanel topIn;
	JPanel veryBottom;
	JButton send;
	public static JPanel bottom;
	JLabel commandLabel;
	static JTextField commandField;
	
	public SequenceOption(ButtonSet[] set) 
	{
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		top = new JPanel();
		topIn = new JPanel();
		topIn.setLayout(new FlowLayout(FlowLayout.LEADING));
		top.add(topIn);
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.setAlignmentX(LEFT_ALIGNMENT);
		add(top);
		bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		bottom.setAlignmentX(LEFT_ALIGNMENT);
		add(bottom);
		veryBottom = new JPanel();
		veryBottom.setLayout(new FlowLayout(FlowLayout.TRAILING));
		veryBottom.setAlignmentX(LEFT_ALIGNMENT);
		add(veryBottom);
		send = new JButton("Send");
		send.addActionListener(this);
		commandLabel = new JLabel("Command: ");
		commandField = new JTextField();
		commandField.setEditable(false);
		commandField.setColumns(20);
		veryBottom.add(commandLabel);
		veryBottom.add(commandField);
		veryBottom.add(send);
		names = new String[set.length];
		lDirections = new String[set.length];
		rDirections = new String[set.length];
		verys = new boolean[set.length];
		lLabels = new String[set.length];
		rLabels = new String[set.length];
		for(int i = 0; i < set.length; i++)
		{
			names[i] = set[i].lbl.getText();
			lDirections[i] = set[i].lAction;
			rDirections[i] = set[i].rAction;
			lLabels[i] = set[i].left.getText();
			rLabels[i] = set[i].right.getText();
			verys[i] = (set[i].veryLeft != null);
		}
		options = new JComboBox<String>(names);
		options.addActionListener(this);
		topIn.add(options);
		topIn.add(Box.createRigidArea(new Dimension(5,0)));
		left = new JButton(lLabels[0]);
		left.setPreferredSize(new Dimension(70, 30));
		left.addActionListener(this);
		left.setEnabled(false);
		right = new JButton(rLabels[0]);
		right.setPreferredSize(new Dimension(70, 30));
		right.addActionListener(this);
		topIn.add(left);
		topIn.add(Box.createRigidArea(new Dimension(5,0)));
		topIn.add(right);
		topIn.add(Box.createRigidArea(new Dimension(5,0)));
		System.out.println("yes");
		label = new JLabel("Very: ");
		topIn.add(label);
		topIn.add(Box.createRigidArea(new Dimension(5,0)));
		very = new JCheckBox();
		topIn.add(very);
		topIn.add(Box.createRigidArea(new Dimension(5,0)));
		add = new JButton("Add");
		add.addActionListener(this);                  //stellt die bevorzugte Größe von Buttons. Hier wird die Länge und die Breite von Buttons gegeben.
		add.setPreferredSize(new Dimension(70, 30));
		topIn.add(add);
                add = new JButton("Add");
		commands = new ArrayList<String>();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) //Nimmt Buttons und Labels von ActionListener und gibt Positionen fur denen
	{
		if((bottom.getComponentCount() == 0 && arg0.getSource() != add) || SerialArm.serial == null)
		{
			send.setEnabled(false);
		}
		else
		{
			send.setEnabled(true);
		}

		if(arg0.getSource() == options)
		{
			very.setEnabled(verys[options.getSelectedIndex()]);
			very.setSelected(verys[options.getSelectedIndex()]);
			left.setText(lLabels[options.getSelectedIndex()]);
			right.setText(rLabels[options.getSelectedIndex()]);
		}
		else if(arg0.getSource() == left)
		{
			right.setEnabled(true);
			left.setEnabled(false);
		}
		else if(arg0.getSource() == right)
		{
			left.setEnabled(true);
			right.setEnabled(false);
		}
		else if(arg0.getSource() == add)
		{
			System.out.println(names[options.getSelectedIndex()]);
			String dir;
			String com;
			if(left.isEnabled())
			{
				dir = right.getText();
				com = rDirections[options.getSelectedIndex()];
			}
			else
			{
				dir = left.getText();
				com = lDirections[options.getSelectedIndex()];
			}
			bottom.add(new SequenceCommand(names[options.getSelectedIndex()], dir, very.isSelected(), com));
			fixButtons();
			UpdateCommand();
		}
		else if(arg0.getSource() == send)
		{
			if(SerialArm.serial != null)
			{
				Send();
			}
			else
			{
				send.setEnabled(false);
			}
		}
	}

	void Send()//Es sendet einen String in GUI und macht einen Exception.
	{
		try {
			SerialArm.serial.writeString(UpdateCommand());
		} catch (SerialPortException e) {
			
			e.printStackTrace();
		}
	}

	static void fixButtons()//Es selektiert alle GUI Komponenten und buttons gibt zum Sequencer.
	{
		for (Component c : bottom.getComponents())
		{
			SequenceCommand sc = (SequenceCommand) c;
			sc.buttons();
		}
		SerialArm.sequencer.pack();
	}

	static void moveComp(int index, int amount)//Es bewegt die Komponenten.
	{
		Component[] comps = bottom.getComponents();
		bottom.removeAll();
		Component temp = comps[index];
		comps[index] = comps[index + amount];
		comps[index + amount] = temp;
		for (Component c : comps)
		{
			bottom.add(c);
		}
		bottom.revalidate();
		bottom.repaint();
		fixButtons();
		System.out.println("Move");
	}

	static String UpdateCommand()//Es updated die Kommands von Komponenten.
	{
		int count = bottom.getComponentCount();
		System.out.println(count);
		commands.clear();
		for(int i = 0; i < count; i++)
		{
			SequenceCommand com = (SequenceCommand) bottom.getComponent(i);
			commands.add(com.c);
		}
		String command = "";
		for(String s : commands)
		{
			command = command + s;
		}
		commandField.setText(command);
		commandField.repaint();
		return command;
	}
}
