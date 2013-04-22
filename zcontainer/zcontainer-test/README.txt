* TO DO

The current way to isolate test class and classes packed in a module is pretty lame..
Check use of 3rd party frameworks in MainModule classloader (really, should it be allowed? 
   we shouldn't change module's classpath that much. after all, we are testing the module as is!)