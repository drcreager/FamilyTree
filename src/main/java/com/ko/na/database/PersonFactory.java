package com.ko.na.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ko.na.Person;

public class PersonFactory extends SqlTable {
	
	private static final String QUERY = "update " + SqlTable.DATABASE + ".t_person set "
			+ "given = ?, surname = ?,  familiar = ?, birth_surname = ?, birthdate = ?, deathdate = ?, "
			+ "gender = ?, birthloc = ?, deathloc = ?, internment = ?, notations = ?, "
			+ "source = ?, adopted = ? where id = ?";

	public PersonFactory() throws Exception {
		super();
		prepareStatement(QUERY);
	} // end constructor

	public void execEdits() throws Exception{
		for (int i=1; i<= getMaxId("t_person"); i++) {
			try{
				Person p = instanceOf(i);
				System.out.println(p);
				persist(p);
				
			} catch (Exception ex1){
				System.err.println(ex1.getMessage());
			} // end try/catch
		} // end for 
	} // end execEdits();

	public Integer[] getIdGapList() throws Exception{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=1; i<=getMaxId("t_person"); i++) {
			try {
				instanceOf(i);
				
			} catch (Exception ex1){
				list.add(i);
			} // end try/catch 
		} // end for
		return list.toArray(new Integer[list.size()]);
	} // end getIdGapList() method
	
	public Person[] getChildren(int id1, int id2) throws SQLException, Exception {
        ArrayList<Person> list = getChildrenList(id1, id2);
		return list.toArray(new Person[list.size()]);
	}
	
	public ArrayList<Person> getChildrenList(int id1, int id2) throws SQLException, Exception {
		ArrayList<Person> list = new ArrayList<Person>();
		String sql = "SELECT t_person.id AS childId, t_couple.effDate "
                   + "FROM (" + SqlTable.DATABASE + ".t_family JOIN (" + SqlTable.DATABASE + ".t_couple "
                   + "JOIN " + SqlTable.DATABASE + ".t_person) ON (((" 
                   + SqlTable.DATABASE + ".t_family.parents = " + SqlTable.DATABASE + ".t_couple.id) "
                   + "AND (" + SqlTable.DATABASE + ".t_family.child = " 
                   + SqlTable.DATABASE + ".t_person.id)))) "
	               + "where " + SqlTable.DATABASE + ".t_couple.person1 = " + id1 + " and " 
                   + SqlTable.DATABASE + ".t_couple.person2 = " + id2 + " "
                   + "ORDER BY " + SqlTable.DATABASE + ".t_family.sequence";
		
		/*
		 * Build the response array
		 */
		ResultSet rs1 = exec(sql);
		rs1.beforeFirst();
		while (rs1.next()) {
			Person p1 = instanceOf(rs1.getInt("childId"));
			p1.setWorkDate(rs1.getString("effDate"));
			list.add(p1);
		} // end while

		return list;
	} // end getChildren() method

	public Person instanceOf(int id) throws Exception {
		Statement stmt = connect.createStatement();
		ResultSet rs = exec(stmt, "SELECT * FROM " + SqlTable.DATABASE + ".t_person where id=" + id + ";");
		rs.first();
		Person per = instanceOf(rs);
		rs.close();
		stmt.close();
		return per;
	} // instanceOf() method

	public Person instanceOf(ResultSet rs) throws Exception {
		Person per = new Person();

		per.setId(rs.getInt("id"));
		per.setGiven(rs.getString("given"));
		per.setSurname(rs.getString("surname"));
		per.setFamiliar(rs.getString("familiar"));
		per.setBirth_surname(rs.getString("birth_surname"));
		per.setBirthdate(rs.getString("birthdate"));
		per.setDeathdate(rs.getString("deathdate"));
		per.setGender(rs.getString("gender"));
		per.setBirthloc(rs.getString("birthloc"));
		per.setDeathloc(rs.getString("deathloc"));
		per.setInternment(rs.getString("internment"));
		per.setNotations(rs.getString("notations"));
		per.setSource(rs.getInt("source"));
		per.setAdopted(rs.getInt("adopted"));
		return per;
	} // end instanceOf() method

	public Person instanceOf(String given, String surname) throws Exception {
		Statement stmt = connect.createStatement();
		ResultSet rs = exec(stmt, "SELECT * FROM " + SqlTable.DATABASE + ".t_person where given like'" + given
				+ "%' and surname like '" + surname + "%';");
		Person per = null;
		
		if (rs.first()){
			per = instanceOf(rs);
			rs.close();
			stmt.close();
		} // end if 
		return per;
	} // end instanceOf() method

	public int getPersonId(String given, String surname) throws Exception {
		ResultSet rs = exec("SELECT id FROM " + SqlTable.DATABASE + ".t_person where given='" + given
				+ "' and surname = '" + surname + "';");
		if (rs.first()) {
			return rs.getInt("id");
		} else {
			return 0;
		}
	} // end getPersonId() method
	
	public Person[] getParents(int arg) throws SQLException, Exception {
        ArrayList<Person> list = getParentList(arg);
		return list.toArray(new Person[list.size()]);
	} // end getParents() method

	public ArrayList<Person> getParentList(int arg) throws SQLException, Exception {
		ArrayList<Person> list = new ArrayList<Person>();
		String sql = "select * from " + SqlTable.DATABASE + ".t_couple where id = (SELECT parents FROM "
				+ SqlTable.DATABASE + ".t_family WHERE child = " + arg + ");";
		/*
		 * Build the response array
		 */
		ResultSet rs = exec(sql);
		if (rs.first()) {
			list.add(instanceOf(rs.getInt("person1")));
			list.add(instanceOf(rs.getInt("person2")));
		} // end if
		rs.close();

		return list;
	} // end getParents() method

	public Person[] getSpouses(int arg) throws SQLException, Exception {
		ArrayList<Person> list = new ArrayList<Person>();
		String sql = "select person2 as spouse, effDate from " + SqlTable.DATABASE + ".t_couple where person1 = " + arg
				+ " union " + "select person1 as spouse, effDate from " + SqlTable.DATABASE
				+ ".t_couple where person2 = " + arg + " " + "order by if(length(effDate) = 4, "
				+ "str_to_date(concat(\"1/1/\" + effDate),'%m/%d/%Y'), " + "str_to_date(effDate,'%m/%d/%Y'));";
		/*
		 * Build the response array
		 */
		ResultSet rs1 = exec(sql);
		rs1.beforeFirst();
		while (rs1.next()) {
			int itm = rs1.getInt("spouse");
			Person per = instanceOf(itm);
			per.setWorkDate(rs1.getString("effDate"));
			list.add(per);
		} // end while

		return list.toArray(new Person[list.size()]);
	} // end getSpouses() method
	
	/*
	 * Update a set of person records in the Database based upon the contents of the person array provided.
	 */
	public int persist(ArrayList<Person> p1) throws Exception {
		int result = -1;
		
		for (Person p2 : p1){
			result = persist(p2);
		} // end for 
		return result;
	} // end persist() method 
	
	/*
	 * Update a set of person records in the Database based upon the contents of the person array provided.
	 */
	public int persist(Person[] p1) throws Exception {
		int result = -1;
		
		for (Person p2 : p1){
			result = persist(p2);
			if (result != 1) throw new Exception("PersonFactory::setPerson: Update failed for " + p2);
		} // end for 
		return result;
	} // end persist() method 
	
	/*
	 * Update a person record in the Database based upon the contents of the person object provided.
	 */
	public int persist(Person p) throws Exception {
		pStmt.setString(1, p.getGiven());
		pStmt.setString(2, p.getSurname());
		pStmt.setString(3, p.getFamiliar());
		pStmt.setString(4, p.getBirth_surname());
		pStmt.setString(5, (p.getBirthdate() != null) ? p.getBirthdateStr() : "");
		pStmt.setString(6, (p.getDeathdate() != null) ? p.getDeathdateStr() : "");
		pStmt.setString(7, p.getGender());
		pStmt.setString(8, p.getBirthlocStr());
		pStmt.setString(9, p.getDeathlocStr());
		pStmt.setString(10, p.getInternmentStr());
		pStmt.setString(11, p.getNotations());
		pStmt.setInt(12, p.getSource());
		pStmt.setInt(13, p.getAdopted());
        /*
         * OnNewRecord, create a skelton record and then update it
         */
		if (p.isEmpty()){
			return 0;
			
		} else if (p.isNewPerson()){
			pStmt.setInt(14, getNewId("t_person"));
			
		} else {
			pStmt.setInt(14, p.getId());
			
		} // end if/else
	
		return pStmt.executeUpdate();
	} // end persist() method
	
	public static void main(String[] args) throws Exception {
		PersonFactory fct = new PersonFactory();
		//fct.execEdits();
		System.out.println(fct.getIdGapList()[0]);
	} // end main() method
	
} // end PersonFactory class
