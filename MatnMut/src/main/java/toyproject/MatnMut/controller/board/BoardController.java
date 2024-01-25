package toyproject.MatnMut.controller.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.MatnMut.controller.SessionConst;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.domain.board.Post;
import toyproject.MatnMut.domain.board.PostRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boardList")
public class BoardController {

    private final PostRepository postRepository;

    @GetMapping("/write")
    public String boardWriteForm(@ModelAttribute Post post, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "/login/loginForm";
        }

        return "board/writeForm";
    }

    @PostMapping("/write")
    public String boardWrite(@Valid @ModelAttribute Post post, HttpServletRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            return "board/write";
        }

        Member loginMember = (Member) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        String loginMemberName = loginMember.getName();
        if(loginMemberName == null){
            return "login/loginForm";
        }

        post.setWriter(loginMemberName);
        post.setPostTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")));

        postRepository.save(post);

        redirectAttributes.addAttribute("postId", post.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/board/{postId}"; //리다이렉트를 하지 않으면 글쓴 것이 바로 보이지 않음
    }

    @GetMapping("/{boardId}")
    public String postView(@PathVariable Long boardId, Model model){
        Post post = postRepository.findById(boardId);
        model.addAttribute("post", post);
        return "board/postView";
    }

    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable Long postId, Model model) {
        Post post = postRepository.findById(postId);
        model.addAttribute("post", post);

        return "board/editPostForm";
    }

    @PostMapping("/{postId}/edit")
    public String edit(@PathVariable Long postId, @ModelAttribute Post post) {
        postRepository.update(postId, post);
        return "redirect:/board/{postId}";
    }

    @GetMapping("/notice")
    public String noticeBoard(@ModelAttribute Post post, Model model) {
        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList", postList);

        return "board/noticeBoard";
    }

    @GetMapping("/shoppingInfo")
    public String shoppingInfoBoard(@ModelAttribute Post post, Model model) {
        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList", postList);

        return "board/shoppingInfoBoard";
    }

    @GetMapping("/mat")
    public String matBoard(@ModelAttribute Post post, Model model) {
        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList", postList);

        return "board/matBoard";
    }

    @GetMapping("/mut")
    public String mutBoard(@ModelAttribute Post post, Model model) {
        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList", postList);

        return "board/mutBoard";
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
        //관리자만 접근 가능하게 해야함
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
