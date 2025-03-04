package com.example.samuraitravel.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository, HouseRepository houseRepository, UserRepository userRepository) {
		this.favoriteRepository = favoriteRepository;
		this.houseRepository = houseRepository;
		this.userRepository = userRepository;
	}
	
	//お気に入りに追加されているかの確認
	public boolean hasUserFavorited(Integer houseId, Integer userId) {
		return favoriteRepository.existsByHouseIdAndUserId(houseId, userId);
	}

	//ログイン済みのユーザーのお気に入り一覧を表示するメソッド
	public Page<Favorite> findFavoriteByUserIdOrderByHouseIdDesc(Integer userId, Pageable pageable) {
		return favoriteRepository.findByUserIdOrderByHouseIdDesc(userId, pageable);
	}
	
	public void addFavorite(Integer houseId, Integer userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
		House house = houseRepository.findById(houseId).orElseThrow(() -> new IllegalArgumentException("Inavalid house ID"));
		
		
		Favorite favorite = new Favorite();
		favorite.setUser(user);
		favorite.setHouse(house);
		favoriteRepository.save(favorite);
	}
	
	public void removeFavorite(Integer houseId, Integer userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
		House house = houseRepository.findById(houseId).orElseThrow(() -> new IllegalArgumentException("Inavalid house ID"));
		List<Favorite> favorites = favoriteRepository.findByUserAndHouse(user, house);
		if(favorites != null) {
			favoriteRepository.deleteAll(favorites);
		}
	}
}