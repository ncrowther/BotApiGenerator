package rpa.api.parameters;


public class RpaParameter {

	private ParameterDirection direction;
	private String objectType;
	private String name;

	public RpaParameter(ParameterDirection direction,String name, String  objectType) {
		this.direction = direction;
		this.name = name;
		this.objectType = objectType;
	}

	@Override
	public String toString() {
		return "RpaParameter [direction=" + direction + ", objectType=" + objectType
				+ ", name=" + name  + "]";
	}

	public ParameterDirection getDirection() {
		return direction;
	}

	public void setDirection(ParameterDirection direction) {
		this.direction = direction;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
