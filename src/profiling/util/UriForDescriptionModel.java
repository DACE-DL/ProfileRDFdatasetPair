package profiling.util;

import java.util.ArrayList;

import org.apache.jena.ontology.OntModel;

public class UriForDescriptionModel {
	
	private OntModel descriptionModel;
	private ArrayList<UriListAndUriList> listOfRelationshipsDomain;
	private ArrayList<UriListAndUriList> listOfRelationshipsRange;
	
	public UriForDescriptionModel() {
	}

	public UriForDescriptionModel(OntModel descriptionModel,
			ArrayList<UriListAndUriList> listOfRelationshipsDomain,
			ArrayList<UriListAndUriList> listOfRelationshipsRange) {
		this.descriptionModel = descriptionModel;
		this.listOfRelationshipsDomain = listOfRelationshipsDomain;
		this.listOfRelationshipsRange = listOfRelationshipsRange;
	}

	public OntModel getDescriptionModel() {
		return descriptionModel;
	}

	public void setDescriptionModel(OntModel descriptionModel) {
		this.descriptionModel = descriptionModel;
	}

	public ArrayList<UriListAndUriList> getListOfRelationshipsDomain() {
		return listOfRelationshipsDomain;
	}

	public void setListOfRelationshipsDomain(ArrayList<UriListAndUriList> listOfRelationshipsDomain) {
		this.listOfRelationshipsDomain = listOfRelationshipsDomain;
	}

	public ArrayList<UriListAndUriList> getListOfRelationshipsRange() {
		return listOfRelationshipsRange;
	}

	public void setListOfRelationshipsRange(ArrayList<UriListAndUriList> listOfRelationshipsRange) {
		this.listOfRelationshipsRange = listOfRelationshipsRange;
	}
	
}
