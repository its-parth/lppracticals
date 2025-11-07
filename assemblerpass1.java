import java.io.*;
import java.util.*;
class Instruction {
    String mnemonic, opcode, type;
    Instruction(String m, String o, String t) {
        mnemonic = m; opcode = o; type = t;
    }
}
public class assemblerpass1 {
    static Map<String, Instruction> IS = new HashMap<>();
    static Map<String, Integer> symbolTable = new HashMap<>();
    static List<String> literalTable = new ArrayList<>();
    static int LC = 0;

    static void initIS() {
        IS.put("STOP", new Instruction("STOP", "00", "IS"));
        IS.put("ADD", new Instruction("ADD", "01", "IS"));
        IS.put("SUB", new Instruction("SUB", "02", "IS"));
        IS.put("MOVER", new Instruction("MOVER", "04", "IS"));
        IS.put("MOVEM", new Instruction("MOVEM", "05", "IS"));
        IS.put("READ", new Instruction("READ", "09", "IS"));
        IS.put("PRINT", new Instruction("PRINT", "10", "IS"));
    }
    public static void main(String[] args) throws Exception {
        initIS();
        BufferedReader br = new BufferedReader(new FileReader("input.asm"));
        BufferedWriter ic = new BufferedWriter(new FileWriter("intermediate.txt"));
        BufferedWriter sym = new BufferedWriter(new FileWriter("symtab.txt"));
        BufferedWriter lit = new BufferedWriter(new FileWriter("littab.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] token = line.trim().split("\\s+");
            String first = token[0].toUpperCase();
            // START
            if (first.equals("START")) {
                LC = Integer.parseInt(token[1]);
                ic.write("(AD,01) (C," + LC + ")\n");
                continue;
            }
            // END
            if (first.equals("END")) {
                ic.write("(AD,02)\n");
                break;
            }
            // Handle label
            int index = 0;
            if (!IS.containsKey(first)) {
                symbolTable.put(first, LC);
                index = 1;
            }
            String opcode = token[index].toUpperCase();
            Instruction inst = IS.get(opcode);

            if (inst != null) {
                ic.write("(IS," + inst.opcode + ") ");
                if (token.length > index + 1) {
                    String op1 = token[index + 1].replace(",", "");
                    if (op1.equalsIgnoreCase("AREG")) ic.write("(1) ");
                    else if (op1.equalsIgnoreCase("BREG")) ic.write("(2) ");
                    else if (op1.equalsIgnoreCase("CREG")) ic.write("(3) ");
                    else symbolTable.putIfAbsent(op1, 0);
                }
                if (token.length > index + 2) {
                    String op2 = token[index + 2];
                    if (op2.startsWith("=")) {
                        literalTable.add(op2);
                        ic.write("(L," + literalTable.size() + ")");
                    } else {
                        symbolTable.putIfAbsent(op2, 0);
                        ic.write("(S," + op2 + ")");
                    }
                }
                ic.write("\n");
                LC++;
            }
        }
        // Write symbol table
        sym.write("Symbol\tAddress\n");
        for (String s : symbolTable.keySet())
            sym.write(s + "\t" + symbolTable.get(s) + "\n");
        // Write literal table
        lit.write("Literal\tIndex\n");
        int i = 1;
        for (String l : literalTable)
            lit.write(l + "\t" + i++ + "\n");

        br.close(); ic.close(); sym.close(); lit.close();

        System.out.println("Pass-1 completed. Files generated: intermediate.txt, symtab.txt, littab.txt");
    }
}