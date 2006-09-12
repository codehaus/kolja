package com.baulsupp.less;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResponsiveLock implements Lock {
  private Lock target = new ReentrantLock();

  private AtomicLong lastAccessed = new AtomicLong(System.currentTimeMillis());

  private AtomicBoolean canRun = new AtomicBoolean(false);

  public ResponsiveLock() {
  }

  public void lock() {
    updateLastAccessed();

    target.lock();

    updateLastAccessed();
  }

  public void lockInterruptibly() throws InterruptedException {
    updateLastAccessed();

    target.lockInterruptibly();

    updateLastAccessed();
  }

  public Condition newCondition() {
    return target.newCondition();
  }

  public boolean tryLock() {
    updateLastAccessed();

    try {
      return target.tryLock();
    } finally {
      updateLastAccessed();
    }
  }

  public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
    try {
      return target.tryLock(arg0, arg1);
    } finally {
      updateLastAccessed();
    }
  }

  public void unlock() {
    updateLastAccessed();

    target.unlock();

    updateLastAccessed();
  }

  private void updateLastAccessed() {
    lastAccessed.set(System.currentTimeMillis());
  }

  public Lock createBackgroundLock(long delay) {
    return new BackgroundLock(delay);
  }

  private class BackgroundLock implements Lock {
    private long delay;

    public BackgroundLock(long delay) {
      this.delay = delay;
    }

    public void lock() {
      waitForDelay();

      target.lock();
    }

    public void lockInterruptibly() throws InterruptedException {
      waitForDelay();

      target.lockInterruptibly();
    }

    public Condition newCondition() {
      return target.newCondition();
    }

    public boolean tryLock() {
      waitForDelay();

      return target.tryLock();
    }

    public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
      waitForDelay();

      return target.tryLock(arg0, arg1);
    }

    public void unlock() {
      target.unlock();
    }

    private void waitForDelay() {
      while (true) {        
        synchronized (canRun) {
          if (!canRun.get()) {
            try {
              canRun.wait();
            } catch (InterruptedException e) {
              // nothing
              continue;
            }
          }
        }
        
        long left = delay - (System.currentTimeMillis() - lastAccessed.get());

        if (left > 0) {
          sleep(left);
        } else {
          break;
        }
      }
    }
  }

  public void startBackgroundThreads() {
    synchronized (canRun) {
      canRun.set(true);
      canRun.notifyAll();
    }
  }

  private void sleep(long left) {
    try {
      Thread.sleep(left);
    } catch (InterruptedException e) {
      // nothing
    }
  }
}
