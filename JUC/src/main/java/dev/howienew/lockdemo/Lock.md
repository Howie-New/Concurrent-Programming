隐式锁 synchronized
显式锁 Lock lock = new ReentrantLock();


死锁案例和排查命令
jps -l 找到对应线程对应的线程id

jstack thread id

或者jconsole等图形化界面