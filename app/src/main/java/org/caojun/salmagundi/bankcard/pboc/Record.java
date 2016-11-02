package org.caojun.salmagundi.bankcard.pboc;


import org.caojun.salmagundi.utils.FormatUtils;

/**
 * 读记录
 *
 * @author ghli
 *
 */
public class Record
{

    public Record()
    {

    }

    private String	date;
    private String	time;
    private String	tranMoney;
    private String	otherMoney;
    private String	Country;
    private String	Currency;
    private String	merchant;
    private String	type;
    private String	ATC;

    int				dateLength			= 3;
    int				timeLength			= 3;
    int				tranMoneyLength		= 6;
    int				otherMoneyLength	= 6;
    int				CountryLength		= 2;
    int				CurrencyLenth		= 2;
    int				merchantLenth		= 20;
    int				typeLenth			= 1;
    int				ATCLenth			= 2;

    String			prefix				= "00B2";
    String			endprefix			= "5C00";

    public void setDate(String date)
    {
        this.date = date;
        // ATC.startsWith(prefix)
    }

    public boolean IsStartsWith(byte[] cmd)
    {
        if (cmd.length == 0)
            return false;

        return FormatUtils.byteArrayToHexString(cmd).startsWith(prefix)
                && FormatUtils.byteArrayToHexString(cmd).endsWith(endprefix);
    }

    public String getDate()
    {
        return this.date;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getTime()
    {
        return this.time;
    }

    public void setTranMoney(String tranMoney)
    {
        this.tranMoney = tranMoney;
    }

    public String getTranMoney()
    {
        return this.tranMoney;
    }

    public void setOtherMoney(String otherMoney)
    {
        this.otherMoney = otherMoney;
    }

    public String getOtherMoney()
    {
        return this.otherMoney;
    }

    public void setCountry(String Country)
    {
        this.Country = Country;
    }

    public String getCountry()
    {
        return this.Country;
    }

    public void setCurrency(String Country)
    {
        this.Currency = Country;
    }

    public String getCurrency()
    {
        return this.Currency;
    }

    public void setMerchant(String merchant)
    {
        this.merchant = merchant;
    }

    public String getMerchant()
    {
        return this.merchant;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

    public void setATC(String ATC)
    {
        this.ATC = ATC;
    }

    public Record parse(byte[] data)
    {
        Record record = new Record();
        if (data.length == (dateLength + timeLength + tranMoneyLength
                + otherMoneyLength + CountryLength + CurrencyLenth
                + merchantLenth + typeLenth + ATCLenth))
        {
            int length = 0;
            byte[] strDate = getData(data, 0, dateLength);
            record.setDate(dateToString(FormatUtils.byteArrayToHexString(strDate)));
            length = dateLength;
            byte[] strtime = getData(data, dateLength, timeLength);
            record.setTime(timeToString(FormatUtils.byteArrayToHexString(strtime)));
            length = dateLength + timeLength;
            byte[] strtranMoney = getData(data, length, tranMoneyLength);
            record.setTranMoney(parseInt(FormatUtils
                    .byteArrayToHexString(strtranMoney)));
            length = dateLength + timeLength + tranMoneyLength;
            byte[] strotherMoney = getData(data, length, otherMoneyLength);
            record.setOtherMoney(parseInt(FormatUtils
                    .byteArrayToHexString(strotherMoney)));
            length = dateLength + timeLength + tranMoneyLength
                    + otherMoneyLength;
            byte[] strCountry = getData(data, length, CountryLength);
            record.setCountry(FormatUtils.byteArrayToHexString(strCountry));
            length = dateLength + timeLength + tranMoneyLength
                    + otherMoneyLength + CountryLength;
            byte[] strCurrency = getData(data, length, CurrencyLenth);
            record.setCurrency(FormatUtils.byteArrayToHexString(strCurrency));
            length = dateLength + timeLength + tranMoneyLength
                    + otherMoneyLength + CountryLength + CurrencyLenth;
            byte[] strmerchant = getData(data, length, merchantLenth);
            record.setMerchant(FormatUtils.byteArrayToHexString(strmerchant));
            length = dateLength + timeLength + tranMoneyLength
                    + otherMoneyLength + CountryLength + CurrencyLenth
                    + merchantLenth;
            byte[] strtype = getData(data, length, typeLenth);
            record.setType(FormatUtils.byteArrayToHexString(strtype));
            length = dateLength + timeLength + tranMoneyLength
                    + otherMoneyLength + CountryLength + CurrencyLenth
                    + merchantLenth + typeLenth;
            byte[] strATC = getData(data, length, ATCLenth);
            record.setATC(FormatUtils.byteArrayToHexString(strATC));
        }
        return record;
    }

    public byte[] getData(byte[] data, int Start, int length)
    {
        byte[] Data = new byte[length];

        System.arraycopy(data, Start, Data, 0, length);

        return Data;
    }

    /***
     * 日期
     *
     * @param date
     * @return
     */
    public String dateToString(String date)
    {
        String Date = "";
        if (date.length() == 6)
        {
            Date = date.substring(0, 2) + "-" + date.substring(2, 4) + "-"
                    + date.substring(4, 6);
        }
        return Date;
    }

    /***
     * 时间
     *
     * @param Time
     * @return
     */
    public String timeToString(String Time)
    {
        String time = "";
        if (Time.length() == 6)
        {
            time = Time.substring(0, 2) + ":" + Time.substring(2, 4) + ":"
                    + Time.substring(4, 6);
        }
        return time;
    }

    /***
     * 转数字型
     *
     * @param money
     * @return
     */
    public String parseInt(String money)
    {
        int fen = Integer.parseInt(money, 10);

//        return String.valueOf(new BigDecimal(fen / 100.0d));
        StringBuffer sb = new StringBuffer(String.valueOf(fen));
        while(sb.length() < 3)
        {
            sb.insert(0, '0');
        }
        sb.insert(sb.length() - 2, '.');
        return sb.toString();
    }

}
