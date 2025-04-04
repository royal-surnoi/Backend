package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CommunityMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @ManyToMany
    @JoinTable(
            name = "Community_members",
            joinColumns = @JoinColumn(name = "Community_messages_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    @Lob
    @Column(name = "image", columnDefinition = "MEDIUMBLOB", nullable = false)
    private byte[] image;

    private String description;


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public CommunityMessages() {
    }

    public CommunityMessages(Long id, String name, User admin, Set<User> members, byte[] image, String description) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.members = members;
        this.image = image;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }


    @Override
    public String toString() {
        return "CommunityMessages{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", admin=" + admin +
                ", members=" + members +
                ", image=" + Arrays.toString(image) +
                ", description='" + description + '\'' +
                '}';
    }
}