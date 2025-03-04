package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;
import com.example.samuraitravel.service.HouseService;

@Controller
public class FavoriteController {
	private final FavoriteService favoriteService;
	
	public FavoriteController(FavoriteService favoriteService, HouseService houseService) {
		this.favoriteService = favoriteService;
	}

	//ログイン済みのユーザーのお気に入り一覧を表示するメソッド
	@GetMapping("/favorites")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
					    @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC)
						Pageable pageable, Model model) {
		
		User user = (userDetailsImpl != null) ? userDetailsImpl.getUser() : null;
		if(user == null) {
			return "redirect:/login";
		}
		
		model.addAttribute("user", user);
		Page<Favorite> favoritePage = favoriteService.findFavoriteByUserIdOrderByHouseIdDesc(user.getId(), pageable);
		model.addAttribute("favoritePage", favoritePage);
		
		return "favorite/index";
	}
	
	//お気に入りを追加するメソッド
	@PostMapping("/houses/{houseId}/favorite")
	public String addFavorite(@PathVariable Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
							  RedirectAttributes redirectAttributes) {
		if(userDetailsImpl != null) {
			Integer userId = userDetailsImpl.getUser().getId();
			favoriteService.addFavorite(houseId, userId);
		}
		return "redirect:/houses/" + houseId;
	}
	
	//お気に入りを解除するメソッド
	@PostMapping("/houses/{houseId}/unfavorite")
	public String removeFavorite(@PathVariable Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
							  RedirectAttributes redirectAttributes) {
		if(userDetailsImpl != null) {
			Integer userId = userDetailsImpl.getUser().getId();
			favoriteService.removeFavorite(houseId, userId);
		}
		return "redirect:/houses/" + houseId;
	}
}
