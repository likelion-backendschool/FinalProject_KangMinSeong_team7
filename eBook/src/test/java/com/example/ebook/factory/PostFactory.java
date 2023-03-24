package com.example.ebook.factory;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.post.entity.Post;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.inClass;
import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class PostFactory {

    static public EasyRandom getEasyRandom(Member member, LocalDate startDate, LocalDate lastDate) {

        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        Predicate<Field> memberPredicate = named("member")
                .and(ofType(Member.class))
                .and(inClass(Post.class));

        EasyRandomParameters parameters = new EasyRandomParameters()
                .excludeField(idPredicate)
                .dateRange(startDate, lastDate)
                .randomize(memberPredicate, () -> member);

        return new EasyRandom(parameters);
    }
}
