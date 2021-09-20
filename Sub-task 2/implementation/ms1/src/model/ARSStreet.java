package model;
import java.util.List;

/**
 * Class holds ARSField objects of type Street collected and sorted by coordinate into a list with a name.
 * 
 * @author nenad.cikojevic
 *
 */
public class ARSStreet {
	
	private String name;
	private List<ARSField> fields;
	
	private int numbers;

	public ARSStreet(String name, List<ARSField> fields) {
		super();
		
		if(name==null||name.isEmpty())throw new IllegalArgumentException("NAme of street is either null or empty");
		this.name = name;
		
		if(fields==null||fields.isEmpty())throw new IllegalArgumentException("List of fields is either null or empty");
		this.fields = fields;
		this.numbers = this.fields.size();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {	
		if(name==null||name.isEmpty())throw new IllegalArgumentException("NAme of street is either null or empty");
		this.name = name;
	}

	public List<ARSField> getFields() {
		return fields;
	}

	public void setFields(List<ARSField> fields) {
		if(fields==null||fields.isEmpty())throw new IllegalArgumentException("List of fields is either null or empty");
		this.fields = fields;
	}

	public int getNumbers() {
		return numbers;
	}
	
	@Override
	public String toString() {
		
		return name+" "+numbers;
	}
	
}
