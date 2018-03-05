//need max finishing time variable

import java.io.*;
import java.util.*;

public class Scheduler {

	public static void main (String [] args){
		ArrayList<Process> procArr1 = new ArrayList<Process>();
		ArrayList<Process> procArr2 = new ArrayList<Process>();
		ArrayList<Process> procArr3 = new ArrayList<Process>();
		ArrayList<Process> procArr4 = new ArrayList<Process>();
		File f;
		boolean verbose = false;

		if(args.length > 1){
			f = new File(args[1]);
			verbose = true;
		}
		else{
			f = new File (args[0]);
		}
		try{
			Scanner input = new Scanner(f);
			int numProcesses = input.nextInt();
			int A,B,C,io;
			for (int i = 0; i < numProcesses; i++){
				A = input.nextInt();
				B = input.nextInt();
				C = input.nextInt();
				io = input.nextInt();
				procArr1.add(new Process(A,B,C,io));
				procArr2.add(new Process(A,B,C,io));
				procArr3.add(new Process(A,B,C,io));
				procArr4.add(new Process(A,B,C,io));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}

}