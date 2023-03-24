package com.example.ebook;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.member.repository.MemberRepository;
import com.example.ebook.domain.post.entity.Post;
import com.example.ebook.domain.post.repository.PostRepository;
import com.example.ebook.factory.MemberFactory;
import com.example.ebook.factory.PostFactory;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("dev")
public class BulkInsertTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void postBulkInsert() {

        Member member = memberRepository.save(MemberFactory.createMember(1L));

        EasyRandom easyRandom = PostFactory.getEasyRandom(
                member,
                LocalDate.of(1900, 1, 1),
                LocalDate.of(2022, 2, 1));

        List<Post> posts = IntStream.range(0, 1000)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .toList();

        postRepository.saveAll(posts);
    }
}
