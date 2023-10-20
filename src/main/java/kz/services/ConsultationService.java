
package kz.services;

import kz.model.Consultation;
import kz.model.Doctor;
import kz.model.Patient;
import kz.reps.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final ConsultationRepository consultationRepository;

    public Consultation scheduleConsultation(Patient patient, Doctor doctor, LocalDateTime preferredTime) {
        Consultation consultation = new Consultation(null, patient, doctor, preferredTime);
        return consultationRepository.save(consultation);
    }
    public List<Consultation> getAllConsultationsForPatient(Long patientId) {
        return consultationRepository.findAllByPatientId(patientId);
    }
}
