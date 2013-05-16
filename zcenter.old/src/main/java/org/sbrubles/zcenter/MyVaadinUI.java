package org.sbrubles.zcenter;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The Application's "main" class
 */
@Named
@Singleton
public class MyVaadinUI extends UI
{
	
//    @WebServlet(urlPatterns = { "/*", "/VAADIN/*" }, initParams = {
//            @WebInitParam(name = VaadinSession.UI_PARAMETER, value = "org.sbrubles.zcenter.MyVaadinUI"),
//            @WebInitParam(name = Constants.SERVLET_PARAMETER_UI_PROVIDER, value = "com.vaadin.cdi.CDIUIProvider") })
//    public static class AddressBookApplicationServlet extends VaadinServlet {
//    }

	@WebListener
  public static class AddressBookApplicationServlet implements ServletContextListener {
		
		@Inject
		private BeanManager bm;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println(bm);
		
	}
  }

	
    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        
        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                layout.addComponent(new Label("Thank you for clicking"));
            }
        });
        layout.addComponent(button);
    }

    
    @PostConstruct
    public void start() {
    	System.out.println("asd");
    }
}
