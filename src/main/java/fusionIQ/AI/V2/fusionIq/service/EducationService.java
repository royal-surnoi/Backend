package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Education;
import fusionIQ.AI.V2.fusionIq.data.PersonalDetails;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.EducationRepo;
import fusionIQ.AI.V2.fusionIq.repository.PersonalDetailsRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class EducationService {

    @Autowired
    private EducationRepo educationRepository;

    @Autowired
    private PersonalDetailsRepo personalDetailsRepository;
    @Autowired
    private UserRepo userRepo;



    // Update an existing Education record
    public Education updateEducation(Long id, Education updatedEducation) {
        Optional<Education> existingEducation = educationRepository.findById(id);
        if (existingEducation.isPresent()) {
            Education education = existingEducation.get();
            copyNonNullProperties(updatedEducation, education);
            return educationRepository.save(education);
        } else {
            throw new ResourceNotFoundException("Education not found with id: " + id);
        }
    }

    public void deleteEducation(Long id) {
        if (educationRepository.existsById(id)) {
            educationRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Education not found with id: " + id);
        }
    }

//    public List<Education> getAllEducationByPersonalDetailsId(Long personalDetailsId) {
//        return educationRepository.findByPersonalDetailsId(personalDetailsId);
//    }

    // Utility method to copy non-null properties
    private void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    // Get the names of properties that are null
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public List<Education> getEducationByUserId(Long userId) {
        return educationRepository.findByUserId(userId);
    }


    public Education saveEducation(Long userId, Education education) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        education.setUser(user); // Setting the user in the education entity.
        return educationRepository.save(education);
    }
    public Education updateEducation(Long userId, Long educationId, Education updatedEducation) {
        Optional<Education> existingEducation = educationRepository.findById(educationId);

        if (existingEducation.isPresent()) {
            Education education = existingEducation.get();

            // Check if the Education record is associated with the provided userId
            if (education.getUser() != null && Long.valueOf(education.getUser().getId()).equals(userId)) {
                copyNonNullProperties(updatedEducation, education);
                return educationRepository.save(education);
            } else {
                throw new IllegalArgumentException("User ID does not match the owner of the Education record.");
            }
        } else {
            throw new ResourceNotFoundException("Education not found with id: " + educationId);
        }
    }
}

