package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.service.DeviceService;
import fusionIQ.AI.V2.fusionIq.service.EmailSenderService;
import fusionIQ.AI.V2.fusionIq.service.TokenService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/add")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user) {
        User save = userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findUserById(id);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> userOpt = userService.findByEmail(email);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/enrollments/{userId}")
    public ResponseEntity<List<Enrollment>> getUserEnrollments(@PathVariable Long userId) {
        List<Enrollment> enrollments = userService.getUserEnrollments(userId);
        return ResponseEntity.ok(enrollments);
    }


    @PostMapping("/{followerId}/follow/{followingId}")
    public ResponseEntity<Void> followUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        userService.followUser(followerId, followingId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{followerId}/unfollow/{followingId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        userService.unfollowUser(followerId, followingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/enroll/{courseId}")
    public ResponseEntity<Void> enrollUserInCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        userService.enrollUserInCourse(userId, courseId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/unenroll/{courseId}")
    public ResponseEntity<Void> unenrollUserFromCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        userService.unenrollUserFromCourse(userId, courseId);
        return ResponseEntity.noContent().build();
    }


//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody User registrationData) {
//        if (userService.existsByEmail(registrationData.getEmail())) {
//            return new ResponseEntity<>("Email is already taken. Please use another email.", HttpStatus.CONFLICT);
//        }
//        User registeredUser = userService.registerUser(registrationData);
//        if (registeredUser != null) {
//            sendRegistrationSuccessEmail(registeredUser.getEmail(), registeredUser.getName());
//            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User registrationData) {
        try {
            if (userService.existsByEmail(registrationData.getEmail())) {
                return new ResponseEntity<>("Email is already taken. Please use another email.", HttpStatus.CONFLICT);
            }
            User registeredUser = userService.registerUser(registrationData);
            if (registeredUser != null) {
                sendRegistrationSuccessEmail(registeredUser.getEmail(), registeredUser.getName());
                return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void sendRegistrationSuccessEmail(String to, String name) {
        String subject = "Welcome to FusionIQ.ai!";
        String body = "Dear " + name + ",\n\n" +
                "Welcome to FusionIQ.ai! We are delighted to have you join our community. " +
                "Your account has been successfully created, and you are now part of an exciting journey with us.\n\n" +
                "As a registered member, you have access to a range of features and services designed to help you " +
                "achieve your goals. If you have any questions or need assistance, please don't hesitate to contact us. " +
                "We are here to support you every step of the way.\n\n" +
                "Thank you for choosing FusionIQ.ai. We look forward to working with you.\n\n" +
                "Best regards,\n" +
                "The FusionIQ.ai Team\n" +
                "FusionIQ.ai\n" +
                "support@fusioniq.ai\n" +
                "+123-456-7890\n" +
                "www.fusioniq.ai";

        emailSenderService.sendEmail(to, subject, body);
    }


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
//            return new ResponseEntity<>("Invalid login request", HttpStatus.BAD_REQUEST);
//        }
//        User authenticatedUser = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
//        if (authenticatedUser != null) {
//
//            if (userService.isUserLoggedIn(authenticatedUser.getId())) {
//                // Get the active token for the user
//                String activeToken = tokenService.getActiveTokenForUser(authenticatedUser.getId());
//                // Invalidate the previous token
//                tokenService.invalidateToken(activeToken);
//                // Log out the previous session
//                userService.logoutUser(authenticatedUser.getId());
//            }
//
//            String jwtToken = tokenService.generateToken(loginRequest.getEmail());
//            tokenService.setActiveTokenForUser(authenticatedUser.getId(), jwtToken);
//
//            JwtResponse response = new JwtResponse(jwtToken, authenticatedUser.getId(), authenticatedUser.getName(), authenticatedUser.getEmail());
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
//        }
//    }

    //    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, @RequestHeader("Device-Name") String deviceName) {
//        if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
//            return new ResponseEntity<>("Invalid login request", HttpStatus.BAD_REQUEST);
//        }
//        String loginResult = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
//        if ("INVALID_EMAIL".equals(loginResult)) {
//            return new ResponseEntity<>("Invalid email address", HttpStatus.UNAUTHORIZED);
//        } else if ("INVALID_PASSWORD".equals(loginResult)) {
//            return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
//        }
//        User authenticatedUser = userService.findUserByEmail(loginRequest.getEmail());
//        if (userService.isUserLoggedIn(authenticatedUser.getId())) {
//            String activeToken = tokenService.getActiveTokenForUser(authenticatedUser.getId());
//            tokenService.invalidateToken(activeToken);
//            userService.logoutUser(authenticatedUser.getId());
//        }
//        String jwtToken = tokenService.generateToken(loginRequest.getEmail());
//        tokenService.setActiveTokenForUser(authenticatedUser.getId(), jwtToken);
//        Device existingDevice = deviceService.findByToken(jwtToken);
//        if (existingDevice != null) {
//            existingDevice.setDeviceName(deviceName);
//            existingDevice.setLastActive(LocalDateTime.now());
//            deviceService.save(existingDevice);
//        } else {
//            Device newDevice = new Device();
//            newDevice.setDeviceName(deviceName);
//            newDevice.setToken(jwtToken);
//            newDevice.setLastActive(LocalDateTime.now());
//            newDevice.setUser(authenticatedUser);
//            deviceService.save(newDevice);
//        }
//        List<Device> devices = deviceService.findByUserId(authenticatedUser.getId());
//        JwtResponse response = new JwtResponse(jwtToken, authenticatedUser.getId(), authenticatedUser.getName(), authenticatedUser.getEmail(), devices);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, @RequestHeader("Device-Name") String deviceName) {
        if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return new ResponseEntity<>("Invalid login request", HttpStatus.BAD_REQUEST);
        }

        String loginResult = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if ("INVALID_EMAIL".equals(loginResult)) {
            return new ResponseEntity<>("Invalid email address", HttpStatus.UNAUTHORIZED);
        } else if ("INVALID_PASSWORD".equals(loginResult)) {
            userService.sendIncorrectPasswordEmail(loginRequest.getEmail());
            return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
        }

        User authenticatedUser = userService.findUserByEmail(loginRequest.getEmail());

        if (userService.isUserLoggedIn(authenticatedUser.getId())) {
            String activeToken = tokenService.getActiveTokenForUser(authenticatedUser.getId());
            tokenService.invalidateToken(activeToken);
            userService.logoutUser(authenticatedUser.getId());
        }

        String jwtToken = tokenService.generateToken(loginRequest.getEmail());
        tokenService.setActiveTokenForUser(authenticatedUser.getId(), jwtToken);

        Device existingDevice = deviceService.findByToken(jwtToken);
        if (existingDevice != null) {
            existingDevice.setDeviceName(deviceName);
            existingDevice.setLastActive(LocalDateTime.now());
            deviceService.save(existingDevice);
        } else {
            Device newDevice = new Device();
            newDevice.setDeviceName(deviceName);
            newDevice.setToken(jwtToken);
            newDevice.setLastActive(LocalDateTime.now());
            newDevice.setUser(authenticatedUser);
            deviceService.save(newDevice);
        }

        List<Device> devices = deviceService.findByUserId(authenticatedUser.getId());
        JwtResponse response = new JwtResponse(jwtToken, authenticatedUser.getId(), authenticatedUser.getName(), authenticatedUser.getEmail(), devices);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/saveNewUser")
    public ResponseEntity<User> addNewUsers(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String preferences,
            @RequestParam String language,
            @RequestParam String createdAt,
            @RequestParam String updatedAt,
            @RequestParam("userImage") MultipartFile userImage,
            @RequestParam String userDescription) {
        try {
            User savedUser = userService.addNewUser(name, email, password, preferences, language, createdAt, updatedAt, userImage, userDescription);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/{userId}/status")
    public ResponseEntity<Map<String, Object>> getUserStatus(@PathVariable Long userId) {
        User user = userService.getUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("onlineStatus", user.getOnlineStatus());
//        response.put("lastSeen", user.getLastSeen());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("updateUserById/{id}")
    public ResponseEntity<User> patchUserById(
            @PathVariable long id,
            @RequestBody Map<String, Object> updates) {
        User updatedUser = userService.patchUserById(id, updates);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadUserImage(@PathVariable long id, @RequestParam("image") MultipartFile image) {
        try {
            userService.saveUserImage(id, image);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
        }
    }


//    @PostMapping("/logout")
//    public ResponseEntity<Map<String, Object>> logout(@RequestParam Long userId) {
//        Map<String, Object> response = userService.updateUserStatusAndGetLastSeen(userId, User.OnlineStatus.OFFLINE);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam Long userId) {
        String activeToken = tokenService.getActiveTokenForUser(userId);
        tokenService.invalidateToken(activeToken);
        userService.logoutUser(userId);
        tokenService.removeActiveTokenForUser(userId);
        return ResponseEntity.ok("User logged out successfully");
    }

    @PostMapping("/{userId}/registerMentor")
    public ResponseEntity<?> registerMentor(@PathVariable Long userId, @RequestBody Mentor mentor) {
        try {
            if (userService.existsByEmail(mentor.getUsername())) {
                return new ResponseEntity<>("Email is already taken. Please use another email.", HttpStatus.CONFLICT);
            }

            if (userService.existsByUsername(mentor.getUsername())) {
                return new ResponseEntity<>("Username is already taken. Please use another username.", HttpStatus.CONFLICT);
            }

            Mentor registeredMentor = userService.registerMentor(userId, mentor);
            return ResponseEntity.ok(registeredMentor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mentors")
    public ResponseEntity<List<Mentor>> getAllMentors() {
        List<Mentor> mentors = userService.getAllMentors();
        return ResponseEntity.ok(mentors);
    }

    @PostMapping("/MentorLogin")
    public ResponseEntity<?> loginMentor(@RequestParam String username, @RequestParam String password) {
        try {
            Mentor mentor = userService.loginMentor(username, password);
            String jwtToken = tokenService.generateToken(mentor.getUsername());
            MentorResponse jwtResponse = new MentorResponse(jwtToken, mentor.getMentorId(), mentor.getUsername());
            return ResponseEntity.ok(jwtResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/online")
    public ResponseEntity<?> setUserOnline(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        userService.updateUserStatusAndGetLastSeen(userId, User.OnlineStatus.ONLINE);
        return new ResponseEntity<>("User is now online", HttpStatus.OK);
    }

    @PostMapping("/offline")
    public ResponseEntity<?> setUserOffline(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        userService.updateUserStatusAndGetLastSeen(userId, User.OnlineStatus.OFFLINE);
        return new ResponseEntity<>("User is now offline", HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/is-mentor")
    public ResponseEntity<Boolean> isUserMentor(@PathVariable Long userId) {
        boolean isMentor = userService.isUserMentor(userId);
        return ResponseEntity.ok(isMentor);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestParam String email, @RequestParam String currentPassword, @RequestParam String newPassword) {
        boolean updated = userService.updatePassword(email, currentPassword, newPassword);
        if (updated) {
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Incorrect current password or user not found", HttpStatus.NOT_FOUND);
        }
    }
//        @PatchMapping("/{id}/name")
//        public ResponseEntity<String> updateUserName(@PathVariable Long id, @RequestParam String name) {
//            userService.updateUserName(id, name);
//            return ResponseEntity.ok("User name updated successfully");
//        }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Map<String, String>> updateUserName(@PathVariable Long id, @RequestParam String name) {
        userService.updateUserName(id, name);
        Map<String, String> response = new HashMap<>();
        response.put("name", name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/MentorFollowed/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getMentorsFollowedByUser(@PathVariable Long userId) {
        List<Map<String, Object>> mentors = userService.getMentorsFollowedByUser(userId);
        return ResponseEntity.ok(mentors);
    }
    @PostMapping("/generate-otp-change-password")
    public ResponseEntity<?> generateAndSendOTP(@RequestParam String email) {
        try {
            User user = userService.generateAndSendOTP(email);
            return ResponseEntity.ok().body("OTP sent successfully to " + email);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating OTP: " + e.getMessage());
        }
    }

    @PutMapping("/change-password-with-otp")
    public ResponseEntity<?> changePassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        try {
            userService.changePasswordWithOTP(email, otp, newPassword);
            return ResponseEntity.ok().body("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error changing password: " + e.getMessage());
        }
    }

    @PostMapping("/mentor/forgot-username")
    public ResponseEntity<?> forgotMentorUsername(@RequestParam String email) {
        try {
            String result = userService.handleForgotMentorUsername(email);
            return ResponseEntity.ok().body(Collections.singletonMap("message", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/mentor/generate-otp-for-forgot-password")
    public ResponseEntity<String> mentorForgotPassword(@RequestParam String email) {
        String response = userService.processMentorForgotPassword(email);
        if (response.equals("OTP sent to your email")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/mentor/change-password")
    public ResponseEntity<String> changeMentorPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {

        String response = userService.changeMentorPassword(email, otp, newPassword);

        if (response.equals("Password changed successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/mentorsIds")
    public ResponseEntity<List<Long>> getAllMentorsIds() {
        List<Long> mentors = userService.getAllMentorsids();
        return ResponseEntity.ok(mentors);
    }
}
