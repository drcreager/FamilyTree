package com.ko.na;

/*
 * A location is a concatenated value of multiple governing entities separated by a delimiter. 
 *   Position   Precedence Mandatory  Name 
 *      1           2         No      City 
 *      2           5         No      Sub-Region (township)
 *      3           4         No      Region     (county | kommune | parish)
 *      4           3         No      State     
 *      5           1         Yes     Country
 *      
 *      The outer three entities are assumed fixed
 *      The inner two   entities are variable and may not exist.
 */
public class Location extends TreeComponents {
	
	public static final String PARSE_DLMTR = "[,;]";
	public static final String TOSTR_DLMTR = ";";
	
	protected String city;
	protected String subRegion;    // township 
	protected String region;       // county   | kommune | parish
	protected String state;        // May be written out or abbreviated
	protected String country;      // May be written out or abbreviated
	
	public Location (){
		city = null;
		subRegion = null;
		region = null;
		state = null;
		country = null;
	} // end constructor 

	public String getCity() {
		return (city != null) ? city : "";
	}

	public String getCountry() {
		return (country != null) ? country : "";
	}

	public String getRegion() {
		return (region != null) ? region : "";
	}

	public String getState() {
		return (state != null) ? state : "";
	}

	public String getSubRegion() {
		return (subRegion != null) ? subRegion : "";
	}

	public static Location parse(String locStr) throws Exception {
		return parse(locStr, PARSE_DLMTR);
	}
	public static Location parse(String locStr, String delimiter) throws Exception{
		Location result = new Location();
		if (locStr != null){
			String[] wrk = locStr.trim().split(delimiter);
			switch (wrk.length){
			case 1:
				result.setCountry(wrk[0]);
				break;
				
			case 2:
				result.setCity(wrk[0]);
				result.setCountry(wrk[1]);
				break;
				
			case 3:
				result.setCity(wrk[0]);
				result.setState(wrk[1]);
				result.setCountry(wrk[2]);
				break;
				
			case 4:
				result.setCity(wrk[0]);
				result.setRegion(wrk[1]);
				result.setState(wrk[2]);
				result.setCountry(wrk[3]);
				break;
				
			case 5:
				result.setCity(wrk[0]);
				result.setSubRegion(wrk[1]);
				result.setRegion(wrk[2]);
				result.setState(wrk[3]);
				result.setCountry(wrk[4]);
				break;
				
			default:
				throw new Exception("More than 5 location components found in " + locStr);
			} // end switch
		} // end if 
		return result;
	} // end parse() method 

	public void setCity(String city) {
		this.city = (city.length() > 0) ? city.trim() : null;
	}

	public void setCountry(String country) {
		this.country = (country.length() > 0) ? country.trim() : null;
	}

	public void setRegion(String region) {
		this.region = (region.length() > 0) ? region.trim() : null;
	}

	public void setState(String state) {
		this.state = (state.length() > 0) ? state.trim() : null;
	}

	public void setSubRegion(String subRegion) {
		this.subRegion = (subRegion.length() > 0) ? subRegion.trim() : null;
	}
	
	public String toString(){
		return ((city != null) ? city + TOSTR_DLMTR : "")
		     + (((subRegion != null) && (region != null)) ? subRegion + TOSTR_DLMTR : "")
	         + (((subRegion != null) && (region == null)) ? subRegion + TOSTR_DLMTR + TOSTR_DLMTR: "")
			 + ((region != null) ?  region + TOSTR_DLMTR : "") 
			 + ((state != null) ?  state + TOSTR_DLMTR : "")
			 + ((country != null) ? country : "");
	}
	public static void main(String[] args) throws Exception{
		System.out.println(Location.parse("Conklin,Chester,,MI;USA"));
	} // end main() method 
} // end Location class
