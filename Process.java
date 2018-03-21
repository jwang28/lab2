public class Process implements Comparable<Process> {
	//inputs
	private int A;
	private int B;
	private int C;
	private int io;
	private int order;
	private int quantum;

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

	private int age;

	public Process (int A, int B, int C, int io, int order){
		this.A = A;
		this.B = B;
		this.C = C;
		this.io = io;
		this.order = order;

		
		this.finishTime=0;
		this.turnaroundTime=0;
		this.IOTime = 0;
		this.waitTime=0;
		this.state=0; //0 = unstarted, 1 = ready, 2 = blocked, 3 = running, 4 = terminated
		this.CPUBurst = 0;
		this.IOBurst=0;
		this.time=0;

		this.IO_Util = 0;
		this.CPU_Util = 0;
		this.age = 0;
		this.quantum = 2;

	}

	@Override
	public int compareTo(Process p){
		if (this.age != p.age){
			return (this.age - p.age) * -1;
		}
		if (this.A - p.A != 0){
			return this.A - p.A;
		}
		return this.order - p.order;

	}
	public int getA(){
		return this.A;
	}
	public int getB(){
		return this.B;
	}
	public int getC(){
		return this.C;
	}
	public int getio(){
		return this.io;
	}
	public int getOrder(){
		return this.order;
	}
	public int getRemaining(){
		return this.C - this.CPU_Util;
	}
	public int getQuantum(){
		return this.quantum;
	}
	public void subQuantum(){
		this.quantum--;
	}
	public void setQuantum(){
		this.quantum = 2;
	}
	public void setCPUBurst(int burst){
		this.CPUBurst = burst;
	}
	public int getCPUBurst(){
		return this.CPUBurst;
	}
	public void subCPUBurst(){
		this.CPUBurst--;
	}
	public void setIOBurst(int burst){
		this.IOBurst = burst;
	}
	public int getIOBurst(){
		return this.IOBurst;
	}
	public void subIOBurst(){
		this.IOBurst--;
	}
	public void setFinishTime(int time){
		this.finishTime = time;
	}
	public int getFinishTime(){
		return this.finishTime;
	}
	public int getTurnaroundTime(){
		return this.finishTime - this.A;
	}
	public int getCPU_Util(){
		return this.CPU_Util;
	}
	public void addCPU_Util(){
		this.CPU_Util++;
	}
	public int getIO_Util(){
		return this.IO_Util;
	}
	public void addIO_Util(){
		this.IO_Util++;
	}
	public void addWaitTime(){
		this.waitTime++;
	}
	public int getWaitTime(){
		return this.waitTime;
	}
	public void clearAge(){
		this.age = 0;
	}
	public void addAge(){
		this.age++;
	}
	public void setState(int state){
		this.state = state;
	}
	public String getState(){
		String str;
		switch (this.state) {
			case 0: str="unstarted"; break;
			case 1: str="ready"; break;
			case 2: str="running"; break;
			case 3: str="blocked"; break;
			case 4: str="terminated"; break;
			default: str=""; break;
		}
		return str;
	}
	public int getBurst(){
		
		if (this.state == 0 || this.state ==1 || this.state==4){
			return 0;
		}
		if (this.state == 2){
			return this.CPUBurst;
		}
		return this.IOBurst;
	}

}