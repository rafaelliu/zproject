package org.sbrubles.zcenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleServiceImpl implements ModuleService {

    private ArrayList<Module> modules = new ArrayList<Module>();
    
    private ArrayList<ModuleServiceListener> listeners = new ArrayList<ModuleServiceListener>();

    @SuppressWarnings("unchecked")
    public synchronized void registerModule(Module module) {
        modules.add(module);
        for (ModuleServiceListener listener : (ArrayList<ModuleServiceListener>) listeners.clone()) {
            listener.moduleRegistered(this, module);
        }
    }
    
    @SuppressWarnings("unchecked")
    public synchronized void unregisterModule(Module module) {
        modules.remove(module);
        for (ModuleServiceListener listener : (ArrayList<ModuleServiceListener>) listeners.clone()) {
            listener.moduleUnregistered(this, module);
        }
    }
    
    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }
    
    public synchronized void addListener(ModuleServiceListener listener) {
        listeners.add(listener);
    }
    
    public synchronized void removeListener(ModuleServiceListener listener) {
        listeners.remove(listener);
    }
}