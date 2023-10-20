package kz.controllers;

import kz.model.Diagnosis;
import kz.model.Patient;
import kz.services.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

    @PostMapping
    public Diagnosis createDiagnosis(@RequestBody Patient patient, @RequestParam String symptoms) {
        return diagnosisService.createDiagnosis(patient, symptoms);
    }
}

