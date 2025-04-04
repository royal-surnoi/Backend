package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Criteria;
import fusionIQ.AI.V2.fusionIq.data.Grade;
import fusionIQ.AI.V2.fusionIq.data.Project;
import fusionIQ.AI.V2.fusionIq.repository.CriteriaRepo;
import fusionIQ.AI.V2.fusionIq.repository.GradeRepo;
import fusionIQ.AI.V2.fusionIq.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradingService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private CriteriaRepo criteriaRepo;

    @Autowired
    private GradeRepo gradeRepo;

    public Project addProject(Project project) {
        return projectRepo.save(project);

    }



    public Criteria addCriteria(Criteria criteria) {
        return criteriaRepo.save(criteria);
    }

    public Grade addGrade(Grade grade) {
        return gradeRepo.save(grade);
    }



    public Grade addGrade(Long projectId, Long criteriaId, Grade grade) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Criteria criteria = criteriaRepo.findById(criteriaId)
                .orElseThrow(() -> new RuntimeException("Criteria not found"));
        Grade grade1 = new Grade();
        grade1.setProject(project);
        grade1.setCriteria(criteria);
        return gradeRepo.save(grade1);
    }


    public List<Grade> getGradesForProject(Long projectId) {
        return gradeRepo.findAllByProjectId(projectId);
    }


    public double calculateFinalGrade(Long projectId) {
        List<Grade> grades = getGradesForProject(projectId);
        double totalPoints = grades.stream().mapToInt(Grade::getPoints).sum();
        double maxPoints = grades.stream().mapToInt(grade -> grade.getCriteria().getMaxPoints()).sum();
        return (totalPoints / maxPoints) * 100;
    }


}
