package model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class Coordinate{
	
	@Autowired
	private int x;
	
	@Autowired
	private int y;
	
	public Coordinate(int x, int y) {
		this.y = y;
		this.x = x;
		
	}	
	
	public int getX() {
		return x;
	}

	@Bean
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	
	@Bean
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return	"[x: "+x+", y:"+y +"]";
	}
		
}
