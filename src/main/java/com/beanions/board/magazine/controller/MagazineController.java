package com.beanions.board.magazine.controller;

import com.beanions.board.common.dto.PostAndMemberDTO;
import com.beanions.board.magazine.service.MagazineService;
import com.beanions.board.magazine.dto.MagazineDTO;
import com.beanions.board.magazine.controller.PagingResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/board")
public class MagazineController {
    private final MagazineService magazineService;

    public MagazineController (MagazineService magazineService){
        this.magazineService = magazineService;
    }

    @GetMapping("/magazine")
    public String magazineList(Model model, @ModelAttribute("params") final MagazineDTO params) {

        PagingResponse<PostAndMemberDTO> magazineList = magazineService.allMagazineList(params);

        model.addAttribute("magazineList", magazineList);

        return "/user/board/magazineList";
    }

    @GetMapping( "/magazinedetail")
    public String magazineDetail(@RequestParam("id") String id, Model model) {

        PostAndMemberDTO magazine = magazineService.selectMagazine(id);

        String text = magazine.getPostContext().replace("\n", "<br>"); // "\r\n", "\n" 둘다 가능

        System.out.println("text = " + text);

        magazine.setPostContext(text);


        model.addAttribute("magazine", magazine);

        return "/user/board/magazineDetail";
    }
}
