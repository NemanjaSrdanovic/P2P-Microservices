package messages;
import java.io.Serializable;
import java.util.List;

public class Street implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private List<Field> fields;
	
	private int numbers;

	public Street(String name, List<Field> fields) {
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

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
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
