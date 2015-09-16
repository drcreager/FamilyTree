package com.ko.na;

import java.text.SimpleDateFormat;

import com.ko.na.database.SqlTable;
import com.ko.na.messaging.MessageException;

/**
 * Logical expressions that can be nested with other expressions.
 * Example: 
 *      ((name == 'Dan') AND (age < 63))  is expressed as:
 * 		Expression(new Expression("name","eq","Dan"),
 *                 "and",
 *                 new Expression("age","lt","63"));
 *                 
 * @author Daniel R Creager
 *
 */
public class ExpressionNL extends Expression implements java.io.Serializable {
	private static final long serialVersionUID = -6490673745920531511L;
	
	/**
	 * Create a valid expression based upon "command line"/"search field" input syntax 
	 * Syntax:  [ajective] subject verb predicate ->  [qualifier] dataElement relation literal
	 * 
	 * Use cases: 
	 * 1. whose maiden name was batson? 
	 * 2. who was born before 1921?
	 * 3. who died in 2003?
	 * 4. who was born in 1953?
	 * 5. who was born after 1950?
	 * 6. whose name matches D*Cre*?
	 * 7. whose name is Daniel Ross Creager? 
	 * 8. whose name was Daniel Kreiger?
	 * 9. who lived in georgia?
	 * 10. who lives in Iowa?
	 * 11. my father's name was Donald.
	 * 12. my mother's name was Virginia.
	 * 13. my mother's name is Maria.
	 * 14. who was my grandmother?
	 * 15. who were my grandmother's daughters?
	 * 16. who lived in georgia?
	 * 17. who was born before 1920 and died after 1950?
	 * 18. who were my ancestors?
	 * 19. who are my descendants?
	 * 20. who are the descendants of Thomas Henry Creager?
	 * 21. list out my cousins.
	 * 22. who were my wife's second cousins?
	 * 23. show me my son.
	 * 24. list my great great grandfather.
	 * 25. get the people named Krieger.
	 * 26. show me the couples named Batson.
	 * 27. get the children of couples whose name matches Dals*.
	 * 28. retrieve the parents of people named Daniel.
	 * 
	 * @param cmmd
	 */
	protected final static String[] QUAL   = {"maiden[.]*","father[.]*","mother[.]*","birth","death","first","last"};
    protected final static String[] SUBJ   = {"born","died","name","lived","father","mother","birth","death"};
    protected final static String[] VERBS  = {"was","is","before","after","in","on"};

    protected final static String[] PROCESS = {"q0s2v0","q0s2v1","q1s2v0","q1s2v1","q1s6v0","q1s6v1",
    		                                   "q2s6v0","q2s6v1","q1s7v0","q1s7v1","q2s7v0","q2s7v1",
                                               "s0v2","s0v3","s0v4","s0v5",
    		                                   "s1v2","s1v3","s1v4","s1v5",
    		                                   "s3v2","s3v3","s3v4","s3v5",
    		                                   "s4v0","s4v1","s5v0","s0v1"};
	
    protected final static String[] RTN = {"maiden name was",    "maiden name is",    
    		                               "father's name was",  "father's name is",
    		                               "mother's name was",  "mother's name is",
    		                               "mother's birth was", "mother's birth is",
    		                               "father's death was", "father's death is",
    		                               "mother's death was", "mother's death is",
    		                               "born before",  "born after",  "born in",  "born on",
    		                               "died before",  "died after",  "died in",  "died on",
    		                               "lived before", "lived after", "lived in", "lived on",
    		                               "father was",   "father is",
    		                               "mother was",   "mother is",
    		                               };

	public ExpressionNL(String cmmd) throws MessageException {
		super();
		String[] word = cmmd.split("\\s");
		int indx = 0;
		String key = "";
		
		switch (word.length){
		case 3:
			key += findKey(word, indx++, "s", SUBJ);
			key += findKey(word, indx++, "v", VERBS);
			break;
		case 4:
			key += findKey(word, indx++, "q", QUAL);
			key += findKey(word, indx++, "s", SUBJ);
			key += findKey(word, indx++, "v", VERBS);
			break;
		} // end switch
		System.out.println(key + "=" + cmmd);
		
	} // end constructor
	
	private String findKey(String[] word, int indx, String pfx, String[] LIST){
		String key = "";
		
		for(int i=0; i<LIST.length; i++){
			if (word[indx].matches(LIST[i])) {
				key =  pfx + i;
				break;
			} // end if 
		} // end for 
		return key;
	} // end findKey() method 
	
	/**
	 * Basic expression which can be evaluated based on string values.
	 * 
	 * @param opr1       First value to evaluate
	 * @param comp       Type of evaluation to be performed
	 * @param opr2       Second value to evaluate
	 * @throws MessageException 
	 */
	public ExpressionNL(String opr1, String comp, String opr2) throws MessageException{
		super(opr1, comp, opr2);
		sdf = new SimpleDateFormat(SqlTable.JSDF_DATE_FMT);
		opr = new String[2];
		opr[0] = opr1;
		cmp    = comp.toLowerCase();
		
		System.out.println("Retrieve data using Expression(" + opr1 + "," + comp + "," + opr2 + ")");
		if (! isValidOpr(comp)) throw new MessageException("Invalid comparator " + comp + " was provided.");
		
		opr[1] = ((cmp.equals("like")) 
	 		       ? opr2.replace('*', '%') 
	 		       : opr2);
		for (int i=0; i<opr.length; i++){
			opr[i] = cvtAlias((String) opr[i]);
			if (isDate((String) opr[1])) opr[i] = cvt2Date(comp, (String) opr[i]);
		} // end for 
	} // end constructor
	
	public static void main(String... args) throws MessageException{
		/*
		QUAL   = {"maiden[.]*","father[.]*","mother[.]*","birth","death","first","last"};
	    SUBJ   = {"born","00","died","01","name","02","lived","03"};
	    VERBS  = {"was","is","before","after","in","on"};
	    */
		new ExpressionNL("mother was Virginia");
	}
	
} // end class
