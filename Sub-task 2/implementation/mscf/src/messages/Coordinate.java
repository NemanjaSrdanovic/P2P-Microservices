package messages;

import java.io.Serializable;

public class Coordinate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	
	public Coordinate(int x, int y) {
		validateXY(x, y);
		this.y = y;
		this.x = x;
		
	}
	
	public static void validateXY(int x, int y) {
		if(x<0||x>=15||y<0||y>=15)throw new IllegalArgumentException("Coordinate not valid, must be between" +0+" "+15);
	}
	
	
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "x= "+x+" y="+y;
	}
	
	@Override
	public boolean equals(Object obj) {
		   // If the object is compared with itself then return true   
        if (obj == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Coordinate)) { 
            return false; 
        } 
          
        // typecast o to Complex so that we can compare data members  
        Coordinate c = (Coordinate) obj; 
          
        // Compare the data members and return accordingly  
        return Integer.compare(x, c.x) == 0
                && Integer.compare(y, c.y) == 0; 
    } 
		
}
