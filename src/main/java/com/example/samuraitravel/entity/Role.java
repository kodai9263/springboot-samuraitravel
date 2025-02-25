package com.example.samuraitravel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity //エンティティとして機能
@Table(name = "roles") //対応づけるテーブル名
@Data //ゲッターセッター自動生成
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //主キー
	@Column(name = "id") //対応するカラム名
	private Integer id;

	@Column(name = "name")
	private String name;
}
