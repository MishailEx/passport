package ru.job4j.passport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.job4j.passport.model.Passport;
import ru.job4j.passport.service.PassportService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@EnableScheduling
@RestController
@RequestMapping("/passport")
public class PassportController {
    private final PassportService service;
    private final ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    public PassportController(PassportService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public void add(@RequestBody Passport passport) {
        service.save(passport);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestParam int id, @RequestBody Passport passport) {
        boolean rsl = service.update(id, passport);
        return ResponseEntity
                .status(rsl ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam int id) {
        int rsl = service.delete(id);
        return ResponseEntity
                .status(rsl != 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .build();
    }

    @GetMapping("/find")
    public List<Passport> findAll(@RequestParam(required = false) String serial) {
        if (serial != null) {
            int check = Integer.parseInt(serial);
            return service.findBySerial(check);
        }
        return service.findAll();
    }

    @ExceptionHandler(value = {NumberFormatException.class})
    public void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
    }

    @GetMapping("/unavaliabe")
    @Scheduled(cron = "0 0 8 * * *")
    public List<Passport> unavaliabe() {
        return service.getUnavaliablePassport().stream()
                .peek((p) -> kafkaTemplate.send("email", p.getId(), "replace passport"))
                .collect(Collectors.toList());
    }

    @GetMapping("/find-replaceable")
    @Scheduled(cron = "0 0 8 * * *")
    public List<Passport> findReplaceable() {
        return service.findReplaceable().stream()
                .peek((p) -> kafkaTemplate.send("email", p.getId(), "soon replace passport"))
                .collect(Collectors.toList());
    }
}
