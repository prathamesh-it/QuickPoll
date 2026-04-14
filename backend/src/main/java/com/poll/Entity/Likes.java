package com.poll.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poll.dtos.LikesDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Likes
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //One user can post many comments
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Poll poll;

    public LikesDTO getLikeDTO()
    {
        LikesDTO likesDTO = new LikesDTO();
        likesDTO.setId(id);
        likesDTO.setUsername(user.getFirstName() + " "+ user.getLastName());
        likesDTO.setUserId(user.getId());
        likesDTO.setPollId(poll.getId());
        return likesDTO;

    }
}
