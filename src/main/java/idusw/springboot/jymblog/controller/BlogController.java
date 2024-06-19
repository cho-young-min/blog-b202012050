package idusw.springboot.jymblog.controller;

import idusw.springboot.jymblog.model.BlogDto;
import idusw.springboot.jymblog.serivce.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("blogs/")
public class    BlogController {
    final BlogService blogService;
    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }
//
    @PostMapping("/delete/{idx}")
    public String delete(@PathVariable Long idx, Model model){
        // controller가 service에게 요청
        BlogDto dto = BlogDto.builder()
                .idx(idx)
                .build();
        blogService.delete(dto); // repository에게 요청하는 코드가 있을 것임
        //controller가 view에게 처리결과를 전달
        model.addAttribute("attr-name", "attr-value"); //delete때는 크게 필요 없음
        //등록이나 수정 할 때 필요
        return "redirect:/";
    }
}
