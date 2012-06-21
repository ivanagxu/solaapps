package tk.solaapps.ohtune.pattern;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OhtuneServiceHolder {
	private static OhtuneServiceHolder instance = null;
	private static Object lockObj = new Object();
	
	private ApplicationContext context = null;
	
	
	private OhtuneServiceHolder()
	{
		context = new 
				ClassPathXmlApplicationContext( new String[] {"ohtune-context.xml"} ); 
	}
	
	public static OhtuneServiceHolder getInstence()
	{
		synchronized(lockObj)
		{
			if(instance == null)
			{
				instance = new OhtuneServiceHolder();
			}
			
			return instance;
		}
	}
	
	public BeanFactory getBeanFactory()
	{
		return context;
	}
}
