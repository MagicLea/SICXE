import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SICXELinker {
	int progAddr;
	int progLen;
	Scanner sc;
	ArrayList<ControlSection> csTable;
	HashMap<String, Symbol> esTable; // External Symbol Table
	int execAddr;
	StringBuilder[] mems;

	public SICXELinker(String fileName) throws FileNotFoundException {
		File srcFile = new File(fileName);
		sc = new Scanner(srcFile);
		progLen = 0;
		csTable = new ArrayList<ControlSection>();
		esTable = new HashMap<String, Symbol>();
		execAddr = 0;

	}

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 1) {
			System.out.println("usage: SICXELinker  sourceFilePath ...");
			return;
		}
		SICXELinker linker = new SICXELinker(args[0]);
		linker.pass1();
		linker.printLoadMap();
		System.out.println();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println();
		linker.pass2();
		linker.printMemory();
		System.out.printf("Transfer control to this program, execute from %06X.",linker.execAddr);
	}

	void pass1() {
		progAddr = 0x4000; // just assume
		ControlSection curCS = null; // current CS
		ControlSection preCS = null; // previous CS
		int lineNum = 0;
		while (sc.hasNextLine()) {
			lineNum++;
			String record = sc.nextLine();
			if (record.equals("")) {
				continue;
			}
			else if (record.substring(0, 1).equals("H")) {
				curCS = new ControlSection();
				curCS.name = record.substring(1, 7);
				curCS.length = Integer.parseInt(record.substring(13, 19), 16);
				if (preCS == null) {
					curCS.startAddr = progAddr;
				}
				else {
					curCS.startAddr = preCS.startAddr + preCS.length;
				}
				if (esTable.put(curCS.name, new Symbol(curCS.name, curCS.startAddr, curCS)) != null) {
					System.out.println(lineNum + ":Duplicate Control Section's name.(Stop)");
					System.exit(1);
				}
				csTable.add(curCS);
				progLen += curCS.length;
				while (sc.hasNextLine()) {
					lineNum++;
					record = sc.nextLine();
					if (record.equals("")) {
						continue;
					}
					else if (record.substring(0, 1).equals("E")) {
						if (record.length() > 1) {
							if (execAddr != 0) {
								System.out.println(lineNum + ":Already has executeAddress.(Abort line)");
							}
							else {
								execAddr = Integer.parseInt(record.substring(1, 7), 16) + curCS.startAddr;
							}
						}
						preCS = curCS;
						break;
					}
					else if (record.substring(0, 1).equals("D")) {
						int index = 1;
						while (record.length() >= index + 12) {
							String name = record.substring(index, index + 6);
							int addr = Integer.parseInt(record.substring(index + 6, index + 12), 16) + curCS.startAddr;
							Symbol symbol = new Symbol(name, addr, curCS);
							if (esTable.put(name, symbol) != null) {
								System.out.println(lineNum + ":Duplicate Symobl name.(Stop)");
								System.exit(1);
							}
							curCS.symbols.add(symbol);
							index += 12;
						}
					}
					else {
						curCS.lines.add(record);
					}
				}

			}
		}

	}

	void pass2() {
		// initial memory
		int len = (progLen / 0x20) * 2;
		if (progLen % 0x20 != 0) {
			len += 2;
		}
		mems = new StringBuilder[len];
		int count = progLen * 2;
		for (int i = 0; i < mems.length; i++) {
			char[] fill;
			if (count >= 0x20) {
				fill = new char[0x20];
				count -= 0x20;
			}
			else {
				fill = new char[count];
				count -= count;
			}
			Arrays.fill(fill, '.');
			mems[i] = new StringBuilder(new String(fill));
			if (mems[i].length() < 0x20) {
				fill = new char[0x20 - mems[i].length()];
				Arrays.fill(fill, 'x');
				mems[i].append(fill);
			}
		}
		// load
		for (ControlSection cs : csTable) {
			for (String line : cs.lines) {
				if (line.substring(0, 1).equals("T")) {
					int addr = Integer.parseInt(line.substring(1, 7), 16) + cs.startAddr - progAddr;
					int lineIndex = addr / (0x20 / 2);
					int columnIndex = (addr % (0x20 / 2)) * 2;
					StringBuilder mem = mems[lineIndex];
					for (int i = 9; i < line.length(); i++) {
						if (columnIndex >= 0x20) {
							lineIndex++;
							mem = mems[lineIndex];
							columnIndex -= 0x20;
						}
						mem.replace(columnIndex, columnIndex + 1, line.substring(i, i + 1));
						columnIndex++;
					}
				}
				else if (line.substring(0, 1).equals("M")) {
					int offest = Integer.parseInt(line.substring(7, 9), 16);
					if (offest % 2 != 0) {
						offest++;
					}
					int addr = Integer.parseInt(line.substring(1, 7), 16) + cs.startAddr - progAddr;
					int lineIndex = addr / (0x20 / 2);
					int columnIndex = (addr % (0x20 / 2)) * 2;
					String oldString = null;
					if (columnIndex + offest < mems[lineIndex].length()) {
						oldString = mems[lineIndex].substring(columnIndex, columnIndex + offest);
					}
					else {
						oldString = mems[lineIndex].substring(columnIndex);
						oldString += mems[lineIndex + 1].substring(0, columnIndex + offest - mems[lineIndex].length());
					}
					int oldValue = Integer.parseInt(oldString, 16);
					if (oldValue > 0x7FFFFF) {
						oldValue = ((oldValue ^ 0xFFFFFF) + 1) * -1;
					}
					int totalModValue = 0;

					StringTokenizer st = new StringTokenizer(line.substring(9), "+-", true);
					while (st.hasMoreTokens()) {
						int modValue = 0;
						String sign = st.nextToken();
						String symbol = st.nextToken();
						while (symbol.length() < 6) {
							symbol += " ";
						}
						try {
							modValue = esTable.get(symbol).addr;
						} catch (NullPointerException e) {
							System.out.println("Symbol isn't exist.(STOP)");
							System.exit(2);
						}
						if (sign.equals("-")) {
							modValue *= -1;
						}
						totalModValue += modValue;
					}
					int newValue = oldValue + totalModValue;
					if(newValue < 0 ){
						newValue = ((-newValue ^ 0xFFFFFF) + 1);
					}
					for (int i = 0; i < 6; i++) {
						if (columnIndex >= 0x20) {
							lineIndex++;
							columnIndex -= 0x20;
						}
						mems[lineIndex].replace(columnIndex, columnIndex + 1, String.format("%06X", newValue).substring(i, i + 1));
						columnIndex++;
					}
				}
				else {
					// ignore
				}
			}
		}
	}

	void printLoadMap() {
		System.out.println("Load Map:");
		System.out.println("CS      SymName   Address Length");
		System.out.println("================================");
		for (ControlSection cs : csTable) {
			System.out.printf("%6s%6s   %7X    %04X%n", cs.name, "", cs.startAddr, cs.length);
			for (Symbol symbol : cs.symbols) {
				System.out.printf("%6s %7s    %04X    %n", "", symbol.name, symbol.addr);
			}
		}
	}

	void printMemory() {
		System.out.println("Memory:");
		System.out.println("Addr               Contents");
		System.out.println("========================================");
		for (int n = 0; n < mems.length; n++) {
			System.out.print(String.format("%4X", progAddr + 0x10 * n, 16) + " ");
			for (int i = 0; i < 4; i++) {
				System.out.print(mems[n].substring(i * 8, i * 8 + 8) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

}

class ControlSection {
	ArrayList<String> lines;
	ArrayList<Symbol> symbols;
	int length;
	int startAddr;
	String name;

	public ControlSection() {
		lines = new ArrayList<String>();
		symbols = new ArrayList<Symbol>();
	}
}

class Symbol {
	String name;
	int addr;
	ControlSection own;

	public Symbol(String name, int addr, ControlSection own) {
		this.name = name;
		this.addr = addr;
		this.own = own;
	}

	public String toString() {
		return name + "->" + addr;
	}
}
