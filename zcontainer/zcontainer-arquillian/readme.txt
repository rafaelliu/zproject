You got to keep in mind that the Test Class injection is actually a proxy to the bean inside the module.
That means that Object level operations as well as ClassLoader scoped operations (static for instance)
won't behave as expected: