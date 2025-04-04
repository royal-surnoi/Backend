//package fusionIQ.AI.V2.fusionIq.service;
//
//import fusionIQ.AI.V2.fusionIq.data.Company;
//import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
//import fusionIQ.AI.V2.fusionIq.repository.CompanyRepository;
//import fusionIQ.AI.V2.fusionIq.repository.JobAdminRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class CompanyService {
//
//    private final CompanyRepository companyRepository;
//    private final JobAdminRepository jobAdminRepository;
//
//    public CompanyService(CompanyRepository companyRepository, JobAdminRepository jobAdminRepository) {
//        this.companyRepository = companyRepository;
//        this.jobAdminRepository = jobAdminRepository;
//    }
//
//    public Company createCompany(Company company, Long jobAdminId) {
//        if (jobAdminId != null) {
//            jobAdminRepository.findById(jobAdminId)
//                    .ifPresent(company::setJobAdmin);
//        }
//        return companyRepository.save(company);
//    }
//
//    public List<Company> getAllCompanies() {
//        return companyRepository.findAll();
//    }
//
//    public Optional<Company> getCompanyById(Long companyId) {
//        return companyRepository.findById(companyId);
//    }
//
//    public Company updateCompany(Long companyId, Company updatedCompany) {
//        return companyRepository.findById(companyId)
//                .map(company -> {
//                    // Update fields
//                    company.setCompanyLogo(updatedCompany.getCompanyLogo());
//                    company.setCompanyDescription(updatedCompany.getCompanyDescription());
//                    company.setCompanyTypeOfIndustry(updatedCompany.getCompanyTypeOfIndustry());
//                    company.setCompanyWebsiteLink(updatedCompany.getCompanyWebsiteLink());
//                    company.setCompanyStrength(updatedCompany.getCompanyStrength());
//                    company.setCompanyLocation(updatedCompany.getCompanyLocation());
//                    company.setCompanyLicense(updatedCompany.getCompanyLicense());
//                    company.setCompanyGstNumber(updatedCompany.getCompanyGstNumber());
//                    company.setCompanyCinNumber(updatedCompany.getCompanyCinNumber());
//                    return companyRepository.save(company);
//                })
//                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
//    }
//
//    public void deleteCompany(Long companyId) {
//
//        companyRepository.deleteById(companyId);
//    }
//    public Company createCompany(Company company) {
//        return companyRepository.save(company);
//    }
//
//}
