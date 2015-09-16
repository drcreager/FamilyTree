package com.ko.na.database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.ko.na.Family;
import com.ko.na.FamilyTreeWriter;
import com.ko.na.Person;

public class FamilyTreeFactory extends SqlTable {
	
	protected PersonFactory pFact = null;

	public static final int ANCESTORS = 0;
	public static final int DESCENDANTS = 1;

	public FamilyTreeFactory() throws Exception {
		super();
		pFact = new PersonFactory();
	} // end constructor

	public ArrayList<Family> getFamilies(int generation, int id) throws Exception {
		ArrayList<Family> fam = new ArrayList<Family>();
		Person base = null;

		/*
		 * Locate the Base person for this Family
		 */
		ResultSet rs = exec("SELECT * FROM " + SqlTable.DATABASE + ".t_person where id=" + id + ";");
		if (rs.first()) {
			base = pFact.instanceOf(rs);
			rs.close();

			switch (generation) {
			case DESCENDANTS:
				/*
				 * Retrieve all spouses of the base person creating a family
				 * with each
				 */
				for (Person itm : pFact.getSpouses(base.getId())) {
					Family f1 = new Family(base, itm);
					f1.setEffDate(itm.getWorkDate());
					fam.add(f1);
				} // end for
				break;

			case ANCESTORS:
				/*
				 * Retrieve parents of the base person creating a family
				 */
				Person[] per = pFact.getParents(base.getId());
				if (per.length > 0) {
					Family f1 = new Family(per[0], per[1]);
					fam.add(f1);
				} // end if
				break;
			} // end switch

			return fam;
		} else {
			return null;
		} // end if/else
	} // end getFamilies() method

	public Family[] instanceOf(int generation, int id) throws Exception {
		ArrayList<Family> fam = null;

		switch (generation) {
		case ANCESTORS:
			/*
			 * For Root Person, get the Previous Generation Family
			 */
			fam = getFamilies(generation, id); // retrieves the family the root
												// person grew up in.

			/*
			 * OnEachParent, insert their Parent's family
			 */
			if ((fam != null) && (fam.size() > 0)) {
				for (Family f1 : fam) {
					setParents(f1);
				} // end for
			} // end if
			break;

		case DESCENDANTS:
			/*
			 * For Root Person, create a list of Current Generation Families
			 */
			fam = getFamilies(generation, id); // retrieves all families started
												// by the Root person.

			/*
			 * OnEachFamily, insert the Children
			 */
			if ((fam != null) && (fam.size() > 0)) {
				for (Family f1 : fam) {
					setChildren(f1);
				} // end for
			} // end if
			break;
		} // end switch

		if ((fam != null) && (fam.size() > 0)) {
			return fam.toArray(new Family[fam.size()]);
		} else {
			return null;
		}
	} // end instanceOf() method

	public Family[] instanceOf(int generation, String given, String surname) throws Exception {
		return instanceOf(generation, pFact.getPersonId(given, surname));
	} // end instanceOf() method

	public Family setChildren(Family f1) throws Exception {
		/*
		 * insert the Children
		 */
		Statement stmt = connect.createStatement();
		String sql = "select child from  " + SqlTable.DATABASE + ".t_family where parents = " + "  (select id from  "
				+ SqlTable.DATABASE + ".t_couple where person1 = " + f1.getFather().getId() + " and person2 = "
				+ f1.getMother().getId() + ");";
		ResultSet rs = exec(stmt, sql);
		rs.beforeFirst();
		while (rs.next()) {
			f1.setChild(pFact.instanceOf(rs.getInt("child")));
		} // end while
		rs.close();
		stmt.close();

		/*
		 * OnEachCoupledChild, replace by a Next Family
		 */
		for (Person p1 : f1.getChildren()) {
			for (Family f2 : getFamilies(DESCENDANTS, p1.getId())) {
				setChildren(f2);
				f1.setNextFam(f2);
			} // end for
		} // end for

		return f1;
	} // end setChildren() method

	public Family setParents(Family f1) throws Exception {
		/*
		 * insert the Parents
		 */
		Person[] par = new Person[2];
		par[0] = f1.getFather();
		par[1] = f1.getMother();

		for (Person p1 : par) { // ForEachParent
			f1.setChild(p1);
		} // end for

		/*
		 * OnEachParent, replace by a Next Family
		 */
		for (Person p1 : f1.getChildren()) {
			if (p1 != null) {
				for (Family f2 : getFamilies(ANCESTORS, p1.getId())) {
					setParents(f2);
					f1.setNextFam(f2);
				} // end for
			} // end if
		} // end for
		return f1;
	}

	public static void main(String[] args) throws Exception {
		FamilyTreeFactory fact = new FamilyTreeFactory();
		FamilyTreeWriter wtr = new FamilyTreeWriter();
		Family[] tree = null;
		boolean showId = true;
		fact.setDebug(false);

		if (args.length >= 2) {
			int generation = (args[0].substring(0, 1).equalsIgnoreCase("A")) ? FamilyTreeFactory.ANCESTORS
					: FamilyTreeFactory.DESCENDANTS;
			String pfx = ((generation == FamilyTreeFactory.ANCESTORS) ? "Ancestral " : "Decendant ");

			if (args.length == 2) {
				System.out.println(pfx + "Family Tree for Person<" + args[1] + ">\n");
				tree = fact.instanceOf(generation, Integer.parseInt(args[1]));

			} else if (args.length == 3) {
				System.out.println(pfx + "Family Tree for " + args[1] + " " + args[2] + "\n");
				tree = fact.instanceOf(generation, args[1], args[2]);

			} else {
				System.out.println(pfx + "Family Tree for " + args[1] + " " + args[2] + " " + args[3] + "\n");
				tree = fact.instanceOf(generation, args[1] + " " + args[2], args[3]);
			} // end if/else

			if (tree == null) {
				System.out.println("No one found.");
			} else {
				wtr.showLineage(FamilyTreeFactory.DESCENDANTS,tree, showId);
			} // end if
		} else {
			System.out
					.println("Usage: FamilyTreeFactory [Ancestors|Descendants] [<PersonId> | [<GivenName> <Surname>]]");
		} // end if/else

		// System.out.println(wtr.getJson(tree));
	} // end main() method
} // end FamilyTree class
