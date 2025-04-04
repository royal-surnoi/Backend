package fusionIQ.AI.V2.fusionIq.data;


public class DashboardOverview {
    private int totalCourses;
    private int activeStudents;
    private int upcomingClasses;
    private int pendingAssignments;


    public DashboardOverview(int totalCourses, int activeStudents, int upcomingClasses, int pendingAssignments) {
        this.totalCourses = totalCourses;
        this.activeStudents = activeStudents;
        this.upcomingClasses = upcomingClasses;
        this.pendingAssignments = pendingAssignments;
    }


    public int getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(int totalCourses) {
        this.totalCourses = totalCourses;
    }

    public int getActiveStudents() {
        return activeStudents;
    }

    public void setActiveStudents(int activeStudents) {
        this.activeStudents = activeStudents;
    }

    public int getUpcomingClasses() {
        return upcomingClasses;
    }

    public void setUpcomingClasses(int upcomingClasses) {
        this.upcomingClasses = upcomingClasses;
    }

    public int getPendingAssignments() {
        return pendingAssignments;
    }

    public void setPendingAssignments(int pendingAssignments) {
        this.pendingAssignments = pendingAssignments;
    }
}

