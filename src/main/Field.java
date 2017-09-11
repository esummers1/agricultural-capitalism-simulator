package main;

public class Field {
	
	private int fieldID;
	private String name;
	private int cropID;
	private int content;
	private int area;
	
	public Field(
			int fieldID,
			int cropID,
			String name,
			int content,
			int area) {
		
		this.fieldID = fieldID;
		this.cropID = cropID;
		this.content = content;
		this.area = area;
	}
	
	public int getFieldID() {
		return fieldID;
	}
	
	public int getCropID() {
		return cropID;
	}
	
	public String getName() {
		return name;
	}
	
	public int getContent() {
		return content;
	}
	
	public int getArea() {
		return area;
	}

}
