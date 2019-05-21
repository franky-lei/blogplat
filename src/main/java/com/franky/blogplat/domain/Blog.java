package com.franky.blogplat.domain;

import com.github.rjeschke.txtmark.Processor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Franky on 2019/4/26.
 */
@Entity
public class Blog implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "不能没有标题")
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String title;

    @NotEmpty(message = "不能没有摘要")
    @Size(min = 2, max = 200)
    @Column(nullable = false)
    private String summary;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Size(min = 2)
    @NotEmpty(message = "文章还没有填写内容")
    @Column(nullable = false)
    private String content;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Size(min = 2)
    @Column(nullable = false)
    private String parsedContent;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp timestamp;

    @Column
    private Integer upVoteTimes = 0;

    @Column
    private Integer commentTimes = 0;

    @Column
    private Integer readTimes = 0;

    @Column(length = 100)
    private String tags;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //当fetchType是EAGER时，会出现cannot simultaneously fetch multiple bags错误，为止原因，待解决
    @JoinTable(name = "blog_upvote", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "upvote_id", referencedColumnName = "id"))
    private List<UpVote> upVotes;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_id")
    private Classification classification;  //分类，一对一关系

    protected Blog(){

    }

    public Blog(@NotEmpty(message = "不能没有标题") @Size(min = 2, max = 50) String title, @NotEmpty(message = "不能没有摘要") @Size(min = 2, max = 200) String summary, @Size(min = 2) @NotEmpty(message = "文章还没有填写内容") String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setParsedContent(String parsedContent) {
        this.parsedContent = parsedContent;
    }

    public String getParsedContent() {
        return parsedContent;
    }

    public Integer getUpVoteTimes() {
        return upVoteTimes;
    }

    public void setUpVoteTimes(Integer upVoteTimes) {
        this.upVoteTimes = upVoteTimes;
    }

    public Integer getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(Integer commentTimes) {
        this.commentTimes = commentTimes;
    }

    public Integer getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Integer readTimes) {
        this.readTimes = readTimes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentTimes = this.comments.size();
    }

    public void addComments(Comment comment){
        this.comments.add(comment);
        this.commentTimes = comments.size();
    }

    public void removeComment(Long commentId){
        for(int index = 0; index < comments.size(); index++){
            if(comments.get(index).getId() == commentId){
                comments.remove(index);
                comments.remove(comments.get(1));
                break;
            }
        }
        this.commentTimes = comments.size();
    }

    public List<UpVote> getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(List<UpVote> upVotes) {
        this.upVotes = upVotes;
        this.upVoteTimes = upVotes.size();
    }

    public boolean addUpVote(UpVote upVote){
        boolean voted = false;
        if(upVotes.contains(upVote))
            return voted;
        else{
            upVotes.add(upVote);
            upVoteTimes = upVotes.size();
            return true;
        }
    }

    public void removeUpVote(Long voteId){
        boolean voted = false;
        for(int index = 0; index < upVotes.size(); index++){
            if(upVotes.get(index).getId() == voteId){
                upVotes.remove(index);
                upVoteTimes = upVotes.size();
                break;
            }
        }
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }
}
