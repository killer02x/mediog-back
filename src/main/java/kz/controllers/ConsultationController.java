
package kz.controllers;
import kz.model.Consultation;
import kz.model.Doctor;
import kz.model.Patient;
import kz.services.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {
    private final ConsultationService consultationService;

    @PostMapping
    public Consultation scheduleConsultation(@RequestBody Patient patient,
                                             @RequestBody Doctor doctor,
                                             @RequestParam LocalDateTime preferredTime) {
        return consultationService.scheduleConsultation(patient, doctor, preferredTime);
    }
    @GetMapping("/patient/{patientId}")
    public List<Consultation> getAllConsultationsForPatient(@PathVariable Long patientId) {
        return consultationService.getAllConsultationsForPatient(patientId);
    }
}

