package sample.model;

public class GroupTable {

    private String groupArea, numberOfPeople;

    public GroupTable(String groupArea, String numberOfPeople) {
        this.groupArea = groupArea;
        this.numberOfPeople = numberOfPeople;
    }

    public void setnumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public String getGroupArea() {
        return groupArea;
    }

    public void setGroupArea(String groupArea) {
        this.groupArea = groupArea;
    }
}
