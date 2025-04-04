package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.InstituteClass;
import fusionIQ.AI.V2.fusionIq.data.InstituteClassRoomStudents;
import fusionIQ.AI.V2.fusionIq.data.InstituteStudent;
import fusionIQ.AI.V2.fusionIq.data.InstituteTeacher;
import fusionIQ.AI.V2.fusionIq.repository.InstituteClassRepo;
import fusionIQ.AI.V2.fusionIq.repository.InstituteClassRoomStudentsRepository;
import fusionIQ.AI.V2.fusionIq.repository.InstituteStudentRepo;
import fusionIQ.AI.V2.fusionIq.repository.InstituteTeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstituteClassRoomStudentsService {

    @Autowired
    private InstituteClassRoomStudentsRepository repository;

    @Autowired
    private InstituteStudentRepo studentRepository;

    @Autowired
    private InstituteClassRepo classRoomRepository;

    @Autowired
    private InstituteTeacherRepo teacherRepository;

    public InstituteClassRoomStudents addStudentToClassroom(Long studentId, Long classroomId, Long teacherId) {
        InstituteStudent student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        InstituteClass classroom = classRoomRepository.findById(classroomId).orElseThrow(() -> new RuntimeException("Classroom not found"));
        InstituteTeacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));

        InstituteClassRoomStudents record = new InstituteClassRoomStudents();
        record.setStudent(student);
        record.setClassroom(classroom);
        record.setTeacher(teacher);
        record.setActive(true);

        return repository.save(record);
    }

    public InstituteClassRoomStudents removeStudentFromClassroom(Long studentId, Long classroomId) {
        InstituteClassRoomStudents record = repository.findByStudentIdAndClassroom_Id(studentId, classroomId)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        record.setActive(false);
        return repository.save(record);
    }

    public List<InstituteClassRoomStudents> getStudentsByStudentId(Long studentId) {
        return repository.findByStudentId(studentId);
    }

    public List<InstituteClassRoomStudents> getStudentsByClassId(Long classId) {
        return repository.findByClassId(classId);
    }
}