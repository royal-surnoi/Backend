package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserDocunment;
import fusionIQ.AI.V2.fusionIq.service.UserDocumentService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user-documents")
public class UserDocumentController {

    @Autowired
    private UserDocumentService userDocumentService;

    @Autowired
    private UserService userService;

    // Endpoint to upload a document using @RequestParam for document name and file
    @PostMapping("/add/{userId}")  // Updated endpoint
    public ResponseEntity<UserDocunment> uploadDocument(
            @RequestParam("documentName") String documentName,
            @RequestParam("file") MultipartFile file,
            @PathVariable Long userId) { // Keep userId as a PathVariable here

        try {
            // Create a new UserDocunment object with file bytes
            UserDocunment userDocument = new UserDocunment();
            userDocument.setDocumentName(documentName);
            userDocument.setDocumentData(file.getBytes());

            // Fetch the User by ID
            User user = userService.getUserById(userId);  // Update as per your existing UserService
            userDocument.setUser(user);

            // Save the document
            UserDocunment savedDocument = userDocumentService.saveDocument(userDocument);

            return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to delete a document by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        userDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to get all documents
    @GetMapping("/getall")
    public ResponseEntity<List<UserDocunment>> getAllDocuments() {
        List<UserDocunment> documents = userDocumentService.getAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    // Endpoint to get a document by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDocunment> getDocumentById(@PathVariable Long id) {
        UserDocunment document = userDocumentService.getDocumentById(id);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDocunment> updateDocumentByUserId(
            @PathVariable Long userId,
            @RequestParam(value = "documentName", required = false) String documentName,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            UserDocunment existingDocument = userDocumentService.getDocumentsByUserId(userId).stream().findFirst().orElse(null);
            if (existingDocument == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (documentName != null) {
                existingDocument.setDocumentName(documentName);
            }

            if (file != null) {
                existingDocument.setDocumentData(file.getBytes());
            }

            UserDocunment updatedDocument = userDocumentService.saveDocument(existingDocument);
            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get documents by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserDocunment>> getDocumentsByUserId(@PathVariable Long userId) {
        try {
            List<UserDocunment> userDocuments = userDocumentService.getDocumentsByUserId(userId);
            if (userDocuments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(userDocuments, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}