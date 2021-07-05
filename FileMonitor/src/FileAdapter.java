import FileWatcher.*;

public abstract class FileAdapter implements FileListener {
	
	@Override
	public void fileCreated(FileEvent event) {
		// nothing
	}
	
	@Override
	public void fileDeleted(FileEvent event) {
		// nothing
	}
	
	@Override
	public void fileModified(FileEvent event) {
		// nothing
	}

}
