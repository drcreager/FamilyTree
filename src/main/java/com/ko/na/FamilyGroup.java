package com.ko.na;

import java.util.ArrayList;
import java.util.Date;

public class FamilyGroup extends TreeComponents implements Comparable<FamilyGroup> {
	protected Date[]            effDate;
	protected int[]             coupleId;
	protected ArrayList<Person> people;
	public static final int FATHER = 0;
	public static final int MOTHER = 1;
	public static final int FFATHER = 2;
	
	public static final int FMOTHER = 3;
	public static final int MFATHER = 4;
	public static final int MMOTHER = 5;
	public static final int FIRST_CHILD = 6;
	
	public FamilyGroup() {
		super();
		coupleId = new int[3];
		effDate  = new Date[3];
		for (int i=0; i<coupleId.length; i++) coupleId[i] = NEW_RCRD;
		for (int i=0; i<coupleId.length; i++) effDate[i]  = null;
		people = new ArrayList<Person>();
	} // end constructor

	public FamilyGroup(Person p1, Person p2) {
		this();
		init(p1,p2);
	} // end constructor

	public int compareTo(FamilyGroup f1) {
		if ((this.effDate[0] == null) && (f1.effDate[0] == null)) {
			return 0;
		} else if ((this.effDate[0] == null) && (f1.effDate[0] != null)) {
			return 1;
		} else if ((this.effDate[0] != null) && (f1.effDate[0] == null)) {
			return -1;
		} else {
			return (this.effDate[0].compareTo(f1.effDate[0]));
		} // end if/else
	} // end compareTo() method

	public Person getChild(int i) {
		if (i < FIRST_CHILD) i = FIRST_CHILD;
		return people.get(i);
	}
	
	public ArrayList<Person> getChildren() {
		return people;
	}
	
	public Person[] getChildrenArray() {
		ArrayList<Person> work = new ArrayList<Person>();
		
		for(int i=FIRST_CHILD; i<people.size(); i++) work.add(people.get(i));
		work.trimToSize();
		return work.toArray(new Person[work.size()]);
	}
	
	public int[] getCouple() {
		return coupleId;
	}
	
	public int getCoupleId(int ofst) {
		return coupleId[ofst];
	}
	
	public Date getEffDate(int ofst) {
		return effDate[ofst];
	}
	
	public Person getFather() {
		return getPerson(FATHER);
	}

	public String getFbirthCity(){
		return (getFather() != null) ? getFather().getBirthloc().getCity() : "";		
	}
	
	public String getFbirthCountry(){
		return (getFather() != null) ? getFather().getBirthloc().getCountry() : "";		
	}
	
	public String getFbirthdate(){
		return (getFather() != null) ? getFather().getBirthdateStr() : "";
	}
	
	public String getFoccupation(){
		return (getFather() != null) ? getFather().getOccupation() : "";
	}
	
	public String getMoccupation(){
		return (getMother() != null) ? getMother().getOccupation() : "";
	}
	
	public void setFoccupation(String arg){
		if (getFather() != null) getFather().setOccupation(arg);
	}
	
	public void setMoccupation(String arg){
		if (getMother() != null) getMother().setOccupation(arg);
	}

	public String getFbirthloc(){
		return (getFather() != null) ? getFather().getBirthlocStr() : "";		
	}
	
	public String getFbirthRegion(){
		return (getFather() != null) ? getFather().getBirthloc().getRegion() : "";		
	}
	
	public String getFbirthState(){
		return (getFather() != null) ? getFather().getBirthloc().getState() : "";		
	}
	
	public String getFbirthSubReg(){
		return (getFather() != null) ? getFather().getBirthloc().getSubRegion() : "";		
	}
	
	public String getFburiedCity(){
		return (getFather() != null) ? getFather().getInternment().getCity() : "";		
	}
	
	public String getFburiedCountry(){
		return (getFather() != null) ? getFather().getInternment().getCountry() : "";		
	}
	
	public String getFburiedRegion(){
		return (getFather() != null) ? getFather().getInternment().getRegion() : "";		
	}
	
	public String getFburiedState(){
		return (getFather() != null) ? getFather().getInternment().getState() : "";		
	}
	
	public String getFburiedSubReg(){  // Cemetary
		return (getFather() != null) ? getFather().getInternment().getSubRegion() : "";		
	}
	
	public String getFdeathCity(){
		return (getFather() != null) ? getFather().getDeathloc().getCity() : "";		
	}
	
	public String getFdeathCountry(){
		return (getFather() != null) ? getFather().getDeathloc().getCountry() : "";		
	}
	
	public String getFdeathdate(){
		return (getMother() != null) ? getFather().getDeathdateStr() : "";
	}
	
	public String getFdeathRegion(){
		return (getFather() != null) ? getFather().getDeathloc().getRegion() : "";		
	}
	
	public String getFdeathState(){
		return (getFather() != null) ? getFather().getDeathloc().getState() : "";		
	}

	public String getFdeathSubReg(){
		return (getFather() != null) ? getFather().getDeathloc().getSubRegion() : "";		
	}
	
	public Person getFFather() {
		return getPerson(FFATHER);
	}
	
	public String getFfsurname(){
		return (getFFather() != null) ? getFFather().getSurname() : "";	
	}
	
	public String getFfgiven(){
		return (getFFather() != null) ? getFFather().getGiven() : "";	
	}

	public String getFmgiven(){
		return (getFMother() != null) ? getFMother().getGiven() : "";	
	}

	public String getFgiven(){
		return (getMother() != null) ? getFather().getGiven() : "";	
	}
	
	public Person getFMother() {
		return getPerson(FMOTHER);
	}
	
	public String getFmsurname(){
		return (getFMother() != null) ? getFMother().getSurname() : "";	
	}
	
	public String getFsurname(){
		return (getMother() != null) ? getFather().getSurname() : "";		
	}
	
	public ArrayList<Person> getGrandParents(int parentCode) {
		ArrayList<Person> work = new ArrayList<Person>();
		switch (parentCode){
		case FATHER:
			work.add(getPerson(FFATHER));
			work.add(getPerson(FMOTHER));
			break;
			
		case MOTHER:
			work.add(getPerson(MFATHER));
			work.add(getPerson(MMOTHER));
			break;
			
		} // end switch
		return work;
	}
	
	public Person[] getGrandParentsArray(int parentCode) {
		ArrayList<Person> list = getGrandParents(parentCode);
		return list.toArray(new Person[list.size()]);
	} // end getGrandParentsArray() method 
	
	public String getMbirthCity(){
		return (getMother() != null) ? getMother().getBirthloc().getCity() : "";		
	}
	
	public String getMbirthCountry(){
		return (getMother() != null) ? getMother().getBirthloc().getCountry() : "";		
	}
	
	public String getMbirthdate(){
		return (getMother() != null) ? getMother().getBirthdateStr() : "";
	}
	
	public String getMbirthloc(){
		return (getMother() != null) ? getMother().getBirthlocStr() : "";		
	}
	
	public String getMbirthRegion(){
		return (getMother() != null) ? getMother().getBirthloc().getRegion() : "";		
	}
	
	public String getMbirthState(){
		return (getMother() != null) ? getMother().getBirthloc().getState() : "";		
	}
	
	public String getMbirthSubReg(){
		return (getMother() != null) ? getMother().getBirthloc().getSubRegion() : "";		
	}
	
	public String getMburiedCity(){
		return (getMother() != null) ? getMother().getInternment().getCity() : "";		
	}
	
	public String getMburiedCountry(){
		return (getMother() != null) ? getMother().getInternment().getCountry() : "";		
	}
	
	public String getMburiedRegion(){
		return (getMother() != null) ? getMother().getInternment().getRegion() : "";		
	}
	
	public String getMburiedState(){
		return (getMother() != null) ? getMother().getInternment().getState() : "";		
	}
	
	public String getMburiedSubReg(){  // Cemetary
		return (getMother() != null) ? getMother().getInternment().getSubRegion() : "";		
	}

	public String getMdeathCity(){
		return (getMother() != null) ? getMother().getDeathloc().getCity() : "";		
	}
	
	public String getMdeathCountry(){
		return (getMother() != null) ? getMother().getDeathloc().getCountry() : "";		
	}
	
	public String getMdeathdate(){
		return (getMother() != null) ? getMother().getDeathdateStr() : "";
	}
	
	public String getMdeathRegion(){
		return (getMother() != null) ? getMother().getDeathloc().getRegion() : "";		
	}
	
	public String getMdeathState(){
		return (getMother() != null) ? getMother().getDeathloc().getState() : "";		
	}

	public String getMdeathSubReg(){
		return (getMother() != null) ? getMother().getDeathloc().getSubRegion() : "";		
	}
	
	public Person getMFather() {
		return getPerson(MFATHER);
	}

	public String getMfgiven(){
		return (getMFather() != null) ? getMFather().getGiven() : "";	
	}
	
	public String getMfsurname(){
		return (getMFather() != null) ? getMFather().getSurname() : "";	
	}
	
	public String getMgiven(){
		return (getMother() != null) ? getMother().getGiven() : "";	
	}

	public String getMmgiven(){
		return (getMMother() != null) ? getMMother().getGiven() : "";	
	}

	public Person getMMother() {
		return getPerson(MMOTHER);
	}
	
	public String getMmsurname(){
		return (getMMother() != null) ? getMMother().getSurname() : "";	
	}

	public Person getMother() {
		return getPerson(MOTHER);
	}
	
	public String getMsurname(){
		return (getMother() != null) 
				? getMother().getSurname()
				: "";		
	}
	
	/*
	public String getMsurname(){
		return (getMother() != null) 
				? (getMother().getBirth_surname() != null) 
						? getMother().getBirth_surname() 
						: getMother().getSurname() 
				: "";		
	}
	*/
	public String getName() {
		return getName(true);
	}
	public String getName(boolean idFlg) {
		return getPerson(FATHER).getName(idFlg, false, false) + " and " + getPerson(MOTHER).getName(idFlg, false, true);
	}
	
	public 	ArrayList<Person> getPeople(){
		return people;
	}

	public Person getPerson(int ofst) {
		return (people.size() > ofst) ? people.get(ofst) : null;
	}

	public void init(Person p1, Person p2){
		if (p1.isMale()) {
			setFather(p1);
		} else {
			setMother(p1);
		} // end if

		if (p2.isMale()) {
			setFather(p2);
		} else {
			setMother(p2);
		} // end if
	} // end init() method

	public boolean isEmpty(){
		return (people.get(0) == null || people.get(1) == null);
	}
	
	public boolean isNewCouple(int ofst){
		return (coupleId[ofst] == NEW_RCRD);
	}
	
	public void setChild(int pos, Person p1) {
		if (pos < FIRST_CHILD) pos = FIRST_CHILD;
		//TODO do we need to vary where the date is set?
		setEffDate(0, p1.getWorkDate());
		people.add(p1);
	}
	
	public void setChildren(ArrayList<Person> children){
		int i = FIRST_CHILD;
		for(Person child : children){
			setChild(i++, child);
		} // end for 
	} // end setChildren() method
	
	public void setCoupleId(int ofst, int coupleId) {
		this.coupleId[ofst] = coupleId;
	}
	
	public void setEffDate(int ofst, Date effDate) {
		this.effDate[ofst] = effDate;
	}
	
	public void setEffDate(int ofst, String effDate) throws Exception {
		this.effDate[ofst] = CDate(effDate);
	}
	
	public void setFather(Person father) {
		setPerson(FATHER, father);
	}
	
	public void setFbirthCity(String city){
		if (getFather() != null) getFather().getBirthloc().setCity(city);		
	}
	
	public void setFbirthCountry(String country){
		if (getFather() != null) getFather().getBirthloc().setCountry(country);		
	}
	
	public void setFbirthdate(String date) throws Exception{
		if (date != null) getFather().setBirthdate(date);
	}
	
	public void setFbirthloc(String loc) throws Exception{
		if (loc != null) getFather().setBirthloc(loc);		
	}
	
	public void setFbirthRegion(String reg){
		if (getFather() != null) getFather().getBirthloc().setRegion(reg);		
	}
	
	public void setFbirthState(String state){
		if (getFather() != null) getFather().getBirthloc().setState(state);		
	}
	
	public void setFbirthSubReg(String subReg){
		if (getFather() != null) getFather().getBirthloc().setSubRegion(subReg);		
	}

	public void setFburiedCity(String city){
		if (getFather() != null) getFather().getInternment().setCity(city);		
	}

	public void setFburiedCountry(String country){
		if (getFather() != null) getFather().getInternment().setCountry(country);		
	}
	
	public void setFburiedRegion(String region){
		if (getFather() != null) getFather().getInternment().setRegion(region);		
	}
	
	public void setFburiedState(String state){
		if (getFather() != null) getFather().getInternment().setState(state);		
	}
	
	public void setFburiedSubReg(String subReg){  // Cemetary
		if (getFather() != null) getFather().getInternment().setSubRegion(subReg);		
	}
	
	public void setFdeathCity(String city){
		if (getFather() != null) getFather().getDeathloc().setCity(city);		
	}
	
	public void setFdeathCountry(String country){
		if (getFather() != null) getFather().getDeathloc().setCountry(country);		
	}
	
	public void setFdeathdate(String date) throws Exception{
		if (date != null) getFather().setDeathdate(date);
	}
	
	public void setFdeathRegion(String reg){
		if (getFather() != null) getFather().getDeathloc().setRegion(reg);		
	}
	
	public void setFdeathState(String state){
		if (getFather() != null) getFather().getDeathloc().setState(state);		
	}
	
	public void setFdeathSubReg(String subReg){
		if (getFather() != null) getFather().getDeathloc().setSubRegion(subReg);		
	}
	
	public void setFFather(Person father) {
		setPerson(FFATHER,father);
	}
	
	public void setFfgiven(String given){
		if (getFFather() != null) getFFather().setGiven(given);	
	}
	
	public void setFgiven(String name){
		if (name != null) getFather().setGiven(name);	
	}
	
	public void setFmgiven(String given){
		if (getFMother() != null) getFMother().setGiven(given);	
	}
	
	public void setFMother(Person mother) {
		setPerson(FMOTHER, mother);
	}
	
	public void setFmsurname(String surname){
		if (getMMother() != null) getMMother().setSurname(surname);	
	}
	
	public void setFsurname(String name){
		if (name != null) getFather().setSurname(name);		
	}
	
	public void setGrandParents(int parentCode, ArrayList<Person> grandparents) {
		switch (parentCode){
		case FATHER:
			if (grandparents.size() >= 2){
				people.add(FFATHER,grandparents.get(0));
				people.add(FMOTHER,grandparents.get(1));
			} else {
				people.add(FFATHER,new Person("","","Male"));
				people.add(FMOTHER,new Person("","","Female"));
			}// end if 
			break;
			
		case MOTHER:
			if (grandparents.size() >= 2){
				people.add(MFATHER,grandparents.get(0));
				people.add(MMOTHER,grandparents.get(1));
			} else {
				people.add(MFATHER,new Person("","","Male"));
				people.add(MMOTHER,new Person("","","Female"));
			}// end if  
			break;
			
		} // end switch
	} // setGrandParents() method 
	
	public void setMbirthCity(String city){
		if (getMother() != null) getMother().getBirthloc().setCity(city);		
	}
	
	public void setMbirthCountry(String country){
		if (getMother() != null) getMother().getBirthloc().setCountry(country);		
	}
	
	public void setMbirthdate(String date) throws Exception{
		if (date != null) getMother().setBirthdate(date);
	}
	
	public void setMbirthloc(String loc) throws Exception{
		if (loc != null) getMother().setBirthloc(loc);		
	}
	
	public void setMbirthRegion(String reg){
		if (getMother() != null) getMother().getBirthloc().setRegion(reg);		
	}

	public void setMbirthState(String state){
		if (getMother() != null) getMother().getBirthloc().setState(state);		
	}

	public void setMbirthSubReg(String subReg){
		if (getMother() != null) getMother().getBirthloc().setSubRegion(subReg);		
	}

	public void setMburiedCity(String city){
		if (getMother() != null) getMother().getInternment().setCity(city);		
	}
	
	public void setMburiedCountry(String country){
		if (getMother() != null) getMother().getInternment().setCountry(country);		
	}
	
	public void setMburiedRegion(String region){
		if (getMother() != null) getMother().getInternment().setRegion(region);		
	}
	
	public void setMburiedState(String state){
		if (getMother() != null) getMother().getInternment().setState(state);		
	}
	
	public void setMburiedSubReg(String subReg){  // Cemetary
		if (getMother() != null) getMother().getInternment().setSubRegion(subReg);		
	}
	
	public void setMdeathCity(String city){
		if (getMother() != null) getMother().getDeathloc().setCity(city);		
	}
	
	public void setMdeathCountry(String country){
		if (getMother() != null) getMother().getDeathloc().setCountry(country);		
	}
	
	public void setMdeathdate(String date) throws Exception{
		if (date != null) getMother().setDeathdate(date);
	}

	public void setMdeathRegion(String reg){
		if (getMother() != null) getMother().getDeathloc().setRegion(reg);		
	}

	public void setMdeathState(String state){
		if (getMother() != null) getMother().getDeathloc().setState(state);		
	}
	
	public void setMdeathSubReg(String subReg){
		if (getMother() != null) getMother().getDeathloc().setSubRegion(subReg);		
	}

	public void setMFather(Person father) {
		setPerson(MFATHER,father);
	}
	
	public void setMfgiven(String given){
		if (getMFather() != null) getMFather().setGiven(given);	
	}

	public void setMfsurname(String surname){
		if (getMFather() != null) getMFather().setSurname(surname);	
	}

	public void setMgiven(String name){
		if (name != null) getMother().setGiven(name);	
	}

	public void setMmgiven(String given){
		if (getMMother() != null) getMMother().setGiven(given);	
	}

	public void setMMother(Person mother) {
		setPerson(MMOTHER, mother);
	}

	public void setMmsurname(String surname){
		if (getMMother() != null) getMMother().setSurname(surname);	
	}

	public void setMother(Person mother) {
		setPerson(MOTHER, mother);
	}

	public void setMsurname(String name){
		if (name != null) getMother().setSurname(name);		
	}

	public void setPeople(ArrayList<Person> people) {
		this.people = people;
	}

	public void setPerson(int ofst, Person p) {
		people.add(ofst, p);
	}
	
	public String toString(){
		return people.toString();
	}

	public static void main(String[] args) throws Exception {
		FamilyGroup f = new FamilyGroup(new Person("Don", "Creager", "Male"), 
	            new Person("Ginny", "Creager", "Female"));
		f.setEffDate(0,"8/17/1949");
		f.getMother().setAdopted(true);
		f.getFather().setDeathloc(Location.parse("Conklin;Chester Twp.;Ottawa Co.;MI;USA"));
		f.getMother().setDeathloc(Location.parse("Conklin;Chester Twp.;Ottawa Co.;MI;USA"));
	
		ArrayList<Person> g1 = new ArrayList<Person>();
		g1.add(new Person("Thomas Henry","Creager"));
		g1.add(new Person("Helen Ethel","Pratt"));
	
		ArrayList<Person> g2 = new ArrayList<Person>();
		g2.add(new Person("Charles Grover","Batson"));
		g2.add(new Person("Helen Sarah","Stephens"));
		
		f.setGrandParents(FamilyGroup.FATHER, g1);
		f.setGrandParents(FamilyGroup.MOTHER, g2);
		
		f.setChild(FamilyGroup.FIRST_CHILD, new Person("Charles Thomas","Creager"));		
		f.setChild(FamilyGroup.FIRST_CHILD, new Person("Frederick Joseph","Creager"));		
		f.setChild(FamilyGroup.FIRST_CHILD, new Person("Daniel Ross","Creager"));		
		f.setChild(FamilyGroup.FIRST_CHILD, new Person("Kenneth Stanley","Creager"));		
		f.setChild(FamilyGroup.FIRST_CHILD, new Person("Richard Frances","Creager"));		
		f.setChild(FamilyGroup.FIRST_CHILD, new Person("Michael Patrick","Creager"));		
		f.setChild(FamilyGroup.FIRST_CHILD, new Person("Francis Helen","Creager"));
		
		System.out.println(f);
	} 	// end main() method
} // end FamilyGroup class