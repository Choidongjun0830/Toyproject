package toyproject.wintersnack.controller.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.wintersnack.controller.SessionConst;
import toyproject.wintersnack.domain.member.Member;
import toyproject.wintersnack.domain.board.Post;
import toyproject.wintersnack.domain.board.PostRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final PostRepository postRepository;

    @GetMapping
    public String boardList(@ModelAttribute Post post, Model model) {

        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList", postList);

        return "board/boardList";
    }

    @GetMapping("/write")
    public String boardWriteForm(@ModelAttribute Post post) {
        return "board/writeForm";
    }

    @PostMapping("/write")
    public String boardWrite(@Valid @ModelAttribute Post post, HttpServletRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            return "board/write";
        }

        HttpSession session = request.getSession();
        if(session == null){
            return "login/loginForm";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        String loginMemberName = loginMember.getName();

        post.setWriter(loginMemberName);
        post.setPostTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")));

        postRepository.save(post);

//      redirectAttributes.addAttribute("postId", post.getId());
//      redirectAttributes.addAttribute("status", true);

        return "redirect:/board"; //리다이렉트를 하지 않으면 글쓴 것이 바로 보이지 않음
    }

    @GetMapping("/{boardId}")
    public String postView(@PathVariable Long boardId, Model model){
        Post post = postRepository.findById(boardId);
        model.addAttribute("post", post);
        return "board/post-view";
    }

    @GetMapping("/notice")
    public String noticeList(@ModelAttribute Post post) {
        return "board/notice";
    }

    @GetMapping("/notice/write")
    public String noticeWriteForm(@ModelAttribute Post post) {
        return "board/noticeWriteForm";
    }

    @PostMapping("/notice/write")
    public String noticeWrite(@ModelAttribute Post post, HttpServletRequest request,BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "redirect:/board";
        }
        //관리자만 접근 가능하게
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        String loginMemberName = "Admin " + loginMember.getName();

        post.setHeader("공지");
        post.setWriter(loginMemberName);
        post.setPostTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")));

        postRepository.save(post);

        return "board/notice";
    }



}
