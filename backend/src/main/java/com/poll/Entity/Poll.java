package com.poll.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Poll
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private Date postedDate;

    private Date expiredAt;

    private Integer totalVoteCount = 0;

    private boolean isExpired = false;

    private Integer voteCount = 0;

    // One Poll has MANY options
// mappedBy = "poll" → refers to poll field in Options class
// cascade = ALL → if Poll deleted → all its Options deleted too
    //mapped by - no extra column
    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Options> options;


    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "user_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

}
