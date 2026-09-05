package com.example.textile.controller;

import com.example.textile.entity.YarnBuilty;
import com.example.textile.service.YarnBuiltyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
@RestController
@RequestMapping("yarnBuilty")
public class YarnBuiltyController extends BaseController {

    @Autowired
    private YarnBuiltyService yarnBuiltyService;

    @PostMapping
    public ResponseEntity<YarnBuilty> save(@RequestBody YarnBuilty yarnBuilty) {
        String logPrefix = "save()";
        log.info(createEntryLog(logPrefix));
        YarnBuilty saved = yarnBuiltyService.save(yarnBuilty);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<YarnBuilty> findById(@PathVariable Long id) {
        String logPrefix = "findById()";
        log.info(createEntryLog(logPrefix));
        YarnBuilty yarnBuilty = yarnBuiltyService.findById(id);
        if (yarnBuilty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(yarnBuilty, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<YarnBuilty>> findAll() {
        String logPrefix = "findAll()";
        log.info(createEntryLog(logPrefix));
        List<YarnBuilty> all = yarnBuiltyService.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
