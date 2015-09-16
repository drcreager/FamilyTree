package com.ko.na;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map.Entry;

import com.ko.na.database.FamilyTreeFactory;

import java.util.TreeMap;

public class Family extends TreeComponents implements Comparable<Family> {
	protected Person father;
	protected Person mother;
	protected Date effDate;
	protected ArrayList<Person> nextGetPerson;
	protected ArrayList<Family> nextFam;

	public Family(Person p1, Person p2) {
		super();
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
		nextGetPerson = new ArrayList<Person>();
		nextFam = new ArrayList<Family>();
	} // end constructor

	public int compareTo(Family f1) {
		if ((this.effDate == null) && (f1.effDate == null)) {
			return 0;
		} else if ((this.effDate == null) && (f1.effDate != null)) {
			return 1;
		} else if ((this.effDate != null) && (f1.effDate == null)) {
			return -1;
		} else {
			return (this.effDate.compareTo(f1.effDate));
		} // end if/else
	} // end compareTo() method

	public Person getChild(int i) {
		return this.nextGetPerson.get(i);
	}

	public ArrayList<Person> getChildren() {
		return nextGetPerson;
	}

	public Person[] getChildrenArray() {
		return nextGetPerson.toArray(new Person[nextGetPerson.size()]);
	}

	public Date getEffDate() {
		return effDate;
	}

	public Person getFather() {
		return father;
	}

	public ArrayList<Person> getGrandParents() {
		return nextGetPerson;
	}

	public Person[] getGrandParentsArray() {
		return nextGetPerson.toArray(new Person[nextGetPerson.size()]);
	}

	public Person getMother() {
		return mother;
	}

	public String getName() {
		return getName(true);
	}

	public String getName(boolean idFlg) {
		return father.getName(idFlg, false, false) + " and " + mother.getName(idFlg, false, true);
	}

	public ArrayList<Family> getNextFam() {
		return nextFam;
	}

	public Family getNextFam(int i) {
		return this.nextFam.get(i);
	}

	public ArrayList<Family> getNextFam(Person p1) {
		ArrayList<Family> result = new ArrayList<Family>();

		for (Family f1 : this.getNextFam()) {
			if (p1.isMale()) {
				if (p1.getId() == f1.getFather().getId()) {
					result.add(f1);
				} // end if
			} else {
				if (p1.getId() == f1.getMother().getId()) {
					result.add(f1);
				} // end if
			} // end if/else
		} // end for
		return result;
	} // end getNextFam() method

	public Family[] getNextFamArray() {
		return nextFam.toArray(new Family[nextFam.size()]);
	}

	public boolean hasOwnFamily(Person p1) {
		boolean result = false;
		for (Family f1 : getNextFam()) {
			if (p1.isMale()) {
				result = (f1.getFather().getId() == p1.getId());
			} else {
				result = (f1.getMother().getId() == p1.getId());
			} // end if/else
			if (result)
				break;
		} // end for
		return result;
	} // end hasOwnFamily() method

	public void setChild(Person p1) {
		this.nextGetPerson.add(p1);
	}

	public void setChildren(ArrayList<Person> children) {
		this.nextGetPerson = children;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public void setEffDate(String effDate) throws Exception {
		this.effDate = CDate(effDate);
	}

	public void setFather(Person father) {
		this.father = father;
	}

	public void setMother(Person mother) {
		this.mother = mother;
	}

	public void setNextFam(ArrayList<Family> nextFam) {
		this.nextFam = nextFam;
	}

	public void setNextFam(Family f1) {
		this.nextFam.add(f1);
	}

	public TreeMap<String, String> toAncestralMap(String indx, boolean idFlg) throws Exception {
		TreeMap<String, String> tmap = new TreeMap<String, String>();
		int i = 0;
		/*
		 * Insert Family Name
		 */
		tmap.put(indx,  ((father != null) ? father.toMap(idFlg) : "Unknown") + " and " 
		              + ((mother != null) ? mother.toMap(idFlg) : "Unknown") 
				+ ((effDate != null) ? " m. " + CDate(1, getEffDate()) : ""));
		/*
		 * Browse Next Families adding an entry for each
		 */
		for (Family f1 : getNextFam()) {
			if (f1 != null) {
				String newIndx = indx + "." + CHARS.charAt(i);
				for (Entry<String, String> entry : f1.toAncestralMap(newIndx, idFlg).entrySet()) {
					tmap.put(entry.getKey(), entry.getValue());
				}
				i++;
			}
		} // end for

		return tmap;
	} // end toAncestralMap method

	public TreeMap<String, String> toDescendantsMap(String indx, boolean idFlg) throws Exception {
		TreeMap<String, String> tmap = new TreeMap<String, String>();
		int i = 0;
		/*
		 * Insert Family Name
		 */
		tmap.put(indx, father.toMap(idFlg) + " and " + mother.toMap(idFlg)
				+ ((effDate != null) ? " m. " + CDate(1, getEffDate()) : ""));
		/*
		 * Browse Children adding an entry for each
		 */
		for (Person child : getChildren()) {
			String newIndx = indx + "." + CHARS.charAt(i);
			if (hasOwnFamily(child)) {
				/*
				 * Add all of each child's families
				 */
				int j = 0;
				ArrayList<Family> famList = getNextFam(child);
				Collections.sort(famList);
				for (Family f1 : famList) {
					/*
					 * Separate multiple families with an alpha suffix
					 */
					if (famList.size() > 1) {
						for (Entry<String, String> entry : f1.toDescendantsMap(newIndx + ":" + CHARS.charAt(j++), idFlg)
								.entrySet()) {
							tmap.put(entry.getKey(), entry.getValue());
						}

					} else {
						for (Entry<String, String> entry : f1.toDescendantsMap(newIndx, idFlg).entrySet()) {
							tmap.put(entry.getKey(), entry.getValue());
						}

					} // end if/else
				} // end for
			} else {
				tmap.put(newIndx, child.toMap(idFlg));
			} // end if
			i++;
		} // end for

		return tmap;
	} // end toDescendantsMap method

	public String toJson() {
		return toJson("Family", false);
	}

	public String toJson(String objName, boolean embedded) {
		StringBuffer buf = new StringBuffer();

		buf.append(((embedded) ? "\"" : "{\"") + objName + "\":{\n");
		if (father != null)
			buf.append(father.toJson("Father", embedded) + ",\n");
		if (mother != null)
			buf.append(mother.toJson("Mother", embedded) + ",\n");
		if (effDate != null)
			buf.append("\"Effective Date\":\"" + CDate(effDate) + "\",\n");

		for (Person p1 : nextGetPerson) {
			if (p1 != null)
				buf.append(p1.toJson("Child", embedded) + ",\n");
		}
		for (Family f1 : nextFam) {
			if (f1 != null)
				buf.append(f1.toJson("Family", embedded) + ",\n");
		}

		return buf.toString().substring(0, buf.length() - 2) + ((embedded) ? "}" : "}}");
	} // end toJson() method

	public TreeMap<String, String> toMap(int generation, String indx, boolean idFlg) throws Exception {
		TreeMap<String, String> map = null;
		switch (generation) {
		case FamilyTreeFactory.ANCESTORS:
			map = toAncestralMap(indx, idFlg);
			break;

		case FamilyTreeFactory.DESCENDANTS:
			map = toDescendantsMap(indx, idFlg);
			break;
		} // end switch
		return map;
	}

	public String toString() {
		return toString(false, false);
	}

	public String toString(boolean children, boolean families) {
		StringBuffer buf = new StringBuffer();
		buf.append(getName() + "\n");
		if (children) {
			for (Person child : getChildren()) {
				if (!hasOwnFamily(child)) {
					buf.append(child.toString() + "\n");
				} // end if
			} // end for
		} // end if

		if (families) {
			for (Family f1 : this.getNextFam()) {
				buf.append(f1.toString(children, families) + "\n");
			} // end for
		} // end if

		return buf.toString().substring(0, buf.length() - 1);
	} // end toString() method

	public static void main(String[] args) throws Exception {
		Family f = new Family(new Person("Dad", "Creager", "Male"), new Person("Mom", "Creager", "Female"));
		System.out.println(f.toJson("Family", true));
	} // end main() method
} // end Family class