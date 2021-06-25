package sample.model;

public class ConnectionTable {

    private String fullName, email, covixScore;

    public ConnectionTable(String fullName, String email, String covixScore) {
        this.fullName = fullName;
        this.email = email;
        this.covixScore = covixScore;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCovixScore() {
        return covixScore;
    }

    public void setCovixScore(String covixScore) {
        this.covixScore = covixScore;
    }
}
