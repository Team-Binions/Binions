package com.beanions.admin.controller;

import com.beanions.common.dto.PostDTO;
import com.beanions.admin.service.AdminService;
import com.beanions.admin.dto.AdminPostDTO;
import com.beanions.common.dto.SearchDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminBoardController {

    private final AdminService adminService;

    public AdminBoardController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/post")
    public String selectAllPost(@ModelAttribute("params") final SearchDTO params, Model model) {

        String Keyword = params.getKeyword();
        System.out.println("keyword = " + params.getKeyword());

        List<AdminPostDTO> postList = adminService.selectAllPost(params);

        int count = adminService.count(params);
        System.out.println("count = " + count);

        for (AdminPostDTO post : postList) {
            System.out.println("post = " + post);
        }


        model.addAttribute("count", count);
        model.addAttribute("keyword", Keyword);
        model.addAttribute("postList", postList);

        return "/admin/post/list";
    }

    @GetMapping("/post/detail")
    public String postDetail(@RequestParam("id") String id, Model model) {

        System.out.println("id = " + id);

        int code = Integer.parseInt(id);

        AdminPostDTO postDetail = adminService.selectPost(code);

        String text = postDetail.getPostContext().replace("\n", "<br>");

        System.out.println("text = " + text);

        postDetail.setPostContext(text);

        System.out.println("postDetail = " + postDetail);

        model.addAttribute("postDetail", postDetail);

        return "/admin/post/detail";
    }

//    @GetMapping("/post/update")
//    public String postUpdate(@RequestParam("id") String id, Model model) {
//
//        System.out.println("id = " + id);
//
//        int code = Integer.parseInt(id);
//
//        List<AdminPostDTO> postDetail = adminService.selectPost(code);
//
//        System.out.println("postDetail = " + postDetail);
//
//        model.addAttribute("postDetail", postDetail);
//
//        return "/admin/post/update";
//    }

    /* vef_status */
    @PostMapping("/post/update")
    public String postUpdate(PostDTO post, RedirectAttributes rttr, HttpServletRequest request) {

        System.out.println("post = " + post);

        adminService.postUpdate(post);


        rttr.addFlashAttribute("successMessage", "게시글 상태 변경 성공!");

        if (request.getHeader("Referer") != null) {

            return "redirect:" + request.getHeader("Referer");
        } else {

            return "redirect:/admin/post";
        }
    }

    /* review_status */
    @PostMapping("/post/review")
    public String postReview(PostDTO post, RedirectAttributes rttr,  HttpServletRequest request) {

        System.out.println("post = " + post);

        adminService.postReview(post);

        rttr.addFlashAttribute("successMessage", "게시물 상태 변경 성공!");

//        return "redirect:/admin/post";
        if (request.getHeader("Referer") != null) {

            return "redirect:" + request.getHeader("Referer");
        } else {

            return "redirect:/admin/post";
        }
    }


    @PostMapping("/post/delete")
    public String postDelete(@RequestParam("id") String id, RedirectAttributes rttr) {

        int postCode = Integer.parseInt(id);

        adminService.postDelete(postCode);

        rttr.addFlashAttribute("successMessage", postCode + "번 게시글 삭제 성공!");

        return "redirect:/admin/post";
    }

    @GetMapping("/notice")
    public String selectAllNotice(@ModelAttribute("params") final SearchDTO params, Model model) {

        String Keyword = params.getKeyword();
        System.out.println("keyword = " + Keyword);

        List<AdminPostDTO> noticeList = adminService.selectAllNotice(params);

        int count = adminService.countNotice(params);

        for (AdminPostDTO notice : noticeList) {
            System.out.println("notice = " + notice);
        }

        model.addAttribute("count", count);
        model.addAttribute("keyword", Keyword);
        model.addAttribute("noticeList", noticeList);

        return "/admin/notice/list";
    }

    @GetMapping("/notice/regist")
    public void noticeRegist() {}

    @PostMapping("/notice/regist")
    public String noticeRegist(PostDTO newNotice, RedirectAttributes rttr) {

        adminService.noticeRegist(newNotice);

        rttr.addFlashAttribute("successMessage", "신규 공지사항 등록 성공!");

        return "redirect:/admin/notice";
    }

    @GetMapping("/notice/detail")
    public String noticeDetail(@RequestParam("id") String id, Model model) {

        System.out.println("id = " + id);

        int code = Integer.parseInt(id);

        AdminPostDTO noticeDetail = adminService.selectPost(code);

        String text = noticeDetail.getPostContext().replace("\r\n", "<br>");

        System.out.println("text = " + text);

        noticeDetail.setPostContext(text);


        System.out.println("noticeDetail = " + noticeDetail);

        model.addAttribute("noticeDetail", noticeDetail);

        return "/admin/notice/detail";
    }

    @GetMapping("/notice/update")
    public String noticeUpdate(@RequestParam("id") String id, Model model) {

        System.out.println("id = " + id);

        int code = Integer.parseInt(id);

        AdminPostDTO noticeDetail = adminService.selectPost(code);

        System.out.println("noticeDetail = " + noticeDetail);

        model.addAttribute("noticeDetail", noticeDetail);

        return "/admin/notice/update";
    }

    @PostMapping("/notice/update")
    public String noticeUpdate(PostDTO post, RedirectAttributes rttr) {

//        System.out.println("id = " + id);

//        int code = Integer.parseInt(id);
        System.out.println("post = " + post);

        adminService.noticeUpdate(post);

        rttr.addFlashAttribute("successMessage", "공지사항 수정 성공!");

        return "redirect:/admin/notice";
    }

    @PostMapping("/notice/delete")
    public String noticeDelete(@RequestParam("id") String id, RedirectAttributes rttr) {

        int postCode = Integer.parseInt(id);

        adminService.postDelete(postCode);

        rttr.addFlashAttribute("successMessage", postCode + "번 공지사항 삭제 성공!");

        return "redirect:/admin/notice";
    }

    @GetMapping("/magazine")
    public String selectAllMagazine(@ModelAttribute("params") final SearchDTO params, Model model) {

        String Keyword = params.getKeyword();
        System.out.println("keyword = " + Keyword);

        List<AdminPostDTO> magazineList = adminService.selectAllMagazine(params);

        int count = adminService.countMagazine(params);

        for (AdminPostDTO magazine : magazineList) {
            System.out.println("magazine = " + magazine);
        }

        model.addAttribute("count", count);
        model.addAttribute("keyword", Keyword);
        model.addAttribute("magazineList", magazineList);

        return "/admin/magazine/list";
    }

    @GetMapping("/magazine/regist")
    public void magazineRegist() {}

    @PostMapping("/magazine/regist")
    public String magazineRegist(PostDTO newMagazine, RedirectAttributes rttr) {

        adminService.magazineRegist(newMagazine);

        rttr.addFlashAttribute("successMessage", "신규 매거진 등록 성공!");

        return "redirect:/admin/magazine";
    }

    @GetMapping("/magazine/detail")
    public String magazineDetail(@RequestParam("id") String id, Model model) {

        System.out.println("id = " + id);

        int code = Integer.parseInt(id);

        AdminPostDTO magazineDetail = adminService.selectPost(code);

        String text = magazineDetail.getPostContext().replace("\n", "<br>");

        System.out.println("text = " + text);

        magazineDetail.setPostContext(text);


        System.out.println("magazineDetail = " + magazineDetail);

        model.addAttribute("magazineDetail", magazineDetail);

        return "/admin/magazine/detail";
    }

    @GetMapping("/magazine/update")
    public String magazineUpdate(@RequestParam("id") String id, Model model) {

        System.out.println("id = " + id);

        int code = Integer.parseInt(id);

        AdminPostDTO magazineDetail = adminService.selectPost(code);

        System.out.println("magazineDetail = " + magazineDetail);

        model.addAttribute("magazineDetail", magazineDetail);

        return "/admin/magazine/update";
    }

    @PostMapping("/magazine/update")
    public String magazineUpdate(PostDTO post, RedirectAttributes rttr) {

//        System.out.println("id = " + id);

//        int code = Integer.parseInt(id);
        System.out.println("post = " + post);

        adminService.noticeUpdate(post);


        rttr.addFlashAttribute("successMessage", "매거진 수정 성공!");

        return "redirect:/admin/magazine";
    }

    @PostMapping("/magazine/delete")
    public String magazineDelete(@RequestParam("id") String id, RedirectAttributes rttr) {

        int postCode = Integer.parseInt(id);

        adminService.postDelete(postCode);

        rttr.addFlashAttribute("successMessage", postCode + "번 매거진 삭제 성공!");

        return "redirect:/admin/magazine";
    }




//    @PostMapping("/post")
//    public String postDelete(int id, RedirectAttributes rttr) {
//
//        adminService.postDelete(id);
//
//        rttr.addFlashAttribute("successMessage", id + "번 메뉴 삭제 성공!");
//
//        return "redirect:/admin/post";
//    }
}
