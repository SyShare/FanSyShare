package sy.com.lib_http.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import sy.com.lib_http.bean.BaseResponse;


class CustomResponseConverter<T> implements Converter<ResponseBody, T> {

    /**
     * 成功
     */
    private static final int SUCCESS = 200;

    private final Gson gson;
    private final TypeAdapter<T> adapter;


    CustomResponseConverter(Gson gson, Type type) {
        this.gson = gson;
        this.adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(type));
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        String json = value.string();
        try {
            verify(json);
            return adapter.read(gson.newJsonReader(new StringReader(json)));
        } finally {
            value.close();
        }
    }

    private void verify(String json) {
        BaseResponse result = gson.fromJson(json, BaseResponse.class);
        if (result.getCode() != SUCCESS) {
            throw new IllegalStateException(result.getMsg());
        }
    }

}
