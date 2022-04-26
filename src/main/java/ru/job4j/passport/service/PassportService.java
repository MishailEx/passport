package ru.job4j.passport.service;

import org.springframework.stereotype.Service;
import ru.job4j.passport.model.Passport;
import ru.job4j.passport.repository.PassportRepository;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public int delete(int id) {
        return repository.deleteById(id);
    }

    public List<Passport> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public List<Passport> findBySerial(int serial) {
        return repository.findBySerial(serial);
    }

    public List<Passport> getUnavaliablePassport() {
        List<Passport> list = new ArrayList<>();
        LocalDate date20 = minusYear(20);
        LocalDate date45 = minusYear(45);
        list.addAll(repository.findByDateIssueBetween(date20.minusMonths(1), date20));
        list.addAll(repository.findByDateIssueBetween(date45.minusMonths(1), date45));
        return list;
    }

    public List<Passport> findReplaceable() {
        List<Passport> list = new ArrayList<>();
        LocalDate date20 = minusYear(20);
        LocalDate date45 = minusYear(45);
        list.addAll(repository.findByDateIssueBetween(date20, date20.plusMonths(3)));
        list.addAll(repository.findByDateIssueBetween(date45, date45.plusMonths(3)));
        return list;
    }

    private LocalDate minusYear(int year) {
       return LocalDate.now().minusYears(year);
    }
}
