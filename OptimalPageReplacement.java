import java.util.*;
public class OptimalPageReplacement {
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
optimal(referenceString, frames);
 sc.close();
 }
 // ------------------ Optimal ------------------
 static void optimal(int[] pages, int frames) {
 List<Integer> memory = new ArrayList<>();
 int pageFaults = 0;
 System.out.println("\nOptimal Page Replacement\n");
 for (int i = 0; i < pages.length; i++) {
 int page = pages[i];
 if (!memory.contains(page)) {
 if (memory.size() == frames) {
 int indexToReplace = -1;
 int farthest = i + 1;
 for (int j = 0; j < memory.size(); j++) {
 int nextIndex = findNextIndex(pages, i + 1,
memory.get(j));
 if (nextIndex == -1) {
 indexToReplace = j;
 break;
 } else if (nextIndex > farthest) {
 farthest = nextIndex;
 indexToReplace = j;
 }
 }
 if (indexToReplace == -1)
 indexToReplace = 0;
 memory.set(indexToReplace, page);
 } else {
 memory.add(page);
 }
 pageFaults++;
 System.out.println("Page " + page + " -> Fault\t" + memory);
 } else {
System.out.println("Page " + page + " -> Hit\t" + memory);
 }
 }
 System.out.println("\nTotal Page Faults: " +
pageFaults);
 }
 static int findNextIndex(int[] pages, int start, int value) {
 for (int i = start; i < pages.length; i++) {
 if (pages[i] == value)
 return i;
 }
 return -1;
 }

}