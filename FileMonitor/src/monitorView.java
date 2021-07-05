import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class monitorView {
	JFrame frame = null; 
	JPanel button_panel = null;
	JPanel display_panel = null;
	Map<String, JButton> buttons = new ConcurrentHashMap<>();
	
	public monitorView() {
		frame = new JFrame();
		frame.setSize(500,500);
		display_panel = new JPanel();
		frame.add(display_panel);
		button_panel = new JPanel();
		frame.add(button_panel);
	    frame.setLayout(new GridLayout(3, 1));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void addButton(String name, ActionListener callback) {
		JButton button = new JButton(name);
		button.addActionListener(callback);
		buttons.put(name, button);
		button_panel.add(button);
		button_panel.revalidate();
		button_panel.repaint();
	}
	
	public void removeButton(String name) {
		JButton button = buttons.get(name);
		buttons.remove(name);
		button_panel.remove(button);;
		button_panel.revalidate();
		button_panel.repaint();
	}

	public void createDialog(String message, Map<String, ActionListener> options) {
		// TODO Auto-generated method stub
		JDialog d = new JDialog(frame);
		JPanel DialogPanel = new JPanel();
		DialogPanel.setLayout(new GridLayout(2, 1));
		d.add(DialogPanel);
		JPanel TextPanel = new JPanel();
		JPanel OptionPanel = new JPanel();
		JLabel text = new JLabel(message);
		TextPanel.add(text);
		options.forEach((k,v) -> {
			JButton b = new JButton(k);
			b.addActionListener(v);
			OptionPanel.add(b);
		});
		DialogPanel.add(TextPanel);
		DialogPanel.add(OptionPanel);
		DialogPanel.setSize(300, 100);
		d.pack();
		d.setVisible(true);
	}
}
