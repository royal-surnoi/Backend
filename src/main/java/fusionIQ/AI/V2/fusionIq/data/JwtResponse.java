package fusionIQ.AI.V2.fusionIq.data;

import java.util.List;

public class JwtResponse {
        private String token;
        private long id;
        private String name;
        private String email;
        private List<Device> devices;



    public JwtResponse(String token, long id, String name, String email, List<Device> devices) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.devices = devices;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

