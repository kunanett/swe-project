package results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for wrapping information about players.
 *
 * The class wraps essential information about players, such as their nickname, accumulated points and best rank.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Player {
    private String nickname;
    private long points;
    private long bestRank;

}
