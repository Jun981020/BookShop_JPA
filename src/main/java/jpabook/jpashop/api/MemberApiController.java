package jpabook.jpashop.api;

import jpabook.jpashop.api.dto.RequestMemberDto;
import jpabook.jpashop.api.dto.ResponseMemberDto;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    @PostMapping("api/v1/members")
    public ResponseMemberDto createMember(@RequestBody RequestMemberDto dto){
        Member member = new Member();
        member.setName(dto.getName());
        Long joinMember = memberService.join(member);
        return new ResponseMemberDto(joinMember);
    }
}
