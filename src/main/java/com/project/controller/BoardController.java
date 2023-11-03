package com.project.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.domain.LoginUser;
import com.project.domain.BoardVO;
import com.project.service.BoardService;
import com.project.service.MenuService;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService bService;
	
	@Autowired
	private MenuService mService;
	
	@Resource(name="sessionUser")
	private LoginUser sessionUser;
	
	@GetMapping("/main")
	public String board(@RequestParam("board_idx") int board_idx, Model model) {
		model.addAttribute("board_idx",board_idx); //카테고리번호
		model.addAttribute("boardMenu", mService.getMenu(board_idx)); //카테고리이름
		model.addAttribute("list", bService.getContentList(board_idx)); //게시글목록
		return "board/main";
	
	}
	
	@GetMapping("/write")
	public String write(@RequestParam("board_idx") int board_idx,
			            @ModelAttribute("writeContent") BoardVO content, Model model) {
		model.addAttribute("board_idx",board_idx); //카테고리번호
		model.addAttribute("boardMenu", mService.getMenu(board_idx)); //카테고리이름
		return "board/write";
	}
	
	//유효성검사하고 데이터 저장하기
	@PostMapping("/write_pro")
	public String write_pro(@Valid @ModelAttribute("writeContent") BoardVO writeContent,
							BindingResult result,Model model,
							@RequestParam("board_idx") int board_idx) {
		if(result.hasErrors()) {
			return "board/write";
		}
		//DB저장
		bService.addBoardContent(writeContent);
		model.addAttribute("board_idx",board_idx); //카테고리번호
		return "board/write_success";
	}
	
	@GetMapping("/view")
	public String view(@RequestParam("board_idx") int board_idx,
					   @RequestParam("content_idx") int content_idx, Model model) {
		model.addAttribute("board_idx",board_idx); //카테고리 번호
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("boardMenu", mService.getMenu(board_idx)); //카테고리이름

		//DB데이터가져오기
		model.addAttribute("content",bService.viewCotent(content_idx));
		//조회수카운트
		bService.view(content_idx);
		return "board/view";
	}
	
}
