package otob.enumerator;

public enum Status {
    WAIT("Waiting"),
    ACCEPT("Accepted"),
    REJECT("Rejected");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
