package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Institute;
import fusionIQ.AI.V2.fusionIq.data.InstituteClass;
import fusionIQ.AI.V2.fusionIq.data.InstituteTeacher;
import fusionIQ.AI.V2.fusionIq.repository.InstituteClassRepo;
import fusionIQ.AI.V2.fusionIq.repository.InstituteRepo;
import fusionIQ.AI.V2.fusionIq.repository.InstituteTeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstituteClassService {


    @Autowired
    private InstituteClassRepo classRoomRepository;

    @Autowired
    private InstituteTeacherRepo teacherRepository;

    @Autowired
    private InstituteRepo instituteRepository;

    public InstituteClass createClassRoom(InstituteClass acadamicClass, Long teacherId, Long instituteId) {
        InstituteTeacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Institute institute = instituteRepository.findById(instituteId).orElseThrow(() -> new RuntimeException("Institute not found"));

        acadamicClass.setTeacher(teacher);
        acadamicClass.setInstitute(institute);

        return classRoomRepository.save(acadamicClass);
    }

    public List<InstituteClass> getClassesByTeacherId(Long teacherId) {
        return classRoomRepository.findByTeacherId(teacherId);
    }

    public List<InstituteClass> getClassesByInstituteId(Long instituteId) {
        return classRoomRepository.findByInstituteId(instituteId);
    }

}
