package com.example.textile.controller;

import com.example.textile.dto.YarnDto;
import com.example.textile.entity.Yarn;
import com.example.textile.service.YarnService;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/yarn")
public class YarnController {

    private final YarnService yarnService;

    public YarnController(YarnService yarnService) {
        this.yarnService = yarnService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping
    public List<YarnDto> fetchAll() {
        return yarnService.findAll();
    }

    public ResponseEntity<List<YarnDto>> fetchAllYarns() {
        List<YarnDto> yarnDtos = yarnService.findAll();
        return new ResponseEntity<>(yarnDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<YarnDto> getYarnById(@PathVariable Long id) {
        YarnDto yarnDto = yarnService.findById(id);
        if (yarnDto != null) {
            return new ResponseEntity<>(yarnDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<YarnDto> save(@Valid @RequestBody Yarn yarn) {
        YarnDto yarnDto = yarnService.save(yarn);
        return new ResponseEntity<>(yarnDto, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<YarnDto> patch(@PathVariable Long id, @RequestBody YarnDto yarnDto) {
        YarnDto dto = yarnService.updateYarn(id, yarnDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteYarn(@PathVariable Long id) {
        yarnService.deleteYarn(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
