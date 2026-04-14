package com.poll.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Data
public class Options
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;   // "Red", "Blue", "Green"

    private Integer voteCount =0;

    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    // Creates poll_id column in Options table
// Links option to its poll
    @JoinColumn(name = "poll_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Poll poll;   // which poll this option belongs to

    @OneToMany(mappedBy = "options" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vote> voteList;

}

//OPTIONS TABLE
//┌────┬─────────┬─────────┐
//│ id │ title   │ poll_id │
//├────┼─────────┼─────────┤
//│ 1  │ Red     │ 1       │ ← belongs to Poll 1
//│ 2  │ Blue    │ 1       │ ← belongs to Poll 1
//│ 3  │ Green   │ 1       │ ← belongs to Poll 1
//│ 4  │ Java    │ 2       │ ← belongs to Poll 2
//│ 5  │ Python  │ 2       │ ← belongs to Poll 2
//└────┴─────────┴─────────┘
