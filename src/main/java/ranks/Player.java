package ranks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Player {
    public long rownum;
    public String nickname;
    public long points;
    public long bestRank;

}
