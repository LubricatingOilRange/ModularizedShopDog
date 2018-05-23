package com.library.common.entry;

import android.os.Parcel;
import android.os.Parcelable;

import com.library.common.entry.base.ChannelsBean;

import java.util.List;

public class UserCommand implements Parcelable {

    /**
     * data : {"brandCode":"ESPRIT","channelCode":"","channels":[{"brandCode":"ESPRIT","channelCode":"1esprit官方旗舰店","lpType":1,"pgMode":2,"pgraMode":2,"printTemp":1,"printType":1,"sfPrintSize":1,"splitMax":"","splitMode":0,"version":""}],"createTime":"","isDeli":1,"isOrder":1,"isReturn":1,"isSelfPick":1,"isWeigh":0,"loginName":"805","password":"","printType":1,"sfPrintSize":2,"storeCode":"LSS","storeId":147,"storeName":"龙之梦购物中心","token":"964d9c16103d49edac830d42b3e1f7b0","userId":183,"userName":"805","userStatus":1,"userType":0}
     * errorCode : 0
     * message :
     * result : 1
     */

    /**
     * brandCode : ESPRIT
     * channelCode :
     * channels : [{"brandCode":"ESPRIT","channelCode":"1esprit官方旗舰店","lpType":1,"pgMode":2,"pgraMode":2,"printTemp":1,"printType":1,"sfPrintSize":1,"splitMax":"","splitMode":0,"version":""}]
     * createTime :
     * isDeli : 1
     * isOrder : 1
     * isReturn : 1
     * isSelfPick : 1
     * isWeigh : 0
     * loginName : 805
     * password :
     * printType : 1
     * sfPrintSize : 2
     * storeCode : LSS
     * storeId : 147
     * storeName : 龙之梦购物中心
     * token : 964d9c16103d49edac830d42b3e1f7b0
     * userId : 183
     * userName : 805
     * userStatus : 1
     * userType : 0
     */
    private String brandCode;
    private String channelCode;
    private String createTime;
    private int isDeli;//是否支持配货 0，不支持，1，支持
    private int isOrder;//是否支持门店下单 0，不支持，1支持
    private int isReturn;//是否支持顾客退货 0，不支持 1支持
    private int isSelfPick; //是否支持顾客自提 0，不支持，1支持
    //包裹管理：快递取件永远都有
    /*
    * 门店自提控制 ：1.小铃铛 2，快递送货 3 顾客取货 4，超期件
    * 配货控制 1，门店配货
    * 下单控制 1，店员下单
    * 退货控制 1，顾客退货
    * 自提和退货控制 1.门店报表（全部为0才禁止），否则开启对应的
    * */
    private String isWeigh;
    private String loginName;
    private String password;
    private int printType;//打印机类型
    private int sfPrintSize;//顺丰面单尺寸 1.180mm*100mm,2.150mm*110mm
    private String storeCode;
    private int storeId;
    private String storeName;
    private String token;
    private String userId;
    private String userName;
    private int userStatus;
    private int userType;
    private Integer isAutoPrint;//是否自动打印

    protected UserCommand(Parcel in) {
        brandCode = in.readString();
        channelCode = in.readString();
        createTime = in.readString();
        isDeli = in.readInt();
        isOrder = in.readInt();
        isReturn = in.readInt();
        isSelfPick = in.readInt();
        isWeigh = in.readString();
        loginName = in.readString();
        password = in.readString();
        printType = in.readInt();
        sfPrintSize = in.readInt();
        storeCode = in.readString();
        storeId = in.readInt();
        storeName = in.readString();
        token = in.readString();
        userId = in.readString();
        userName = in.readString();
        userStatus = in.readInt();
        userType = in.readInt();
        if (in.readByte() == 0) {
            isAutoPrint = null;
        } else {
            isAutoPrint = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandCode);
        dest.writeString(channelCode);
        dest.writeString(createTime);
        dest.writeInt(isDeli);
        dest.writeInt(isOrder);
        dest.writeInt(isReturn);
        dest.writeInt(isSelfPick);
        dest.writeString(isWeigh);
        dest.writeString(loginName);
        dest.writeString(password);
        dest.writeInt(printType);
        dest.writeInt(sfPrintSize);
        dest.writeString(storeCode);
        dest.writeInt(storeId);
        dest.writeString(storeName);
        dest.writeString(token);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeInt(userStatus);
        dest.writeInt(userType);
        if (isAutoPrint == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isAutoPrint);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserCommand> CREATOR = new Creator<UserCommand>() {
        @Override
        public UserCommand createFromParcel(Parcel in) {
            return new UserCommand(in);
        }

        @Override
        public UserCommand[] newArray(int size) {
            return new UserCommand[size];
        }
    };

    public Integer getIsAutoPrint() {
        return isAutoPrint;
    }

    public void setIsAutoPrint(Integer isAutoPrint) {
        this.isAutoPrint = isAutoPrint;
    }

    private List<ChannelsBean> channels;

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsDeli() {
        return isDeli;
    }

    public void setIsDeli(int isDeli) {
        this.isDeli = isDeli;
    }

    public int getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(int isOrder) {
        this.isOrder = isOrder;
    }

    public int getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(int isReturn) {
        this.isReturn = isReturn;
    }

    public int getIsSelfPick() {
        return isSelfPick;
    }

    public void setIsSelfPick(int isSelfPick) {
        this.isSelfPick = isSelfPick;
    }

    public String getIsWeigh() {
        return isWeigh;
    }

    public void setIsWeigh(String isWeigh) {
        this.isWeigh = isWeigh;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPrintType() {
        return printType;
    }

    public void setPrintType(int printType) {
        this.printType = printType;
    }

    public int getSfPrintSize() {
        return sfPrintSize;
    }

    public void setSfPrintSize(int sfPrintSize) {
        this.sfPrintSize = sfPrintSize;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public List<ChannelsBean> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelsBean> channels) {
        this.channels = channels;
    }
}
