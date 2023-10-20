
package kz.services;

import kz.model.HealthRecord;
import kz.reps.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthRecordService {
    
    private final HealthRecordRepository healthRecordRepository;
    
    @Autowired
    public HealthRecordService(HealthRecordRepository healthRecordRepository) {
        this.healthRecordRepository = healthRecordRepository;
    }
    
    public List<HealthRecord> getAllHealthRecords() {
        return healthRecordRepository.findAll();
    }
}
