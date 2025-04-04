package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.InstituteClassRoomStudents;
import fusionIQ.AI.V2.fusionIq.service.InstituteClassRoomStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classroom/students")
public class InstituteClassRoomStudentsController {

    @Autowired
    private InstituteClassRoomStudentsService service;

    @PostMapping("/add")
    public ResponseEntity<InstituteClassRoomStudents> addStudentToClassroom(
            @RequestParam Long studentId,
            @RequestParam Long classroomId,
            @RequestParam Long teacherId) {
        InstituteClassRoomStudents student = service.addStudentToClassroom(studentId, classroomId, teacherId);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/remove")
    public ResponseEntity<InstituteClassRoomStudents> removeStudentFromClassroom(
            @RequestParam Long studentId,
            @RequestParam Long classroomId) {
        InstituteClassRoomStudents student = service.removeStudentFromClassroom(studentId, classroomId);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/by-student")
    public ResponseEntity<List<InstituteClassRoomStudents>> getStudentsByStudentId(@RequestParam Long studentId) {
        List<InstituteClassRoomStudents> students = service.getStudentsByStudentId(studentId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/by-class")
    public ResponseEntity<List<InstituteClassRoomStudents>> getStudentsByClassId(@RequestParam Long classId) {
        List<InstituteClassRoomStudents> students = service.getStudentsByClassId(classId);
        return ResponseEntity.ok(students);
    }


}