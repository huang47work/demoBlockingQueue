package thread_pool_manager;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by huangsiqian on 2017/3/1 0001.
 */
public class ThreadPoolManager {
    //单例滴
    private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();

    public static ThreadPoolManager newInstance() {
        return threadPoolManager;
    }

    private ThreadPoolManager() {}


    // 线程池维护线程的最少数量
    private final static int CORE_POOL_SIZE = 4;

    // 线程池维护线程的最大数量
    private final static int MAX_POOL_SIZE = 10;

    // 线程池维护线程所允许的空闲时间
    private final static int KEEP_ALIVE_TIME = 0;

    // 线程池所使用的缓冲队列大小
    private final static int WORK_QUEUE_SIZE = 10;

    //执行失败了后的事情,被线程池回掉
    private final RejectedExecutionHandler handler = new RejectedExecutionHandler(){
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(((AccessDBThread )r).getMsg()+"消息放入队列中重新等待执行");
            msgQueue.offer((( AccessDBThread ) r ).getMsg() );
        }
    };

    // 管理数据库访问的线程池
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,new ArrayBlockingQueue( WORK_QUEUE_SIZE ), this.handler);

    // 调度线程池
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 100 );

    // 消息缓冲队列
//    private Queue<String> msgQueue = new LinkedList<>();

    //我要用blockingQueue
    private BlockingQueue<String> msgQueue = new ArrayBlockingQueue<>(20);

    // 访问消息缓存的调度线程
    // 查看是否有待定请求，如果有，则创建一个新的AccessDBThread，并添加到线程池中
    private final Runnable accessBufferThread = new Runnable() {

        @Override
        public void run() {
            if(hasMoreAcquire()){
                String msg = msgQueue.poll();
                Runnable task = new AccessDBThread( msg );
                threadPool.execute( task );
            }
        }
    };

    @SuppressWarnings("rawtypes")
    private final ScheduledFuture taskHandler = scheduler.scheduleAtFixedRate(accessBufferThread, 0, 1, TimeUnit.SECONDS);





    private boolean hasMoreAcquire(){
        return !msgQueue.isEmpty();
    }

    public void addLogMsg( String msg ) {
        Runnable task = new AccessDBThread( msg );
        threadPool.execute( task );
    }
}



