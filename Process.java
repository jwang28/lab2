public class Process implements Comparable<Process> {
	//inputs
	private int A;
	private int B;
	private int C;
	private int io;

	private int finishTime;
	private int turnaroundTime; //do I need this
	private int IOTime;
	private int waitTime;
	private int state; //0=unstarted; 1=ready; 2=running; 3=blocked
	private int CPUBurst; //calc
	private int IOBurst;

	private int time;

	private int CPU_Util;
	private int IO_Util;

	public Process (int A, int B, int C, int io){
		this.A = A;
		this.B = B;
		this.C = C;
		this.io = io;

		
		this.finishTime=0;
		this.turnaroundTime=0;
		this.IOTime = 0;
		this.waitTime=0;
		this.state=0;
		this.CPUBurst = 0;
		this.IOBurst=0;
		this.time=0;

		this.CPU_Util = 0;

	}

	@Override
	public int compareTo(Process p){
		return this.A - p.A;
	}
}