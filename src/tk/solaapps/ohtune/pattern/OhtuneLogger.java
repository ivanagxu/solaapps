package tk.solaapps.ohtune.pattern;

import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public final class OhtuneLogger {
    public static final Logger logger = Logger.getLogger("Ohtune");
    public static void info(String logTxt)
    {
    	logger.info(logTxt);
    }
    public static void error(String logTxt)
    {
    	logger.error(logTxt);
    }
    public static void error(Exception e, String logTxt)
    {
    	logger.error(logTxt);
    	if(e != null)
    	{
    		logger.error(e.getMessage());
    		
    		StackTraceElement[] stes = e.getStackTrace();
    		if(stes != null)
    		{
    			StringBuilder sb = new StringBuilder("Stack trace: \r\n");
	    		for(int i = 0; i < stes.length; i++)
	    		{
	    			sb.append('\t').append(stes[i].toString()).append("\r\n");
	    		}
	    		logger.error(sb.toString());
    		}
    		
    	}
    }
    
    static{
    	try
    	{
    		Properties prop = new Properties();
    		prop.load(OhtuneLogger.class.getResourceAsStream("/ohtune.log4j.properties"));
        	PropertyConfigurator.configure(prop);
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}