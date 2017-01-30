package com.OpenTasks.Tasks.AntibanTasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;

import java.util.Random;

/**
 * Created by VTM on 21/7/2016.
 */
public class TakeBreak extends Task {

  /* FIELDS */

  private long minIntervalMilli;
  private long maxIntervalMilli;

  private long minDurationMilli;
  private long maxDurationMilli;

  private long breakStartsAt;
  private long breakEndsAt;

  private boolean isFirstValidation;

  /* METHODS */

  public TakeBreak(int priority, long minIntervalMilli, long maxIntervalMilli, long minDurationMilli, long maxDurationMilli) {
    super("TakeBreak", priority);

    this.minIntervalMilli = minIntervalMilli;
    this.maxIntervalMilli = maxIntervalMilli;

    this.minDurationMilli = minDurationMilli;
    this.maxDurationMilli = maxDurationMilli;

    this.isFirstValidation = true;
  }

  @Override
  public boolean validate() {
    try {
      // 1. Check if is the first time the validation function is run
      if(isFirstValidation) {
        firstValidation();
        return false;
      }

      // 2. Verify if should take a break
      long timeMillis = System.currentTimeMillis();

      return timeMillis > breakStartsAt
          && timeMillis < breakEndsAt;

    } catch (Exception e) {
      Logger.error("Unable to validate.", e);
      return false;
    }
  }

  @Override
  public void end() {
    rollBreak();
  }

  private void firstValidation() {
    rollBreak();
    isFirstValidation = false;
  }

  private void rollBreak() {
    // 1.
    Random random = new Random();

    // 2. Roll when the next break will occur
    breakStartsAt = (random.nextLong() % (maxIntervalMilli - minIntervalMilli)) + minIntervalMilli + System.currentTimeMillis();

    // 3. Roll how long the break will last
    breakEndsAt = breakStartsAt + ((random.nextLong() % (maxDurationMilli - minDurationMilli)) + minDurationMilli);
  }
}
