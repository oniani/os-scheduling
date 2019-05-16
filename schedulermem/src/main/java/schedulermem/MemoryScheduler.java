/*
 * File:   MemoryScheduler.java
 * Author: David Oniani
 * (c) 2019
 * Created on May 15, 2019, 11:20 PM
 *
 * License:
 * The code is licensed under GNU General Public License v3.0.
 * Please read the LICENSE file in this distribution for details
 * regarding the licensing of this code.
 *
 * Description:
 * The package simulates three memory scheduling algorithms.
 * These algorithms include FIFO, OPT, LRU.
 *
 */

package schedulermem;

import java.util.Queue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author oniani
 */
public class MemoryScheduler {

    private int pageFaultCount;
    private int frames;

    public MemoryScheduler(int frames) {
        this.pageFaultCount = 0;
        this.frames = frames;
    }

    public int getPageFaultCount() {
        return this.pageFaultCount;
    }

    public void useFIFO(String referenceString) {
        // Convert a reference string to the array of integers
        int[] intReferenceList = Arrays.stream(referenceString.split(",")).mapToInt(Integer::parseInt).toArray();

        // A set to look up the current pages
        HashSet<Integer> currentPages = new HashSet<>(this.frames);

        // The queue to store the pages
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < intReferenceList.length; i++) {
            // Check if the size of the set is less than the number
            // of the frames. If it is not, then there is no place.
            // Besides, we also check if the set contains the page
            // that has to be added. If it does not, we proceed
            // with the algorithm and otherwise, we don't do anything.
            if (currentPages.size() < this.frames && !currentPages.contains(intReferenceList[i])) {
                    // Since the page is not present in the set
                    // we insert it in the set.
                    currentPages.add(intReferenceList[i]);
                    
                    // We automatically have a page fault and
                    // therefore, we increment page faults' count.
                    this.pageFaultCount++;

                    // Push the current page into the queue
                    queue.add(intReferenceList[i]);
            }

            else {
                // If the size of the set is not less than the number
                // of the frames, we proceed with the algorithm if
                // and only if the set does not contain the current
                // page.
                if (!currentPages.contains(intReferenceList[i])) {
                    // Get the first page of the queue.
                    int page = queue.peek();

                    // Pop the first page from the queue.
                    queue.poll();

                    // Remove the previous first page from the queue.
                    currentPages.remove(page);

                    // Add the current page to the set.
                    currentPages.add(intReferenceList[i]);

                    // Push the current page onto the queue.
                    queue.add(intReferenceList[i]);

                    // Increment page faults' count.
                    this.pageFaultCount++;
                } 
            } 
        }
    }

    public void useOPT(String referenceString) {
        // Convert a reference string to the array of integers
        int[] intReferenceList = Arrays.stream(referenceString.split(",")).mapToInt(Integer::parseInt).toArray();

	    // An array of frames
        ArrayList<Integer> framesArray = new ArrayList<Integer>();

        // Count the number of hits
        // If the page is found in the frame, we have a hit
        int hitsNum = 0;

        // Go through the reference list
	    for (int i = 0; i < intReferenceList.length; i++) {
            // If the page is in the array
            if (framesArray.contains(intReferenceList[i]))
                // We have a hit and therefore, we increment the counter
                hitsNum++;

            else {
                // Check if the size of the array is less than the number
                // of the frames.
                if (framesArray.size() < this.frames)
                    framesArray.add(intReferenceList[i]);

                // If the size of the array is less than the number of the frames,
                // we predict the page to be replaced.
                else {
                    // Predicted index
                    int predictedIndex = -1;
                    // Farthest index for the given position
                    int farthest = i + 1;

                    // Go through the framesArray
                    for (int k = 0; k < framesArray.size(); k++) {
                        int j;

                        // We start at i + 1 position
                        for (j = i + 1; j < intReferenceList.length; j++) {
                            if (framesArray.get(k) == intReferenceList[j]) {
                                if (j > farthest) {
                                    farthest = j;
                                    predictedIndex = k;
                                }
                                break;
                            }
                        }

                        // If we reached the end of the loop, set
                        // the predictedIndex equal to k
                        if (j == intReferenceList.length) {
                            predictedIndex = k;
                            break;
                        }
                    }

                    // If the predicted index is still -1, set it to 0
                    if (predictedIndex == -1)
                        predictedIndex = 0;

                    // Put a page in the predictedIndex index.
                    framesArray.set(predictedIndex, intReferenceList[i]);
                }
            }
        }

        // Change the count of page faults
        this.pageFaultCount = intReferenceList.length - hitsNum;
    }

    public void useLRU(String referenceString) {
        // Convert a reference string to the array of integers
        int[] intReferenceList = Arrays.stream(referenceString.split(",")).mapToInt(Integer::parseInt).toArray();

        // A set for the current pages
        HashSet<Integer> currentPages = new HashSet<>(this.frames);

        // To store least recently used indexes
        // of pages. It is a page, index pair.
        HashMap<Integer, Integer> page_index_map = new HashMap<>();

        for (int i = 0; i < intReferenceList.length; i++) {
            // Check if the set can hold more pages
            if (currentPages.size() < this.frames) {
                // Insert the page if it is not present in the set
                if (!currentPages.contains(intReferenceList[i])) {
                    currentPages.add(intReferenceList[i]);

                    // Increment page faults' count
                    this.pageFaultCount++;
                }

                // For every page, we need to store
                // the recently used index.
                page_index_map.put(intReferenceList[i], i);
            }

            // If the size of the set is greater than or
            // equal to the number of frames, we do
            // proceed with the LRU algorithm and replace
            // the least recently used page with the
            // current page.
            else {
                // Check whether the current page is already
                // in the set or not.
                if (!currentPages.contains(intReferenceList[i])) {
                    int leastRecentlyUsed = Integer.MAX_VALUE;
                    int page = Integer.MIN_VALUE;

                    for (Integer temp : currentPages) {
                        if (page_index_map.get(temp) < leastRecentlyUsed) {
                            leastRecentlyUsed = page_index_map.get(temp);
                            page = temp;
                        }
                    }

                    // Remove the page
                    currentPages.remove(page);

                    // Add the current page to the set
                    currentPages.add(intReferenceList[i]);

                    // Increment page faults' count
                    this.pageFaultCount++;
                }

                // Update the current page index
                page_index_map.put(intReferenceList[i], i);
            } 
        } 
    }

}
