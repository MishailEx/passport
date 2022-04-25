package ru.job4j.passport.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.passport.model.Passport;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface PassportRepository extends CrudRepository<Passport, Integer> {
    List<Passport> findBySerial(int serial);
    void deleteById(int id);
    List<Passport> findByDateIssueBefore(LocalDate date);
    List<Passport> findByDateIssueBetween(LocalDate dateStart, LocalDate dateFinish);
}
