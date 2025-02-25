package com.example.textile.controller;

import com.example.textile.dto.ErrorResponseDto;
import com.example.textile.entity.FabricDesign;
import com.example.textile.service.FabricDesignService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("fabricDesign")
public class FabricDesignController {
    FabricDesignService fabricDesignService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody FabricDesign design) {
        try {
            FabricDesign fabricDesign = fabricDesignService.saveOrUpdate(design);
            URI location = URI.create("/"+fabricDesign.getId());
            return ResponseEntity.created(location).body(fabricDesign);
        } catch (DataAccessException e) {
            log.error("Exception while findById() :{}", e.getLocalizedMessage(), e);
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorDateTime(LocalDateTime.now())
                    .errorMessages(Map.of("name", "Already Exist in DB", "id", String.valueOf(design.getId())))
                    .build();
            return ResponseEntity.badRequest().body(errorResponseDto);
        } catch (Exception e) {
            log.error("Exception while findById() :{}", e.getLocalizedMessage(), e);
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorDateTime(LocalDateTime.now())
                    .errorMessages(Map.of("system", e.getLocalizedMessage()))
                    .build();
            return ResponseEntity.internalServerError().body(errorResponseDto);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        String logPrefix = "findById()";
        String logSuffix = createLogSuffix("id",id);
        log.info(createEntryLog(logPrefix));

        try {
            FabricDesign byId = fabricDesignService.findById(id);
            log.info(createExitLog(logPrefix, logSuffix));
            return ResponseEntity.ok(byId);
        } catch (Exception e) {
            log.error("Exception while findById() :{}", e.getLocalizedMessage(), e);
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorDateTime(LocalDateTime.now())
                    .errorMessages(Map.of("system", e.getLocalizedMessage()))
                    .build();
            log.info(createExitLog(logPrefix, logSuffix));
            return ResponseEntity.internalServerError().body(errorResponseDto);
        }
    }

    @GetMapping
    public ResponseEntity<List<FabricDesign>> fetchAll() {
        return ResponseEntity.ok(fabricDesignService.findAll());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteDesign(@RequestParam Long id) {
        try {
            fabricDesignService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Exception while deleteDesign() :" + e.getLocalizedMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}
