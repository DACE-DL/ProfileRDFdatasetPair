package profiling.util;

public class ProfilingResultsObject {
	private Integer NumberOfTriples;
	
	private Integer propertyUsageDistinctPerSubjectSubjectCount;
	private Integer propertyUsageDistinctPerSubjectUsageSum;
	private double propertyUsageDistinctPerSubjectUsageMean;
	private Integer propertyUsageDistinctPerSubjectUsageMin;
	private Integer propertyUsageDistinctPerSubjectUsageMax;
	
	private Integer propertyUsageDistinctPerObjectObjectCount;
	private Integer propertyUsageDistinctPerObjectUsageSum;
	private double propertyUsageDistinctPerObjectUsageMean;
	private Integer propertyUsageDistinctPerObjectUsageMin;
	private Integer propertyUsageDistinctPerObjectUsageMax;

	private double outDegree;
	private double inDegree;

	private Integer propertyHierarchyDeep;
	private boolean propertyHierarchyLoop;
	
	private Integer subclassUsage;

	private Integer entitiesMentioned;

	private Integer distinctEntities;

	private Integer numberOfLiterals;

	private Integer numberBlanksAsSubj;

	private Integer numberBlanksAsObj;

	private double numberTypedStringLength;

	private double numberUntypedStringLength;

	private Integer numberTypedSubjects;

	private Integer numberLabeledSubjects;

	private Integer numberSameAs;

	private Integer classHierarchyDeep;
	private boolean classHierarchyLoop;
	
	public Integer getNumberOfTriples() {
		return NumberOfTriples;
	}

	public void setNumberOfTriples(Integer numberOfTriples2) {
		NumberOfTriples = numberOfTriples2;
	}
	
	public double getOutDegree() {
		return outDegree;
	}


	public void setOutDegree(double outDegree) {
		this.outDegree = outDegree;
	}


	public double getInDegree() {
		return inDegree;
	}


	public void setInDegree(double inDegree) {
		this.inDegree = inDegree;
	}


	public Integer getPropertyUsageDistinctPerSubjectSubjectCount() {
		return propertyUsageDistinctPerSubjectSubjectCount;
	}


	public void setPropertyUsageDistinctPerSubjectSubjectCount(Integer propertyUsageDistinctPerSubjectSubjectCount) {
		this.propertyUsageDistinctPerSubjectSubjectCount = propertyUsageDistinctPerSubjectSubjectCount;
	}


	public Integer getPropertyUsageDistinctPerSubjectUsageSum() {
		return propertyUsageDistinctPerSubjectUsageSum;
	}


	public void setPropertyUsageDistinctPerSubjectUsageSum(Integer propertyUsageDistinctPerSubjectUsageSum) {
		this.propertyUsageDistinctPerSubjectUsageSum = propertyUsageDistinctPerSubjectUsageSum;
	}


	public double getPropertyUsageDistinctPerSubjectUsageMean() {
		return propertyUsageDistinctPerSubjectUsageMean;
	}


	public void setPropertyUsageDistinctPerSubjectUsageMean(double propertyUsageDistinctPerSubjectUsageMean) {
		this.propertyUsageDistinctPerSubjectUsageMean = propertyUsageDistinctPerSubjectUsageMean;
	}


	public Integer getPropertyUsageDistinctPerSubjectUsageMin() {
		return propertyUsageDistinctPerSubjectUsageMin;
	}


	public void setPropertyUsageDistinctPerSubjectUsageMin(Integer propertyUsageDistinctPerSubjectUsageMin) {
		this.propertyUsageDistinctPerSubjectUsageMin = propertyUsageDistinctPerSubjectUsageMin;
	}


	public Integer getPropertyUsageDistinctPerSubjectUsageMax() {
		return propertyUsageDistinctPerSubjectUsageMax;
	}


	public void setPropertyUsageDistinctPerSubjectUsageMax(Integer propertyUsageDistinctPerSubjectUsageMax) {
		this.propertyUsageDistinctPerSubjectUsageMax = propertyUsageDistinctPerSubjectUsageMax;
	}


	public Integer getPropertyUsageDistinctPerObjectObjectCount() {
		return propertyUsageDistinctPerObjectObjectCount;
	}


	public void setPropertyUsageDistinctPerObjectObjectCount(Integer propertyUsageDistinctPerObjectObjectCount) {
		this.propertyUsageDistinctPerObjectObjectCount = propertyUsageDistinctPerObjectObjectCount;
	}


	public Integer getPropertyUsageDistinctPerObjectUsageSum() {
		return propertyUsageDistinctPerObjectUsageSum;
	}


	public void setPropertyUsageDistinctPerObjectUsageSum(Integer propertyUsageDistinctPerObjectUsageSum) {
		this.propertyUsageDistinctPerObjectUsageSum = propertyUsageDistinctPerObjectUsageSum;
	}


	public double getPropertyUsageDistinctPerObjectUsageMean() {
		return propertyUsageDistinctPerObjectUsageMean;
	}


	public void setPropertyUsageDistinctPerObjectUsageMean(double propertyUsageDistinctPerObjectUsageMean) {
		this.propertyUsageDistinctPerObjectUsageMean = propertyUsageDistinctPerObjectUsageMean;
	}


	public Integer getPropertyUsageDistinctPerObjectUsageMin() {
		return propertyUsageDistinctPerObjectUsageMin;
	}


	public void setPropertyUsageDistinctPerObjectUsageMin(Integer propertyUsageDistinctPerObjectUsageMin) {
		this.propertyUsageDistinctPerObjectUsageMin = propertyUsageDistinctPerObjectUsageMin;
	}


	public Integer getPropertyUsageDistinctPerObjectUsageMax() {
		return propertyUsageDistinctPerObjectUsageMax;
	}


	public void setPropertyUsageDistinctPerObjectUsageMax(Integer propertyUsageDistinctPerObjectUsageMax) {
		this.propertyUsageDistinctPerObjectUsageMax = propertyUsageDistinctPerObjectUsageMax;
	}


	public ProfilingResultsObject() {
		super();
	}

	public Integer getPropertyHierarchyDeep() {
		return propertyHierarchyDeep;
	}


	public void setPropertyHierarchyDeep(Integer propertyHierarchyDeep) {
		this.propertyHierarchyDeep = propertyHierarchyDeep;
	}


	public boolean isPropertyHierarchyLoop() {
		return propertyHierarchyLoop;
	}


	public void setPropertyHierarchyLoop(boolean propertyHierarchyLoop) {
		this.propertyHierarchyLoop = propertyHierarchyLoop;
	}

	public Integer getSubclassUsage() {
		return subclassUsage;
	}

	public void setSubclassUsage(Integer subclassUsage) {
		this.subclassUsage = subclassUsage;
	}

	public Integer getEntitiesMentioned() {
		return entitiesMentioned;
	}

	public void setEntitiesMentioned(Integer entitiesMentioned) {
		this.entitiesMentioned = entitiesMentioned;
	}

	public Integer getDistinctEntities() {
		return distinctEntities;
	}

	public void setDistinctEntities(Integer distinctEntities) {
		this.distinctEntities = distinctEntities;
	}

	public Integer getNumberOfLiterals() {
		return numberOfLiterals;
	}

	public void setNumberOfLiterals(Integer numberOfLiterals) {
		this.numberOfLiterals = numberOfLiterals;
	}

	public Integer getNumberBlanksAsSubj() {
		return numberBlanksAsSubj;
	}

	public void setNumberBlanksAsSubj(Integer numberBlanksAsSubj) {
		this.numberBlanksAsSubj = numberBlanksAsSubj;
	}

	public Integer getNumberBlanksAsObj() {
		return numberBlanksAsObj;
	}

	public void setNumberBlanksAsObj(Integer numberBlanksAsObj) {
		this.numberBlanksAsObj = numberBlanksAsObj;
	}

	public double getNumberTypedStringLength() {
		return numberTypedStringLength;
	}

	public void setNumberTypedStringLength(double numberTypedStringLength) {
		this.numberTypedStringLength = numberTypedStringLength;
	}

	public double getNumberUntypedStringLength() {
		return numberUntypedStringLength;
	}

	public void setNumberUntypedStringLength(double numberUntypedStringLength) {
		this.numberUntypedStringLength = numberUntypedStringLength;
	}

	public Integer getNumberTypedSubjects() {
		return numberTypedSubjects;
	}

	public void setNumberTypedSubjects(Integer numberTypedSubjects) {
		this.numberTypedSubjects = numberTypedSubjects;
	}

	public Integer getNumberLabeledSubjects() {
		return numberLabeledSubjects;
	}

	public void setNumberLabeledSubjects(Integer numberLabeledSubjects) {
		this.numberLabeledSubjects = numberLabeledSubjects;
	}

	public Integer getNumberSameAs() {
		return numberSameAs;
	}

	public void setNumberSameAs(Integer numberSameAs) {
		this.numberSameAs = numberSameAs;
	}

	public Integer getClassHierarchyDeep() {
		return classHierarchyDeep;
	}

	public void setClassHierarchyDeep(Integer classHierarchyDeep) {
		this.classHierarchyDeep = classHierarchyDeep;
	}

	public boolean isClassHierarchyLoop() {
		return classHierarchyLoop;
	}

	public void setClassHierarchyLoop(boolean classHierarchyLoop) {
		this.classHierarchyLoop = classHierarchyLoop;
	}

	
	

}
