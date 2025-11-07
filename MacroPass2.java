import java.util.*;
class MNTEntry {
 String name;
 int mdtIndex;
 MNTEntry(String n, int i){ name=n; mdtIndex=i; }
}
public class MacroPass2 {
 static List<String> MDT = new ArrayList<>(); // Macro Definition Table
 static List<MNTEntry> MNT = new ArrayList<>(); // Macro Name Table
 static Map<String, List<String>> ALA = new HashMap<>(); // Argument List Array
 // ---- Expand source program using MNT, MDT, ALA ----
 static void expand(List<String> code) {
 for (String line : code) {
 String[] parts = line.split("\\s+");
 String op = parts[0];
 Optional<MNTEntry> me = MNT.stream().filter(m -> m.name.equals(op)).findFirst();
 if (me.isPresent()) {
 List<String> args = new ArrayList<>();
 if (parts.length > 1) args = Arrays.asList(parts[1].split(","));
 int idx = me.get().mdtIndex;
 for (int j = idx; j < MDT.size(); j++) {
 String body = MDT.get(j);
 if (body.equals("MEND")) break;
 String exp = body;
 for (int k = 0; k < args.size(); k++) {
 exp = exp.replace("#" + k, args.get(k));
 }
 System.out.println(exp);
 }
 } else {
 // Print normal instructions
 if (!line.equalsIgnoreCase("MACRO") && !line.equalsIgnoreCase("MEND"))
 System.out.println(line);
 }
 }
 }
 public static void main(String[] args) {
 Scanner sc = new Scanner(System.in);
 // ---- Input Tables (output of Pass-1) ----
 System.out.println("Enter number of MNT entries:");
 int mntCount = Integer.parseInt(sc.nextLine());
 for (int i = 0; i < mntCount; i++) {
 System.out.println("Enter MNT entry (name mdtIndex):");
 String[] p = sc.nextLine().split("\\s+");
 MNT.add(new MNTEntry(p[0], Integer.parseInt(p[1])));
 }
 System.out.println("Enter number of MDT entries:");
 int mdtCount = Integer.parseInt(sc.nextLine());
 for (int i = 0; i < mdtCount; i++) {
 MDT.add(sc.nextLine().trim());
 }
 System.out.println("Enter number of ALA entries:");
 int alaCount = Integer.parseInt(sc.nextLine());
 for (int i = 0; i < alaCount; i++) {
 System.out.println("Enter macro name and its params (comma-separated):");
 String[] p = sc.nextLine().split("\\s+");
 String name = p[0];
 List<String> params = Arrays.asList(p[1].split(","));
 ALA.put(name, params);
 }
 // ---- Input source program ----
 System.out.println("Enter source program (END to stop):");
 List<String> code = new ArrayList<>();
 while (true) {
 String line = sc.nextLine().trim();
 code.add(line);
 if (line.equalsIgnoreCase("END")) break;
 }
 // ---- Run Pass-2 ----
 System.out.println("\n--- Expanded Program (Pass-2 Output) ---");
 expand(code);
 }
}

// Absolutely 👍
// Let’s go step by step and understand the entire logic of your Assignment No. 4 — Macro Pass 2 code in simple language.

// 🧠 Goal of Pass 2

// After Pass 1, we already have 3 tables:

// Table	Purpose
// MNT (Macro Name Table)	Stores each macro’s name and where its body starts in MDT
// MDT (Macro Definition Table)	Stores the macro body with positional parameters (#0, #1, …)
// ALA (Argument List Array)	Stores formal parameters (like &A, &B) of each macro

// Now in Pass 2, we will use these tables to:

// Expand macro calls in the given assembly source program.

// That means wherever the macro name appears, we replace it with the actual statements from MDT, substituting arguments properly.

// 🧩 Step-by-Step Code Explanation
// 1️⃣ MNTEntry Class
// class MNTEntry {
//     String name;
//     int mdtIndex;
//     MNTEntry(String n, int i) { name = n; mdtIndex = i; }
// }


// A small class to represent one entry of MNT.

// Each macro has:

// name → macro name (like INCR)

// mdtIndex → position in the MDT where its definition starts.

// 2️⃣ Main Data Structures
// static List<String> MDT = new ArrayList<>();
// static List<MNTEntry> MNT = new ArrayList<>();
// static Map<String, List<String>> ALA = new HashMap<>();

// Structure	Description
// MDT	Stores macro definitions (with #0, #1 placeholders).
// MNT	Stores macro names and their corresponding MDT index.
// ALA	Maps macro name → list of formal parameters (e.g., INCR → [&A,&B]).
// 3️⃣ expand() Function

// This is the main logic that expands macros.

// static void expand(List<String> code) {
//     for (String line : code) {
//         String[] parts = line.split("\\s+");
//         String op = parts[0];


// Loops through each line of the input program.

// Splits the line by spaces to get the operation name (like INCR, MOV, etc.).

// 4️⃣ Check if the line is a macro call
// Optional<MNTEntry> me = MNT.stream()
//                            .filter(m -> m.name.equals(op))
//                            .findFirst();


// Looks through the MNT to see if the operation (e.g., INCR) is a macro name.

// If found → it’s a macro call.

// If not → it’s a normal instruction.

// 5️⃣ If It’s a Macro Call
// if (me.isPresent()) {
//     List<String> args = new ArrayList<>();
//     if (parts.length > 1) args = Arrays.asList(parts[1].split(","));
//     int idx = me.get().mdtIndex;


// Extracts the actual arguments from the macro call.

// Example: INCR A,B → args = [A, B]

// Gets the starting index of this macro’s body from MDT.

// 6️⃣ Read the Macro Body and Replace Parameters
// for (int j = idx; j < MDT.size(); j++) {
//     String body = MDT.get(j);
//     if (body.equals("MEND")) break;

//     String exp = body;
//     for (int k = 0; k < args.size(); k++) {
//         exp = exp.replace("#" + k, args.get(k));
//     }
//     System.out.println(exp);
// }


// Reads macro lines from MDT until MEND appears.

// Replaces each positional parameter:

// #0 → 1st argument

// #1 → 2nd argument

// and so on.

// Prints the expanded line.

// 🧩 Example:
// MDT entry: ADD #0, #1
// Call: INCR A,B
// ➡️ After replacement: ADD A, B

// 7️⃣ If It’s NOT a Macro
// else {
//     if (!line.equalsIgnoreCase("MACRO") && !line.equalsIgnoreCase("MEND"))
//         System.out.println(line);
// }


// Simply prints the instruction as-is (e.g., MOV C, D or END).

// 8️⃣ main() Method

// This part just takes input for:

// MNT

// MDT

// ALA

// Source program

// and then calls expand().

// 9️⃣ Program Flow
// User Inputs → Tables (MNT, MDT, ALA)
//           ↓
// User Inputs → Source Program
//           ↓
// Pass-2 expands macros using tables
//           ↓
// Prints final expanded code

// 🧾 Example Recap

// Input Tables:

// MNT:
// INCR 0
// SQUARE 2

// MDT:
// ADD #0, #1
// MEND
// MUL #0, #0
// MEND

// ALA:
// INCR &A,&B
// SQUARE &X


// Source Program:

// START 100
// INCR A,B
// SQUARE NUM
// MOV C,D
// END


// Output:

// --- Expanded Program (Pass-2 Output) ---
// START 100
// ADD A, B
// MUL NUM, NUM
// MOV C, D
// END


// Perfect 👍
// Let’s go step-by-step and understand the input and output for this Macro Pass 2 program.

// 🧠 Purpose of the Program

// This is Pass 2 of the Macro Processor, which expands macro calls using the tables created in Pass 1:

// MNT → Macro Name Table

// MDT → Macro Definition Table

// ALA → Argument List Array

// In short:

// Pass 1 builds tables.
// Pass 2 uses those tables to expand macros in the source program.

// 🧩 Step 1: Tables (Input to this Program)

// These are the same tables you got as output from Assignment No. 3 (Pass 1).

// 🔹 MNT (Macro Name Table)
// Index	Name	MDT Index
// 0	INCR	0
// 1	SQUARE	2
// 🔹 MDT (Macro Definition Table)
// Index	Line
// 0	ADD #0, #1
// 1	MEND
// 2	MUL #0, #0
// 3	MEND
// 🔹 ALA (Argument List Array)
// Macro	Arguments
// INCR	&A, &B
// SQUARE	&X
// 🧮 Step 2: Input to Program (User Enters)

// When you run this program, enter inputs exactly like this 👇

// Enter number of MNT entries:
// 2
// Enter MNT entry (name mdtIndex):
// INCR 0
// Enter MNT entry (name mdtIndex):
// SQUARE 2
// Enter number of MDT entries:
// 4
// ADD #0, #1
// MEND
// MUL #0, #0
// MEND
// Enter number of ALA entries:
// 2
// Enter macro name and its params (comma-separated):
// INCR &A,&B
// Enter macro name and its params (comma-separated):
// SQUARE &X
// Enter source program (END to stop):
// START 100
// INCR A,B
// SQUARE NUM
// MOV C,D
// END

// 🧾 Step 3: Output

// After entering all of that, the program expands the macros using MDT and prints the expanded source code:

// --- Expanded Program (Pass-2 Output) ---
// START 100
// ADD A, B
// MUL NUM, NUM
// MOV C, D
// END

// 🧠 Step 4: Explanation
// 1️⃣ Macro Call: INCR A,B

// Macro name: INCR

// Arguments: A, B

// Definition from MDT:

// ADD #0, #1
// MEND


// Replace #0 → A, #1 → B
// ✅ Output → ADD A, B

// 2️⃣ Macro Call: SQUARE NUM

// Macro name: SQUARE

// Argument: NUM

// Definition from MDT:

// MUL #0, #0
// MEND


// Replace #0 → NUM
// ✅ Output → MUL NUM, NUM

// 3️⃣ Non-macro lines:

// START 100, MOV C, D, and END are printed as-is.

// ✅ Final Output Summary
// Original Line	Expanded Output
// START 100	START 100
// INCR A,B	ADD A, B
// SQUARE NUM	MUL NUM, NUM
// MOV C,D	MOV C, D
// END	END

// Would you like me to show Pass 1 + Pass 2 combined together (so you can run both in one program and see full macro processing automatically)?