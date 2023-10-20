
package kz.controllers;

import kz.model.HealthRecord;
import kz.services.HealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/healthRecords")
public class HealthRecordController {
    
    private final HealthRecordService healthRecordService;
    
    @Autowired
    public HealthRecordController(HealthRecordService healthRecordService) {
        this.healthRecordService = healthRecordService;
    }
    
    @GetMapping
    public ResponseEntity<List<HealthRecord>> getHealthRecords() {
        List<HealthRecord> healthRecords = healthRecordService.getAllHealthRecords();
        return ResponseEntity.ok(healthRecords);
    }
}
