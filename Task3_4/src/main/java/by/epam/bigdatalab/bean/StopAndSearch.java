package by.epam.bigdatalab.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.Objects;

public class StopAndSearch {

    @JSONField(name = "type")
    String type;

    @JSONField(name = "involved_person")
    boolean involvedPerson;

    @JSONField(name = "datetime")
    Date datetime;

    @JSONField(name = "operation")
    boolean operation;

    @JSONField(name = "operation_name")
    String operationName;

    @JSONField(name = "location")
    Location location;

    @JSONField(name = "gender")
    String gender;

    @JSONField(name = "age_range")
    String ageRange;

    @JSONField(name = "self_defined_ethnicity")
    String selfDefinedEthnicity;

    @JSONField(name = "officer_defined_ethnicity")
    String officerDefinedEthnicity;

    @JSONField(name = "legislation")
    String legislation;

    @JSONField(name = "object_of_search")
    String objectOfSearch;

    @JSONField(name = "outcome")
    String outcome;

    @JSONField(name = "outcome_linked_to_object_of_search")
    String outcomeLinkedToObjectOfSearch;

    @JSONField(name = "removal_of_more_than_outer_clothing")
    boolean removalOfMoreThanOuterClothing;

    public String getOutcome() {
        return outcome;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInvolvedPerson() {
        return involvedPerson;
    }

    public void setInvolvedPerson(boolean involvedPerson) {
        this.involvedPerson = involvedPerson;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public boolean isOperation() {
        return operation;
    }

    public void setOperation(boolean operation) {
        this.operation = operation;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getSelfDefinedEthnicity() {
        return selfDefinedEthnicity;
    }

    public void setSelfDefinedEthnicity(String selfDefinedEthnicity) {
        this.selfDefinedEthnicity = selfDefinedEthnicity;
    }

    public String getOfficerDefinedEthnicity() {
        return officerDefinedEthnicity;
    }

    public void setOfficerDefinedEthnicity(String officerDefinedEthnicity) {
        this.officerDefinedEthnicity = officerDefinedEthnicity;
    }

    public String getLegislation() {
        return legislation;
    }

    public void setLegislation(String legislation) {
        this.legislation = legislation;
    }

    public String getObjectOfSearch() {
        return objectOfSearch;
    }

    public void setObjectOfSearch(String objectOfSearch) {
        this.objectOfSearch = objectOfSearch;
    }

    public String isOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcomeLinkedToObjectOfSearch() {
        return outcomeLinkedToObjectOfSearch;
    }

    public void setOutcomeLinkedToObjectOfSearch(String outcomeLinkedToObjectOfSearch) {
        this.outcomeLinkedToObjectOfSearch = outcomeLinkedToObjectOfSearch;
    }

    public boolean isRemovalOfMoreThanOuterClothing() {
        return removalOfMoreThanOuterClothing;
    }

    public void setRemovalOfMoreThanOuterClothing(boolean removalOfMoreThanOuterClothing) {
        this.removalOfMoreThanOuterClothing = removalOfMoreThanOuterClothing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopAndSearch that = (StopAndSearch) o;
        return involvedPerson == that.involvedPerson &&
                operation == that.operation &&
                removalOfMoreThanOuterClothing == that.removalOfMoreThanOuterClothing &&
                Objects.equals(type, that.type) &&
                Objects.equals(datetime, that.datetime) &&
                Objects.equals(operationName, that.operationName) &&
                Objects.equals(location, that.location) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(ageRange, that.ageRange) &&
                Objects.equals(selfDefinedEthnicity, that.selfDefinedEthnicity) &&
                Objects.equals(officerDefinedEthnicity, that.officerDefinedEthnicity) &&
                Objects.equals(legislation, that.legislation) &&
                Objects.equals(objectOfSearch, that.objectOfSearch) &&
                Objects.equals(outcome, that.outcome) &&
                Objects.equals(outcomeLinkedToObjectOfSearch, that.outcomeLinkedToObjectOfSearch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, involvedPerson, datetime, operation, operationName, location, gender, ageRange, selfDefinedEthnicity, officerDefinedEthnicity, legislation, objectOfSearch, outcome, outcomeLinkedToObjectOfSearch, removalOfMoreThanOuterClothing);
    }

    @Override
    public String toString() {
        return "StopAndSearch{" +
                "type='" + type + '\'' +
                ", involvedPerson=" + involvedPerson +
                ", datetime=" + datetime +
                ", operation=" + operation +
                ", operationName='" + operationName + '\'' +
                ", location=" + location +
                ", gender='" + gender + '\'' +
                ", ageRange='" + ageRange + '\'' +
                ", selfDefinedEthnicity='" + selfDefinedEthnicity + '\'' +
                ", officerDefinedEthnicity='" + officerDefinedEthnicity + '\'' +
                ", legislation='" + legislation + '\'' +
                ", objectOfSearch='" + objectOfSearch + '\'' +
                ", outcome=" + outcome +
                ", outcomeLinkedToObjectOfSearch='" + outcomeLinkedToObjectOfSearch + '\'' +
                ", removalOfMoreThanOuterClothing=" + removalOfMoreThanOuterClothing +
                '}';
    }
}
