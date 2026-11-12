package com.blog.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="posts")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int pId;
    private String title;
    private String content;
    private String category;
    private String media;
    
    @Column(name="like_count")
    private int likeCount;

	public Post() {
		super();
		
	}

	public Post(int pId, String title, String content, String category, String media, int likeCount) {
		super();
		this.pId = pId;
		this.title = title;
		this.content = content;
		this.category = category;
		this.media = media;
		this.likeCount = likeCount;
	}

	public int getPId() {
		return pId;
	}

	public void setPId(int pId) {
		this.pId = pId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	@Override
	public String toString() {
		return "Post [pId=" + pId + ", title=" + title + ", content=" + content + ", category=" + category + ", media="
				+ media + ", likeCount=" + likeCount + "]";
	}

	
    
    
    
	    
}
