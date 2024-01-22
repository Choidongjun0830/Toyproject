package toyproject.wintersnack.domain.board;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Repository
public class PostRepository {

    private Map<Long, Post> store = new HashMap<>();
    private static Long sequence = 0L;

    public Post save(Post post) {
        post.setId(++sequence);
        post.setViews(0L);

        log.info("save post = {}", post);
        store.put(post.getId(), post);
        return post;
    }

    public Post findById(Long id) { return store.get(id); }
    public List<Post> findAll() { return new ArrayList<>(store.values()); }
}
