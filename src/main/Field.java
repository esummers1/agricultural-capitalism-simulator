package main;

public class Field {
	
	private int fieldId;
	private String name;
	private String description;
	private Crop crop;
	private int cropQuantity;
	private int maxCropQuantity;
    private double soilQuality;
    private int price;
    private int lastRevenue;
	
	public Field(
			int fieldId,
			String name,
			String description,
			int maxCropQuantity,
			double soilQuality,
			int price) {
		this.fieldId = fieldId;
		this.name = name;
		this.description = description;
		this.maxCropQuantity = maxCropQuantity;
		this.soilQuality = soilQuality;
		this.price = price;
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
	
	public String getDescription() {
		return description;
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
	
	public int getPrice() {
		return price;
	}
	
	public int getLastRevenue() {
		return lastRevenue;
	}

	public boolean isEmpty() {
	    return crop == null;
	}
	
	public void setCropQuantity(int cropQuantity) {
        this.cropQuantity = cropQuantity;
    }
	
	public void setLastRevenue(int lastRevenue) {
		this.lastRevenue = lastRevenue;
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
