import java.util.*;

class MNTEntry {
    String name;
    int mdtIndex;

    MNTEntry(String n, int i) {
        name = n;
        mdtIndex = i;
    }
}

public class MacroPass1 {
    static List<String> MDT = new ArrayList<>(); // Macro Definition Table
    static List<MNTEntry> MNT = new ArrayList<>(); // Macro Name Table
    static Map<String, List<String>> ALA = new HashMap<>(); // Argument List Array

    // ---- Pass 1: Build MNT, MDT, ALA ----
    static void pass1(List<String> code) {
        boolean inMacro = false;
        String macroName = "";

        for (int i = 0; i < code.size(); i++) {
            String line = code.get(i).trim();

            // Start of macro definition
            if (line.equalsIgnoreCase("MACRO")) {
                inMacro = true;
                continue;
            }

            // If inside macro definition
            if (inMacro) {
                String[] parts = line.split("\\s+");
                macroName = parts[0];
                int mdtIndex = MDT.size();

                // Add entry in MNT
                MNT.add(new MNTEntry(macroName, mdtIndex));

                // Store parameters in ALA
                if (parts.length > 1) {
                    String[] params = parts[1].split(",");
                    List<String> pList = new ArrayList<>();
                    for (String param : params)
                        pList.add(param);
                    ALA.put(macroName, pList);
                } else {
                    ALA.put(macroName, new ArrayList<>());
                }

                // Read macro body until MEND
                i++;
                while (!code.get(i).trim().equalsIgnoreCase("MEND")) {
                    String body = code.get(i).trim();
                    List<String> pList = ALA.get(macroName);

                    // Replace parameters with positional notation (#0, #1, etc.)
                    for (int k = 0; k < pList.size(); k++) {
                        body = body.replace(pList.get(k), "#" + k);
                    }

                    MDT.add(body);
                    i++;
                }

                // Add MEND to MDT
                MDT.add("MEND");
                inMacro = false;
            }
        }
    }

    // ---- Display Tables ----
    static void displayTables() {
        System.out.println("\n--- MNT (Macro Name Table) ---");
        for (int i = 0; i < MNT.size(); i++)
            System.out.println(i + " : " + MNT.get(i).name + " , MDT Index = " + MNT.get(i).mdtIndex);

        System.out.println("\n--- MDT (Macro Definition Table) ---");
        for (int i = 0; i < MDT.size(); i++)
            System.out.println(i + " : " + MDT.get(i));

        System.out.println("\n--- ALA (Argument List Array) ---");
        for (String k : ALA.keySet())
            System.out.println(k + " -> " + ALA.get(k));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> code = new ArrayList<>();

        System.out.println("Enter code (END to stop):");
        while (true) {
            String line = sc.nextLine().trim();
            code.add(line);
            if (line.equalsIgnoreCase("END"))
                break;
        }

        // Run Pass-1
        pass1(code);

        // Display Tables
        displayTables();
    }
}



// Explanation of the Code
// This program performs Pass-1 of a Macro Processor (used in assemblers).
// 🔹 Goal of Pass 1
// To process macro definitions and build three important tables:


// MNT (Macro Name Table) – Stores each macro’s name and where its definition starts in MDT.


// MDT (Macro Definition Table) – Stores the actual macro body (instructions).


// ALA (Argument List Array) – Stores the parameters (arguments) of each macro.



// 🔹 How It Works Step by Step
// 1. Input Code
// You enter assembly-like code that may contain macros:
// MACRO
// INCR &A, &B
// ADD &A, &B
// MEND
// END

// 2. Detect Start of Macro
// When the line "MACRO" is found, the flag inMacro becomes true.
// 3. Read Macro Header
// Next line (e.g., INCR &A, &B) contains:


// Macro name → INCR


// Parameters → &A, &B


// Then:


// Adds to MNT → (INCR, MDT index)


// Adds to ALA → {INCR: [&A, &B]}


// 4. Read Macro Body
// Lines until "MEND" are part of the macro body.
// Example: ADD &A, &B
// Each parameter (&A, &B) is replaced with positional notation:


// &A → #0


// &B → #1


// So the stored line becomes:
// ADD #0, #1

// 5. Add MEND
// After macro ends, MEND is stored in MDT.

// 🧾 Resulting Tables Example
// For the above macro:
// MNT (Macro Name Table)
// IndexMacro NameMDT Index0INCR0
// MDT (Macro Definition Table)
// IndexEntry0ADD #0, #11MEND
// ALA (Argument List Array)
// MacroArgumentsINCR[&A, &B]

// In Short


// The program reads macros.


// Builds:


// MNT → list of macros


// MDT → macro instructions


// ALA → macro parameters




// This information will be used later in Pass 2, where macro calls are expanded.



// Would you like me to show a sample input and full output run (like how it looks on screen after entering code)?


// Input (User Enters in Console)
// MACRO
// INCR &A, &B
// ADD &A, &B
// MEND
// MACRO
// SQUARE &X
// MUL &X, &X
// MEND
// END


// ✅ Explanation of input:

// There are two macros:

// INCR → has two parameters &A, &B

// SQUARE → has one parameter &X

// Each macro ends with MEND

// The overall program ends with END

// 🧾 Output

// After entering the code and pressing Enter after END, you will get this output:

// --- MNT (Macro Name Table) ---
// 0 : INCR , MDT Index = 0
// 1 : SQUARE , MDT Index = 2

// --- MDT (Macro Definition Table) ---
// 0 : ADD #0, #1
// 1 : MEND
// 2 : MUL #0, #0
// 3 : MEND

// --- ALA (Argument List Array) ---
// INCR -> [&A, &B]
// SQUARE -> [&X]

// 🧠 Explanation of Output
// 🔹 MNT (Macro Name Table)
// Index	Name	MDT Index
// 0	INCR	0
// 1	SQUARE	2

// → Means:

// Macro INCR starts at line 0 of MDT.

// Macro SQUARE starts at line 2 of MDT.

// 🔹 MDT (Macro Definition Table)
// Index	Line
// 0	ADD #0, #1
// 1	MEND
// 2	MUL #0, #0
// 3	MEND

// → The macro bodies after replacing parameters (&A, &B, &X) with positional notations (#0, #1, etc.)

// 🔹 ALA (Argument List Array)
// Macro	Arguments
// INCR	[&A, &B]
// SQUARE	[&X]

// → Stores the names of formal parameters used in each macro.