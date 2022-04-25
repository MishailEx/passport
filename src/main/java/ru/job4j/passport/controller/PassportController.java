package ru.job4j.passport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.passport.model.Passport;
import ru.job4j.passport.service.PassportService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/passport")
public class PassportController {
    private final PassportService service;
    private final ObjectMapper objectMapper;

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
    public void delete(@RequestParam int id) {
        service.delete(id);
    }

    @GetMapping("/find")
    public List<Passport> findAll(@RequestParam(required = false) String serial) {
        if (serial != null) {
                int check = Integer.parseInt(serial);
                return service.findBySerial(check);
        }
        return service.findAll();
    }

    @ExceptionHandler(value = { NumberFormatException.class })
    public void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
    }

    @GetMapping("/unavaliabe")
    public List<Passport> unavaliabe() throws ParseException {
        return service.getUnavaliablePassport();
    }

    @GetMapping("/find-replaceable")
    public List<Passport> findReplaceable() throws ParseException {
        return service.findReplaceable();
    }
}
