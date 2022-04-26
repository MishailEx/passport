package ru.job4j.passport.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.passport.model.Passport;

import java.time.LocalDate;
import java.util.List;

public interface PassportRepository extends CrudRepository<Passport, Integer> {
    List<Passport> findBySerial(int serial);
    @Modifying
    @Query("delete Passport p where p.id = :id")
    int deleteById(@Param("id") int id);
    List<Passport> findByDateIssueBetween(LocalDate dateStart, LocalDate dateFinish);
}
