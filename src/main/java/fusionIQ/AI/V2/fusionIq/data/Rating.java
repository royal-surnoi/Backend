package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;


@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private int ratingValue;
    private int stars;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rating(Long id, int ratingValue, int stars, Course course, User user) {
        this.id = id;
        this.ratingValue = ratingValue;
        this.stars = stars;
        this.course = course;
        this.user = user;
    }

    public Rating() {
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", ratingValue=" + ratingValue +
                ", stars=" + stars +
                ", course=" + course +
                ", user=" + user +
                '}';
    }
}
