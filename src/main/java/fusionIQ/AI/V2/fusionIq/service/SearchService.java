package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.Search;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.SearchRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SearchService {

    @Autowired
    private SearchRepo searchRepo;

    @Autowired
    private UserRepo userRepo;

    public Search addSearch(long userId, Search search) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            search.setUser(userOpt.get());
            return searchRepo.save(search);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public Optional<Search> findSearchById(Long id) {
        return searchRepo.findById(id);
    }

    public Optional<Search>getSearchById(Long id){
        return searchRepo.findById(id);
    }


    public Search savingSearch(Search search){
        return searchRepo.save(search);
    }

    public void deleteSearch(Long id) {
        searchRepo.deleteById(id);
    }

    public List<Search> findAllSearches() {
        return searchRepo.findAll();
    }




    public Search storeSearchContent(Long userId, String searchContent) {
        Search search = new Search();
        search.setUser(userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        search.setSearchContent(searchContent);
        return searchRepo.save(search);
    }

    public Search storeCourseContent(Long userId, String courseContent) {
        Search search = new Search();
        search.setUser(userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        search.setCourseContent(courseContent);
        return searchRepo.save(search);
    }

    public List<Search> getSearchContentByUserId(Long userId) {
        return searchRepo.findByUserIdAndSearchContentIsNotNull(userId);
    }

    public List<Search> getCourseContentByUserId(Long userId) {
        return searchRepo.findByUserIdAndCourseContentIsNotNull(userId);
    }

    public List<Search> getSearchContent() {
        return searchRepo.findBySearchContentIsNotNull();
    }

    public List<Search> getCourseContent() {
        return searchRepo.findByCourseContentIsNotNull();
    }

    public List<String> getLast10SearchContentsByUserId(Long userId) {
        return searchRepo.findTop10SearchContentByUserId(userId);
    }

    public List<String> getLast10CourseContentByUser(Long userId) {
        return searchRepo.findTop10CourseContentByUserIdOrderBySearchedAtDesc(userId);
    }

    public List<String> getLast10JobContentByUser(Long userId) {
        return searchRepo.findTop10JobContentByUserIdOrderBySearchedAtDesc(userId);
    }
}
