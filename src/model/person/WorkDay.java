package model.person;

public class WorkDay {
    private int startHour;
    private int endHour;

    public WorkDay() {
        startHour = 0;
        endHour = 0;
    }

    public WorkDay(int startHour, int endHour) {
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
}
