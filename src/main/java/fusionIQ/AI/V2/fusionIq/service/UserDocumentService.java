package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserDocunment;
import fusionIQ.AI.V2.fusionIq.repository.UserDocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDocumentService {

    @Autowired
    private UserDocumentRepo userDocumentRepo;

    // Save a document
    public UserDocunment saveDocument(UserDocunment userDocunment) {
        return userDocumentRepo.save(userDocunment);
    }

    // Get all documents
    public List<UserDocunment> getAllDocuments() {
        return userDocumentRepo.findAll();
    }

    // Get a document by ID
    public UserDocunment getDocumentById(Long id) {
        Optional<UserDocunment> document = userDocumentRepo.findById(id);
        if (document.isPresent()) {
            return document.get();
        } else {
            throw new RuntimeException("Document not found with id: " + id);
        }
    }

    // Delete a document by ID
    public void deleteDocument(Long id) {
        if (userDocumentRepo.existsById(id)) {
            userDocumentRepo.deleteById(id);
        } else {
            throw new RuntimeException("Document not found with id: " + id);
        }
    }
    public List<UserDocunment> getDocumentsByUserId(Long userId) {
        return userDocumentRepo.findByUser_Id(userId);
    }


}