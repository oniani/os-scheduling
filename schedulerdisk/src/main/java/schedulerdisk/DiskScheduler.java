/* 
 * File:   DiskScheduler.java
 * Author: David Oniani
 * (c) 2019
 * Created on May 15, 2019, 11:10 PM
 * 
 * License:
 * The code is licensed under GNU General Public License v3.0.
 * Please read the LICENSE file in this distribution for details
 * regarding the licensing of this code.
 * 
 * Description:
 * The package simulates four disk scheduling algorithms.
 * These algorithms include FCFS, SSTF, LOOK, and CLOOK.
 * 
 */

package schedulerdisk;

import java.util.Arrays;

/**
 *
 * @author @oniani
 */
public class DiskScheduler {

    private final int cylinders;
    private int currentCylinder;
    private final int previousCylinder;
    private int totalMoves;

    public DiskScheduler(int cylinders, int currentCylinder, int previousCylinder) {
        this.cylinders = cylinders;
        this.currentCylinder = currentCylinder;
        this.previousCylinder = previousCylinder;
        this.totalMoves = 0;
    }

    public int getTotalMoves() {
        return this.totalMoves;
    }

    public void useFCFS(String requestQueue) {
        // Convert a reference string to the array of integers
        int[] intRequestList = Arrays.stream(requestQueue.split(",")).mapToInt(Integer::parseInt).toArray();

        // A variable to store the total number of moves
        int totalMovement = 0;

        /* * * * * * * * * * * * * * * *
         *           F C F S           *
         * * * * * * * * * * * * * * * */

        for (int intRequest : intRequestList) {
            totalMovement += Math.abs(this.currentCylinder - intRequest);
            this.currentCylinder = intRequest;
        }

        // Update the total number of moves
        this.totalMoves = totalMovement;
    }

    public void useSSTF(String requestQueue) {
        // Convert a reference string to the array of integers
        int[] intRequestList = Arrays.stream(requestQueue.split(",")).mapToInt(Integer::parseInt).toArray();

        // A variable to store the total number of moves
        int totalMovement = 0;

        // Array of arrays
        int[][] lookupMatrix = new int[intRequestList.length][2];

        // Populate the array of arrays
        // Not-visited = 0, visited = 1
        for (int i = 0; i < intRequestList.length; i++)
            for (int j = 0; j < 2; j++)
                lookupMatrix[i][j] = 0;

        /* * * * * * * * * * * * * * * *
         *           S S T F           *
         * * * * * * * * * * * * * * * */

        for (int i = 0; i < intRequestList.length; i++) {
            // Add the first values in the list of lists
            for (int j = 0; j < lookupMatrix.length; j++)
                lookupMatrix[j][0] = Math.abs(intRequestList[j] - this.currentCylinder);

            // Find the minimum distance in the list of lists
            // We use a simple guess-and-check pattern here
            int index = -1;
            int minimumDistance = Integer.MAX_VALUE;

            for (int k = 0; k < lookupMatrix.length; k++) {
                if (lookupMatrix[k][1] != 1 && lookupMatrix[k][0] < minimumDistance) {
                    minimumDistance = lookupMatrix[k][0];
                    index = k;
                }
            }

            // Mark as visited
            lookupMatrix[index][1] = 1;

            // Increase the total count
            totalMovement += lookupMatrix[index][0];

            // Make the value at the current index a new currentCylinder (or head)
            this.currentCylinder = intRequestList[index];
        }

        // Update the total number of moves
        this.totalMoves = totalMovement;
    }

    public void useLOOK(String requestQueue) {
        // Convert a reference string to the array of integers
        int[] intRequestList = Arrays.stream(requestQueue.split(",")).mapToInt(Integer::parseInt).toArray();

        // A variable to store the total number of moves
        int totalMovement = 0;

        // Sort the integer ArrayList of requests
        Arrays.sort(intRequestList);

        // Current index
        int index = 0;

        /* * * * * * * * * * * * * * * *
         *           L O O K           *
         * * * * * * * * * * * * * * * */

        // Find the right index
        for (int i = 0; i < intRequestList.length; i++) {
    	    if (intRequestList[i] > this.currentCylinder) {
	            index = i - 1;
		        break;
	        }
	    }

        // Do we go left?
        if (this.currentCylinder - this.previousCylinder < 0) {
            // First loop (go to the left)
            for (int i = index; i >= 0; i--) {
                totalMovement += Math.abs(this.currentCylinder - intRequestList[i]);
                this.currentCylinder = intRequestList[i];
            }

            // Second loop (go to the right)
            for (int i = index + 1; i < intRequestList.length; i++) {
                totalMovement += Math.abs(this.currentCylinder - intRequestList[i]);
                this.currentCylinder = intRequestList[i];
            }
        }

        // Or do we go right?
        else {
            // First loop (go to the right)
            for (int i = index + 1; i < intRequestList.length; i++) {
                totalMovement += Math.abs(this.currentCylinder - intRequestList[i]);
                this.currentCylinder = intRequestList[i];
            }

            // Second loop (go to the left)
            for (int i = index; i >= 0; i--) {
                totalMovement += Math.abs(this.currentCylinder - intRequestList[i]);
                this.currentCylinder = intRequestList[i];
            }
        }

        // Update the total number of moves
        this.totalMoves = totalMovement;
    }

    public void useCLOOK(String requestQueue) {
        // Convert a reference string to the array of integers
        int[] intRequestList = Arrays.stream(requestQueue.split(",")).mapToInt(Integer::parseInt).toArray();

        // A variable to store the total number of moves
        int totalMovement = 0;

        // Sort the integer ArrayList of requests
        Arrays.sort(intRequestList);

        // Current index
        int index = 0;

        /* * * * * * * * * * * * * * * * *
         *           C L O O K           *
         * * * * * * * * * * * * * * * * */

        // Find the right index
        for (int i = 0; i < intRequestList.length; i++) {
            if (intRequestList[i] > this.currentCylinder) {
                index = i;
                break;
            }
        }

        // First loop (go to the right)
        for (int i = index; i < intRequestList.length; i++) {
            totalMovement += Math.abs(this.currentCylinder - intRequestList[i]);
            this.currentCylinder = intRequestList[i];
        }

        // Second loop (go to the right)
        for (int i = 0; i < index; i++) {
            totalMovement += Math.abs(this.currentCylinder - intRequestList[i]);
            this.currentCylinder = intRequestList[i];
        }

        // Update the total number of moves
        this.totalMoves = totalMovement;
    }

}
