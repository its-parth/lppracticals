import java.util.*;
public class LeastRecentlyUsed {
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
lru(referenceString, frames);
 sc.close();
 }

 // ------------------ LRU ------------------
 static void lru(int[] pages, int frames) {
 LinkedHashSet<Integer> memory = new
LinkedHashSet<>();
 int pageFaults = 0;
 System.out.println("\nLRU Page Replacement\n");
 for (int page : pages) {
 if (!memory.contains(page)) {
 if (memory.size() == frames) {
 int first = memory.iterator().next();
 memory.remove(first);
 }
 pageFaults++;
 memory.add(page);
 System.out.println("Page " + page + " -> Fault\t" +
memory);
 } else {
 memory.remove(page);
 memory.add(page);
 System.out.println("Page " + page + " -> Hit\t" +
memory);
 }
 }
 System.out.println("\nTotal Page Faults: " +
pageFaults);
 }
 
}