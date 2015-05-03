package au.com.pactera.sampledownloader.dto;

/**
 * Created by priju.jacobpaul on 2/05/15.
 */
public class Error {
    private int mErrorCode;
    private String mErrorDescription;

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        this.mErrorCode = errorCode;
    }

    public String getErrorDescription() {
        return mErrorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.mErrorDescription = errorDescription;
    }
}
