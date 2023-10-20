package kz.services;

import kz.model.Diagnosis;
import kz.model.Patient;
import kz.reps.DiagnosisRepository;
import kz.reps.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;
    private final PatientRepository patientRepository;

    public Diagnosis createDiagnosis(Patient patient, String symptoms) {
        String preliminaryDiagnosis = "Example Diagnosis";  // Mocked for example

        Diagnosis diagnosis = new Diagnosis(null, patient, symptoms, preliminaryDiagnosis);
        return diagnosisRepository.save(diagnosis);
    }
}
