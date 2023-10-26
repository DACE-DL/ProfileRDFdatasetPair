package profiling.util;

public class Usage {
	
	private Integer resourceCount;
	private Integer usageSum;
	private double usageMean;
	private Integer usageMin;
	private Integer usageMax;
	
	public Usage() {
	}

	public Integer getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(Integer resourceCount) {
		this.resourceCount = resourceCount;
	}

	public Integer getUsageSum() {
		return usageSum;
	}

	public void setUsageSum(Integer usageSum) {
		this.usageSum = usageSum;
	}

	

	public Integer getUsageMin() {
		return usageMin;
	}

	public void setUsageMin(Integer usageMin) {
		this.usageMin = usageMin;
	}

	public Integer getUsageMax() {
		return usageMax;
	}

	public void setUsageMax(Integer usageMax) {
		this.usageMax = usageMax;
	}

	public double getUsageMean() {
		return usageMean;
	}

	public void setUsageMean(double nUsageMean) {
		this.usageMean = nUsageMean;
	}
	
	
}
