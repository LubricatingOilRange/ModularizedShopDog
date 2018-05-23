package com.library.common.entry.base;

import android.os.Parcel;
import android.os.Parcelable;

public class ChannelsBean implements Parcelable {

    /**
     * brandCode : ESPRIT
     * channelCode : 1esprit官方旗舰店
     * lpType : 1
     * pgMode : 2
     * pgraMode : 2
     * printTemp : 1
     * printType : 1
     * sfPrintSize : 1
     * splitMax :
     * splitMode : 0
     * version :
     */

    private String brandCode; //品牌编码
    private String channelCode;//渠道编码
    private String channelName;//渠道名称
    private int lpType;//物流对接类型 1.宝尊物流服务 2.顺风接口
    // private int pgMode;// 自提模式 1.标准模式 2.只推模式
    // private int pgraMode;//自提退货模式 1.标准模式 2.退门店模式
    private int printTemp;//打印模板定制 1.标准 2.定制
    private int printType;//打印机类型 1.博思德 2.斑马203 3.斑马303
    private int sfPrintSize;//顺风面单 1.180*100 2.15.*100
    //    private String splitMax;//最大拆单包裹数
    private int splitMode;// 拆单模式  0.不拆单 1.拆单
    //    private String version;//
    private int type;

    protected ChannelsBean(Parcel in) {
        brandCode = in.readString();
        channelCode = in.readString();
        channelName = in.readString();
        lpType = in.readInt();
        printTemp = in.readInt();
        printType = in.readInt();
        sfPrintSize = in.readInt();
        splitMode = in.readInt();
        type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandCode);
        dest.writeString(channelCode);
        dest.writeString(channelName);
        dest.writeInt(lpType);
        dest.writeInt(printTemp);
        dest.writeInt(printType);
        dest.writeInt(sfPrintSize);
        dest.writeInt(splitMode);
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChannelsBean> CREATOR = new Creator<ChannelsBean>() {
        @Override
        public ChannelsBean createFromParcel(Parcel in) {
            return new ChannelsBean(in);
        }

        @Override
        public ChannelsBean[] newArray(int size) {
            return new ChannelsBean[size];
        }
    };

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

    public int getLpType() {
        return lpType;
    }

    public void setLpType(int lpType) {
        this.lpType = lpType;
    }

  /*  public int getPgMode() {
        return pgMode;
    }

    public void setPgMode(int pgMode) {
        this.pgMode = pgMode;
    }

    public int getPgraMode() {
        return pgraMode;
    }

    public void setPgraMode(int pgraMode) {
        this.pgraMode = pgraMode;
    }*/

    public int getPrintTemp() {
        return printTemp;
    }

    public void setPrintTemp(int printTemp) {
        this.printTemp = printTemp;
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

//    public String getSplitMax() {
//        return splitMax;
//    }
//
//    public void setSplitMax(String splitMax) {
//        this.splitMax = splitMax;
//    }

    public int getSplitMode() {
        return splitMode;
    }

    public void setSplitMode(int splitMode) {
        this.splitMode = splitMode;
    }

    /* public String getVersion() {
         return version;
     }

     public void setVersion(String version) {
         this.version = version;
     }*/
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
