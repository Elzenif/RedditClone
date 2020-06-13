package gso.training.reddit.dto;

import gso.training.reddit.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

    private VoteType type;
    private Long postId;

}
