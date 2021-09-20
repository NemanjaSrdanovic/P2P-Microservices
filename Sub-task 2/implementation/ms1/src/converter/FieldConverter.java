package converter;

import messages.Field;
import model.ARSField;

/**
 * This class is used to convert Field Objects from network int ARSField objects and vice- versa
 * 
 * @author nenad.cikojevic
 *
 */
public class FieldConverter {

	public static ARSField convertFrom(Field field) {

		return new ARSField(field.getCoordinate(), field.getFieldType(), field.isDrivable());
	}

	public static Field convertFrom(ARSField field) {

		Field fieldToRet = new Field(field.getCoordinate());
		fieldToRet.setFieldType(field.getFieldType());
		fieldToRet.setDrivable(field.isDrivable());

		return fieldToRet;
	}
}
