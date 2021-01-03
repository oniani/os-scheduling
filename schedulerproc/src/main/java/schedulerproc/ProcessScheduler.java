/*
 * File:   ProcessScheduler.java
 * Author: David Oniani
 * (c) 2019
 * Created on May 15, 2019, 11:30 PM
 *
 * License:
 * The code is licensed under MIT License. Please read the LICENSE file in
 * this distribution for details regarding the licensing of this code.
 *
 * Description:
 * The package simulates four process scheduling algorithms.
 * These algorithms include FCFS, SJF, Priority, and Round-Robin.
 *
 */


package schedulerproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.*;
import java.util.Arrays;

/**
 * @author oniani
 */

/**
 * Process scheduler
 * 
 * readyQueue is a list of processes ready for execution
 * rrQuantum is the time quantum used by round-robin algorithm
 * add() and clear() are wrappers around ArrayList methods
 */
public class ProcessScheduler {
    private final ArrayList<SimpleProcess> readyQueue;
    private final int rrQuantum;

    public ProcessScheduler() {
        this.readyQueue = new ArrayList<>();
        this.rrQuantum = 4;
    }

    public void add(SimpleProcess newProcess) {
        this.readyQueue.add(newProcess);
    }

    public void clear() {
        this.readyQueue.clear();
    }

    /**
     * FCFS scheduling algorithm implementation
     * 
     * @return average waiting time for all processes
     */
    public double useFirstComeFirstServe() {
        // No need to set the first one to 0 since it is automatically set to 0
        int[] waitlist = new int[this.readyQueue.size()];

        // A simple for loop to populate the waitlist
        for (int i = 1; i < this.readyQueue.size(); i++)
            waitlist[i] = waitlist[i - 1] + this.readyQueue.get(i - 1).getNextBurst();

        // Return the average waiting time
        return (double) IntStream.of(waitlist).sum() / waitlist.length;
    }

    /**
     * SJF scheduling algorithm implementation
     * 
     * @return average waiting time for all processes
     */
    public double useShortestJobFirst() {
        int[] sorted = new int[this.readyQueue.size()];

        for (int i = 0; i < this.readyQueue.size(); i++)
            sorted[i] = this.readyQueue.get(i).getNextBurst();

        // This sorts in the ascending order, just what I need!
        Arrays.sort(sorted);

        // No need to set the first one to 0 since it is automatically set to 0
        int[] waitlist = new int[this.readyQueue.size()];

        // A simple for loop to populate the waitlist
        for (int i = 1; i < this.readyQueue.size(); i++)
            waitlist[i] = waitlist[i - 1] + sorted[i - 1];

        // Return the average waiting time
        return (double) IntStream.of(waitlist).sum() / waitlist.length;
    }

    /**
     * Priority scheduling algorithm implementation
     * 
     * @return average waiting time for all processes
     */
    public double usePriorityScheduling() {
        // Sorting by priority
        // Rxpression with an arrow is the lambda expression
        Collections.sort(this.readyQueue, (o1, o2) -> Integer.compare(o1.getPriority(), o2.getPriority()));

        // No need to set the first one to 0 since it is automatically set to 0
        int waitlist[] = new int[readyQueue.size()];

        // A simple for loop to populate the waitlist
        for (int i = 1; i < this.readyQueue.size(); i++)
            waitlist[i] = waitlist[i - 1] + this.readyQueue.get(i - 1).getNextBurst();

        // Return the average waiting time
        return (double) IntStream.of(waitlist).sum() / waitlist.length;
    }

    /**
     * Round-Robin scheduling algorithm implementation
     * 
     * @return average waiting time for all processes
     */
    public double useRoundRobin() {
        // No need to set the first one to 0 since it is automatically set to 0
        int[] waitlist = new int[this.readyQueue.size()];

        // Array for bursts
        int[] bursts = new int[readyQueue.size()];

        // Array for bursts (need another one for subtractions)
        int[] burstsCopy = new int[readyQueue.size()];

        // Populate the arrays
        for (int i = 0; i < this.readyQueue.size(); i++) {
            bursts[i] = this.readyQueue.get(i).getNextBurst();
            burstsCopy[i] = this.readyQueue.get(i).getNextBurst();
        }

        // Time variable for storing wait times
        int time = 0;

        // Boolean value that determines whether to run the loop
        boolean runRoundRobinLoop = true;

        // Round-robin loop
        while (runRoundRobinLoop) {
            // This is a promise that if no burst times are greater than 0,
            // we get out of the loop. This is used to avoid the infinite loop.
            runRoundRobinLoop = false;

            // For each process in the readyQueue
            for (int i = 0 ; i < this.readyQueue.size(); i++) {

                // If the process burst time is greater than 0
                if (burstsCopy[i] > 0) {

                    // All good, we still loop
                    runRoundRobinLoop = true;

                    // If the burst time is greater than the quantum
                    if (burstsCopy[i] > this.rrQuantum) {

                        // Time variable goes up by the size of the quantum
                        time += this.rrQuantum;

                        // And we substract the quantum from the burst time
                        // as this is the way the round-robin algorithm works.
                        burstsCopy[i] -= this.rrQuantum;
                    }

                    else {
                        // If the brust time is less than or equal to the
                        // quantum, we add the burst time to the time directly.
                        // This is due to the fact that quantum is bigger
                        // than the time burst and we cannot end up with the
                        // negative time.
                        time += burstsCopy[i];

                        // The process waiting time would be the time minus
                        // the corresponsing time burst in the bursts array.
                        // This is why we created the copy of the bursts.
                        waitlist[i] = time - bursts[i];

                        // We set the current burst time to 0 as there is nothing
                        // left at the current position.
                        burstsCopy[i] = 0;
                    }
                }
            }
        }

        // Return the average waiting time
        return (double) IntStream.of(waitlist).sum() / waitlist.length;
    }
}
