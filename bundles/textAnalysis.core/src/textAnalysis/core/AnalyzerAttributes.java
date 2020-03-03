package textAnalysis.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import textAnalysis.provider.AProvider;

public class AnalyzerAttributes {

    // Names of the Attributes
    public static final String FILE_NAME = "edu.kit.analyzer.FILE_TEXT";

    // Was da reinkommt ist eine Map<String,String>: Name auf 'true'/ 'false'.
    // TODO man k�nnte auch nur eine Liste mit allen Namen machen von denen, die ausgef�hrt werden sollen,
    // wenn eine abgeklickt wird m�sste man sie halt immer aus der Liste l�schen, das ist wahrscheinlich zu
    // aufw�ndig.
    public static final String CHECKBOX_ACTIVATION = "edu.kit.analyzer.CHECKBOX_ACTIVATION";
    public static final String SERVICE_CHECKBOX_VALUES = "edu.kit.analyzer.SERVICE_CHECKBOX_VALUES";
    public static final String EXECUTION_SERVICE_CLASS_NAMES = "edu.kit.analyzer.EXECUTION_SERVICE_CLASS_NAMES";

    // TODO is there a more pretty Way to register the Analysis so the Delegate can use them?
    public static Map<String, AProvider> AnalysisRegistry;

    /**
     * Reads the config file and returns the name of the Folder where the Analysis-Jars are.
     * The config file is a file called .textanalysisconfig in the users home directory.
     * It has to contain the specific path to the folder.
     * 
     * If the config file can not be found, it is created with an entry to a default 
     * folder which is the folder '.textanalysis' in the user's home directory.
     * 
     * @return The folder name or "" if no folder is specified / the specified folder can not be found.
     * @throws IOException
     */
    protected static String getAnalysisSrcFolder() throws IOException {
    	
    	String homeDir = System.getProperty("user.home");
    	File tmpDir = new File(homeDir + "\\.textanalysisconfig");
    	if (!tmpDir.exists()){
			String fileData = homeDir + "\\.textanalysis\\";
			FileOutputStream fos = new FileOutputStream(tmpDir);
			fos.write(fileData.getBytes());
			fos.flush();
			fos.close();
			File analysisDir = new File(fileData);
			if (!analysisDir.exists()) {
				analysisDir.mkdir();
			}
			System.out.println("successfull");
		}
    	    	
    	BufferedReader br = new BufferedReader(new FileReader(tmpDir)); 
    	String configFolder = br.readLine();
    	br.close();
    	
    	if (configFolder == null) {
    		return "";
    	}
    	if (new File(configFolder).exists()) {   
    		return configFolder;
    	} else {
    		return "";
    	}
    }
    
    
    /***
     * Creates a ClassLoader that loads the jar-files from the specified configFolder
     * @param configFolder
     * @return The created ClassLoader
     */
    protected static URLClassLoader getURLCL(String configFolder) {
    	File loc = new File(configFolder);
    	   
        File[] flist = loc.listFiles((FileFilter) file -> file.getPath()
                                                              .toLowerCase()
                                                              .endsWith(".jar"));
        System.out.println("file list: " + flist.length);

        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++) {
            try {
                urls[i] = flist[i].toURI()
                                  .toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        // URL[] classLoaderUrls = new URL[]{new URL("file:///C://Maike//KIT//PraktikumWfAM//Analysis//bean.jar")};
        // URL[] classLoaderUrls = null;
        // try {
        // classLoaderUrls = new URL[]{new
        // URL("file:///C://Maike//KIT//PraktikumWfAM//Analysis/dcountservice-0.0.1-SNAPSHOT.jar")};
        // } catch (MalformedURLException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        System.out.println("url list: " + urls.length);
        System.out.println(flist[0]);
        System.out.println(urls[0]);
        URLClassLoader ucl = new URLClassLoader(urls);

        return ucl;
    }

    private AnalyzerAttributes() {
    }

}