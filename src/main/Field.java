package main;

public class Field {
	
	private int fieldId;
	private String name;
	private Crop crop;
	private int cropQuantity;
	private int maxCropQuantity;
    private double soilQuality;
	
	public Field(
			int fieldId,
			String name,
			int maxCropQuantity,
			double soilQuality) {
		this.fieldId = fieldId;
		this.name = name;
		this.maxCropQuantity = maxCropQuantity;
		this.soilQuality = soilQuality;
	}
	
	public void setCrop(Crop crop) {
		this.crop = crop;
	}
	
	public int getFieldId() {
		return fieldId;
	}
	
	public Crop getCrop() {
		return crop;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCropQuantity() {
		return cropQuantity;
	}
	
	public int getMaxCropQuantity() {
		return maxCropQuantity;
	}
	
	public double getSoilQuality() {
        return soilQuality;
    }

	public boolean isEmpty() {
	    return crop == null;
	}
	
	public void setCropQuantity(int cropQuantity) {
        this.cropQuantity = cropQuantity;
    }

    public void clear() {
        crop = null;
        cropQuantity = 0;
    }
    
    @Override
    public String toString() {
    	return name;
    }
	
}
