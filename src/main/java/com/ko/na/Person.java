package com.ko.na;

import java.util.Date;
import java.util.Map.Entry;

import com.ko.na.database.FamilyTreeFactory;

public class Person extends TreeComponents {
	protected int      id;
	protected String   given;
	protected String   surname;
	protected String   familiar;
	protected String   birth_surname;
	protected Date     birthdate;
	protected Date     deathdate;
	protected String   gender;
	protected Location birthloc;
	protected Location deathloc;
	protected Location internment;
	protected String   notations;
	protected int      source;
	protected String   occupation;

	protected boolean  adopted;

	public Person() {
		this("", "", "Male");
	}

	public Person(String given, String surname) {
		this(given, surname, "Male");
	}

	public Person(String given, String surname, String gender) {
		super();
		setId(NEW_RCRD);
		setGiven(given);
		setSurname(surname);
		setGender(gender);
		setAdopted(FALSE);
	}

	public int getAdopted() {
		return (isAdopted()) ? TRUE : FALSE;
	}

	public String getBirth_surname() {
		return birth_surname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public String getBirthdateStr() {
		return (birthdate != null) ? CDate(0, birthdate) : "";
	}

	public String getBirthlocStr() {
		return birthloc.toString();
	}
	
	public Location getBirthloc() {
		return birthloc;
	}

	public Date getDeathdate() {
		return deathdate;
	}

	public String getDeathdateStr() {
		return (deathdate != null) ? CDate(0, deathdate) : "";
	}

	public String getDeathlocStr() {
		return deathloc.toString();
	}

	public Location getDeathloc() {
		return deathloc;
	}

	public String getFamiliar() {
		return familiar;
	}

	public String getGender() {
		return gender;
	}

	public String getGiven() {
		return given;
	}

	public int getId() {
		return id;
	}
	
	public Location getInternment() {
		return internment;
	}

	public String getInternmentStr() {
		return internment.toString();
	}

	public String getName() {
		return getName(true);
	}

	public String getName(boolean idFlg) {
		return getName(idFlg, false, true);
	}

	public String getName(boolean idFlg, boolean familiarFlg, boolean maiden) {
		if (isEmpty()) {
			return "[Empty Person]";
		} else {
			return getGiven()
			        + (((familiar == null) || (familiar.length() <= 0) || !familiarFlg) ? " " : " '" + familiar + "' ")
					+ (((birth_surname == null) || (birth_surname.length() <= 0) || !maiden) ? surname
							: ((birth_surname == null) || (birth_surname.length() <= 0)) ? surname : birth_surname)
					+ ((idFlg) ? "(" + getId() + ")" : "");
		} // end if/else
	} // end getName() method

	public String getNotations() {
		return notations;
	}

	public String getOccupation() {
		return (occupation != null) ? occupation : "";
	}

	public int getSource() {
		return source;
	}

	public String getSurname() {
		return surname;
	}

	public boolean isAdopted() {
		return adopted;
	}

	public boolean isEmpty() {
		return ((id == NEW_RCRD) && ((given == null) || (given.equals("")))
				&& ((surname == null) || (surname.equals(""))));
	}

	public boolean isMale() {
		return gender.equalsIgnoreCase("Male");
	}

	public boolean isNewPerson() {
		return (id == NEW_RCRD);
	}

	public void setAdopted(boolean adopted) {
		this.adopted = adopted;
	}

	public void setAdopted(int adopted) {
		this.adopted = (adopted == TRUE);
	}

	public void setBirth_surname(String birth_surname) {
		this.birth_surname = birth_surname;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public void setBirthdate(String birthdate) throws Exception {
		this.birthdate = CDate(birthdate);
	}
	
	public void setBirthloc(Location loc) {
		this.birthloc = loc;
	}

	public void setBirthloc(String birthloc) throws Exception {
		this.birthloc = Location.parse(birthloc);
	}

	public void setDeathdate(Date deathdate) {
		this.deathdate = deathdate;
	}

	public void setDeathdate(String deathdate) throws Exception {
		this.deathdate = CDate(deathdate);
	}
	
	public void setDeathloc(Location loc) {
		this.deathloc = loc;
	}

	public void setDeathloc(String deathloc) throws Exception {
		this.deathloc = Location.parse(deathloc);
	}

	public void setFamiliar(String familiar) {
		this.familiar = familiar;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setGiven(String given) {
		this.given = given;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setInternment(Location internment) {
		this.internment = internment;
	}

	public void setInternment(String internment) throws Exception {
		this.internment = Location.parse(internment);
	}

	public void setNotations(String notations) {
		this.notations = notations;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String toJson() {
		return toJson("Person", false);
	}

	public String toJson(String objName, boolean embedded) {
		StringBuffer buf = new StringBuffer();

		buf.append(((embedded) ? "\"" : "{\"") + objName + "\":{\n");
		if (isAvailable(id))
			buf.append("\"Id\":\"" + id + "\",\n");
		if (isAvailable(given))
			buf.append("\"Given\":\"" + given + "\",\n");
		if (isAvailable(surname))
			buf.append("\"Surname\":\"" + surname + "\",\n");
		if (isAvailable(familiar))
			buf.append("\"Familiar\":\"" + familiar + "\",\n");
		if (isAvailable(birth_surname))
			buf.append("\"Birth Surname\":\"" + birth_surname + "\",\n");
		if (isAvailable(birthdate))
			buf.append("\"Birth Date\":\"" + CDate(birthdate) + "\",\n");
		if (isAvailable(deathdate))
			buf.append("\"Death Date\":\"" + CDate(deathdate) + "\",\n");
		if (isAvailable(gender))
			buf.append("\"Gender\":\"" + gender + "\",\n");
		if (isAvailable(birthloc))
			buf.append("\"Birth Loc\":\"" + getBirthlocStr() + "\",\n");
		if (isAvailable(internment))
			buf.append("\"Internment\":\"" + getInternmentStr() + "\",\n");
		if (isAvailable(notations))
			buf.append("\"Notations\":\"" + notations + "\",\n");
		if (isAvailable(source))
			buf.append("\"Source\":\"" + source + "\",\n");
		if (isAvailable(adopted))
			buf.append("\"Adopted\":\"" + getAdopted() + "\",\n");

		return buf.toString().substring(0, buf.length() - 2) + ((embedded) ? "\n}" : "\n}}");
	} // end toJson() method

	/*
	 * this method gathers the data for a map of descendants
	 */
	public String toMap(boolean idFlg) {
		return getName(idFlg) + ((birthdate != null) ? " b. " + CDate(1, getBirthdate()) : "")
				+ ((deathdate != null) ? " d. " + CDate(1, getDeathdate()) : "");
	} // end toMap() method

	public String toString() {
		return getName();
	} // end toString() method

	public String tree2Json(Family[] tree, boolean idFlg) throws Exception {
		StringBuffer buf = new StringBuffer();
		for (Family f1 : tree) {
			buf.append("{\"FamilyTree\":[\n");
			for (Entry<String, String> entry : f1.toMap(FamilyTreeFactory.DESCENDANTS, "1", idFlg).headMap("z")
					.entrySet()) {
				buf.append("{\"Key\":\"" + entry.getKey() + "\",");
				buf.append("\"Value\":\"" + entry.getValue() + "\"},\n");
			} // end for
		} // end for
		return buf.toString().substring(0, buf.length() - 2) + "\n]}";
	} // end tree2Json() method

	public static void main(String[] args) throws Exception {
		Person p = new Person("John", "Doe");
		p.setBirthdate("1953");
		System.out.println(p.toJson());
	} // end main() method

} // end Person class
