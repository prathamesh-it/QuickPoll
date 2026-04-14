package com.poll.services.user;

import com.poll.Entity.*;
import com.poll.Repositories.*;
import com.poll.dtos.*;
import com.poll.utils.JWTUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService
{
    private final JWTUtil jwtUtil;                 // get logged in user

    private final PollRepository pollRepository;   // poll DB operations

    private final OptionsRepository optionsRepository; // options DB operations

    private final JavaMailSender javaMailSender;   // send emails

    private final VoteRepository voteRepository;   // vote DB operations

    private final LikesRepository likesRepository; // likes DB operations

    private final CommentRepository commentRepository; // comment DB operations


    @Override
    public PollDTO postPoll(PollDTO pollDTO)
    {
        User user = jwtUtil.getLoggedInUser();
        if(user != null)
        {
            Poll poll = new Poll();
            poll.setQuestion(pollDTO.getQuestion());
            poll.setPostedDate(new Date());
            poll.setExpiredAt(pollDTO.getExpiredAt());
            poll.setUser(user);
            poll.setTotalVoteCount(0);
            Poll createdPoll = pollRepository.save(poll);

            List<Options> options = new ArrayList<>();
            for(String optionTitle : pollDTO.getOptions())
            {
                Options option = new Options();
                option.setTitle(optionTitle);
                option.setPoll(createdPoll);
                option.setVoteCount(0);

                options.add(option);
            }
            List<Options>savedOptions =  optionsRepository.saveAll(options);
            poll.setOptions(savedOptions);
            pollRepository.save(poll);

            if(createdPoll.getId() != null)
            {
                try
                {
                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage , true);
                    mimeMessageHelper.setFrom("prathameshnistane1412@gmail.com");
                    mimeMessageHelper.setTo(user.getEmail());
                    mimeMessageHelper.setSubject("New Poll Posted");
                    mimeMessageHelper.setText("Dear "+user.getFirstName() + "! I trust this message finds you in good spirits. " +
                            "I wanted to inform you that a new poll has been successfully posted." +
                            "The questjon you submitted is as follows :'"+ createdPoll.getQuestion()+"'. " +
                            "This poll was posted on " + createdPoll.getPostedDate() + "." +
                            "Thank you for your engagement and contribution to our platform. POLL APP");
                    javaMailSender.send(mimeMessage);
                    System.out.println("Email sent successfully to " + user.getEmail());

                } catch (Exception e) {
                    System.out.println("Failed to send email:" + e.getMessage());;
                }
            }
            return getPollDTOInService(createdPoll);
        }
        return null;
    }

    @Override
    public void deletePoll(Long id)
    {
        pollRepository.deleteById(id);

    }

    @Override
    public List<PollDTO> getAllPolls() {
        return pollRepository.findAll()
                .stream()                    // process each poll
                .sorted(Comparator.comparing(Poll::getPostedDate).reversed()) // sort by postedDate (newest first)
                .map(this::getPollDTOInService) // convert each Poll to PollDTO
                .collect(Collectors.toList()); // collect results into a List
    }

    @Override
    public List<PollDTO> getMyPolls() {
        User user = jwtUtil.getLoggedInUser();
        if(user != null)
        {
            return pollRepository.findAllByUserId(user.getId())
                    .stream()                    // process each poll
                    .sorted(Comparator.comparing(Poll::getPostedDate).reversed()) // sort by postedDate (newest first)
                    .map(this::getPollDTOInService) // convert each Poll to PollDTO
                    .collect(Collectors.toList());
        }
        throw new EntityNotFoundException("User not found");

    }

    @Override
    public LikesDTO giveLikeToPoll(Long id)
    {
        Optional<Poll>optionalPoll = pollRepository.findById(id);
        User user = jwtUtil.getLoggedInUser();
        if(user != null && optionalPoll.isPresent())
        {
            Likes like  = new Likes();
            like.setUser(user);
            like.setPoll(optionalPoll.get());
            Likes savedLike = likesRepository.save(like);
            return savedLike.getLikeDTO();
//            Find poll by id → get logged in user
//            Both exist?
//                ↓
//            Create Likes object
//            Set: who liked (user) + which poll (poll)
//                    ↓
//            Save to likes table
//                    ↓
//            Return LikesDTO
        }
        return null;
    }

    @Override
    public CommentDTO postCommentOnPoll(CommentDTO commentDTO)
    {
       Optional<Poll> optionalPoll = pollRepository.findById(commentDTO.getPollId());
       User user = jwtUtil.getLoggedInUser();
       if(user != null && optionalPoll.isPresent())
       {
           Comment comment = new Comment();
           comment.setUser(user);
           comment.setPoll(optionalPoll.get());
           comment.setContent(commentDTO.getContent());
           comment.setCreatedAt(new Date());
           return commentRepository.save(comment).getCommentDTO();

       }
       return null;
    }

    @Override
    public VoteDTO postVoteOnPoll(VoteDTO voteDTO)
    {
        Optional<Poll> optionalPoll = pollRepository.findById(voteDTO.getPollId());
        Optional<Options> optionalOptions = optionsRepository.findById(voteDTO.getOptionId());
        User user = jwtUtil.getLoggedInUser();
        if(user != null && optionalPoll.isPresent() && optionalOptions.isPresent())
        {

            //Check if the poll has expired
            if(optionalPoll.get().getExpiredAt().before(new Date()))
            {
                throw new EntityNotFoundException("This poll has expired. You cannot vote on it.");
            }

            //Check if user has already voted on this poll
            boolean alreadyVoted = voteRepository.existsByPollIdAndUserId(voteDTO.getPollId() , user.getId());
            if(alreadyVoted)
            {
                throw new RuntimeException("You have already voted on this poll");
            }

            Vote vote = new Vote();
            vote.setUser(user);
            vote.setPoll(optionalPoll.get());
            vote.setOptions(optionalOptions.get());
            vote.setPostedDate(new Date());
            optionsRepository.save(optionalOptions.get());
            Vote voted = voteRepository.save(vote);


            //Update total vote count in Poll
            Poll poll = optionalPoll.get();
            poll.setTotalVoteCount(poll.getTotalVoteCount() + 1);
            pollRepository.save(poll);

            //Update vote count in Options
            Options options = optionalOptions.get();
            options.setVoteCount(options.getVoteCount() + 1);
            optionsRepository.save(options);

            return voted.getVoteDTO();
         }
        return null;
    }

    @Override
    public PollDetailsDTO getPollById(Long pollId)
    {
        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        User user = jwtUtil.getLoggedInUser();
        if(user != null && optionalPoll.isPresent())
        {
            List<Likes> likesList = likesRepository.findAllByPollId(optionalPoll.get().getId());
            List<Comment> commentList = commentRepository.findAllByPollId(optionalPoll.get().getId());
            PollDetailsDTO pollDetailsDTO = new PollDetailsDTO();
            pollDetailsDTO.setPollDTO(getPollDTOInService(optionalPoll.get()));
            pollDetailsDTO.getPollDTO().setLiked(likesRepository.findByPollIdAndUserId(pollId , user.getId()). isPresent());
            List<CommentDTO> commentDTOList = commentList.stream().map(comment -> {
                CommentDTO commentDTO = comment.getCommentDTO();
                if(comment.getUser().getId().equals(user.getId()))
                {
                    commentDTO.setUsername("You");  //Set "You" if user posted the comment
                }
                return commentDTO;
            }).toList();
            pollDetailsDTO.setCommentDTOS(commentDTOList);
            pollDetailsDTO.setLikesCount((long)likesList.size());
            pollDetailsDTO.setCommentsCount((long)commentList.size());
            return pollDetailsDTO;
        }
        return null;
    }


    //Poll Entity --> DTO
    public PollDTO getPollDTOInService(Poll poll)
    {
        User loggedInUser = jwtUtil.getLoggedInUser();

        PollDTO pollDTO = new PollDTO();
        pollDTO.setId(poll.getId());
        pollDTO.setQuestion(poll.getQuestion());
        pollDTO.setExpiredAt(poll.getExpiredAt());
        pollDTO.setExpired(poll.getExpiredAt() != null && poll.getExpiredAt().before(new java.util.Date()));
        pollDTO.setPostedDate(poll.getPostedDate());
        pollDTO.setOptionsDTOS(poll.getOptions().stream().map(options -> this.getOptionsDTO(options , loggedInUser.getId() , poll.getId())).collect(Collectors.toList()));
        pollDTO.setTotalVoteCount(poll.getTotalVoteCount());
        // poll.getOptions() → [Red, Blue, Green]
        //      ↓
        // .stream() → process each option one by one
        //      ↓
        // .map() → convert each Options to OptionsDTO
        //      ↓
        // getOptionsDTO(options, userId, pollId)
        //   → adds userVotedThisOption for John

        User pollOwner = poll.getUser();  //User who posted the poll

        //Check if logged in user is the poll owner
        if(loggedInUser != null && pollOwner.getId().equals(loggedInUser.getId()))
        {
            pollDTO.setUsername("You");  //logged in user created this poll
        }
        else
        {
            pollDTO.setUsername(pollOwner.getFirstName() + " "+pollOwner.getLastName());
        }

        pollDTO.setUserId(pollOwner.getId());

        //Check if Login user has Voted
        if(loggedInUser != null)
        {
            pollDTO.setVoted(voteRepository.existsByPollIdAndUserId(poll.getId() , loggedInUser.getId()));
        }

        return pollDTO;
    }


    private OptionsDTO getOptionsDTO(Options options , Long userId , Long pollId)
    {
        OptionsDTO optionsDTO = new OptionsDTO();
        optionsDTO.setId(options.getId());
        optionsDTO.setTitle(options.getTitle());
        optionsDTO.setPollId(options.getPoll().getId());
        optionsDTO.setVoteCount(options.getVoteCount());
        // "Did THIS user vote THIS option on THIS poll?"
        optionsDTO.setUserVotedThisOption(voteRepository.existsByPollIdAndUserIdAndOptionsId(pollId,userId,options.getId()));
        return optionsDTO;
    }


}
