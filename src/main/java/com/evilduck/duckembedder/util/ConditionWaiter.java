package com.evilduck.duckembedder.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.Callable;

@Slf4j
public class ConditionWaiter {

    private final Duration timeout;
    private final Duration pollInterval;

    public ConditionWaiter(final Duration timeout,
                           final Duration pollInterval) {
        this.timeout = timeout;
        this.pollInterval = pollInterval;
    }

    public boolean waitUntilTrue(final Callable<Boolean> testFunction) {
        final long startTime = System.currentTimeMillis();
        try {
            final long currentExecutionTime = System.currentTimeMillis() - startTime;
            final long remainingTime = timeout.toMillis() - currentExecutionTime;
            if (testFunction.call()) {
                // Condition has been met
                return true;
            } else if (remainingTime <= 0) {
                // Condition was not met in time
                return false;
            } else {
                // Condition is still not met, wait for poll time
                Thread.sleep(pollInterval.toMillis());
            }
        } catch (InterruptedException e) {
            log.error("Condition waiter was interrupted", e);
        } catch (Exception e) {
            log.error("Something went wrong evaluating condition", e);
        }
        return false;
    }

}
