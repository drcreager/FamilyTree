package com.ko.na.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.ko.na.CommandLineUI;
import com.ko.na.Family;
import com.ko.na.FamilyTreeWriter;

public class DocGen extends SqlTable {

	public DocGen() throws Exception {
		super();
	}

	public String getCSVData(String sql) throws Exception {
		ResultSet rs = getResultSet(sql);
		if (rs == null) {
			return null;
		} else {
			return resultSet2CSV(rs);
		} // end if/else
	} // end getData() method

	public String getFamilyTree(CommandLineUI cmmdUI) throws Exception {
		StringBuffer buf = new StringBuffer();
		FamilyTreeFactory fact = new FamilyTreeFactory();
		FamilyTreeWriter wtr = new FamilyTreeWriter();
		Family[] tree = null;
		fact.setDebug(cmmdUI.isDebug());

		/*
		 * Construct the Family Tree
		 */
		if (cmmdUI.isPerSpecAnID()) {
			buf.append(cmmdUI.getGenerationStr() + "Family Tree for Person<" + cmmdUI.getPersonSpec(0) + ">\n\n");
			tree = fact.instanceOf(cmmdUI.getGeneration(), Integer.parseInt(cmmdUI.getPersonSpec(0)));

		} else {
			buf.append(cmmdUI.getGenerationStr() + "Family Tree for " + cmmdUI.getGivenName() + " "
					+ cmmdUI.getSurname() + "\n\n");
			tree = fact.instanceOf(cmmdUI.getGeneration(), cmmdUI.getGivenName(), cmmdUI.getSurname());
		} // end if/else

		/*
		 * Now, Display the results
		 */
		if (tree == null) {
			System.out.println("No one found.");
		} else {
			buf.append(wtr.getLineageText(cmmdUI.getGeneration(), tree, cmmdUI.isShowId()));
		} // end if

		return buf.toString().substring(0, buf.length() - 1); // strip off the
																// final newline
																// character

	}
	
	public String getHtml(String sql) throws Exception {
		ResultSet rs = getResultSet(sql);
		
		if (rs == null) {
			return null;
		} else {
			return resultSet2Html(sql, rs);
		} // end if/else
	} // end

	public String getJson(String sql) throws Exception {
		ResultSet rs = getResultSet(sql);
		
		if (rs == null) {
			return null;
		} else {
			return resultSet2Json(sql, rs);
		} // end if/else
	} // end getJson() method

	public ArrayList<HashMap<String,Object>> getList(String sql) throws Exception {
		ResultSet rs = getResultSet(sql);
		
		if (rs == null) {
			return null;
		} else {
			return resultSet2List(rs);
		} // end if/else
	} // end getJson() method
	
	public ArrayList<HashMap<String,Object>> getListData(String sql) throws Exception {
		ResultSet rs = getResultSet(sql);
		if (rs == null) {
			return null;
		} else {
			return resultSet2List(rs);
		} // end if/else
	} // end getListData() method

	public ResultSet getResultSet(String sql) throws Exception {
		ResultSet rs = null;
		
		if (sql.split("\\s").length == 1){
			rs = exec(insertDBName("SELECT * FROM " + sql + ";"));  // process a view name
		} else {
			rs = exec(insertDBName(sql));  // process a query
		} // end if/else
		
        return rs;
	} // end getResultSet() method 

	public void writeFile(String fileName, String content) {
		try {
			File file = new File("WebContent/" + fileName);
			if (!file.exists())
				file.createNewFile();
			BufferedWriter wtr = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			wtr.write(content);
			wtr.close();

		} catch (IOException e) {
			e.printStackTrace();
		} // end try/catch
	} // end writeFile() method

	public static void main(String[] args) throws Exception {
		CommandLineUI cmmdUI = new CommandLineUI(args);
		DocGen doc = new DocGen();
	
		if (cmmdUI.isActive()) {
			doc.setDebug(cmmdUI.isDebug());
			cmmdUI.showProcessHeader();
			if (cmmdUI.getRptSpec().equalsIgnoreCase("Tree")) {
				doc.writeFile(cmmdUI.getFileName(), doc.getFamilyTree(cmmdUI));
	
			} else {
	
				switch (cmmdUI.getFormat()) {
				case CommandLineUI.FMT_JSON:
					doc.writeFile(cmmdUI.getFileName(), doc.getJson(cmmdUI.getRptSpec()));
					break;
	
				case CommandLineUI.FMT_CSV:
					doc.writeFile(cmmdUI.getFileName(), doc.getCSVData(cmmdUI.getRptSpec()));
					break;
	
				case CommandLineUI.FMT_HTML:
					doc.writeFile(cmmdUI.getFileName(), doc.getHtml(cmmdUI.getRptSpec()));
					break;
					
				case CommandLineUI.FMT_LIST:
					for (HashMap<String,Object> itm : doc.getList(cmmdUI.getRptSpec())){
						System.out.println(itm.entrySet());
					}
					//doc.writeFile(cmmdUI.getFileName(), doc.getList(cmmdUI.getRptSpec()));
					break;
	
				} // end switch
			} // end if/else
	
			doc.terminate();
		} // end if
	} // end main() method

} // end DocumentGenerator class
