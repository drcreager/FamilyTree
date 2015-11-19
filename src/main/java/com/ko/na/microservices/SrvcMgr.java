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
	protected String  pckgName;
	
	public final static int  POLL_INTVL  = 5;   // unit of time is seconds
	public final static long MILLISECOND = 1000;
	public final static String[] SERVICES = {"PersonSrvc","FamilySrvc","EventSrvc","MediaSrvc"};
	public final static String PATH = "C:/Users/Z8364A/Documents/My Projects/GeneologyWS/FamilyTree2";
	
	public SrvcMgr(){
		pckgName = getClass().getPackage().getName();
		process = new Process[10];
		inst = 0;
		setActive(true);
	} // end constructor
	
	public Process addProcess(String service) throws IOException{

		Process p = null;
		try {
			p = new ProcessBuilder("cmd", PATH + "/process.bat ", service).start();
		} catch (IOException ex1){
			ex1.printStackTrace();
		} // end try/catch 
		return p;
	} // end addProcess() method

	public Process[] getProcess() {
		return process;
	} // end getProcess() method

	public boolean isActive(){
		return active;
	}
	
	public void loadSrvcs() throws Exception{
        for (String name : SrvcMgr.SERVICES){
        	process[inst++] = addProcess(name);
        } // end for 
	} // end loadSrvcs() method
	
	public void loadSrvcClasses() throws Exception{
        for (String name : SrvcMgr.SERVICES){
    		Class<?> cls = Class.forName(pckgName + "." + name);
    		SrvcBase inst = (SrvcBase) cls.newInstance();
    		Runtime.getRuntime().addShutdownHook(new ShutdownThrd(inst));
        } // end for 
	} // end loadSrvcClasses() method
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	public void setProcess(Process[] process) {
		this.process = process;
	} // end setProcess() method 
	
	class ShutdownThrd extends Thread {
		protected SrvcBase srvc;
		
		public ShutdownThrd(SrvcBase arg){
			srvc = arg;
		} // end constructor
		
		public void run(){
			System.out.println(this.getClass().getName() + " is running shutdown procedure ...");
			((SrvcBase) srvc).terminate();
			setActive(false);
		} // end run() method 
	} // end ShutdownThrd class

	public static void main(String[] args) throws Exception {
		SrvcMgr srvcMgr = new SrvcMgr();
		srvcMgr.loadSrvcClasses();
		while (srvcMgr.isActive()) Thread.sleep(POLL_INTVL * MILLISECOND);
	} // end main() method

}
