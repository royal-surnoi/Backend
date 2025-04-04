package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.CriteriaRepo;
import fusionIQ.AI.V2.fusionIq.repository.GradeRepo;
import fusionIQ.AI.V2.fusionIq.repository.ProjectRepo;
import fusionIQ.AI.V2.fusionIq.service.GradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
public class GradingController {

    @Autowired
    private GradingService gradingService;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private CriteriaRepo criteriaRepo;

    @Autowired
    private GradeRepo gradeRepo;

    @PostMapping("/grade")
    public Grade addGrade(@RequestBody Grade grade) {
        return gradingService.addGrade(grade);
    }

    @PostMapping("/criteria")
    public Criteria addCriteria(@RequestBody Criteria criteria) {
        return gradingService.addCriteria(criteria);
    }

    @PostMapping("/add/{projectId}/{criteriaId}")
    public Grade addGrade(@PathVariable Long projectId,
                          @PathVariable Long criteriaId,
                          @RequestParam int points) {
        Optional<Project> project = projectRepo.findById(projectId);
        Optional<Criteria> criteria = criteriaRepo.findById(criteriaId);

        if (project.isPresent() && criteria.isPresent()) {
            Grade grade = new Grade();
            grade.setCriteria(criteria.get());
            grade.setProject(project.get());
            grade.setPoints(points);
            return gradeRepo.save(grade);
        } else {
            throw new RuntimeException("projectId or criteriaId not found");
        }
    }

    @GetMapping("/{projectId}")
    public List<Grade> getGradesForProject(@PathVariable Long projectId) {
        return gradingService.getGradesForProject(projectId);
    }


    @GetMapping("/final/{projectId}")
    public double calculateFinalGrade(@PathVariable Long projectId) {
        return gradingService.calculateFinalGrade(projectId);
    }
}

