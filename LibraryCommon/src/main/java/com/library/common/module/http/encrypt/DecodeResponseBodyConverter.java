package com.library.common.module.http.encrypt;

import android.support.annotation.NonNull;

import com.google.gson.TypeAdapter;
import com.library.common.module.http.helper.En_DecryptionHelper;
import com.library.core.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;


//对返回的数据进行解密处理
public class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        //获取ResponseBody 传递的Json字符串
        String resultJson = value.string();
        String content = "";
        try {
            JSONObject jsonObject = new JSONObject(resultJson);
            //将resultJson进行解密处理
            content = En_DecryptionHelper.getInstance().decode(jsonObject.getString("result"));
        } catch (JSONException e) {
            LogUtil.e(e.getMessage());
        }
        return adapter.fromJson(content);
    }
}
