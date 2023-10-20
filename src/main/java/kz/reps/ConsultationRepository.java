package kz.reps;

import kz.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    @Query(value = "SELECT * FROM consultations WHERE patient_id = :patientId", nativeQuery = true)
    List<Consultation> findAllByPatientId(Long patientId);
}
