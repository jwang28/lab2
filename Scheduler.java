//need max finishing time variable

import java.io.*;
import java.util.*;

public class Scheduler {
	static Scanner rand;
	public static void main (String [] args){
		ArrayList<Process> fcfsProcs = new ArrayList<Process>();
		ArrayList<Process> rrProcs = new ArrayList<Process>();
		ArrayList<Process> uniProcs = new ArrayList<Process>();
		ArrayList<Process> srtnProcs = new ArrayList<Process>();
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
			int order = 0;
			for (int i = 0; i < numProcesses; i++){
				A = input.nextInt();
				B = input.nextInt();
				C = input.nextInt();
				io = input.nextInt();
				fcfsProcs.add(new Process(A,B,C,io,order));
				rrProcs.add(new Process(A,B,C,io,order));
				uniProcs.add(new Process(A,B,C,io,order));
				srtnProcs.add(new Process(A,B,C,io,order));
				order++;
				System.out.println(A + " " + B + " " +C + " " + io);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		//do fcfs
		try {
			rand = new Scanner (new File("random-numbers.txt"));
		} catch (Exception e){
			e.printStackTrace();
		}
		fcfs(fcfsProcs, verbose);
		rand.close();
	}

	private static int randomOS(int U){
		int randInt = rand.nextInt();
		
		return 1 + (randInt % U);
	}

	private static void fcfs(ArrayList<Process> procs, boolean verbose){
		int cpu_util=0, io_util=0, cycle = 0,num_procs = procs.size(), num_terminated = 0;
		Process running = null;
		PriorityQueue <Process> ready = new PriorityQueue<Process>();
		PriorityQueue <Process> transition_ready = new PriorityQueue<Process>();
		PriorityQueue <Process> blocked = new PriorityQueue<Process>();

		System.out.print("The original input was: " + procs.size());
		for (int i = 0; i<procs.size(); i++){
			System.out.print("  " + procs.get(i).getA() + " " + procs.get(i).getB() + " " + procs.get(i).getC() + " " + procs.get(i).getio());
		}
		Collections.sort(procs);
		System.out.print("\nThe (sorted) input is:  " + procs.size());
		for (int i = 0; i<procs.size(); i++){
			System.out.print("  " + procs.get(i).getA() + " " + procs.get(i).getB() + " " + procs.get(i).getC() + " " + procs.get(i).getio());
		}
		//0 = unstarted, 1 = ready, 2 = running, 3 = blocked, 4 = terminated
		if (verbose){
			System.out.println("\n\nThis detailed printout gives the state and remaining burst for each process\n");
		}	
		while (num_procs > 0){

			if (verbose){
				System.out.printf("Before cycle %5d:  ", cycle);
				for (Process p: procs){
					System.out.printf("%12s %3d ", p.getState(), p.getBurst());
				}
			}
			
			System.out.println();

			
			if (blocked.size() > 0){
				io_util++;
				for (Process p: blocked){
					p.subIOBurst();
					p.addIO_Util();
					/*p.setState(3);*/
					if (p.getIOBurst() == 0){
						p.setState(1);
						transition_ready.add(p);
						blocked.remove(p);
					}
				}
				for (Process p: transition_ready){
					ready.add(transition_ready.poll());
				}
			}
			
			
				//do running process
			if (running != null){
				running.subCPUBurst();
				running.addCPU_Util();
				cpu_util++;
				//check if process terminates
				if (running.getCPU_Util() >= running.getC()){
					running.setState(4);
					running.setFinishTime(cycle);
					running = null;
					num_procs--;
				}
				//else check if process still running
				else if (running.getCPUBurst() == 0){
					//what if burst > io?
					int io_burst = randomOS(running.getio());
					if (io_burst > running.getio()){
						io_burst = running.getio();
					}
					running.setIOBurst(io_burst);
					running.setState(3);
					blocked.add(running);
					running=null;
				}
				//add pre-empted for round robin
			}
			//process arrival
			for (Process p: procs){
				if (p.getA() == cycle){
					p.setState(1);
					ready.add(p);
				}
			}

			//process ready queue
			if (ready.size() > 0){
				if (running == null){
					running = ready.poll();
					running.setState(2);
					running.clearAge();
					int cpu_burst = randomOS(running.getB());
					if (cpu_burst > running.getRemaining()){
						cpu_burst = running.getRemaining();
					}
					running.setCPUBurst(cpu_burst);
				}
				for (Process p: ready){
					p.addWaitTime();
					p.addAge();
				}
			}
			cycle++;
		}
	}

}