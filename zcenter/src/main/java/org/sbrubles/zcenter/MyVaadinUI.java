package org.sbrubles.zcenter;

import java.util.Iterator;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinUI extends UI implements ModuleServiceListener
{

    private ModuleService moduleService;
    
    public MyVaadinUI(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
    
    private TabSheet tabs;
    
    @Override
    protected void init(VaadinRequest request) {
        tabs = new TabSheet();
        tabs.setSizeFull();		
        for (Module module : moduleService.getModules()) {
            tabs.addTab(module.createComponent(), module.getName(), null);
        }		
        setContent(tabs);
        moduleService.addListener(this);
    }
    
    @Override
    public void close() {
        moduleService.removeListener(this);
        super.close();
    }
    
    public void moduleRegistered(ModuleService source, Module module) {
        tabs.addTab(module.createComponent(), module.getName(), null);
    }
    
    public void moduleUnregistered(ModuleService source, Module module) {
        Iterator<Component> it = tabs.getComponentIterator();
        while (it.hasNext()) {
            Component c = it.next();
            if (tabs.getTab(c).getCaption().equals(module.getName())) {
                tabs.removeComponent(c);
                return;
            }
        }
    }

}
