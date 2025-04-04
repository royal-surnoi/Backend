package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class GroupMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_messages_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    //    here
    @ElementCollection
    @CollectionTable(name = "group_members", joinColumns = @JoinColumn(name = "group_messages_id"))
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "isAccepted", nullable = true)
    private Map<User, Boolean> memberAcceptanceStatus = new HashMap<>();


    public GroupMessages(){

    }

    public GroupMessages(Long id, String name, User admin, Set<User> members) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.members = members;
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


    public Map<User, Boolean> getMemberAcceptanceStatus() {
        return memberAcceptanceStatus;
    }

    public void setMemberAcceptanceStatus(Map<User, Boolean> memberAcceptanceStatus) {
        this.memberAcceptanceStatus = memberAcceptanceStatus;
    }

    @Override
    public String toString() {
        return "GroupMessages{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", admin=" + admin +
                ", members=" + members +
                '}';
    }
}
