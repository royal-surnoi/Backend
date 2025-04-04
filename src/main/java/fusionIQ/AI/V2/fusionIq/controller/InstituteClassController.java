package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.InstituteClass;
import fusionIQ.AI.V2.fusionIq.service.InstituteClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classrooms")
public class InstituteClassController {

    @Autowired
    private InstituteClassService classRoomService;

    @PostMapping("/CreateClass/{teacherId}/{instituteId}")
    public ResponseEntity<InstituteClass> createClassRoom(
            @PathVariable Long teacherId,
            @PathVariable Long instituteId,
            @RequestBody InstituteClass acadamicClass) {
        InstituteClass createdClass = classRoomService.createClassRoom(acadamicClass, teacherId, instituteId);
        return ResponseEntity.ok(createdClass);
    }

    @GetMapping("/by-teacher/{teacherId}")
    public List<InstituteClass> getClassesByTeacherId(@PathVariable Long teacherId) {
        return classRoomService.getClassesByTeacherId(teacherId);
    }

    @GetMapping("/by-institute/{instituteId}")
    public List<InstituteClass> getClassesByInstituteId(@PathVariable Long instituteId) {
        return classRoomService.getClassesByInstituteId(instituteId);
    }



}