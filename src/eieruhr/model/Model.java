/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eieruhr.model;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;



/**
 *
 * @author Christian
 */
public class Model implements Runnable
{  
  private int val;
  private SubmissionPublisher<Integer> iPublisher;
  private ExecutorService eService;
  private volatile boolean running;

  
  public Model()
  {
    running = false;
    eService = Executors.newSingleThreadExecutor();
    iPublisher = new SubmissionPublisher<>();

    val = 30;
  }

  
  public void addSubscription(Subscriber<Integer> subscriber)
  {
    iPublisher.subscribe(subscriber);
  }

  public synchronized void start()
  {
    running = true;
    // LSG 3 \\
    notifyAll();
    // LSG 3 \\
    
    eService.submit(this);
  }
  
  public synchronized void stop()
  {
    running = false;
    // LSG 3 \\
    notifyAll();
    // LSG 3 \\
  }

  public void setVal(int val)
  {
    this.val = val;
  }

  public int getVal()
  {
    return val;
  }
  
  
  
  @Override
  public void run()
  {
    while(true)
    {
      synchronized(this)
      {
      while(running == false)
      {
        try
        {
          this.wait();
        }
        catch(InterruptedException e)
        {
          System.out.println(e);
        }
      }
      }
      if(running == true)
      {
      try
      {
        Thread.sleep(1000);
      }
      catch(InterruptedException e)
      {
        System.out.println(e);
      }
      
      val--;
      
      
      iPublisher.submit(val);
      }
    }
  }
}
