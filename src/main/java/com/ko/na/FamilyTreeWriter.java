package com.ko.na;

import java.io.PrintStream;
import java.util.Map.Entry;

import com.ko.na.database.FamilyTreeFactory;

import java.util.NavigableMap;

public class FamilyTreeWriter {

	public static final int ANCESTORS = 0;
	public static final int DESCENDANTS = 1;

	public FamilyTreeWriter() throws Exception {
		super();
	} // end constructor

	public String getJson(Family[] tree) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		for (Family f1 : tree) {
			buf.append(f1.toJson("Family", true));
		} // end for
		return buf.toString().substring(0, buf.length() - 2) + "\n}}}";
	} // end getJson() method

	public String getLineageJson(int generation, Family[] tree, boolean idFlg) throws Exception {
		StringBuffer buf = new StringBuffer();
		for (Family f1 : tree) {
			buf.append("{\"FamilyTree\":[\n");
			for (Entry<String, String> entry : f1.toMap(generation,"1", idFlg).headMap("z").entrySet()) {
				buf.append("{\"Key\":\"" + entry.getKey() + "\",");
				buf.append("\"Value\":\"" + entry.getValue() + "\"},\n");
			} // end for
		} // end for
		return buf.toString().substring(0, buf.length() - 2) + "\n]}";
	} // end getLineageJson() method
	
	public String getLineageText(int generation, Family[] tree, boolean idFlg) throws Exception {
		StringBuffer buf = new StringBuffer();
		if (tree != null) {
			for (Family f1 : tree) {
				NavigableMap<String, String> nmap = f1.toMap(generation,"1", idFlg).headMap("z", true);
				for (Entry<String, String> entry : nmap.entrySet()) {
					buf.append(entry.getKey() + " " + entry.getValue() + "\n");
				} // end for
			} // end for
		} // end if
		return buf.toString();
	} // end getLineageText() method

	public void showLineage(int generation, Family[] tree, boolean idFlg) throws Exception {
		showLineage(generation, System.out, tree, idFlg);
	}

	public void showLineage(int generation, PrintStream out, Family[] tree, boolean idFlg) throws Exception {
		if (tree != null) {
			for (Family f1 : tree) {
				NavigableMap<String, String> nmap = f1.toMap(generation,"1", idFlg).headMap("z", true);
				for (Entry<String, String> entry : nmap.entrySet()) {
					out.println(entry.getKey() + " " + entry.getValue());
				} // end for
			} // end for
		} // end if
	} // end showLineage() method

	public static void main(String[] args) throws Exception {
		FamilyTreeFactory fact = new FamilyTreeFactory();
		FamilyTreeWriter wtr = new FamilyTreeWriter();
		Family[] tree = null;
		boolean showId = true;
		fact.setDebug(false);

		/*
		 * Construct the Family Tree
		 */
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

			/*
			 * Now, Display the results
			 */
			if (tree == null) {
				System.out.println("No one found.");
			} else {
				wtr.showLineage(generation, tree, showId);
				// System.out.println(wtr.getJson(tree));
			} // end if

		} else {
			System.out
					.println("Usage: FamilyTreeFactory [Ancestors|Descendants] [<PersonId> | [<GivenName> <Surname>]]");
		} // end if/else

	} // end main() method
} // end FamilyTree class
