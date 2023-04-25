package com.ll.gramgram.base.initData;

import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            InstaMemberService instaMemberService,
            LikeablePersonService likeablePersonService
    ) {
        return args -> {
            Member memberAdmin = memberService.join("admin", "1234", "admin@naver.com").getData();
            Member memberUser1 = memberService.join("user1", "1234", "test@naver.com").getData();
            Member memberUser2 = memberService.join("user2", "1234", "test1@naver.com").getData();
            Member memberUser3 = memberService.join("user3", "1234", "test2@naver.com").getData();
            Member memberUser4 = memberService.join("user4", "1234", "test3@naver.com").getData();

            //Member memberUser5ByKakao = memberService.whenSocialLogin("KAKAO", "KAKAO__2733176564").getData();
            //Member memberUser5ByGoogle = memberService.whenSocialLogin("Google", "GOOGLE__107628735217902602702").getData();

            instaMemberService.connect(memberUser2, "insta_user2", "M");
            instaMemberService.connect(memberUser3, "insta_user3", "W");
            instaMemberService.connect(memberUser4, "insta_user4", "M");

            likeablePersonService.like(memberUser3, "insta_user4", 1);
            likeablePersonService.like(memberUser3, "insta_user100", 2);

            // Only Test.
            // LikeablePersonControllerTests / t008
            likeablePersonService.like(memberUser2, "insta_test0", 2);
            likeablePersonService.like(memberUser2, "insta_test1", 2);
            likeablePersonService.like(memberUser2, "insta_test2", 2);
            likeablePersonService.like(memberUser2, "insta_test3", 2);
            likeablePersonService.like(memberUser2, "insta_test4", 2);
            likeablePersonService.like(memberUser2, "insta_test5", 2);
            likeablePersonService.like(memberUser2, "insta_test6", 2);
            likeablePersonService.like(memberUser2, "insta_test7", 2);
            likeablePersonService.like(memberUser2, "insta_test8", 2);
            likeablePersonService.like(memberUser2, "insta_test9", 2);
        };
    }
}
