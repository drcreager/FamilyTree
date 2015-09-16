package com.ko.na;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * CommandLineUI processes all command line information into its internal
 * representation. It also contains the format and content of the process header
 * and on-line help text.
 * 
 * @author Daniel R. Creager
 * @version 1.0.000
 */
//
// History:
// Date Who Build Description
// 03/10/2006 DRC 00000 Created original version.
//
public class CommandLineUI {
	public static final int FMT_CSV = 1;
	public static final int FMT_TEXT = 2;
	public static final int FMT_JSON = 3;
	public static final int FMT_HTML = 4;
	public static final int FMT_LIST = 5;

	public static final int TYPE_ANCESTRAL = 0;
	public static final int TYPE_DESCANDANT = 1;

	public static final String HEADER = "\n\nDocGen";
	public static final String DLMTRSTR = "===========================================================================";
	public static final String Version = "1";

	public static final String Level = "0";
	public static final String Build = "000";

	protected boolean active;
	protected boolean debug;
	protected boolean showId;
	protected int format;
	protected int type;
	protected String rptSpec;
	protected ArrayList<String> personSpec;

	protected String fileName;

	public final String HELP_TEXT = getHeader() + "\n" + "Generates Various Reports from a Family Tree.\n\n"
			+ "Usage: DocGen [-dh] [-o[ctjh]] [-r[ad]] <ReportSpec> <PersonSpec> <FileName>\n\n" + "Options\n"
			+ " -d debug mode    Display diagnostic information.\n"
			+ " -h Help          Display this usage information.\n\n"
			+ " -i Identifiers   Display person identifiers.\n\n"
			+ " -oc CSV          Output Format is Coma Separated Value. (Default)\n"
			+ " -ot Text         Output Format is Text.\n" 
			+ " -oj Json         Output Format is JSON.\n"
			+ " -oh Html         Output Format is HTML.\n"
			+ " -ol List         Output Format is List object.\n\n"
			+ " -ra Ancestral    Report on Ancesters.\n"
			+ " -rd Descendant   Report on Descandants. (Default)\n" + "\nReport Specification\n"
			+ " Tree               Nested Family Tree Listing\n" + " <sql>              Structured Query Language\n"
			+ "\nPerson Specification for Tree Listings\n"
			+ " <999>              Identification number for an individual.\n"
			+ " <Given>,<Surname>  If multiple words in either element surround with quotes.\n"
			+ "\nFileName          File name for the output file.\n\n";

	public CommandLineUI(String[] args) throws Exception, IOException, SQLException, ClassNotFoundException {
		setActive(true);
		setDebug(false);
		this.setShowId(false);
		setFormat(FMT_CSV);
		setType(TYPE_DESCANDANT);
		setFileName("output.csv");
		personSpec = new ArrayList<String>();

		parseCmmds(args);
		
		if (isTree()) setFormat(FMT_TEXT);  // Tree Listing are only produced as Textual data
	} // end constructor

	public String getFileName() {
		return fileName;
	}

	public int getFormat() {
		return format;
	}

	public int getGeneration() {
		return type;
	}

	public String getGenerationStr() {
		return (getGeneration() == TYPE_ANCESTRAL) ? "Ancestral " : "Decendant ";
	}

	public String getGivenName() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < personSpec.size() - 1; i++) {
			buf.append(personSpec.get(i) + " ");
		} // end for
		return buf.toString().trim();
	}

	public String getHeader() {
		return HEADER + " " + Version + "." + Level + "." + Build;
	} // end getHeader method

	public ArrayList<String> getPersonSpec() {
		return personSpec;
	}

	public String getPersonSpec(int ofst) {
		return personSpec.get(ofst);
	}

	public String getRptSpec() {
		return rptSpec;
	}


	public String getSurname() {
		return getPersonSpec().get(personSpec.size() - 1).trim();
	}

	public int getType() {
		return type;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isDebug() {
		return debug;
	}

	public boolean isFileInput() {
		if (fileName == null)
			return false;
		return (fileName.length() > 0);
	}

	public boolean isNumeric(String arg) {
		return arg.matches("\\d*");
	}

	public boolean isPerSpecAnID() {
		return isNumeric(personSpec.get(0));
	}

	public boolean isShowId() {
		return showId;
	}
	
	public boolean isTree(){
		return rptSpec.equalsIgnoreCase("Tree");
	}

	public void parseCmmds(String[] args) throws Exception {
		int parmCount = 0;
		// String[] parameters = new String[5];
		StringBuffer options = new StringBuffer();

		if (args.length < 1) {
			System.out.println(HELP_TEXT);
			active = false;
		} else {
			// Parse the command line options
			for (int i = 0; i < args.length; i++) {
				if (args[i].charAt(0) == '-') {
					options.append(args[i].substring(1));
					for (int j = 0; (options.length() > j && isActive()); j++) {
						/*
						 * process options
						 */
						switch (options.charAt(j)) {
						case 'h': // -h Help Display this usage information.
							System.out.println(HELP_TEXT);
							setActive(false);
							break;

						case 'd': // -d debug mode Display diagnostic
									// information.
							setDebug(true);
							break;

						case 'i': // -i Display person identifiers.
							this.setShowId(true);
							break;

						case 'o': // -o* Output Format
							switch (options.charAt(++j)) {
							case 'c': // Output Format is Coma Separated
										// Value.
								setFormat(FMT_CSV);
								break;

							case 'j': // Output Format is JSON.
								setFormat(FMT_JSON);
								break;

							case 'h': // Output Format is HTML.
								setFormat(FMT_HTML);
								break;
								
							case 'l': // Output Format is List object.
								setFormat(FMT_LIST);
								break;

							default: // Text Output Format is Text.
								setFormat(FMT_TEXT);
								break;

							} // end switch
							break;

						case 'r':
							switch (options.charAt(++j)) {
							case 'a': // Ancestral Report on Ancesters.
								setType(TYPE_ANCESTRAL);
								break;

							default: // Descendant Report on Descandants.
								setType(TYPE_DESCANDANT);
								break;
							} // end switch
							break;

						} // end switch
					} // end for

				} else {

					// process parameters
					parmCount++;
					switch (parmCount) {
					case 1:
						if (args[i].length() > 0)
							setRptSpec(args[i]);
						break;

					default:
						if (args.length == i + 1) { // this is the last
													// parameter in the list
							if (args[i].length() > 0) {
								setFileName(args[i]);
							} // end if
						} else {
							if (args[i].length() > 0) {
								setPersonSpec(args[i]);
							} // end if
						} // end if/else
					} // end switch
				} // end if

			} // end for
		} // end if

	} // end parseCmmds method

	public void setActive(boolean arg) {
		active = arg;
	}

	public void setDebug(boolean arg) {
		debug = arg;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFormat(int format) {
		this.format = format;
	}

	public void setPersonSpec(String personSpec) {
		this.personSpec.add(personSpec);
	}

	public void setRptSpec(String rptSpec) {
		this.rptSpec = rptSpec;
	}

	public void setShowId(boolean showId) {
		this.showId = showId;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void showProcessHeader() {
		System.out.println(getHeader() + "\n" + DLMTRSTR);
		System.out.println("Exec Date:\t" + new java.util.Date());
		System.out.println("User Id:\t" + System.getProperty("user.name"));
		System.out.println("Java Version:\t" + System.getProperty("java.version"));
		System.out.println("Class Path:");
		for (String itm : System.getProperty("java.class.path").split(";"))
			System.out.println("\t\t" + itm.trim());

		if (getRptSpec().equalsIgnoreCase("Tree")) {
			System.out.println("Report:\t\t" + ((getType() == TYPE_ANCESTRAL) ? "Ancestral " : "Descandant ")
					+ "Tree as " + ((getFormat() == FMT_CSV) ? "CSV"
							: ((getFormat() == FMT_TEXT) ? "TEXT" : ((getFormat() == FMT_JSON) ? "JSON" : "HTML"))));
			System.out.println("Identifiers:\t" + ((this.isShowId()) ? "Shown" : "Hidden"));
			System.out.print("Person Spec:\t");
			for (String itm : getPersonSpec())
				if (itm != null)
					System.out.print(itm + " ");
			System.out.println();

		} else {
			System.out.println("Rpt Spec:\t" + getRptSpec());
			System.out.println("Rpt Format:\t" + ((getFormat() == FMT_CSV) ? "CSV"
					: ((getFormat() == FMT_TEXT) ? "TEXT" : ((getFormat() == FMT_JSON) ? "JSON" : "HTML"))));
		} // end if/else
		System.out.println("Output:\t\t" + getFileName());
		if (isDebug())
			System.out.println("Debug Mode:\tActive");
		System.out.println(DLMTRSTR + "\n");
	} // end showProcessHeader method

	public void showProcessTrailer(int aRecordCnt) {
		System.out.println("\nRecords:\t" + aRecordCnt);
	} // end showProcessHeader method

	public static void main(String args[]) throws Exception, IOException {
		CommandLineUI ui = new CommandLineUI(args);
		if (ui.isActive()) {
			ui.showProcessHeader();
			ui.showProcessTrailer(0);
		}
	} // end main method

} // end CommandLineUI class
