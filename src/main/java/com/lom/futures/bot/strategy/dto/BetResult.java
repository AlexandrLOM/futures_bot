package com.lom.futures.bot.strategy.dto;

import com.lom.futures.enums.PositionSide;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BetResult {

    Integer bet;
    PositionSide positionSide;

    public static BetResult create (PositionSide positionSide,  Integer bet){
        var betResult = new BetResult();
        betResult.setPositionSide(positionSide);
        betResult.setBet(bet);
        return betResult;
    }
}
