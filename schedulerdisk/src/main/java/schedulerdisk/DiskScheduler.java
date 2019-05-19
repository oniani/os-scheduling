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
 * Note that the current implementations could be heavily
 * optimized, but they are intentionally kept this way for
 * the sake of making them "true" simulations.
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

        /* * * * * * * * * * * * * * * *
         *           F C F S           *
         * * * * * * * * * * * * * * * */

        for (int intRequest : intRequestList) {
            // Update the total number of moves
            this.totalMoves += Math.abs(this.currentCylinder - intRequest);
            this.currentCylinder = intRequest;
        }
    }

    public void useSSTF(String requestQueue) {
        // Convert a reference string to the array of integers
        int[] intRequestList = Arrays.stream(requestQueue.split(",")).mapToInt(Integer::parseInt).toArray();

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

            // Find the minimum distance in the matrix
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

            // Update the total number of moves
            this.totalMoves += lookupMatrix[index][0];

            // Make the value at the current index a new currentCylinder (or head)
            this.currentCylinder = intRequestList[index];
        }
    }

    public void useLOOK(String requestQueue) {
        // Convert a reference string to the array of integers
        int[] intRequestList = Arrays.stream(requestQueue.split(",")).mapToInt(Integer::parseInt).toArray();

        // Sort the integer array of requests
        Arrays.sort(intRequestList);

        // Current index
        int index = -1;

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
            // Note that since the intRequestList is sorted, the two loops
            // below could be rewritten as two simple statements (or could be
            // taken to extreme and be written as one bigger statement), but that
            // way, the purpose of the project (simulations) will be disregarded.
            // Therefore, we keep the code below as is, without optimization of any kind.

            // First loop (go to the left)
            for (int i = index; i >= 0; i--) {
                // Update the total number of moves
                this.totalMoves += Math.abs(this.currentCylinder - intRequestList[i]);
                this.currentCylinder = intRequestList[i];
            }

            // Second loop (go to the right)
            for (int i = index + 1; i < intRequestList.length; i++) {
                // Update the total number of moves
                this.totalMoves += Math.abs(this.currentCylinder - intRequestList[i]);
                this.currentCylinder = intRequestList[i];
            }
        }

        // Or do we go right?
        else {
            // Note that since the intRequestList is sorted, the two loops
            // below could be rewritten as two simple statements (or could be
            // taken to extreme and be written as one bigger statement), but that
            // way, the purpose of the project (simulations) will be disregarded.
            // Therefore, we keep the code below as is, without optimization of any kind.

            // First loop (go to the right)
            for (int i = index + 1; i < intRequestList.length; i++) {
                // Update the total number of moves
                this.totalMoves += Math.abs(this.currentCylinder - intRequestList[i]);
                this.currentCylinder = intRequestList[i];
            }

            // Second loop (go to the left)
            for (int i = index; i >= 0; i--) {
                // Update the total number of moves
                this.totalMoves += Math.abs(this.currentCylinder - intRequestList[i]);
                this.currentCylinder = intRequestList[i];
            }
        }
    }

    public void useCLOOK(String requestQueue) {
        // Convert a reference string to the array of integers
        int[] intRequestList = Arrays.stream(requestQueue.split(",")).mapToInt(Integer::parseInt).toArray();

        // Sort the integer array of requests
        Arrays.sort(intRequestList);

        // Current index
        int index = -1;

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

        // Note that since the intRequestList is sorted, the two loops
        // below could be rewritten as three simple statements (or could be
        // taken to extreme and be written as one bigger statement), but that
        // way, the purpose of the project (simulations) will be disregarded.
        // Therefore, we keep the code below as is, without optimization of any kind.

        // First loop (go to the right)
        for (int i = index; i < intRequestList.length; i++) {
            // Update the total number of moves
            this.totalMoves += Math.abs(this.currentCylinder - intRequestList[i]);
            this.currentCylinder = intRequestList[i];
        }

        // Second loop (go to the right)
        for (int i = 0; i < index; i++) {
            // Update the total number of moves
            this.totalMoves += Math.abs(this.currentCylinder - intRequestList[i]);
            this.currentCylinder = intRequestList[i];
        }
    }

}
