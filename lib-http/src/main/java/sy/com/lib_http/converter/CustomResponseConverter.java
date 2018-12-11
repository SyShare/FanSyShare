package sy.com.lib_http.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
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
    private final Type[] modelType;


    CustomResponseConverter(Gson gson, Type... type) {
        this.gson = gson;
        this.adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(type[0]));
        this.modelType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        try {
            if (modelType.length == 1 && modelType[0] == String.class) {
                return (T) value.string();
            }

            Gson gson = new Gson();
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            T t = (T) adapter.read(jsonReader);
            if (t instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) t;
                if (response.getData() != null) {
                    if (response.getData() instanceof LinkedTreeMap && modelType.length > 1) {
                        String str = gson.toJson(response.getData());
                        response.setData(gson.fromJson(str, modelType[1]));
                    }
                }
            }
            return t;
        } finally {
            value.close();
        }
    }

}
