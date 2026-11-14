package com.blog.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.PrePersist;
import java.util.Date;

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

    // Nullable so posts created before the live-feed module remain readable.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = true)
    private UserInfo author;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

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

	public UserInfo getAuthor() { return author; }

	public void setAuthor(UserInfo author) { this.author = author; }

	public Date getCreatedAt() { return createdAt; }

	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

	@PrePersist
	protected void setCreatedAtIfMissing() {
		if (createdAt == null) createdAt = new Date();
	}

	@Override
	public String toString() {
		return "Post [pId=" + pId + ", title=" + title + ", content=" + content + ", category=" + category + ", media="
				+ media + ", likeCount=" + likeCount + "]";
	}

	
    
    
    
	    
}
