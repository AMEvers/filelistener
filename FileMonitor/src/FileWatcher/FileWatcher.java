package FileWatcher;

import static java.nio.file.StandardWatchEventKinds.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

public class FileWatcher implements Runnable{
	protected List<FileListener> listeners = new ArrayList<>();
	protected final File folder;
	protected final FileFilter filter;
	
	public FileWatcher(File folder) {
		this.folder = folder;
		this.filter = null;
	}
	
	public FileWatcher(File folder, RegexFileFilter filter) {
		this.folder = folder;
		this.filter = filter;
	}
	
	public void watch() {
		if (folder.exists()) {
			Thread thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
			Path path = Paths.get(folder.getAbsolutePath());
			path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
			while (true) {
				WatchKey watchKey = watchService.take();
				for (WatchEvent<?> event : watchKey.pollEvents()) {
					notifylistener(event.kind(), path.resolve((Path) event.context()).toFile());
				}
				if(!watchKey.reset()) {
					break;
				}
			}
		} catch (IOException | InterruptedException | ClosedWatchServiceException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	protected void notifylistener(WatchEvent.Kind<?> kind, File file) {
    	FileEvent event = new FileEvent(file);
    	if(filter==null || filter.accept(file)) {
    		for (FileListener listener : listeners) {
		    	if (kind == ENTRY_CREATE) {
		    		listener.fileCreated(event);
		    	}
		    	else if (kind == ENTRY_MODIFY) {
		    		listener.fileModified(event);
		    	}
		    	else if (kind == ENTRY_DELETE) {
		    		listener.fileDeleted(event);
		    	}
    		}
    	}
    }
	
	public FileWatcher addListener(FileListener listener) {
		listeners.add(listener);
		return this;
	}
	
	public FileWatcher removeListener(FileListener listener) {
		listeners.remove(listener);
		return this;
	}
	
	public List<FileListener> getListeners() {
		return listeners;
	}
	
	public FileWatcher setListeners(List<FileListener> listeners) {
		this.listeners = listeners;
		return this;
	}
	
	public File[] getContents() {
		return folder.listFiles(filter);
	}

}
