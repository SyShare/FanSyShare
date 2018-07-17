package sy.com.lib_http;

/**
 * 使用者项目服务器具体的参数配置
 */
public class ServerConfig {

    private static ServerConfig instance = new ServerConfig();
    public static ServerConfig getInstance() {
        return instance;
    }

    /**
     * 以下为默认的配置参数，支持外部配置这些默认值，也支持重写Converter
     */
    private String codeKey = "code";
    private String msgKey = "message";
    private String dataKey = "data";
    private int codeValid = 0;

    public String getCodeKey() {
        return codeKey;
    }

    public ServerConfig setCodeKey(String code) {
        this.codeKey = code;
        return this;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public ServerConfig setMsgKey(String msgKey) {
        this.msgKey = msgKey;
        return this;
    }

    public String getDataKey() {
        return dataKey;
    }

    public ServerConfig setDataKey(String data) {
        this.dataKey = data;
        return this;
    }

    public int getCodeValid() {
        return codeValid;
    }

    public ServerConfig setCodeValid(int codeValid) {
        this.codeValid = codeValid;
        return this;
    }
}
