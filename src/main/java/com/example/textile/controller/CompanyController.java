package com.example.textile.controller;

import com.example.textile.dto.CompanyDropdownDto;
import com.example.textile.dto.CompanyDto;
import com.example.textile.dto.ErrorResponseDto;
import com.example.textile.entity.Company;
import com.example.textile.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/searchByName/{name}")
    @ResponseBody
    public List<Company> searchByName(@PathVariable("name") String name) {
        return companyService.findByNameLike(name);
    }

    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody CompanyDto companyDto, HttpServletRequest request) {
        try {
            CompanyDto saved = companyService.save(companyDto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while saving company: {}", e.getLocalizedMessage(), e);
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .errorMessages(Map.of("SystemError", messageSource.getMessage("System.Error", null, request.getLocale())))
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorDateTime(LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @GetMapping("search/{name}")
    public ResponseEntity<List<CompanyDropdownDto>> search(@PathVariable String name) {
        List<CompanyDropdownDto> byNameLike = companyService.getIdNameAndGstByName(name);
        return new ResponseEntity<>(byNameLike, HttpStatus.OK);
    }
}
