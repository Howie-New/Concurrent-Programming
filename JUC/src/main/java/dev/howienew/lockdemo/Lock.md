隐式锁 synchronized
显式锁 Lock lock = new ReentrantLock();
所有的对象都可以成为锁，即管程(Monitor)，应为所有对象都继承了Object，而Object对应底层ObjectMonitor.cpp 和 ObjectMonitor.hpp
![img.png](img.png)

死锁案例和排查命令JVM进程对应的pid

jstack pid

或者jconsole等图形化界面