import java.util.*;
public class FIFOPageReplacement {
 public static void main(String[] args) {
 Scanner sc = new Scanner(System.in);
 System.out.print("Enter number of frames: ");
 int frames = sc.nextInt();
 System.out.print("Enter number of pages in reference string: ");
 int n = sc.nextInt();
 int[] referenceString = new int[n];
 System.out.print("Enter the reference string: ");
 for (int i = 0; i < n; i++) {
 referenceString[i] = sc.nextInt();
 }
fifo(referenceString, frames);
 sc.close();
 }
 // ------------------ FIFO ------------------
 static void fifo(int[] pages, int frames) {
 Queue<Integer> memory = new LinkedList<>();
 Set<Integer> set = new HashSet<>();
 int pageFaults = 0;
 System.out.println("\nFIFO Page Replacement\n");
 for (int page : pages) {
 if (!set.contains(page)) {
 if (set.size() == frames) {
 int removed = memory.poll();
 set.remove(removed);
 }
 memory.offer(page);
 set.add(page);
 pageFaults++;
 System.out.println("Page " + page + " -> Fault\t" +
memory);
 } else {
 System.out.println("Page " + page + " -> Hit\t" +
memory);
 }
 }
 System.out.println("\nTotal Page Faults: " +
pageFaults);
 }

}