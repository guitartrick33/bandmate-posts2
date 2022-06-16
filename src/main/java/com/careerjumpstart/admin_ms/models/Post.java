package com.careerjumpstart.admin_ms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "username")
    private String username;

    @Column(name = "type")
    private PostType postType;

    @Column(name = "likes")
    private int likes;

    @JsonIgnore
    @OneToMany(mappedBy = "id",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> comments;
}
