package FileWatcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileFilter;

public class RegexFileFilter implements FileFilter {
	final Pattern pattern;
	
	public RegexFileFilter(String regex) {
		pattern = Pattern.compile(regex);
	}
	
	public boolean accept(File file) {
		return pattern.matcher(file.getName()).find();
	}

}
