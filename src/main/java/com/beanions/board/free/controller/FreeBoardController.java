package com.beanions.board.free.controller;

import com.beanions.auth.model.AuthDetails;
import com.beanions.board.common.dto.CommentAndMemberDTO;
import com.beanions.board.common.dto.PostAndMemberDTO;
import com.beanions.board.free.service.FreeBoardService;
import com.beanions.common.dto.CommentsDTO;
import com.beanions.common.dto.PostDTO;
import com.beanions.common.dto.SearchDTO;
import com.beanions.common.paging.PagingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/board")
public class FreeBoardController {


    private final FreeBoardService freeBoardService;

    public FreeBoardController(FreeBoardService freeBoardService){

        this.freeBoardService = freeBoardService;

    }

    @GetMapping("/yesin")
    public String yesinList(@RequestParam("id") String id , Model model, @ModelAttribute("params") final SearchDTO params) {

        PagingResponse<PostAndMemberDTO> PostAndMemberDTOList = freeBoardService.freeList(id, params);

        model.addAttribute("name", id);

        model.addAttribute("PostAndMemberDTOList", PostAndMemberDTOList);

        return "/user/board/freeList";
    }

    @GetMapping("/yerang")
    public String yerangList(@RequestParam("id") String id , Model model, @ModelAttribute("params") final SearchDTO params) {

        PagingResponse<PostAndMemberDTO> PostAndMemberDTOList = freeBoardService.freeList(id, params);

        model.addAttribute("name", id);

        model.addAttribute("PostAndMemberDTOList", PostAndMemberDTOList);

        return "/user/board/freeList";
    }

    @GetMapping("/freedetail")
    public String freeDetail(@RequestParam("id") String code, Model model){

        System.out.println("code : " + code);
        PostAndMemberDTO postAndMemberDTO = freeBoardService.freeDetail(code);
        System.out.println(postAndMemberDTO);
//        PostAndCommentDTO postAndCommentDTO = freeBoardService.selectAllComments(code);
//        System.out.println(postAndCommentDTO);
        String before = postAndMemberDTO.getPostContext();
        String text = before.replace("\n", "<br>");

        System.out.println("before text = " + before);
        System.out.println("after text = " + text);

        postAndMemberDTO.setPostContext(text);

        System.out.println("postAndMemberDTO = " + postAndMemberDTO);
        model.addAttribute("PostAndMemberDTO", postAndMemberDTO);

        return "/user/board/freeDetail";
    }

    @GetMapping(value = "/comments", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Map<String, Object> selectAllComments(@RequestParam int code,int align, Authentication authentication){

        Map<String, Object> response = new HashMap<>();

        if(align == 1) {
            List<CommentAndMemberDTO> comments = freeBoardService.selectAllCommentsDesc(code);
            freeBoardService.selectAllCommentsDesc(code).forEach(System.out::println);
            response.put("comments", comments);
        } else {
            List<CommentAndMemberDTO> comments = freeBoardService.selectAllCommentsAsc(code);
            freeBoardService.selectAllCommentsAsc(code).forEach(System.out::println);
            response.put("comments", comments);
        }


        if( authentication != null ) {
            AuthDetails loginUser = (AuthDetails) authentication.getPrincipal();
            String nickname = loginUser.getLoginUserDTO().getNickname();
            response.put("curNickname", nickname);
        }
        return response;
    }

    @PostMapping(value = "/comment-regist")
    @ResponseBody
    public String registComment(@RequestBody CommentsDTO comment,Authentication authentication) throws JsonProcessingException {
        AuthDetails loginUser = (AuthDetails) authentication.getPrincipal();
        comment.setMemberCode(loginUser.getLoginUserDTO().getMemberCode());
        System.out.println(comment);

        int result = freeBoardService.registComment(comment);
        System.out.println(result);

        return new ObjectMapper().writeValueAsString(result);
    }

    @PostMapping(value = "/comment-modify")
    @ResponseBody
    public String modifyComment(@RequestBody CommentsDTO comment) throws JsonProcessingException {

        System.out.println(comment);

        int result = freeBoardService.modifyComment(comment);
        System.out.println(result);

        return new ObjectMapper().writeValueAsString(result);
    }

    @PostMapping(value = "/comment-delete")
    @ResponseBody
    public String deleteComment(@RequestBody int commentCode) throws JsonProcessingException {

        int result = freeBoardService.deleteComment(commentCode);
        System.out.println(result);

        return new ObjectMapper().writeValueAsString(result);
    }

//    @GetMapping("/freeregist")
//    public String postRegistPage(HttpSession session, RedirectAttributes rttr, Model model) {
//
//        Integer memberCode = (Integer) session.getAttribute("memberCode");
//
//        model.addAttribute("memberCode", memberCode);
//
//        if (memberCode != null) {
//            // 회원 아이디를 글쓰기 페이지로 리다이렉트하면서 전달
//            return "/user/board/freeRegist";
//        } else {
//            // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
//            rttr.addFlashAttribute("errorMessage", "로그인 후에 게시글을 작성할 수 있습니다.");
//            return "redirect:/login";
//        }
//    }

//    @PostMapping("/freeregist")
//    public String postRegist(PostDTO postDTO, RedirectAttributes rttr){
//
//        freeBoardService.freeRegist(postDTO);
//
//        rttr.addAttribute("id", postDTO.getSubCategory());
//        rttr.addFlashAttribute("successMessage", "게시글을 등록하였습니다.");
//
//        if (postDTO.getSubCategory().equals("예신")){
//            return "redirect:/board/yesin";
//
//        } else if (postDTO.getSubCategory().equals("예랑")){
//            return "redirect:/board/yerang";
//
//        } else { return "redirect:/board/review"; }
//
//
//    }

    @GetMapping("/yesinmodify")
    public String yesinModify(@RequestParam("id") String id, Model model){

        PostAndMemberDTO modify = freeBoardService.freeDetail(id);

        model.addAttribute("modify", modify);

        return "user/board/freeModify";
    }

    @GetMapping("/yerangmodify")
    public String yerangModify(@RequestParam("id") String id, Model model){

        PostAndMemberDTO modify = freeBoardService.freeDetail(id);

        model.addAttribute("modify", modify);

        return "user/board/freeModify";
    }

    @PostMapping("/freeModify")
    public String freeModify(PostDTO postDTO, RedirectAttributes rttr){

        freeBoardService.freeModify(postDTO);

        rttr.addAttribute("id", postDTO.getSubCategory());

        rttr.addFlashAttribute("successMessage", "수정 성공");
        // 예랑 게시판 생성 하면 주소 변경하기
        if (postDTO.getSubCategory().equals("예신")){
            return "redirect:/board/yesin";
        } else {
            return "redirect:/board/yerang";
        }
    }

    @PostMapping("/freedelete")
    public String deletePost(RedirectAttributes rttr, PostDTO postDTO){

        freeBoardService.deletePost(postDTO);

        rttr.addAttribute("id", postDTO.getSubCategory());
        rttr.addFlashAttribute("successMessage", "게시글 삭제 성공");

        // 예랑 게시판 생성 하면 주소 변경하기
        if (postDTO.getSubCategory().equals("예신")){
            return "redirect:/board/yesin";
        } else {
            return "redirect:/board/yerang";}
    }

//    //240517 by NJH - 댓글 작업용 도메인
//    @GetMapping("/freedetail")
//    public String freeDetailComment(@RequestParam("id") String id, Model model){
//
//        PostAndMemberDTO PostAndMemberDTO = freeBoardService.freeDetail(id);
//
//        model.addAttribute("PostAndMemberDTO", PostAndMemberDTO);
//
//        return "/user/board/freeDetail";
//    }
}

