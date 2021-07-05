import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import FileWatcher.*;

public class monitorControl {
	monitorView view = null;
	public monitorControl (monitorView view) {
		this.view = view;
		try(InputStream input = monitorControl.class.getClassLoader().getResourceAsStream("config.properties")) {
			Properties prop = new Properties();
			if (input == null) {
				System.out.println("Unable to find properties file");
				return;
			}
			prop.load(input);
			Path path = Paths.get(prop.getProperty("monitorpath"));
			File monitorPath = new File(path.toString());
			if(monitorPath.exists()) {
				FileWatcher watcher = new FileWatcher(monitorPath, new RegexFileFilter("^ClusterInstallStatus.*"));
				initButtons(watcher.getContents());
				watcher.addListener(new FileAdapter() {
					public void fileCreated(FileEvent event) {
						addButton(event.getFile().getName());
					}
					public void fileModified(FileEvent event) {
						System.out.println(String.format("File %s modified!", event.getFile().getName()));
					}
					public void fileDeleted(FileEvent event) {
						removeButton(event.getFile().getName());
					}
				}).watch();
			}
			else {
				System.out.print("nope!");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void addButton(String name) {
		view.addButton(name, new callbackActionListener(this) {
	         public void actionPerformed(ActionEvent e) {
		            monitor.dialog(name);
		     }});
	}
	
	public void removeButton(String name) {
		view.removeButton(name);
	}
	
	public void initButtons(File[] files) {
		for(File file : files) {
			addButton(file.getName());
		}
	}
	
	public void say(String message) {
		System.out.print(message);
	}

	public void dialog(String message) {
		LinkedHashMap<String, ActionListener> options = new LinkedHashMap<String,ActionListener>();
		options.put("Reinstall", new callbackActionListener(this) {
			 public void actionPerformed(ActionEvent e) {
				 monitor.say("Chose yes!");
				 Component component = (Component) e.getSource();
		         JDialog dialog = (JDialog) SwingUtilities.getRoot(component);
		         dialog.dispose();				 
		     }});
		options.put("Cancel", new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
		            Component component = (Component) e.getSource();
		            JDialog dialog = (JDialog) SwingUtilities.getRoot(component);
		            dialog.dispose();
		     }});
		view.createDialog("Are you sure?", options);
	}
	
	public class callbackActionListener implements ActionListener{
		monitorControl monitor = null;
		public callbackActionListener(monitorControl view) {
			this.monitor = view;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
