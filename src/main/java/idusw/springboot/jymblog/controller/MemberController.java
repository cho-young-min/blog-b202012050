package idusw.springboot.jymblog.controller;

import idusw.springboot.jymblog.model.MemberDto;
import idusw.springboot.jymblog.serivce.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("members/")
public class MemberController {

    final MemberService memberService;
    public MemberController(MemberService memberService) { // 생성자 주입
        this.memberService = memberService;
    }

    @GetMapping("logout")
    public String getLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("{idx}")
    public String getMemberById(@PathVariable("idx") Long idx, Model model) {
        MemberDto dto = memberService.readByIdx(idx);
        model.addAttribute("dto", dto);
        return "./members/profile";
    }
    @GetMapping("")
    public String getMembers(Model model) {
        List<MemberDto> dtoList = memberService.readAll();
        model.addAttribute("dtoList", dtoList);
        return "./members/list";
    }
    // get방식으로 members/login을 요청하면 main/login.html로 이동
    @GetMapping("login")
    public String getLogin(Model model) {
        model.addAttribute("memberDto", MemberDto.builder().build());
        return "./main/login";
    }
    // main/login.html 에서 폼을 통해 post 요청, 처리후 main/index.html로 이동
    @PostMapping("login")
    public String postLogin(@ModelAttribute("memberDto") MemberDto memberDto, Model model, HttpSession session) {
        String id = memberDto.getId();
        String pw = memberDto.getPw();

        MemberDto m = MemberDto.builder()
                .id(id)
                .pw(pw)
                .build();
        String msg = "";

        // Database로 부터 정보를 가져올 예정임
        // 사용자가 제공한 정보와 DB로 부터 가져온 정보를 처리
        // 동작전 로그
        MemberDto ret = memberService.loginById(m);
        // 동작 후 로그
        if(ret != null) {
            session.setAttribute("id", id);
            session.setAttribute("idx", ret.getIdx());
            msg = "로그인 성공";
        } else {
            msg = "로그인 실패";
        }
        model.addAttribute("message", msg );
        return "./errors/error-message";
    }

    @GetMapping("register")
    public String getRegister(Model model) {
        model.addAttribute("memberDto", MemberDto.builder().build());
        return "./main/register";
    }

    @PostMapping("register")
    public String postRegister(@ModelAttribute("memberDto") MemberDto memberDto, Model model) {
        System.out.println(memberDto);
        // 등록 처리 -> service -> repository -> service -> controller
        if(memberService.create(memberDto) > 0 ) // 정상적으로 레코드의 변화가 발생하는 경우 영향받는 레코드 수를 반환
            return "redirect:/";
        else
            return "./errors/error-message";
    }



//    @GetMapping("delete")
//    public String confirmDeleteMember(@ModelAttribute("memberDto") Long idx, Model model) {
//        MemberDto dto = memberService.readByIdx(idx);
//        model.addAttribute("dto", dto);
//        return "./main/delete";
//    }

    @PostMapping("delete")
    public String deleteMemberById(@ModelAttribute("memberDto") MemberDto memberDto,HttpSession session, Model model) {
        Long idx = memberDto.getIdx();
        String msg;
        if (idx != null && memberService.delete(memberDto) > 0) {
            session.invalidate();
            return "redirect:/";
        } else {
            msg = "탈퇴 실패: 유효하지 않은 회원 정보입니다.";
        }
        model.addAttribute("message", msg);
        return "./errors/error-message";
    }

    @GetMapping("update/{idx}")
    public String getUpdateForm(@PathVariable("idx") Long idx, Model model) {
        MemberDto dto = memberService.readByIdx(idx);
        model.addAttribute("memberDto", dto);
        return "./members/update";
    }

    // post 방식으로 members/update를 요청하면 해당 회원 정보를 수정하고, 수정 후 main/index.html로 이동
    @PostMapping("update")
    public String postUpdate(@ModelAttribute("memberDto") MemberDto memberDto, HttpSession session, Model model) {
        // 이전에 로그인한 사용자의 정보를 가져옵니다.
        Long idx = (Long) session.getAttribute("idx");
        // 수정된 정보를 새로운 DTO에 설정합니다.
        MemberDto updatedDto = MemberDto.builder()
                .idx(idx) // 해당 사용자의 고유 식별자
                .name(memberDto.getName()) // 새로운 아이디
                .email(memberDto.getEmail())
                .build();
        // 회원 정보를 업데이트합니다.
        if (memberService.update(updatedDto) > 0)
            return "redirect:/"; // 수정 성공 시 메인 페이지로 이동
        else
            return "./errors/error-message"; // 수정 실패 시 에러 페이지로 이동
    }
}
