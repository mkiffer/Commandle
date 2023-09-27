package edu.monash.commandle;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class DatePractice {



    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime endOfDate = LocalTime.MAX.atDate(localDate);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/mm HH:mm:ss");
        System.out.println(dtf.format(endOfDate));
    }
}
