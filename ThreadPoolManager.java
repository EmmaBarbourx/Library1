
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

	
	public class ThreadPoolManager {
    private ExecutorService executor;

    public void runTasks(List<Runnable> tasks) {
        executor = Executors.newFixedThreadPool(10); // create a thread pool with 10 threads

        for (Runnable task : tasks) {
            executor.execute(task); // submit the task to the thread pool
        }
    }

    public void waitForCompletion() {
        executor.shutdown(); // shut down the thread pool after all tasks have been submitted

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // wait for all tasks to complete
        } catch (InterruptedException e) {
            // Exception occurred, log the exception
            e.printStackTrace();
        }
    }
    
    //new challenge - winner gets a bag of k 
}




