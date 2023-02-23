package webServiceListener;

//import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import queriesForWebService.ProfilingQueryInit;

@WebListener
public class AppContextListener implements ServletContextListener {
// Le listener permet de charger un modèle avant le démarage du web service
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		//ServletContext ctx = servletContextEvent.getServletContext();
		try {
			ProfilingQueryInit.init();
		} catch (Exception e) {
			System.out.println("InfModel not initialized for Web Service !");
			//e.printStackTrace();
		}
		System.out.println("InfModel initialized for Web Service.");
	}

}
