package sy.com.lib_http;

import android.support.annotation.NonNull;

import sy.com.lib_http.bean.BaseResponse;

/**
 * description
 *
 * @author SyShare
 * @date 2018/8/24
 */
public class ApiException extends RuntimeException {

    public int code = -1;

    public ApiException(@NonNull BaseResponse response) {
        this(response.getMsg(), response.getCode());
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(String detailMessage, int code) {
        super(detailMessage);
        this.code = code;
    }
}
