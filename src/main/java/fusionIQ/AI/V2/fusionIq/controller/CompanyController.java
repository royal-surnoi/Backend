//package fusionIQ.AI.V2.fusionIq.controller;
//
//import fusionIQ.AI.V2.fusionIq.data.Company;
//import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
//import fusionIQ.AI.V2.fusionIq.service.CompanyService;
//import fusionIQ.AI.V2.fusionIq.service.JobAdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/companies")
//public class CompanyController {
//
//    private final CompanyService companyService;
//    private final JobAdminService jobAdminService;
//
//    @Autowired
//    public CompanyController(CompanyService companyService, JobAdminService jobAdminService) {
//        this.companyService = companyService;
//        this.jobAdminService = jobAdminService;
//    }
//
//    @PostMapping("/create/{jobAdminId}")
//    public ResponseEntity<Company> createCompany(
//            @PathVariable Long jobAdminId, // Get jobAdminId from the path
//            @RequestParam("companyDescription") String companyDescription,
//            @RequestParam("companyTypeOfIndustry") String companyTypeOfIndustry,
//            @RequestParam("companyWebsiteLink") String companyWebsiteLink,
//            @RequestParam("companyStrength") int companyStrength,
//            @RequestParam("companyLocation") String companyLocation,
//            @RequestParam("companyLicense") String companyLicense,
//            @RequestParam("companyGstNumber") String companyGstNumber,
//            @RequestParam("companyCinNumber") String companyCinNumber,
//            @RequestParam(value = "companyLogo", required = false) MultipartFile companyLogo,
//            @RequestParam(value = "companyLicenseDocument", required = false) MultipartFile companyLicenseDocument,
//            @RequestParam(value = "companyGstDocument", required = false) MultipartFile companyGstDocument,
//            @RequestParam(value = "companyCinDocument", required = false) MultipartFile companyCinDocument) throws IOException {
//
//        byte[] logoBytes = (companyLogo != null) ? companyLogo.getBytes() : null;
//        byte[] licenseBytes = (companyLicenseDocument != null) ? companyLicenseDocument.getBytes() : null;
//        byte[] gstBytes = (companyGstDocument != null) ? companyGstDocument.getBytes() : null;
//        byte[] cinBytes = (companyCinDocument != null) ? companyCinDocument.getBytes() : null;
//
//        Company company = new Company(logoBytes, companyDescription, companyTypeOfIndustry, companyWebsiteLink,
//                companyStrength, companyLocation, companyLicense, licenseBytes,
//                companyGstNumber, gstBytes, companyCinNumber, cinBytes);
//
//        // Find the JobAdmin using jobAdminId
//        JobAdmin jobAdmin = jobAdminService.findById(jobAdminId);
//        if (jobAdmin != null) {
//            company.setJobAdmin(jobAdmin);
//        } else {
//            return ResponseEntity.badRequest().body(null); // JobAdmin not found
//        }
//
//        Company createdCompany = companyService.createCompany(company);
//        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
//    }
//
//
//    @GetMapping
//    public ResponseEntity<List<Company>> getAllCompanies() {
//        List<Company> companies = companyService.getAllCompanies();
//        return ResponseEntity.ok(companies);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
//        Optional<Company> company = companyService.getCompanyById(id);
//        return company.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany) {
//        Company company = companyService.updateCompany(id, updatedCompany);
//        return ResponseEntity.ok(company);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
//        companyService.deleteCompany(id);
//        return ResponseEntity.noContent().build();
//    }
//}
