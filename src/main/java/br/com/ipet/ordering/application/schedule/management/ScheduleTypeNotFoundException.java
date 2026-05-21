package br.com.ipet.ordering.application.schedule.management;

public class ScheduleTypeNotFoundException extends RuntimeException {
    public ScheduleTypeNotFoundException(String s) {
        super(s);
    }
}
