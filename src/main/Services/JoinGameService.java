package Services;

import Req_and_Result.JoinGameServiceReq;
import Req_and_Result.JoinGameServiceRes;

public class JoinGameService {

    /**
     * Verifies that the specified game exists, and, if a color is specified,
     * adds the caller as the requested color to the game.
     * If no color is specified the user is joined as an observer.
     * This request is idempotent.
     *
     * @param request A JoinGameServiceRequest object.
     * @return A JoinGameServiceResponse object.
     */
    public JoinGameServiceRes joinGame(JoinGameServiceReq request) {return null;}
}
