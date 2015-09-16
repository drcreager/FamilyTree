package com.ko.na.database;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.ko.na.FamilyGroup;
import com.ko.na.Person;

public class FamilyGroupFactory extends SqlTable {
	private static final String COUPLE_QUERY = "SELECT id, effDate FROM " + SqlTable.DATABASE
			+ ".t_couple where person1 = ? and person2 = ? ";

	private static final String COUPLE_UPDATE = "update " + SqlTable.DATABASE + ".t_couple set "
			+ "person1 = ?, person2 = ?,  effDate = ? where id = ? ";

	private static final String FAMILY_UPDATE = "update " + SqlTable.DATABASE + ".t_family set "
			+ "parents = ?, child = ?, sequence = ? where id = ? ";

	protected PersonFactory pfact = null;
	protected int work;

	public FamilyGroupFactory() throws Exception {
		super();
		pfact = new PersonFactory();
	} // end constructor

	/*
	 * Validate that a Child record exists for this family OnMissing, generate a
	 * new family record.
	 */
	protected int getFamilyId(int cplId, int childId, int seq) throws Exception {
		int result;
		String sql = "SELECT id FROM " + SqlTable.DATABASE + ".t_family where parents = " + cplId + " and child = "
				+ childId + " and sequence = " + seq;
		ResultSet rs = exec(sql);
		result =  (rs.first()) ? rs.getInt("id") : getNewId("t_family");
		rs.close();
		return result;
	} // end getFamilyId() method

	public FamilyGroup[] instanceOf(String sql) throws Exception {
		FamilyGroup[] result;
		ResultSet rs = exec(sql);
		if (rs.first()) {
			Person base = pfact.instanceOf(rs);
			if (base != null)
				result = instanceOf(base);
			else
				throw new Exception("No one identified by \"" + sql + "\" found in the database.");
		} else {
			throw new Exception("No one identified by \"" + sql + "\" found in the database.");
		} // end if
		rs.close();
		return result;
	} // end instanceOf() method

	public FamilyGroup[] instanceOf(String given, String surname) throws Exception {
		Person base = pfact.instanceOf(given, surname);
		if (base != null)
			return instanceOf(base);
		else
			throw new Exception("No one named " + given + " " + surname + " found in the database.");
	} // end instanceOf() method

	public FamilyGroup[] instanceOf(int id) throws Exception {
		return instanceOf(pfact.instanceOf(id));
	} // end instanceOf() method

	public FamilyGroup[] instanceOf(Person p1) throws Exception {
		FamilyGroup[] fg = null;

		/*
		 * Set up a Family Group for each marriage
		 */
		Person[] spouses = pfact.getSpouses(p1.getId());
		fg = new FamilyGroup[spouses.length];
		for (int i = 0; i < spouses.length; i++) {
			/*
			 * Set the Parents in this Family Group
			 */
			if (p1.isMale()) {
				fg[i] = new FamilyGroup(p1, spouses[i]);
			} else {
				fg[i] = new FamilyGroup(spouses[i], p1);
			} // end if

			/*
			 * Retrieve and load the Parent's Couple information
			 */
			prepareStatement(COUPLE_QUERY);
			setCoupleData(fg[i], 0, 0);

			/*
			 * Set the Grandparents in this Family Group
			 */
			int j = 0;
			/*
			 * Retrieve a copy of the current people list
			 */
			ArrayList<Person> p3 = new ArrayList<Person>();
			for (Person p : fg[i].getPeople())
				p3.add(p);
			p3.trimToSize();

			for (Person p4 : p3) {
				ArrayList<Person> grandparents = pfact.getParentList(p4.getId());
				if (j++ == 0)
					fg[i].setGrandParents(FamilyGroup.FATHER, grandparents);
				else
					fg[i].setGrandParents(FamilyGroup.MOTHER, grandparents);
			} // end for

			/*
			 * Retrieve and load the Paternal GrandParent's Couple information
			 */
			setCoupleData(fg[i], 2, 1);

			/*
			 * Retrieve and load the Maternal GrandParent's Couple information
			 */
			setCoupleData(fg[i], 3, 2);

			/*
			 * Set the Children in this Family Group
			 */
			ArrayList<Person> children = pfact.getChildrenList(fg[i].getFather().getId(), fg[i].getMother().getId());
			fg[i].setChildren(children);

		} // end for
		return fg;
	} // end instanceOf() method

	/*
	 * Update/Insert all components of the Family Group based upon the contents
	 * of the FamilyGroup object provided.
	 */
	public int persist(FamilyGroup fg) throws Exception {
		int result = -1;

		/*
		 * Update all the people records
		 */
		result = pfact.persist(fg.getPeople());
		if (result != 1)
			return result;

		/*
		 * Update couple records as needed
		 */
		result = persistCouple(fg);
		if (result != 1)
			return result;

		/*
		 * Update family records as needed
		 */
		result = persistFamily(fg);

		return result;
	} // end persist() method

	/*
	 * Update/Insert the child/family linkages from the FamilyGroup
	 */
	protected int persistChildInFamily(int cplId, int childId, int seq) throws Exception {
		pStmt.setInt(1, cplId);
		pStmt.setInt(2, childId);
		pStmt.setInt(3, seq);
		pStmt.setInt(4, getFamilyId(cplId, childId, seq));

		return pStmt.executeUpdate();
	} // end persistChildInFamily() method

	protected int persistCouple(boolean newCouple, int coupleId, Person p1, Person p2, String dateStr)
			throws Exception {
		pStmt.setInt(1, p1.getId());
		pStmt.setInt(2, p2.getId());
		pStmt.setString(3, dateStr);

		/*
		 * OnNewRecord, create a skelton record and then update it
		 */
		if (newCouple) {
			work = getNewId("t_couple");
			pStmt.setInt(4, work);

		} else {
			work = coupleId;
			pStmt.setInt(4, coupleId);

		} // end if/else
		return pStmt.executeUpdate();
	} // end persistCouple() method

	/*
	 * Update/Insert the couple linkages and anniversary date from the
	 * FamilyGroup
	 */
	protected int persistCouple(FamilyGroup fg) throws Exception {
		prepareStatement(COUPLE_UPDATE);
		int result = -1;

		/*
		 * OnEachCouple (Parents, Both Grandparents) Create/Update the Couple
		 * record
		 */
		int j = 0;
		int[] cplId = fg.getCouple();
		for (int i = 0; i < cplId.length; i++) {
			Person[] p = { fg.getPerson(j++), fg.getPerson(j++) };
			if (p[0].isEmpty() || p[1].isEmpty()){
				pStmt.close();
				return result; // marker entries with no data, abort the update.
			}

			result = persistCouple(fg.isNewCouple(i), cplId[i], p[0], p[1], (j == 1) ? fg.CDate(fg.getEffDate(0)) : "");
			if (result != 1)
				break;
			fg.setCoupleId(i, work); // reset CoupleId to capture value on
										// create
		} // end for
		pStmt.close();
		return result;
	}

	/*
	 * Update/Insert the family linkages from the FamilyGroup
	 */
	protected int persistFamily(FamilyGroup fg) throws Exception {
		int result = -1;
		prepareStatement(FAMILY_UPDATE);
		Person[] children = fg.getChildrenArray();

		for (int i = 0; i < children.length; i++) {
			result = persistChildInFamily(fg.getCoupleId(0), children[i].getId(), i + 1);
			if (result != 1)
				break;
		} // end for
		pStmt.close();
		return result;
	}

	protected FamilyGroup setCoupleData(FamilyGroup fg, int perOfst, int cplOfst) throws Exception {
		pStmt.setInt(1, fg.getPerson(perOfst).getId());
		pStmt.setInt(2, fg.getPerson(perOfst + 1).getId());
		ResultSet rs = pStmt.executeQuery();
		if (rs.first()) {
			fg.setCoupleId(cplOfst, rs.getInt("id"));
			fg.setEffDate(cplOfst, rs.getString("effDate"));
		} // end if
		return fg;
	}

	public static void main(String[] args) throws Exception {
		FamilyGroupFactory fgt = new FamilyGroupFactory();
        if (args.length == 2){
        	fgt.setDebug(args[0].equals("True"));
    		for (FamilyGroup fg : fgt.instanceOf(fgt.insertDBName(args[1]))) {
    			System.out.println(fg);
    		} // end for 
        	
        } else if (args.length >= 3) {
			fgt.setDebug(args[0].equals("True"));
			try {
				for (FamilyGroup fg : fgt.instanceOf(args[1], args[2])) {
					System.out.println(fg);
					fgt.persist(fg);
				} // end for
			} catch (Exception ex1) {
				System.err.println(ex1.getMessage());
			} // end try/catch 
		} else {
			System.out.println("Usage: FamilyGroupFactory <True|False> <sql>|[<GivenName> <Surname>]");
		} // end if/else

	} // end main() method

} // end PersonFactory class
