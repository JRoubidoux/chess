package Services;

import Req_and_Result.ClearAppServiceReq;
import Req_and_Result.ClearAppServiceRes;

/**
 * This Class is intended to clear the DB.
 */
public class ClearAppService {

    /**
     * This function will clear the database. It removes all users, games and authTokens.
     *
     * @param request A ClearAppServiceRequest object.
     * @return A ClearAppServiceResponse object.
     */
    public ClearAppServiceRes clearApp(ClearAppServiceReq request) {return null;}
}
