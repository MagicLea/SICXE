import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class SICXEASM {
	HashMap<String, Mnemonic> opcodeTable;
	HashMap<String, Integer> symbolTable;
	PrintStream asmList;
	PrintStream objProgram;
	String objT;
	SourceLine lineT;
	SourceLine fromLineT;
	String objM;
	Scanner sc;
	ArrayList<SourceLine> lines;
	String name;
	int size;

	public SICXEASM(String fileName) throws FileNotFoundException {
		File srcFile = new File(fileName);
		sc = new Scanner(srcFile);
		lines = new ArrayList<SourceLine>();
		asmList = new PrintStream(new FileOutputStream(
				new File(srcFile.getParent() + File.separator + "asmList.txt")));
		objProgram = new PrintStream(new FileOutputStream(new File(srcFile.getParent() + File.separator
				+ "objProgram.txt")));

		size = 0;
		name = "";
		objM = "";
		symbolTable = new HashMap<String, Integer>();
		symbolTable.put("A", 0);
		symbolTable.put("X", 1);
		symbolTable.put("L", 2);
		symbolTable.put("PC", 8);
		symbolTable.put("SW", 9);
		symbolTable.put("B", 3);
		symbolTable.put("S", 4);
		symbolTable.put("T", 5);
		symbolTable.put("F", 6);
		opcodeTable = new HashMap<String, Mnemonic>();
		opcodeTable.put("ADD", new Mnemonic(3, 0x18));
		opcodeTable.put("ADDF", new Mnemonic(3, 0x58));
		opcodeTable.put("ADDR", new Mnemonic(2, 0x90));
		opcodeTable.put("AND", new Mnemonic(3, 0x40));
		opcodeTable.put("CLEAR", new Mnemonic(2, 0xB4));
		opcodeTable.put("COMP", new Mnemonic(3, 0x28));
		opcodeTable.put("COMPF", new Mnemonic(3, 0x88));
		opcodeTable.put("COMPR", new Mnemonic(2, 0xA0));
		opcodeTable.put("DIV", new Mnemonic(1, 0x24));
		opcodeTable.put("DIVF", new Mnemonic(1, 0x64));
		opcodeTable.put("DIVR", new Mnemonic(1, 0x9C));
		opcodeTable.put("FIX", new Mnemonic(3, 0xC4));
		opcodeTable.put("FLOAT", new Mnemonic(3, 0xC0));
		opcodeTable.put("HIO", new Mnemonic(3, 0xF4));
		opcodeTable.put("J", new Mnemonic(3, 0x3C));
		opcodeTable.put("JEQ", new Mnemonic(3, 0x30));
		opcodeTable.put("JGT", new Mnemonic(3, 0x34));
		opcodeTable.put("JLT", new Mnemonic(3, 0x38));
		opcodeTable.put("JSUB", new Mnemonic(3, 0x48));
		opcodeTable.put("LDA", new Mnemonic(3, 0x00));
		opcodeTable.put("LDB", new Mnemonic(3, 0x68));
		opcodeTable.put("LDCH", new Mnemonic(3, 0x50));
		opcodeTable.put("LDF", new Mnemonic(3, 0x70));
		opcodeTable.put("LDL", new Mnemonic(3, 0x08));
		opcodeTable.put("LDS", new Mnemonic(3, 0x6C));
		opcodeTable.put("LDT", new Mnemonic(3, 0x74));
		opcodeTable.put("LDX", new Mnemonic(3, 0x04));
		opcodeTable.put("LPS", new Mnemonic(3, 0xE0));
		opcodeTable.put("UML", new Mnemonic(3, 0x20));
		opcodeTable.put("MULF", new Mnemonic(3, 0x60));
		opcodeTable.put("MULR", new Mnemonic(2, 0x98));
		opcodeTable.put("NORM", new Mnemonic(1, 0xC8));
		opcodeTable.put("OR", new Mnemonic(3, 0x44));
		opcodeTable.put("RD", new Mnemonic(3, 0xD8));
		opcodeTable.put("RMO", new Mnemonic(2, 0xAC));
		opcodeTable.put("RSUB", new Mnemonic(3, 0x4C));
		opcodeTable.put("SHIFTL", new Mnemonic(2, 0xA4));
		opcodeTable.put("SHIFTR", new Mnemonic(2, 0xA8));
		opcodeTable.put("SIO", new Mnemonic(1, 0xF0));
		opcodeTable.put("SSK", new Mnemonic(3, 0xEC));
		opcodeTable.put("STA", new Mnemonic(3, 0x0C));
		opcodeTable.put("STB", new Mnemonic(3, 0x78));
		opcodeTable.put("STCH", new Mnemonic(3, 0x54));
		opcodeTable.put("STF", new Mnemonic(3, 0x80));
		opcodeTable.put("STI", new Mnemonic(3, 0xD4));
		opcodeTable.put("STL", new Mnemonic(3, 0x14));
		opcodeTable.put("STS", new Mnemonic(3, 0x7C));
		opcodeTable.put("STSW", new Mnemonic(3, 0xE8));
		opcodeTable.put("STT", new Mnemonic(3, 0x84));
		opcodeTable.put("STX", new Mnemonic(3, 0x10));
		opcodeTable.put("SUB", new Mnemonic(3, 0x1C));
		opcodeTable.put("SUBF", new Mnemonic(3, 0x5C));
		opcodeTable.put("SUBR", new Mnemonic(2, 0x94));
		opcodeTable.put("SVC", new Mnemonic(2, 0xB0));
		opcodeTable.put("TD", new Mnemonic(3, 0xE0));
		opcodeTable.put("TIO", new Mnemonic(1, 0xF8));
		opcodeTable.put("TIX", new Mnemonic(3, 0x2C));
		opcodeTable.put("TIXR", new Mnemonic(2, 0xB8));
		opcodeTable.put("WD", new Mnemonic(3, 0xDC));
		opcodeTable.put("START", new Mnemonic(3, -1));
		opcodeTable.put("END", new Mnemonic(3, -1));
		opcodeTable.put("BYTE", new Mnemonic(3, -1));
		opcodeTable.put("RESB", new Mnemonic(3, -1));
		opcodeTable.put("RESW", new Mnemonic(3, -1));
		opcodeTable.put("BASE", new Mnemonic(3, -1));

	}

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 1) {
			System.out.println("usage: SICXEASM  sourceFilePath ...");
			return;
		}
		SICXEASM sicasm = new SICXEASM(args[0]);
		sicasm.assemble();
	}

	void assemble() {
		pass1();
		pass2();
	}

	boolean isOpcode(String str) {
		if (opcodeTable.get(str) != null)
			return true;
		else
			return false;
	}

	boolean isSymbol(String str) {
		if (symbolTable.get(str) != null)
			return true;
		else
			return false;
	}

	boolean isValidSymbol(String str) {
		return str.matches("\\w+");
	}

	SourceLine readLine(String str, int lineNum) {
		String tmp;
		if (str.substring(0, 1).equals(".")) {
			return new SourceLine("", "", "", str);
		}
		String label = "";
		String opcode = "";
		String operand = "";
		String comment = "";
		Scanner srcLine = new Scanner(str);
		try {
			tmp = srcLine.next();
			boolean isOpcode = isOpcode(tmp);
			if (!isOpcode) {
				if (tmp.substring(0, 1).equals("+")) {
					isOpcode = isOpcode(tmp.substring(1));
				}
			}
			if (isOpcode) {
				opcode = tmp;
			}
			else {
				label = tmp;
				opcode = srcLine.next();
			}
			tmp = srcLine.next();
			if (tmp.substring(0, 1).equals("."))
				comment = tmp;
			else {
				operand = tmp;
				comment = srcLine.nextLine();
			}
		} catch (NoSuchElementException e) {
		}
		srcLine.close();
		if (!label.equals("") && !isValidSymbol(label)) {
			System.out.println((lineNum + 1) + ":InValid symbol.(Abort Symbol)");
			label = "";
		}
		return new SourceLine(label, opcode, operand, comment);
	}

	void pass1() {
		int locctr = 0;
		int lineNum = 0;
		while (sc.hasNextLine()) {
			String str = sc.nextLine();
			if (str.length() == 0)
				continue;
			SourceLine line = readLine(str, lineNum);
			lines.add(line);
			line.locctr = locctr;
			lineNum = lines.size();
			if (line.opCode.equals(""))
				continue;
			if (line.opCode.equals("START")) {
				if (lines.size() != 1) {
					System.out.println(lineNum + ":'START' have to be the first.(Abort Line)");
					line.abort = true;
					continue;
				}
				else {
					name = line.label;
					try {
						locctr = Integer.parseInt(line.operand, 16);
					} catch (NumberFormatException e) {
						System.out.println(lineNum + ":Operand parse failed.(Abort Line)");
						line.abort = true;
						continue;
					}
				}
			}
			else if (line.opCode.equals("END")) {
				break;
			}
			else if (line.opCode.equals("RESB")) {
				int codeSize;
				try {
					codeSize = Integer.parseInt(line.operand, 10);
				} catch (NumberFormatException e) {
					System.out.println(lineNum + ":Operand parse failed.(Abort Line)");
					line.abort = true;
					continue;
				}
				if (!line.label.equals("")) {
					if (!isSymbol(line.label)) {
						symbolTable.put(line.label, locctr);
					}
					else {
						System.out.println(lineNum + ":Duplicate symbol.(Abort this Symbol)");
					}
				}
				locctr += codeSize * 1;
				line.opSize = codeSize * 1;
			}
			else if (line.opCode.equals("RESW")) {
				int codeSize;
				try {
					codeSize = Integer.parseInt(line.operand, 10);
				} catch (NumberFormatException e) {
					System.out.println(lineNum + ":Operand parse failed.(Abort Line)");
					line.abort = true;
					continue;
				}
				if (!line.label.equals("")) {
					if (!isSymbol(line.label)) {
						symbolTable.put(line.label, locctr);
					}
					else {
						System.out.println(lineNum + ":Duplicate symbol.(Abort this Symbol)");
					}
				}
				locctr += codeSize * 3;
				line.opSize = codeSize * 3;
			}
			else if (line.opCode.equals("BYTE")) {
				if (!line.label.equals("")) {
					if (!isSymbol(line.label)) {
						symbolTable.put(line.label, locctr);
					}
					else {
						System.out.println(lineNum + ":Duplicate symbol.(Abort this Symbol)");
					}
				}
				locctr += 1;
				line.opSize = 1;
			}
			else if (line.opCode.equals("WORD")) {
				if (!line.label.equals("")) {
					if (!isSymbol(line.label)) {
						symbolTable.put(line.label, locctr);
					}
					else {
						System.out.println(lineNum + ":Duplicate symbol.(Abort this Symbol)");
					}
				}
				locctr += 3;
				line.opSize = 3;
			}
			else if (line.opCode.equals("BASE")) {
			}
			else {
				int opSize = 0;
				if (line.opCode.substring(0, 1).equals("+")) {
					line.mnemonic = opcodeTable.get(line.opCode.substring(1));
					opSize = 4;
				}
				else {
					line.mnemonic = opcodeTable.get(line.opCode);
					if (line.mnemonic != null)
						opSize = line.mnemonic.format;
				}
				if (line.mnemonic != null) {
					if (!line.label.equals("")) {
						if (!isSymbol(line.label)) {
							symbolTable.put(line.label, locctr);
						}
						else {
							System.out.println(lineNum + ":Duplicate symbol.(Abort this Symbol)");
						}
					}
					locctr += opSize;
					line.opSize = opSize;
				}
				else {
					System.out.println(lineNum + ":Unknown OP.(Abort Line)");
					line.abort = true;
					continue;
				}
			}
		}
		size = locctr;
	}

	void pass2() {
		int lineNum = 0;
		int base = 0;
		boolean end = false;
		asmList.printf("%4s %4s %6s %5s %10s %s%n%n", "LINE", "LOC", "LABEL", "OP", "OPERAND", "ObjectCode");
		Iterator<SourceLine> iterator = lines.iterator();
		while (iterator.hasNext()) {
			SourceLine line = iterator.next();
			lineNum++;
			if (line.abort || line.opCode.length() == 0)
				continue;
			if (line.opCode.equals("START")) {
				objProgram.printf("H%-6s%06X%06X%n", name, Integer.parseInt(line.operand, 16), size);
				asmList.printf("%4s %04X %6s %5s %10s%n", lineNum, line.locctr, line.label, line.opCode, line.operand);
			}
			else if (line.opCode.equals("END")) {
				Integer addr = symbolTable.get(line.operand);
				if (addr == null) {
					System.out.println(lineNum + ":Operand parse failed.(Abort Line)");
					line.abort = true;
					continue;
				}
				if (!line.abort) {
					asmList.printf("%4s %04X %6s %5s %10s%n", lineNum, line.locctr, line.label, line.opCode,
							line.operand);
					writeObjT(null, null);
					writeObjM(-1, -1);
					objProgram.printf("E%06x%n", addr);
					end = true;
				}
			}
			else if (line.opCode.equals("RESB")) {
				if (!line.abort) {
					asmList.printf("%4s %04X %6s %5s %10s%n", lineNum, line.locctr, line.label, line.opCode,
							line.operand);
				}
			}
			else if (line.opCode.equals("RESW")) {
				if (!line.abort) {
					asmList.printf("%4s %04X %6s %5s %10s%n", lineNum, line.locctr, line.label, line.opCode,
							line.operand);
				}
			}
			else if (line.opCode.equals("BYTE")) {
				int value;
				try {
					value = Integer.parseInt(line.operand, 10);
				} catch (NumberFormatException e) {
					System.out.println(lineNum + ":Operand parse failed.(Abort Line)");
					line.abort = true;
					continue;
				}
				writeObjT(line, String.format("%02X", value));
				if (!line.abort) {
					asmList.printf("%4s %04X %6s %5s %10s %02X%n", lineNum, line.locctr, line.label, line.opCode,
							line.operand, value);
				}
			}
			else if (line.opCode.equals("WORD")) {
				int value;
				try {
					value = Integer.parseInt(line.operand, 10);
				} catch (NumberFormatException e) {
					System.out.println(lineNum + ":Operand parse failed.(Abort Line)");
					line.abort = true;
					continue;
				}
				writeObjT(line, String.format("%06X", value));
				if (!line.abort) {
					asmList.printf("%4s %04X %6s %5s %10s %06X%n", lineNum, line.locctr, line.label, line.opCode,
							line.operand, value);
				}
			}
			else if (line.opCode.equals("BASE")) {
				Integer loc = symbolTable.get(line.operand);
				if (loc != null) {
					base = loc;
				}
				else {
					System.out.println(lineNum + ":InValid Operand.(Abort Line)");
					line.abort = true;
					continue;
				}
				if (!line.abort) {
					asmList.printf("%4s %4s %6s %5s %10s%n", lineNum, "", line.label, line.opCode,
							line.operand);
				}
			}
			else {
				int opCode = line.mnemonic.opcode;
				if (line.mnemonic.format == 1) {
					if (line.operand.length() > 0) {
						System.out.println(lineNum + ":Operand must be null.(Abort Operand)");
						line.operand = "";
					}
					line.objectCode = opCode;
					if (!line.abort) {
						asmList.printf("%4s %04X %6s %5s %10s %02X%n", lineNum, line.locctr, line.label, line.opCode,
								line.operand, line.objectCode);
						writeObjT(line, String.format("%02X", line.objectCode));
					}
				}
				else if (line.mnemonic.format == 2) {
					if (line.operand.length() == 0) {
						System.out.println(lineNum + ":Have no operand.(Abort Line)");
						line.abort = true;
						continue;
					}
					String[] operands = line.operand.split(",");
					int r1 = 0, r2 = 0;
					if (operands.length > 2) {
						System.out.println(lineNum + ":Operand is too long.(Abort Line)");
						line.abort = true;
						continue;
					}
					if (operands.length == 2) {
						if (symbolTable.get(operands[1]) == null) {
							try {
								r2 = Integer.parseInt(operands[1]);
							} catch (NumberFormatException e) {
								System.out.println(lineNum + ":Operand parse failed.(Abort Line)");
								line.abort = true;
								continue;
							}
						}
						else {
							r2 = symbolTable.get(operands[1]);
						}
					}
					if (symbolTable.get(operands[0]) == null) {
						System.out.println(lineNum + ":Operand parse failed.(Abort Line)");
						line.abort = true;
						continue;
					}
					r1 = symbolTable.get(operands[0]);
					line.objectCode = (opCode << 8) + (r1 << 4) + r2;
					if (!line.abort) {
						asmList.printf("%4s %04X %6s %5s %10s %04X%n", lineNum, line.locctr, line.label, line.opCode,
								line.operand, line.objectCode);
						writeObjT(line, String.format("%04X", line.objectCode));
					}
				}
				else if (line.mnemonic.format == 3) {
					// n,i
					int ni = 3;
					String modifyOperand = "";
					if (line.operand.length() > 0) {
						if (line.operand.substring(0, 1).equals("@")) {
							ni = 2;
							modifyOperand = line.operand.substring(1);
						}
						else if (line.operand.substring(0, 1).equals("#")) {
							ni = 1;
							modifyOperand = line.operand.substring(1);
						}
						else {
							ni = 3;
							modifyOperand = line.operand;
						}
					}
					// x
					int x = 0;
					String[] operands = modifyOperand.split(",");
					if (operands.length >= 2 && operands[1].equals("X")) {
						x = 1;
					}
					// e
					int e = 0;
					int symbolCode = 0;
					boolean number = true;
					try {
						symbolCode = Integer.parseInt(operands[0]);
					} catch (NumberFormatException ee) {
						number = false;
						if (symbolTable.get(operands[0]) != null) {
							symbolCode = symbolTable.get(operands[0]);
						}
					}
					int disp = symbolCode;
					if (line.opCode.substring(0, 1).equals("+")) {
						e = 1;
						line.objectCode = ((opCode + ni) << 24) + (((x << 3) + (0 << 1) + e) << 20) + disp;
						if (!number) {
							writeObjM(line.locctr + 1, 5);
						}
					}
					else {
						// b,p
						int bp = 0;
						if (!number) {
							if (line.operand.length() != 0) {
								int pcRelative = symbolCode - (line.locctr + line.opSize);
								if (pcRelative > 2047 || pcRelative < -2048) {
									bp = 2;
									disp = symbolCode - base;
								}
								else {
									bp = 1;
									if (pcRelative < 0) {
										disp = pcRelative & 4095;
									}
									else
										disp = pcRelative;
								}
							}
						}
						line.objectCode = ((opCode + ni) << 16) + (((x << 3) + (bp << 1) + e) << 12) + disp;
					}
					if (!line.abort) {
						asmList.printf("%4s %04X %6s %5s %10s %06X%n", lineNum, line.locctr, line.label, line.opCode,
								line.operand, line.objectCode);
						writeObjT(line, String.format("%06X", line.objectCode));
					}
				}
			}
		}
		if (!end) {
			writeObjT(null, null);
			writeObjM(-1, -1);
		}
	}

	void writeObjT(SourceLine line, String objCode) {
		if (line == null) {
			if (objT.length() == 0) {
				return;
			}
			objProgram.printf("T%06X%02X%s%n", fromLineT.locctr, objT.length() / 2, objT);
		}
		else {
			if (lineT == null) {
				lineT = line;
				fromLineT = line;
				objT = objCode;
			}
			else {
				if (lineT.locctr + lineT.opSize == line.locctr) {
					if (objT.length() + objCode.length() <= 0x1E * 2) {
						objT += objCode;
						lineT = line;
					}
					else {
						objProgram.printf("T%06X%02X%s%n", fromLineT.locctr, objT.length() / 2, objT);
						objT = objCode;
						lineT = line;
						fromLineT = line;
					}
				}
				else {
					objProgram.printf("T%06X%02X%s%n", fromLineT.locctr, objT.length() / 2, objT);
					objT = objCode;
					lineT = line;
					fromLineT = line;
				}
			}
		}
	}

	void writeObjM(int modAddr, int len) {
		if (modAddr == -1) {
			if (objM.length() == 0) {
				return;
			}
			objProgram.printf("%s", objM);
		}
		else {
			objM += String.format("M%06X%02X%n", modAddr, len);
		}
	}
}

class SourceLine {
	String label;
	String opCode;
	String operand;
	String comment;
	int locctr;
	int opSize;
	boolean abort;
	Mnemonic mnemonic;
	int objectCode;

	public SourceLine(String label, String opCode, String operand, String comment) {
		this.label = label;
		this.opCode = opCode;
		this.operand = operand;
		this.comment = comment;
		this.locctr = -1;
		this.opSize = 0;
		this.abort = false;
		this.objectCode = 0;
	}
}

class Mnemonic {
	int format;
	int opcode;

	public Mnemonic(int format, int opcode) {
		this.format = format;
		this.opcode = opcode;
	}
}
