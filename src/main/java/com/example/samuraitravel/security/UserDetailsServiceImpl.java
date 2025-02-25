package com.example.samuraitravel.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.UserRepository;

// Spring Securityの認証に必要なユーザー情報を提供するサービスクラス
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	// ユーザー情報をデータベースから取得するためのリポジトリ
	private final UserRepository userRepository;

	// コンストラクタインジェクションでUserRepositoryを注入
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	// ユーザーのメールアドレスを使って認証情報を読み込むメソッド
	// @param email ユーザーのメールアドレス
	// @return ユーザーの詳細情報
	// @throws UsernameNotFoundException ユーザーが見つからない場合
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			// メールアドレスを使ってユーザー情報をデータベースから取得
			User user = userRepository.findByEmail(email);
			
			// ユーザーの役割（権限）を取得
			String userRoleName = user.getRole().getName();
			
			// ユーザーの権限情報を設定
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(userRoleName));
			
			// 認証に使用するユーザー情報をUserDetailsImplとして返す
			return new UserDetailsImpl(user, authorities);
			
		} catch (Exception e) {
			// ユーザーが見つからない場合は例外をスロー
			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
		}
	}
}
