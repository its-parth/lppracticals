import java.util.*;

class Process {
    int pid; // Process ID
    int bt; // Burst Time
    int at; // Arrival Time
    int priority; // Priority (for priority scheduling)
    int ct, wt, tat; // Completion, Waiting and Turnaround Times
    int remainingTime; // For preemptive algorithms

    Process(int pid, int at, int bt, int priority) {
        this.pid = pid;
        this.at = at;
        this.bt = bt;
        this.priority = priority;
        this.remainingTime = bt;
    }
}

public class CPUScheduling {

    // ---------- FCFS (Non-Preemptive) ----------
    static void FCFS(List<Process> plist) {
        plist.sort(Comparator.comparingInt(p -> p.at));

        System.out.println("\n--- FCFS Scheduling ---");
        int time = 0;
        for (Process p : plist) {
            if (time < p.at)
                time = p.at;
            p.ct = time + p.bt;
            p.tat = p.ct - p.at;
            p.wt = p.tat - p.bt;
            time = p.ct;
        }
        printTable(plist);
    }

    // ---------- SJF (Preemptive) ----------
    static void SJF_Preemptive(List<Process> plist) {
        System.out.println("\n--- SJF (Preemptive) Scheduling ---");
        int completed = 0, time = 0, n = plist.size();

        while (completed < n) {
            Process shortest = null;
            int min = Integer.MAX_VALUE;

            for (Process p : plist) {
                if (p.at <= time && p.remainingTime > 0 && p.remainingTime < min) {
                    min = p.remainingTime;
                    shortest = p;
                }
            }

            if (shortest == null) {
                time++;
                continue;
            }

            shortest.remainingTime--;
            time++;

            if (shortest.remainingTime == 0) {
                completed++;
                shortest.ct = time;
                shortest.tat = shortest.ct - shortest.at;
                shortest.wt = shortest.tat - shortest.bt;
            }
        }
        printTable(plist);
    }

    // ---------- Priority (Non-Preemptive) ----------
    static void Priority_NonPreemptive(List<Process> plist) {
        System.out.println("\n--- Priority (Non-Preemptive) Scheduling ---");
        int time = 0;
        int completedCount = 0;
        int n = plist.size();

        plist.sort(Comparator.comparingInt(p -> p.at));

        while (completedCount < n) {
            Process highest = null;

            // find process with highest priority (lowest number)
            for (Process p : plist) {
                if (p.ct == 0 && p.at <= time) { // not completed yet
                    if (highest == null || p.priority < highest.priority) {
                        highest = p;
                    }
                }
            }

            // if no process has arrived, increment time
            if (highest == null) {
                time++;
                continue;
            }

            // execute the selected process completely
            time = Math.max(time, highest.at);
            time += highest.bt;
            highest.ct = time;
            highest.tat = highest.ct - highest.at;
            highest.wt = highest.tat - highest.bt;
            completedCount++;
        }

        printTable(plist);
    }
    // ---------- Round Robin (Preemptive) ----------
    static void RoundRobin(List<Process> plist, int quantum) {
        System.out.println("\n--- Round Robin Scheduling ---");

        Queue<Process> q = new LinkedList<>();
        plist.sort(Comparator.comparingInt(p -> p.at));
        int time = 0;
        int completed = 0;
        int n = plist.size();
        int index = 0; // to track next process that should enter the queue

        // Add first process that has arrived
        if (plist.get(0).at <= time) {
            q.add(plist.get(0));
            index = 1;
        } else {
            time = plist.get(0).at;
            q.add(plist.get(0));
            index = 1;
        }

        while (completed < n) {
            if (q.isEmpty()) {
                // CPU idle: move time forward to next arriving process
                time = plist.get(index).at;
                q.add(plist.get(index));
                index++;
            }

            Process p = q.poll();

            if (p.remainingTime > quantum) {
                time = Math.max(time, p.at);
                time += quantum;
                p.remainingTime -= quantum;
            } else {
                time = Math.max(time, p.at);
                time += p.remainingTime;
                p.remainingTime = 0;
                p.ct = time;
                p.tat = p.ct - p.at;
                p.wt = p.tat - p.bt;
                completed++;
            }

            // Add all processes that arrived till now
            while (index < n && plist.get(index).at <= time) {
                q.add(plist.get(index));
                index++;
            }

            // If current process not done, requeue it
            if (p.remainingTime > 0)
                q.add(p);
        }

        printTable(plist);
    }

    // ---------- Helper Function to Print Results ----------
    static void printTable(List<Process> plist) {
        System.out.println("PID\tAT\tBT\tPri\tCT\tTAT\tWT");
        for (Process p : plist) {
            System.out.println(p.pid + "\t" + p.at + "\t" + p.bt + "\t" + p.priority + "\t" +
                    p.ct + "\t" + p.tat + "\t" + p.wt);
        }
    }

    // ---------- MAIN FUNCTION ----------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Process> plist = new ArrayList<>();

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.print("\nEnter Arrival Time, Burst Time and Priority for Process " + (i + 1) + ": ");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            int pr = sc.nextInt();
            Process p = new Process(i + 1, at, bt, pr);
            plist.add(p);
        }

        // Make copies for each algorithm (to keep original data unchanged)
        List<Process> fcfs = cloneList(plist);
        List<Process> sjf = cloneList(plist);
        List<Process> priority = cloneList(plist);
        List<Process> rr = cloneList(plist);

        FCFS(fcfs);
        SJF_Preemptive(sjf);
        Priority_NonPreemptive(priority);

        System.out.print("\nEnter time quantum for Round Robin: ");
        int q = sc.nextInt();
        RoundRobin(rr, q);
        sc.close();
    }

    // Clone list so each algorithm works on fresh data
    static List<Process> cloneList(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original)
            copy.add(new Process(p.pid, p.at, p.bt, p.priority));
        return copy;
    }
}
