package ranks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for wrapping information about players.
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
