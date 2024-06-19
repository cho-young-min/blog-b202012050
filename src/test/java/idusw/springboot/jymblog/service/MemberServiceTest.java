package idusw.springboot.jymblog.service;

import idusw.springboot.jymblog.model.MemberDto;
import idusw.springboot.jymblog.serivce.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberServiceTest {
    // field DI : test, constructor DI : development, deployment code
    @Autowired
    MemberService memberService;
    @Test
    public void initMembers() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            MemberDto dto = MemberDto.builder()
                    .id("id-" + i)
                    .pw("cometrue")
                    .name("name-" + i)
                    .email("id-" + i + "@induk.ac.kr")
                    .build();

            memberService.create(dto);
        });
    }
    @Test
    public void readById() {
        MemberDto dto = MemberDto.builder()
                .idx(6L)
                .build();
        MemberDto ret;
        //memberService.readById(dto.getIdx()) ;-> memberService.readByIdx() 변경
        if((ret = memberService.readByIdx(dto.getIdx())) != null)
            System.out.println("success" + ret );
        else
            System.out.println("fail");
    }
    @Test
    public void readList() {
        List<MemberDto> list = memberService.readAll();
        if(list != null) {
            System.out.println("success");
            for(MemberDto dto : list) {
                System.out.println(dto);
            }
        }
        else
            System.out.println("fail");
    }
    @Test
    public void registerOne() {
        MemberDto dto = MemberDto.builder()
                    .id("admin2")
                    .pw("cometrue")
                    .name("administrator")
                    .email("admin" + "@induk.ac.kr")
                    .build();

        if(memberService.create(dto) > 0)
            System.out.println("success");
        else
            System.out.println("fail");
    }

    @Test
    public void deleteMember() {
        Long idxToDelete = 14L; // 삭제할 회원의 idx를 설정합니다.
        MemberDto memberToDelete = MemberDto.builder()
                .idx(idxToDelete)
                .build();

        int result = memberService.delete(memberToDelete);
        if (result > 0) {
            // 삭제 후 해당 회원이 존재하지 않는지 확인합니다.
            MemberDto deletedMember = memberService.readByIdx(idxToDelete);
            if (deletedMember == null) {
                System.out.println("success");
            } else {
                System.out.println("fail - member still exists");
            }
        } else {
            System.out.println("fail");
        }
    }

    @Test
    public void update() {
        Long idxToUpdate = 1L; // 업데이트할 회원의 idx를 설정합니다.
        MemberDto memberToUpdate = MemberDto.builder()
                .idx(idxToUpdate)
                .id("bbb")
                .pw("cometrueb")
                .name("bbb")
                .email("bbb@induk.ac.kr")
                .build();

        int result = memberService.update(memberToUpdate);
        if (result > 0) {
            // 업데이트 후 해당 회원의 정보가 바뀌었는지 확인합니다.
            MemberDto updatedMember = memberService.readByIdx(idxToUpdate);
            if (updatedMember != null &&
                    updatedMember.getId().equals("updatedId") &&
                    updatedMember.getPw().equals("updatedPassword") &&
                    updatedMember.getName().equals("updatedName") &&
                    updatedMember.getEmail().equals("updatedEmail@induk.ac.kr")) {
                System.out.println("success");
            } else {
                System.out.println("fail - member details did not update correctly");
            }
        } else {
            System.out.println("fail");
        }
    }

}
