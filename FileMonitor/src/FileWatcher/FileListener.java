package FileWatcher;

import java.util.EventListener;

public interface FileListener extends EventListener {
	public void fileCreated(FileEvent event);
	
	public void fileDeleted(FileEvent event);

	public void fileModified(FileEvent event);
}
