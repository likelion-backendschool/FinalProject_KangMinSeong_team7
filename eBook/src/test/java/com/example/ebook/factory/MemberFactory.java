package com.example.ebook.factory;

import com.example.ebook.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.inClass;
import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class MemberFactory {

    public static Member createMember(Long seed) {

        Predicate<Field> authLevelPredicate = named("authLevel")
                .and(ofType(Long.class))
                .and(inClass(Member.class));

        Predicate<Field> restCashPredicate = named("restCash")
                .and(ofType(Integer.class))
                .and(inClass(Member.class));

        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(authLevelPredicate, () -> 3L)
                .randomize(restCashPredicate, () -> 10000)
                .seed(seed);

        return new EasyRandom(parameters).nextObject(Member.class);
    }
}
