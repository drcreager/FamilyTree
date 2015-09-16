package com.ko.na.microservices;

import java.io.IOException;

/**
 * Service Manager to coordinate and balance loads for the Microservices.
 * 
 * @author Daniel R. Creager
 */
public class SrvcMgr  {
	protected Process[] process;
	protected int inst;
	protected boolean active;
	
	public final static int POLL_INTVL = 5;   // unit of time is seconds
	public final static long MILLISECOND = 1000;
	
	public SrvcMgr(){
		process = new Process[10];
		inst = 0;
		setActive(true);
	} // end constructor

	
	public void addProcess(String cmmd, String... arg) throws IOException{
		process[inst] = new ProcessBuilder(cmmd, arg[0]).start();
	} // end addProcess() method

	public Process[] getProcess() {
		return process;
	} // end getProcess() method

	public boolean isActive(){
		return active;
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	public void setProcess(Process[] process) {
		this.process = process;
	} // end setProcess() method 
	
	class ShutdownThrd extends Thread {
		protected PersonSrvc perSrvc;
		
		public ShutdownThrd(PersonSrvc arg){
			perSrvc = arg;
		} // end constructor
		
		public void run(){
			System.out.println("SrvcMgr: running shutdown procedure ...");
			perSrvc.terminate();
			setActive(false);
		} // end run() method 
	} // end ShutdownThrd class

	public static void main(String[] args) throws Exception {
		SrvcMgr srvcMgr = new SrvcMgr();
		PersonSrvc perSrvc = new PersonSrvc();
		Runtime.getRuntime().addShutdownHook(srvcMgr.new ShutdownThrd(perSrvc));		
		
		while (srvcMgr.isActive()) Thread.sleep(POLL_INTVL * MILLISECOND);
	} // end main() method

}
