package org.sbrubles.zcenter;

import com.vaadin.ui.Component;

public interface Module {

    public String getName();
    
    public Component createComponent();
}