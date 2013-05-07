package org.sbrubles.zcenter;
public interface ModuleServiceListener {

    public void moduleRegistered(ModuleService source, Module module);
    
    public void moduleUnregistered(ModuleService source, Module module);
}