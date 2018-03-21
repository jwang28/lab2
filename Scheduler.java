//need max finishing time variable

import java.io.*;
import java.util.*;

public class Scheduler {
	static Scanner rand;
	public static void main (String [] args){
		ArrayList<Process> fcfsProcs = new ArrayList<Process>();
		ArrayList<Process> rrProcs = new ArrayList<Process>();
		ArrayList<Process> uniProcs = new ArrayList<Process>();
		ArrayList<Process> psjfProcs = new ArrayList<Process>();
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
				psjfProcs.add(new Process(A,B,C,io,order));
				order++;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		try {
			rand = new Scanner (new File("random-numbers.txt"));
		} catch (Exception e){
			e.printStackTrace();
		}
		//fcfs(fcfsProcs, verbose);
		//rr(rrProcs, verbose);
		//uni(uniProcs, verbose);
		psjf(psjfProcs,verbose);

		rand.close();
	}

	private static int randomOS(int U){
		int randInt = rand.nextInt();
		
		return 1 + (randInt % U);
	}

	private static void fcfs(ArrayList<Process> procs, boolean verbose){
		int cpu_util=0, io_util=0, cycle = 0,num_procs = procs.size();/* num_terminated = 0;*/
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
			procs.get(i).setPriority(i);
			System.out.print("  " + procs.get(i).getA() + " " + procs.get(i).getB() + " " + procs.get(i).getC() + " " + procs.get(i).getio());
		}
		System.out.println();
		//0 = unstarted, 1 = ready, 2 = running, 3 = blocked, 4 = terminated
		if (verbose){
			System.out.println("\nThis detailed printout gives the state and remaining burst for each process\n");
		}	
		while (num_procs > 0){

			if (verbose){
				System.out.printf("Before cycle %5d:  ", cycle);
				for (Process p: procs){
					System.out.printf("%12s %3d ", p.getState(), p.getBurst());
				}
				System.out.println();
			}
			if (blocked.size() > 0){
				io_util++;
				for (Process p: blocked){
					
					p.subIOBurst();
					p.addIO_Util();
					if (p.getIOBurst() == 0){
						p.setState(1);
						transition_ready.add(p);
					}
				}
				for (Process p: transition_ready){
					ready.add(p);
					blocked.remove(p);
				}
				while (transition_ready.peek() != null){
					transition_ready.poll();
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
					int io_burst = randomOS(running.getio());
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
					running.setCPUBurst(cpu_burst);
				}	
			}
			for (Process p: ready){
				p.addWaitTime();
				p.addAge();
			}
			cycle++;
		}
		System.out.println("The scheduling algorithm used was First Come First Served");
		printOut(procs, cycle, cpu_util, io_util);
	}
	
	private static void rr(ArrayList<Process> procs, boolean verbose){
		int cpu_util=0, io_util=0, cycle = 0,num_procs = procs.size();
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
			procs.get(i).setPriority(i);
			System.out.print("  " + procs.get(i).getA() + " " + procs.get(i).getB() + " " + procs.get(i).getC() + " " + procs.get(i).getio());
		}
		System.out.println();
		//0 = unstarted, 1 = ready, 2 = running, 3 = blocked, 4 = terminated
		if (verbose){
			System.out.println("\nThis detailed printout gives the state and remaining burst for each process\n");
		}	
		while (num_procs > 0){

			if (verbose){
				System.out.printf("Before cycle %5d:  ", cycle);
				for (Process p: procs){
					System.out.printf("%12s %3d ", p.getState(), p.getBurst());
				}
				System.out.println();
			}
			if (blocked.size() > 0){
				io_util++;
				for (Process p: blocked){
					
					p.subIOBurst();
					p.addIO_Util();
					if (p.getIOBurst() == 0){
						p.setState(1);
						transition_ready.add(p);
					}
				}
				for (Process p: transition_ready){
					blocked.remove(p);
				}
			}
			
				//do running process
			if (running != null){
				running.subCPUBurst();
				running.addCPU_Util();
				running.subQuantum();
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
					int io_burst = randomOS(running.getio());
					running.setIOBurst(io_burst);
					running.setState(3);
					blocked.add(running);
					running=null;
				}
				//add pre-empt for round robin
				else if (running.getQuantum() == 0){
					transition_ready.add(running);
					running = null;
				}
			}
			//process arrival
			for (Process p: procs){
				if (p.getA() == cycle){
					p.setState(1);
					ready.add(p);
				}
			}
			for (Process p: transition_ready){
				ready.add(p);
			}
			while (transition_ready.peek() != null){
				transition_ready.poll();
			}

			//process ready queue
			if (ready.size() > 0){
				if (running == null){
					running = ready.poll();
					running.setState(2);
					running.clearAge();
					if (running.getCPUBurst() == 0){
						int cpu_burst = randomOS(running.getB());
						running.setCPUBurst(cpu_burst);
					}
					running.setQuantum();	
				}	
			}
			for (Process p: ready){
				p.addWaitTime();
				p.addAge();
			}
			
			cycle++;
		}
		System.out.println("The scheduling algorithm used was Round Robin");
		printOut(procs, cycle, cpu_util, io_util);
	}
	private static void uni(ArrayList<Process> procs, boolean verbose){
		int cpu_util=0, io_util=0, cycle = 0,num_procs = procs.size(), total_procs = procs.size();
		Process running = null;
		PriorityQueue <Process> almost_ready = new PriorityQueue<Process>(new Comparator<Process>(){
			@Override
			public int compare(Process p1, Process p2){
				return (p1.getPriority() - p2.getPriority());
			}
		});
		PriorityQueue <Process> ready = new PriorityQueue<Process>();
		/*ArrayList <Process> almost_ready = new ArrayList<Process>();*/
		PriorityQueue <Process> transition_ready = new PriorityQueue<Process>();
		PriorityQueue <Process> blocked = new PriorityQueue<Process>();

		System.out.print("The original input was: " + procs.size());
		for (int i = 0; i<procs.size(); i++){
			System.out.print("  " + procs.get(i).getA() + " " + procs.get(i).getB() + " " + procs.get(i).getC() + " " + procs.get(i).getio());
		}
		Collections.sort(procs);
		System.out.print("\nThe (sorted) input is:  " + procs.size());
		for (int i = 0; i<procs.size(); i++){
			procs.get(i).setPriority(i);
			System.out.print("  " + procs.get(i).getA() + " " + procs.get(i).getB() + " " + procs.get(i).getC() + " " + procs.get(i).getio());
		}
		System.out.println();
		//0 = unstarted, 1 = ready, 2 = running, 3 = blocked, 4 = terminated
		if (verbose){
			System.out.println("\nThis detailed printout gives the state and remaining burst for each process\n");
		}	
		while (num_procs > 0){

			if (verbose){
				System.out.printf("Before cycle %5d:  ", cycle);
				for (Process p: procs){
					System.out.printf("%12s %3d ", p.getState(), p.getBurst());
				}
				System.out.println();
			}
			if (blocked.size() > 0){
				io_util++;
				for (Process p: blocked){
					
					p.subIOBurst();
					p.addIO_Util();
					if (p.getIOBurst() == 0){
						p.setState(1);
						transition_ready.add(p);
					}
				}
				for (Process p: transition_ready){
					ready.add(p);
					blocked.remove(p);
				}
				while (transition_ready.peek() != null){
					transition_ready.poll();
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
					int io_burst = randomOS(running.getio());
					running.setIOBurst(io_burst);
					running.setState(3);
					blocked.add(running);
					running=null;
				}
			}
			//process arrival
			for (Process p: procs){
				if (p.getA() == cycle){
					p.setState(1);
					almost_ready.add(p);
				}
			}
			int cur = total_procs - num_procs;
			if (ready.size() == 0 && almost_ready.size() > 0){
				if (cur == almost_ready.peek().getPriority()){
					ready.add(almost_ready.poll());
				}
			}

			//process ready queue
			if (ready.size() > 0){
				if (running == null){
					running = ready.poll();
					running.setState(2);
					running.clearAge();
					int cpu_burst = randomOS(running.getB());
					running.setCPUBurst(cpu_burst);
				}	
			}
			for (Process p: almost_ready){
				p.addWaitTime();
				p.addAge();
			}
			cycle++;
		}
		System.out.println("The scheduling algorithm used was Uniprogrammed");
		printOut(procs, cycle, cpu_util, io_util);
	}
	private static void psjf(ArrayList<Process> procs, boolean verbose){
		int cpu_util=0, io_util=0, cycle = 0,num_procs = procs.size();
		Process running = null;
		PriorityQueue <Process> ready = new PriorityQueue<Process>(new Comparator<Process>(){
			@Override
			public int compare(Process p1, Process p2){
				if (p1.getRemaining() != p2.getRemaining()){
					return p1.getRemaining() - p2.getRemaining();
				}
				if (p1.getA() - p2.getA() != 0){
					return p1.getA() - p2.getA();
				}
				return p1.getPriority() - p2.getPriority();
			}
		});
		PriorityQueue <Process> transition_ready = new PriorityQueue<Process>();
		PriorityQueue <Process> blocked = new PriorityQueue<Process>();

		System.out.print("The original input was: " + procs.size());
		for (int i = 0; i<procs.size(); i++){
			System.out.print("  " + procs.get(i).getA() + " " + procs.get(i).getB() + " " + procs.get(i).getC() + " " + procs.get(i).getio());
		}
		Collections.sort(procs);
		System.out.print("\nThe (sorted) input is:  " + procs.size());
		for (int i = 0; i<procs.size(); i++){
			procs.get(i).setPriority(i);
			System.out.print("  " + procs.get(i).getA() + " " + procs.get(i).getB() + " " + procs.get(i).getC() + " " + procs.get(i).getio());
		}
		System.out.println();
		//0 = unstarted, 1 = ready, 2 = running, 3 = blocked, 4 = terminated
		if (verbose){
			System.out.println("\nThis detailed printout gives the state and remaining burst for each process\n");
		}	
		while (num_procs > 0){

			if (verbose){
				System.out.printf("Before cycle %5d:  ", cycle);
				for (Process p: procs){
					System.out.printf("%12s %3d", p.getState(), p.getBurst());
				}
				System.out.println();
			}
			if (blocked.size() > 0){
				io_util++;
				for (Process p: blocked){
					
					p.subIOBurst();
					p.addIO_Util();
					if (p.getIOBurst() == 0){
						p.setState(1);
						transition_ready.add(p);
					}
				}
				for (Process p: transition_ready){
					ready.add(p);
					blocked.remove(p);
				}
				while (transition_ready.peek() != null){
					transition_ready.poll();
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
					int io_burst = randomOS(running.getio());
					running.setIOBurst(io_burst);
					running.setState(3);
					blocked.add(running);
					running=null;
				}
				//check for shortest remaining
				else if (ready.peek() != null && running.getRemaining() >= ready.peek().getRemaining()){
					running.setState(1);
					ready.add(running);
					running = null;
				}
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
					if (running.getCPUBurst() == 0){
						int cpu_burst = randomOS(running.getB());
						running.setCPUBurst(cpu_burst);
					}	
				}	
			}
			for (Process p: ready){
				p.addWaitTime();
				p.addAge();
			}
			
			cycle++;
		}
		System.out.println("The scheduling algorithm used was Preemptive Shortest Job First");
		printOut(procs, cycle, cpu_util, io_util);
	}
	

	private static void printOut(ArrayList<Process> procs, int cycles, int cpu_util, int io_util){
		int counter = 0;
		double num_procs = procs.size();
		int totalTAT = 0;
		int totalWaitTime = 0;
		double cycle = cycles - 1;
		for (Process p: procs){
			totalTAT+=p.getTurnaroundTime();
			totalWaitTime+=p.getWaitTime();
			System.out.println("Process " + counter + ":");
			System.out.printf("\t(A,B,C,IO) = (%d,%d,%d,%d)\n",p.getA(),p.getB(),p.getC(),p.getio());
			System.out.println("\tFinishing time: " + p.getFinishTime() + "\n\t" +
				"Turnaround time: " + p.getTurnaroundTime() + "\n\t" +
				"I/O time: " + p.getIO_Util() + "\n\t" +
				"Waiting time: " + p.getWaitTime() + "\n");
			counter++;
		}

		System.out.println("Summary Data: " + "\n\t" +
			"Finishing time: " + cycle + "\n\t" +
			"CPU Utilization: " + cpu_util/cycle + "\n\t" +
			"I/O Utilization: " + io_util/cycle + "\n\t" +
			"Throughput: " + (100/(cycle/num_procs)) + " processes per hundred cycles\n\t" +
			"Average turnaround time: " + totalTAT/num_procs + "\n\t" +
			"Average waiting time: " + totalWaitTime/num_procs);
	}

}
















