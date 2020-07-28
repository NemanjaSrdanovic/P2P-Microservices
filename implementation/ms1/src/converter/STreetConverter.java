package converter;

import java.util.ArrayList;
import java.util.List;

import messages.Field;
import messages.Street;
import model.ARSField;
import model.ARSStreet;

/**
 * This Class is used to convert Street objects from network into ARSStreet objects and vice versa
 * @author nenad.cikojevic
 *
 */
public class STreetConverter {
	
	public static Street convertFrom(ARSStreet street) {
		
		List<Field>fields = new ArrayList<>();
		
		for(ARSField f:street.getFields()) {
			Field fieldTORet = FieldConverter.convertFrom(f);
			fields.add(fieldTORet);
		}
		return new Street(street.getName(), fields);
	}
	
	public static ARSStreet convertFrom(Street street) {
		
		List<ARSField>fields = new ArrayList<>();
		
		for(Field f:street.getFields()) {
			ARSField fieldTORet = FieldConverter.convertFrom(f);
			fields.add(fieldTORet);
		}
		return new ARSStreet(street.getName(), fields);
		
	}
	
//	public static Street convertJustDrivableFrom(ARSStreet street) {
//		
//		List<Field>fields = new ArrayList<>();
//		
//		for(ARSField f:street.getFields()) {
//			Field fieldTORet = FieldConverter.convertFrom(f);
//			if(fieldTORet.isDrivable())
//				fields.add(fieldTORet);
//		}
//		return new Street(street.getName(), fields);
//	}
}	
