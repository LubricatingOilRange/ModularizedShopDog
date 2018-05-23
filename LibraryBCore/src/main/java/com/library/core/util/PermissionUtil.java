package com.library.core.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.library.core.base.activity.BaseActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PermissionUtil {
    //危险权限组 -- 动态申请（API > 22）

    //WRITE_CONTACTS,GET_ACCOUNTS(访问GMail账户列表),READ_CONTACTS
    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_CONTACTS = Manifest.permission.READ_CONTACTS;//通讯录权限组

    //READ_CALL_LOG , READ_PHONE_STATE , CALL_PHONE , WRITE_CALL_LOG , USE_SIP , PROCESS_OUTGOING_CALLS , ADD_VOICE MAIL(?)
    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_PHONE = Manifest.permission.CALL_PHONE;//手机权限组

    //READ_CALENDAR , WRITE_CALENDAR
    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_CALENDAR = Manifest.permission.READ_CALENDAR;//日历权限组（允许程序读取用户的日程信息）

    //CAMERA
    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_CAMERA = Manifest.permission.CAMERA;//相机权限组（允许访问摄像头进行拍照）

    //BODY_SENSORS
    //允许从传感器，用户使用来衡量什么是他/她的身体内发生的事情，如心脏速率访问数据的应用程序
    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_SENSORS = Manifest.permission.BODY_SENSORS;//传感器权限组

    //ACCESS_FINE_LOCATION , ACCESS_COARSE_LOCATION
    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;//用于允许访问设备位置的权限组

    //READ_EXTERNAL_STORAGE , WRITE_EXTERNAL_STORAGE
    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;//读写外部存储（SD卡）权限组(用于与共享外部存储相关的运行时权限)

    //RECORD_AUDIO
    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_MICROPHONE = Manifest.permission.RECORD_AUDIO;//用于与从设备访问麦克风音频相关联的权限。

    //READ_SMS , RECEIVE_WAP_PUSH , RECEIVE_MMS , RECEIVE_SMS , SEND_SMS , READ_CELL_BROADCASTS
    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_SMS = Manifest.permission.READ_SMS;//短信相关权限组(用于与用户SMS消息相关的运行时权限)


    //申请相机权限
    public static Disposable applyCameraPermission(BaseActivity baseActivity, @NonNull final PermissionCallBack callBack) {
        RxPermissions rxPermissions = new RxPermissions(baseActivity);
        return rxPermissions
                .requestEach(PermissionUtil.PERMISSION_GROUP_CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {//已经申请过该权限 或 用户点击了统一改权限
                            callBack.onApplySuccessful();//申请成功
                            LogUtil.i("permission", permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            callBack.onApplyFailure();//申请失败
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            LogUtil.i("permission", permission.name + " is denied. More info should be provided.");
                        } else {
                            callBack.onApplyFailure();//申请失败
                            // 用户拒绝了该权限，并且选中『不再询问』
                            LogUtil.i("permission", permission.name + " is denied.");
                        }
                    }
                });
    }

    public interface PermissionCallBack {

        void onApplySuccessful();//申请权限成功

        void onApplyFailure();//申请权限失败
    }
}
