package org.ooad_dws4;

/**
 *
 */
public class Timer {


    /**
     *
     */
    private long deadline;

    /**
     * Default constructor
     */
    public Timer() {
        this.deadline = 600000L;
    }

    /**
     * return deadline
     */
    public long getDeadlineData() {
        return this.deadline;
    }

    public void runTimer() {
        deadline = deadline - Clock.timeUnit;
        deadline = deadline < 0 ? 0 : deadline;
    }

    /**
     * setDeadline
     */
    public void setDeadlineData(long deadline) {
        this.deadline = deadline;
    }

    /**
     * reset deadline
     */
    public void reset() {
        this.deadline = 0;
    }


}