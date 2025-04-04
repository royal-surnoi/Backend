package fusionIQ.AI.V2.fusionIq.data;

public class PostEvent {
    private Long userId;
    private Long postId;

    public PostEvent(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPostId() {
        return postId;
    }
}
