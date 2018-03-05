//need max finishing time variable

import java.io.*;
import java.uti.*;

public class Scheduler {

	public static void main (String [] args){
		Arraylist<Process> procArr1 = new Arraylist<Process>();
		Arraylist<Process> procArr2 = new Arraylist<Process>();
		Arraylist<Process> procArr3 = new Arraylist<Process>();
		Arraylist<Process> procArr4 = new Arraylist<Process>();
		File f;
		boolean verbose = false;

		if(args.length > 1){
			f = new File(args[1]);
			verbose = true;
		}
		else{
			f = new File (args[0]);
		}
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
	}

}