package ru.job4j.passport.service;

import org.springframework.stereotype.Service;
import ru.job4j.passport.model.Passport;
import ru.job4j.passport.repository.PassportRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PassportService {
    private final PassportRepository repository;

    public PassportService(PassportRepository repository) {
        this.repository = repository;
    }

    public void save(Passport passport) {
        repository.save(passport);
    }

    public boolean update(int id, Passport passport) {
        boolean rsl = false;
        passport.setId(id);
        repository.save(passport);
        if (repository.findById(id).get() == passport) {
            rsl = true;
        }
        return rsl;
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public List<Passport> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public List<Passport> findBySerial(int serial) {
        return repository.findBySerial(serial);
    }

    public List<Passport> getUnavaliablePassport() throws ParseException {
        LocalDate date = LocalDate.now().minusYears(10);
        return repository.findByDateIssueBefore(date);
    }

    public List<Passport> findReplaceable() throws ParseException {
        LocalDate dateAfter = LocalDate.now().minusYears(10);
        LocalDate dateBefore = LocalDate.now().minusYears(10).plusMonths(3);
        return repository.findByDateIssueBetween(dateAfter, dateBefore);
    }

//    public static void main(String[] args) throws ParseException {
//        Date date =  new SimpleDateFormat("dd/MM/yyyy")
//                .parse(LocalDate.now().minusYears(10).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        System.out.println(date);
//    }
}
