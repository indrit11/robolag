package serialArm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SequenceCommand extends JPanel implements ActionListener 
{
    //Diese Klasse macht eine Befehl Vorschau und ruft die gleiche Befehle nochmal
    //Die Sequncer haben wir gemacht, weil dieser Arm die Funktionen eines Lagerhalters macht.
    //Die Funktion des Lagerhalters muss Automatisiert sein und die Sequencer macht die automatisch nachdem wir die Befehle 
    //eingefügt haben und mit dem Send Button geprüft haben.
	JLabel label;
	JLabel veryLabel;
	JLabel directionLabel;
	JButton x;
	JButton up;
	JButton down;
	String n;
	String d;
	boolean v;
	String c;

	public SequenceCommand(String name, String direction, boolean very, String command)
	{
		super();
		setAlignmentX(JPanel.LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		//setPreferredSize(new Dimension(300, 40));
		n = name;
		d = direction;
		v = very;
		x = new JButton("X");
		x.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(x);
		add(Box.createRigidArea(new Dimension(2,0)));
		up = new JButton("â–²");
		up.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(up);
		add(Box.createRigidArea(new Dimension(2,0)));
		down = new JButton("â–¼");
		down.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(down);
		add(Box.createRigidArea(new Dimension(5,0)));
		label = new JLabel("Motor: " + name);
		label.setForeground(Color.black);
		label.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(label);
		add(Box.createRigidArea(new Dimension(5,0)));
		directionLabel = new JLabel("Direction: " + direction);
		directionLabel.setForeground(Color.blue);
		directionLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(directionLabel);
		add(Box.createRigidArea(new Dimension(5,0)));
		veryLabel = new JLabel("Very: " + very);
		veryLabel.setForeground(Color.red);
		veryLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(veryLabel);
		x.addActionListener(this);
		up.addActionListener(this);
		down.addActionListener(this);
		if(v)
		{
			c = command.toUpperCase();
		}
		else
		{
			c = command;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) //Nimmt die SequenceOption Anderungen und macht denen besser.
                                                      //Es gibt einigen getParent , getComponents(ruft die Objekten die wir erstellen haben) 
                                                      //und getSource Methoden die den Sequencer GUI bessere Struktur zu haben.


	{
		if(arg0.getSource() == x)
		{
			SequenceOption.bottom.remove(this);
			SequenceOption.bottom.revalidate();
			SequenceOption.bottom.repaint();
			SequenceOption.fixButtons();
		}
		else if(arg0.getSource() == up)
		{
			int i = 0;
			for (Component c : getParent().getComponents())
			{
				if(c == this)
				{
					break;
				}
				else
				{
					i++;
				}
			}
			SequenceOption.moveComp(i, -1);
		}
		else if(arg0.getSource() == down)
		{
			int i = 0;
			for (Component c : getParent().getComponents())
			{
				if(c == this)
				{
					break;
				}
				else
				{
					i++;
				}
			}
			SequenceOption.moveComp(i, 1);
		}
		SequenceOption.UpdateCommand();
	}

	public void buttons()//Macht einen if else zu sehen ob die Objekten geruft sind.
	{
		if(getParent().getComponent(0) == this) { up.setEnabled(false); }
		else {up.setEnabled(true); }
		if(getParent().getComponent(getParent().getComponentCount() - 1) == this) { down.setEnabled(false); }
		else {down.setEnabled(true); }
	}

}
