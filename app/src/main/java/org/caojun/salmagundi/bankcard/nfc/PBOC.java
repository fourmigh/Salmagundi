package org.caojun.salmagundi.bankcard.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.text.TextUtils;

import org.caojun.salmagundi.bankcard.pboc.AC;
import org.caojun.salmagundi.bankcard.pboc.App;
import org.caojun.salmagundi.bankcard.pboc.BelTLV;
import org.caojun.salmagundi.bankcard.pboc.Card;
import org.caojun.salmagundi.bankcard.pboc.DDF;
import org.caojun.salmagundi.bankcard.pboc.DOL;
import org.caojun.salmagundi.bankcard.pboc.ExteneralAuthorize;
import org.caojun.salmagundi.bankcard.pboc.GPO;
import org.caojun.salmagundi.bankcard.pboc.GenerateAC;
import org.caojun.salmagundi.bankcard.pboc.GetData;
import org.caojun.salmagundi.bankcard.pboc.Reader;
import org.caojun.salmagundi.bankcard.pboc.Record;
import org.caojun.salmagundi.bankcard.pboc.Select;
import org.caojun.salmagundi.bankcard.pboc.Trade;
import org.caojun.salmagundi.bankcard.pboc.TradeOption;
import org.caojun.salmagundi.utils.FormatUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 标准PBOC应用
 * 该应用定义了各种PBOC借贷记应用的接口
 * Created by CaoJun on 2016/8/26.
 */
public class PBOC {
    // 终端应用版本2.0
    private final String[] Version = new String[] { "0020", "0030" };
    private static String AIDS[] = { "A000000333010101", "A000000333010102",
            "A000000333010103", "A000000333010106", "A0000000041010",
            "A0000000031010", "A0000000032010", "A0000000651010",
            "A0000000033010", "A0000000043060", "A00000002501",
            "A0000001523010", };
    // 终端特性（9F33）
    private byte[] TerminalCapability = FormatUtils.hexStringToByteArray("E0E1C8");
    private final byte[] ECI = FormatUtils.hexStringToByteArray("01");
    private final byte[] EDF = FormatUtils.hexStringToByteArray("00");
    private final byte[] TTR = FormatUtils.hexStringToByteArray("74000000");
    // 国家和币种代码
    private final byte[] country = FormatUtils.hexStringToByteArray("0156");
    private final byte[] currency = FormatUtils.hexStringToByteArray("0156");

    // 和IC卡交互的传输协议层
    private Transfer transfer;
    //随机数
    private Random randomSeed = new Random();

    // 过程中使用的命令对象
    private Select s = new Select();
    private Reader r = new Reader();
    private GPO g = new GPO();
    private GetData gd = new GetData();
    private GenerateAC ac = new GenerateAC();
    private ExteneralAuthorize ea = new ExteneralAuthorize();

    // 终端控制信息
    private byte TSI;
    private byte[] TVR = new byte[5];
    private ExecutorService es = Executors.newCachedThreadPool();

    private Card card = null;
    private Trade trade = null;
    private AC Ac;

    private App appSelected;
    private List<BelTLV> listBelTLV;

    // 清空当前的Transfer对象，以便重新获取
    private void cleanTransfer() {
        if (transfer != null) {
            try {
                transfer.close();
                transfer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 随机生成交易所需要的模数
     *
     * @return
     */
    private byte[] _generateMagic(int len) {
        StringBuilder buf = new StringBuilder();

        // 生成2位数字bcd码
        for (int i = 0; i < len; i++) {
            int num = 10 + this.randomSeed.nextInt(89);
            buf.append(num);
        }
        return FormatUtils.hexStringToByteArray(buf.toString());
    }

    private Trade getTrade(byte tradeType, BigDecimal amount, App sel) {
        // 构造交易
        Trade trade = new Trade();
        trade.setAmount(amount);
        trade.setDate(FormatUtils.getTradeDate());
        trade.setMagic(this._generateMagic(sel.getDOL().getLen("9F37")));
        trade.setTime(FormatUtils.getTradeTime());
        trade.setType(tradeType);

        return trade;
    }

    /**
     * 解析ADF应用入口
     *
     * @param tlv
     * @return
     */
    private App _parseAppEntry(BelTLV tlv) {
        App app = new App();
        for (BelTLV entry : (List<BelTLV>) tlv.getValues()) {
            String flag = entry.getFlag();
            if ("4F".equals(flag)) {
                // AID
                app.setAid(FormatUtils.byteArrayToHexString((byte[]) entry
                        .getValues()));
            } else if ("50".equals(flag)) {
                // 应用名称
                app.setLabel(new String((byte[]) entry.getValues()));
            } else if ("87".equals(flag)) {
                // 优先级
                app.setPriority(((byte[]) entry.getValues())[0]);
            }
        }
        return app;
    }

    /**
     * 选择优先级最高的应用
     *
     * @param apps
     * @return
     */
    private App _getMaxPriorityApp(List<App> apps) {
        int priority = Integer.MAX_VALUE;
        App sel = null;
        for (App app : apps) {
            // 选择优先级数字最小的
            if (app.getPriority() < priority) {
                priority = app.getPriority();
                sel = app;
            }
        }
        return sel;
    }

    /**
     * PBOC借贷记，第一步：应用选择
     *
     * @return
     * @throws Exception
     */
    private App _1selectApp(Transfer transfer) throws Exception {
        // 选择文件名为“1PAY.SYS.DDF01”的支付系统环境，建立支付系统环境并进入初始目录
        DDF ddf = this.s.selectDDF(transfer);
        List<App> cardSupportApps = new ArrayList<App>();
        if (ddf != null) {
            // 读取DDF中定义的所有ADF入口信息，序号从1开始
            int index = 1;

            while (true) {
                BelTLV record = this.r.readRecord(transfer, ddf.getSFI(),
                        index++);
                if (record == null) {
                    break;
                }
                for (BelTLV tlv : (List<BelTLV>) record.getValues()) {
                    // 获得ADF的应用入口
                    if ("61".equals(tlv.getFlag())) {
                        cardSupportApps.add(this._parseAppEntry(tlv));
                    }
                }
            }
        } else {

            for (int i = 0; i < AIDS.length; i++) {
                App can = new App();
                can.setAid(AIDS[i]);
                can = this.s.selectADF(transfer, can);
                if (can == null) {
                    continue;
                } else {
                    cardSupportApps.add(can);
                    break;
                }

            }
        }

        if (cardSupportApps.size() <= 0) {
            throw new Exception("no PSE in IC card");
        }

        // 使用终端支持的应用列表过滤卡支持的应用列表，选择优先级最大的，
        // 如果没有应用可供选择，流程终止
        // 采用全匹配的方法
        App selected = null;
//        List<App> filters = new ArrayList<App>();
//        for (App app : cardSupportApps) {
//            if (this._isSupportByTerminal(app.getAid())) {
//                filters.add(app);
//            }
//        }
        if (cardSupportApps.isEmpty()) {
            // 如果没有应用可供选择，流程终止
            throw new Exception("no app can use");
        }
        // 挑选优先级最高的应用
        selected = this._getMaxPriorityApp(cardSupportApps);
        // 使用应用的aid选择应用，获取该应用的详细信息，包括PDOL等
        selected = this.s.selectADF(transfer, selected);
        return selected;
    }

    /**
     * 重置交易属性
     */
    private void _resetTSITVR() {
        this.TSI = 0x00;
        for (int i = 0; i < 5; i++) {
            this.TVR[i] = 0x00;
        }
    }

    /**
     * 生成应用初始化所需要的PDOL数据
     * @param trade
     * @param icDatas
     * @param dol
     * @return
     */
    private byte[] _generateDOLData(Trade trade, Map<String, BelTLV> icDatas,
                                    DOL dol) {
        if(dol == null)
        {
            return null;
        }
        // 分配数据空间
        int totalLen = dol.getTotalLen();
        byte[] datas = new byte[totalLen];

        List<String> flags = dol.getFlags();
        List<Integer> lens = dol.getLens();
        int index = 0;
        for (int i = 0; i < flags.size(); i++) {
            String flag = flags.get(i);
            int len = lens.get(i);

            if ("9F7A".equals(flag)) {
                // 电子现金指示器
                System.arraycopy(this.ECI, 0, datas, index, len);
                index += len;

            } else if ("9F66".equals(flag)) {
                // 终端交易属性
                System.arraycopy(this.TTR, 0, datas, index, len);
                index += len;
            } else if ("9F02".equals(flag)) {
                // 交易金额，BCD编码
                // int fen = (int) (trade.getAmount().doubleValue() * 100 +
                // 0.5);
                int fen = (int) (trade.getAmount().doubleValue());
                String amount = FormatUtils.int2str(fen, len << 1, true, '0');
                System.arraycopy(FormatUtils.hexStringToByteArray(amount), 0, datas,
                        index, len);
                index += len;
            } else if ("9F03".equals(flag)) {
                // 其他金额，BCD编码
                byte[] amount = new byte[len];
                System.arraycopy(amount, 0, datas, index, len);
                index += len;
            } else if ("9F1A".equals(flag)) {
                // 国家代码
                System.arraycopy(this.country, 0, datas, index, len);
                index += len;
            } else if ("95".equals(flag)) {
                // 终端验证属性
                System.arraycopy(this.TVR, 0, datas, index, len);
                index += len;
            } else if ("5F2A".equals(flag)) {
                // 货币代码
                System.arraycopy(this.currency, 0, datas, index, len);
                index += len;
            } else if ("9A".equals(flag)) {
                // 交易日期
                System.arraycopy(FormatUtils.hexStringToByteArray(trade.getDate()),
                        0, datas, index, len);
                index += len;
            } else if ("9C".equals(flag)) {
                // 交易类型
                datas[index++] = trade.getType();
            } else if ("9F37".equals(flag)) {
                // 交易模数
                System.arraycopy(trade.getMagic(), 0, datas, index, len);
                index += len;
            } else if ("9F21".equals(flag)) {
                // 交易时间
                System.arraycopy(FormatUtils.hexStringToByteArray(trade.getTime()),
                        0, datas, index, len);
                index += len;
            } else if ("9F4E".equals(flag)) {
                // 商户名称
                byte[] merchant = new byte[len];
                System.arraycopy(merchant, 0, datas, index, len);
                index += len;
            } else if ("8A".equals(flag)) {
                // 授权响应码
                if (icDatas != null) {
                    System.arraycopy((byte[]) (icDatas.get("8A").getValues()),
                            0, datas, index, len);
                    index += len;
                }
            } else if ("DF60".equals(flag)) {
                System.arraycopy(this.EDF, 0, datas, index, len);
                index += len;
            }
        }
        return datas;
    }

    /**
     * 读取每个AEF文件的记录
     * @param transfer
     * @param card
     * @param sfi
     * @param start
     * @param end
     * @throws Exception
     */
    private void _readAEF(Transfer transfer, Card card, byte sfi, int start,
                          int end) throws Exception {
        for (int i = start; i <= end; i++) {
            BelTLV record = this.r.readRecord(transfer, sfi, i);
            card.setCardInfos(record);
        }
    }

    /**
     * PBOC借贷记，第二步：应用初始化
     *
     * @param transfer
     * @param app
     * @throws Exception
     */
    private Card _2InitApp(Transfer transfer, App app, Trade trade)
            throws Exception {
        // 重置TSI和TVR
        this._resetTSITVR();
        // 生成PDOL数据
        byte[] pdoldatas = this._generateDOLData(trade, null, app.getPdol());
        // 发送GPO命令
        Object option = this.g.getOption(transfer, pdoldatas);

        if(option == null)
        {
            return null;
        }
        if(option instanceof TradeOption)
        {
            TradeOption opt = (TradeOption) option;
            int aefNum = opt.getAEFNum();
            // 构造卡片对象，将应用AEF文件中的信息写入Card对象中
            Card card = new Card();
            for (int i = 0; i < aefNum; i++) {
                // 依次读取每一个文件
                byte sfi = opt.getSFI(i);
                int start = opt.getStartRecord(i);
                int end = opt.getEndRecord(i);

                this._readAEF(transfer, card, sfi, start, end);
            }
            card.setOption(opt);
            return card;
        }
        else if(option instanceof Object[])
        {
            Object[] opt = (Object[])option;
            Card card = (Card) opt[0];
            listBelTLV = (List<BelTLV>) opt[1];
            return card;
        }
        return null;
    }

    /**
     * PBOC借贷记，第三步：卡片数据认证（SAD或者DDA）
     * @throws Exception
     */
    private void _3CardDataVerify(/* Transfer transfer, Card card */)
            throws Exception {
        // 检查完毕，标记TSI脱机数据验证完成
        this.TSI |= 0x80;
    }

    /**
     * 生效日期检查
     *
     * @param card
     * @param trade
     * @return
     */
    private void _4createDateCheck(Card card, Trade trade) {
        if (card.getCreateDate() == null) {
            // IC卡缺少必要数据
            this.TVR[0] |= 0x20;
            return;
        }

        if (trade.getDate().compareTo(card.getCreateDate()) < 0) {
            // 终端将TVR中的“应用尚未生效”位设为‘1’
            this.TVR[1] |= 0x20;
        }
    }

    /**
     * 失效日期检查
     *
     * @param card
     * @param trade
     * @return
     */
    private void _4expireDateCheck(Card card, Trade trade) {
        if (card.getCreateDate() == null) {
            // IC卡缺少必要数据
            this.TVR[0] |= 0x20;
            return;
        }
        if ((trade.getDate().compareTo(card.getExpireDate())) > 0) {
            // 终端将TVR中的“应用已经失效”位设为‘1’
            this.TVR[1] |= 0x10;
        }
    }

    /**
     * 应用版本检查
     *
     * @param card
     * @return
     */
    private void _4versionCheck(Card card) {
        if (card.getVersion() == null) {
            // IC卡缺少必要数据
            this.TVR[0] |= 0x20;
            return;
        }
        if (!this.Version[0].equals(card.getVersion())
                && !this.Version[1].equals(card.getVersion())) {
            // 终端设置TVR中的“IC卡和终端应用版本不一致”位为‘1’
            this.TVR[1] |= 0x80;
        }
    }

    /**
     * 交易类型检查
     *
     * @param card
     * @param trade
     * @return
     */
    private void _4tradeTypeCheck(Card card, Trade trade) {
        if (card.getAUC() == null) {
            // IC卡缺少必要数据
            this.TVR[0] |= 0x20;
            return;
        }
        byte[] tradeTypeFlag = new byte[2];

        // if ((TradeType.CASH & trade.getType()) == TradeType.CASH) {
        // tradeTypeFlag[0] |= 0x80;
        // }
        // if ((TradeType.GOODS & trade.getType()) == TradeType.GOODS) {
        // tradeTypeFlag[0] |= 0x20;
        // }
        // if ((TradeType.SERVICE & trade.getType()) == TradeType.SERVICE) {
        // tradeTypeFlag[0] |= 0x08;
        // }
        // if ((TradeType.RETURNCASH & trade.getType()) == TradeType.RETURNCASH)
        // {
        // tradeTypeFlag[1] |= 0x80;
        // }
        if ((TradeType.DEBIT & trade.getType()) == TradeType.DEBIT) {
            tradeTypeFlag[0] |= 0x03;
        }
        if ((TradeType.QUERY & trade.getType()) == TradeType.QUERY) {
            tradeTypeFlag[0] |= 0x03;
        }
        if ((TradeType.TRANSFER & trade.getType()) == TradeType.TRANSFER) {
            tradeTypeFlag[0] |= 0x03;
        }
        if ((TradeType.NFCBAL & trade.getType()) == TradeType.NFCBAL) {
            tradeTypeFlag[0] |= 0x03;
        }
        // if ((TradeType.MANAGE & trade.getType()) == TradeType.MANAGE) {
        // tradeTypeFlag[0] |= 0x03;
        // }

        tradeTypeFlag[0] &= card.getAUC()[0];
        tradeTypeFlag[1] &= card.getAUC()[1];

        if ((tradeTypeFlag[0] | tradeTypeFlag[1]) == 0x00) {
            // 终端将TVR中的“卡片不允许所请求的服务”位设为‘1’
            this.TVR[1] |= 0x10;
        }
    }

    /**
     * PBOC借贷记，第四步：处理限制
     * @param card
     * @param trade
     * @throws Exception
     */
    private void _4TradeRestrict(Card card, Trade trade)
            throws Exception {
        // 应用版本号检查
        this._4versionCheck(card);
        // 交易类型检查
        this._4tradeTypeCheck(card, trade);
        // 生效日期检查
        this._4createDateCheck(card, trade);
        // 失效日期检查
        this._4expireDateCheck(card, trade);
    }

    /**
     * PBOC借贷记，第五步：持卡人认证
     * @param card
     * @param needPin
     * @throws Exception
     */
    private void _5CVM(Card card, boolean needPin)
            throws Exception {
        if (card.getCVM() == null) {
            // IC卡缺少必要数据
            this.TVR[0] |= 0x20;
            return;
        }
        // 标记输入联机pin
        if(needPin)
        {
            this.TVR[2] |= 0x04;
        }
        else
        {
            this.TVR[2] |= 0x00;
        }
        // 标记TSI持卡人验证完成
        this.TSI |= 0x40;
    }

    /**
     * 频度检查
     * @param card
     * @throws Exception
     */
    private void _6PinduCheck(Card card) throws Exception {
        // 获取上次联机应用交易序号寄存器(标签“9F13”)
        // 和应用交易计数器（ATC）(标签“9F36”)
        byte[] _tmp = this.gd.getData(transfer, "9F36");
        int atc = -1;
        if (_tmp != null) {
            atc = ((_tmp[0] & 0x000000FF) << 8) + (_tmp[1] & 0x000000FF);
        }
        _tmp = this.gd.getData(transfer, "9F13");
        int agc = -1;
        if (_tmp != null) {
            agc = ((_tmp[0] & 0x000000FF) << 8) + (_tmp[1] & 0x000000FF);
        }

        if (atc == -1 || agc == -1) {
            // 超过连续脱机交易次数上限&超过连续脱机交易次数下限
            this.TVR[0] |= 0x20;
            this.TVR[3] |= 0x60;
            return;
        }

        if (atc - agc > card.getOfflineTradeDown()) {
            // 超过连续脱机交易次数下限
            this.TVR[3] |= 0x40;
        }
        if (atc - agc > card.getOfflineTradeUp()) {
            // 超过连续脱机交易次数上限
            this.TVR[3] |= 0x20;
        }
        if (agc == 0) {
            // 新卡标识
            this.TVR[1] |= 0x08;
        }

    }

    /**
     * 异常文件检查
     *
     * @param card
     */
    private void _6ExcepFileCheck(Card card) {
        if (card.getPan() == null || "".equals(card.getPan())) {
            // IC卡缺少必要数据
            this.TVR[0] |= 0x20;
            return;
        }
    }

    /**
     * PBOC借贷记，第六步：终端风险管理
     * @param transfer
     * @param card
     * @throws Exception
     */
    private void _6terminalRiskManager(Transfer transfer, Card card)
            throws Exception {
        // 异常文件检查
        this._6ExcepFileCheck(card);
        // 要求强制联机处理
        this.TVR[3] |= 0x08;
        // 最低限额检查
        // int fen = (int) (trade.getAmount().doubleValue() * 100 + 0.5);
        // int fen = (int) (trade.getAmount().doubleValue());
        // if (fen > this.tradeMinAmountFen)
        // {
        // this.TVR[3] |= 0x80;
        // }
        // 频度检查
        if (card.getOfflineTradeDown() != -1 && card.getOfflineTradeUp() != -1) {
            this._6PinduCheck(card);
        }
        this.TSI |= 0x08;
    }

    /**
     * 检查TVR数据是否有相同位都是1的
     *
     * @param tvr1
     * @param tvr2
     * @return
     */
    private boolean _checkTVR(byte[] tvr1, byte[] tvr2) {
        byte result = 0;
        for (int i = 0; i < 5; i++) {
            result |= (tvr1[i] & tvr2[i]);
        }
        return (result != 0x00);
    }

    /**
     * PBOC借贷记，第七步：终端行为分析
     * @param card
     * @throws Exception
     */
    private void _7TerminalTradeDecide(Card card)
            throws Exception {
        // 授权码
        String authorizeCode = null;
        // 应用密文
        String ACtype = null;

        // 比较IAC-拒绝和TVR
        byte[] rejectIAC = card.getRejectIAC();
        if (rejectIAC == null) {
            rejectIAC = new byte[5];
            for (int i = 0; i < 5; i++) {
                rejectIAC[i] = 0x00;
            }
        }

        if (this._checkTVR(this.TVR, rejectIAC)) {
            // TVR和IAC-拒绝有重复的位为1
            authorizeCode = "Z1";
            ACtype = "AAC";
        } else {
            byte[] onlineIAC = card.getOnlineIAC();
            if (onlineIAC == null) {
                onlineIAC = new byte[5];
                for (int i = 0; i < 5; i++) {
                    onlineIAC[i] = (byte) 0xFF;
                }
            }
            if (this._checkTVR(this.TVR, onlineIAC)) {
                // TVR和IAC-联机有重复的位为1
                ACtype = "ARQC";
            } else {
                // 脱机拒绝
                authorizeCode = "Z1";
                ACtype = "AAC";
            }
        }

        trade.setACtype(ACtype);
        trade.setAuthorizeCode(authorizeCode);
    }

    /**
     * PBOC借贷记，第八步：卡片行为分析
     * @param transfer
     * @param card
     * @param trade
     * @return
     * @throws Exception
     */
    private AC _8CardTradeDecide(Transfer transfer, Card card, Trade trade)
            throws Exception {
        // 根据CDOL1生成第一次GenerateAC命令所需要的终端数据
        byte[] datas = this._generateDOLData(trade, null, card.getCdol1());
        // 从卡片获得应用密文
        AC a = this.ac.getAC(transfer, trade.getACtype(), datas);
        return a;
    }

    public String getDCData() {
        return this.getDCData(card, trade, Ac);
    }

    private String getDCData(Card card, Trade trade, AC ac) {

//		//判断是否为信用卡
//		if(appSelected != null && !TextUtils.isEmpty(appSelected.getAid()) && appSelected.getAid().equals(CREDIT_AID))
//		{
//			ClientEngine.nfcNeedPin = false;
//		}
//		else if(!ClientEngine.nfcNeedPin)
//		{
//			//判断是否在免密卡bin表中
//			String cardNo = card.getPan();
//			cardNo = cardNo.substring(0, 6);
//			if(!TextUtils.isEmpty(cardNo))
//			{
//				boolean found = false;
//				String[] cardbin = UtilForDataStorage.loadData(ClientEngine.engineInstance()
//						.getCurrentController(), "cardbin", new String[1]);
//				for(int i = 0;i < cardbin.length;i ++)
//				{
//					if(TextUtils.isEmpty(cardbin[i]) || cardbin[i].contains(cardNo))
//					{
//						found = true;
//						break;
//					}
//				}
//				ClientEngine.nfcNeedPin = !found;
//			}
//		}
//		try {
//			this._5CVM(PBOC.transfer, card/* , trade */, ClientEngine.nfcNeedPin);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		//更新是否免密，供JS使用
//		try {
//			Log.e("readNfcNeedPin", "saveLocal: " + ClientEngine.nfcNeedPin);
//			JSONObject data = new JSONObject();
//			data.put("key", "nfcNeedPin");
//			JSONObject value = new JSONObject();
//			value.put("nfcNeedPin", ClientEngine.nfcNeedPin);
//			data.put("value", value);
//			ClientEngine.engineInstance().saveLocal(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        Map<String, BelTLV> domains = new HashMap<String, BelTLV>();
        List<String> listKey = new ArrayList<String>();
        // 构造联机报文所需要的域

        if(ac != null)
        {
            // 应用密文
            BelTLV AC = new BelTLV("9F26", false, ac.getAc());
            domains.put("9F26", AC);

            // 密文信息
            BelTLV CID = new BelTLV("9F27", false, new byte[] { ac.getCID() });
            domains.put("9F27", CID);

            // 发卡行应用数据
            if (ac.getBankDatas() != null) {
                BelTLV bankData = new BelTLV("9F10", false, ac.getBankDatas());
                domains.put("9F10", bankData);
            }

            // ATC
            BelTLV ATC = new BelTLV("9F36", false, new byte[] {
                    (byte) ((ac.getAtc() & 0x0000FF00) >>> 8),
                    (byte) (ac.getAtc() & 0x000000FF) });
            domains.put("9F36", ATC);

            // AIP
            BelTLV AIP = new BelTLV("82", false, new byte[] {
                    card.getOption().getAIP(), 0x00 });
            domains.put("82", AIP);

            // 发卡行数据
            if (card.getCardIDInfos() != null) {
                BelTLV cardIDInfo = new BelTLV("9F63", false, card.getCardIDInfos());
                domains.put("9F63", cardIDInfo);
            }

            //84
            if(appSelected != null)
            {
                BelTLV beltlv = new BelTLV("84", false, FormatUtils.hexStringToByteArray(appSelected.getAid()));
                domains.put("84", beltlv);
            }
        }
        else if(listBelTLV != null && !listBelTLV.isEmpty())
        {
            for(int i = 0;i < listBelTLV.size();i ++)
            {
                BelTLV beltlv = listBelTLV.get(i);
                String flag = beltlv.getFlag();
                if(TextUtils.isEmpty(flag) || flag.equals("5F34") || flag.equals("57") || flag.equals("9F6C") || flag.equals("9F5D") || flag.equals("5F20"))
                {
                    continue;
                }
                domains.put(flag, beltlv);
            }

            for(int i = 0;i < TVR.length;i ++)
            {
                TVR[i] = 0;
            }

//			String flag = "9F26";
//			BelTLV beltlv = new BelTLV();
//			beltlv.setFlag(flag);
//			beltlv.setValues(Utils.hexStringToByteArray("48EB6796064C23DC"));
//			domains.put(flag, beltlv);

            String flag = "9F27";
            BelTLV beltlv = new BelTLV();
            beltlv.setFlag(flag);
            beltlv.setValues(FormatUtils.hexStringToByteArray("80"));
            domains.put(flag, beltlv);
        }

        // 模数
        BelTLV magic = new BelTLV("9F37", false, trade.getMagic());
        domains.put("9F37", magic);

        // TVR
        BelTLV tvr = new BelTLV("95", false, this.TVR);
        domains.put("95", tvr);

        // 交易日期
        BelTLV date = new BelTLV("9A", false, FormatUtils.hexStringToByteArray(trade
                .getDate()));
        domains.put("9A", date);

        // 交易类型
        BelTLV type = new BelTLV("9C", false, new byte[] { trade.getType() });
        domains.put("9C", type);

        // 交易金额
        int amountfen = (int) (trade.getAmount().doubleValue());
        BelTLV amount = new BelTLV("9F02", false,
                FormatUtils.hexStringToByteArray(FormatUtils.int2str(amountfen, 12, true,
                        '0')));
        domains.put("9F02", amount);

        // 币种
        BelTLV currency = new BelTLV("5F2A", false,
                FormatUtils.hexStringToByteArray("0156"));
        domains.put("5F2A", currency);

        // 国家代码
        BelTLV country = new BelTLV("9F1A", false,
                FormatUtils.hexStringToByteArray("0156"));
        domains.put("9F1A", country);

        // 其他金额
        BelTLV otheramount = new BelTLV("9F03", false,
                FormatUtils.hexStringToByteArray("000000000000"));
        domains.put("9F03", otheramount);

        // 终端属性
        BelTLV capability = new BelTLV("9F33", false, this.TerminalCapability);
        domains.put("9F33", capability);

        //可选信息子域

        // 交易时间
//		BelTLV time = new BelTLV("9F21", false,
//				Utils.hexStringToByteArray(trade.getTime()));
//		domains.put("9F21", time);

        // 主账号
//		String account = card.getPan();
//		if (account.length() % 2 == 1) {
//			account = account + "F";
//		}
//		BelTLV pan = new BelTLV("5A", false,
//				Utils.hexStringToByteArray(account));
//		domains.put("5A", pan);

        // 磁道2
//		String track = card.getTrack2();
//		if (track.length() % 2 == 1) {
//			track = track + "F";
//		}
//		BelTLV track2 = new BelTLV("57", false,
//				Utils.hexStringToByteArray(track));
//		domains.put("57", track2);

        // 过期日期
//		BelTLV expireDate = new BelTLV("5F24", false,
//				Utils.hexStringToByteArray(card.getExpireDate()));
//		domains.put("5F24", expireDate);



        //以下数据拷贝自POS机交易
        //9F35
        BelTLV beltlv = new BelTLV("9F35", false, FormatUtils.hexStringToByteArray("22"));
        domains.put("9F35", beltlv);

        //9F1E
//		beltlv = new BelTLV("9F1E", false, Utils.hexStringToByteArray("3832343539303839"));
        beltlv = new BelTLV("9F1E", false, FormatUtils.hexStringToByteArray("3444343138383537"));
        domains.put("9F1E", beltlv);

        //9F09
        beltlv = new BelTLV("9F09", false, FormatUtils.hexStringToByteArray(card.getVersion()));
        domains.put("9F09", beltlv);

        //9F41
//		beltlv = new BelTLV("9F41", false, card.getVersion());
//		domains.put("9F41", beltlv);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Iterator<Map.Entry<String, BelTLV>> it = domains.entrySet().iterator();
        while (it.hasNext()) {
            try {
                Map.Entry<String, BelTLV> entry = it.next();
                String tag = entry.getKey();
                BelTLV tlv = entry.getValue();

                if(listKey.contains(tag))
                {
                    continue;
                }
                out.write(tlv.toBytes());
                listKey.add(tag);
            } catch (Exception ex) {
            }
        }
        return FormatUtils.byteArrayToHexString(out.toByteArray());
    }

    /**
     * PBOC借贷记，第十步：发卡行认证
     *
     * @param transfer
     * @param datas
     */
    private boolean _10ExtenalAuthorize(Transfer transfer, byte[] datas) {
        // EXTERNAL AUTHENTICATE发卡行数据认证
        boolean result = this.ea.authorize(transfer, datas);
        if (!result) {
            this.TVR[4] |= 0x40;
        }
        // 终端将交易状态信息（TSI）的“发卡行认证已执行”位置为‘1’
        this.TSI |= 0x10;
        return result;
    }

    /**
     * PBOC借贷记，第十一步：交易结束处理
     *
     * @param transfer
     */
    private void _11EndTrade(Transfer transfer, Trade trade,
                             Card card, String authorizeCode) {
        // return tr;
        // TradeResult tr = new TradeResult();
        // tr.setSucc(true);
        // tr.setResult(tradeRes.getResult());
        // tr.setSettleDate(tradeRes.getSettleDate());
        // tr.setPaySeq(tradeRes.getPaySeq());
        // return tr;
        Map<String, BelTLV> icDatas = new HashMap<String, BelTLV>(); // 设置授权响应码
        BelTLV tlv = new BelTLV("8A", false, authorizeCode.getBytes());
        icDatas.put("8A", tlv);
        byte[] acDatas = this._generateDOLData(trade, icDatas, card.getCdol2()); // 生成第二个GenerateAC
        try {
            AC resultAC = this.ac.getAC(transfer, trade.getACtype(), acDatas);
//			if ("TC".equals(resultAC.getACtype())) {
//				tr.setOfflineSucc(true);
//			} else {
//				tr.setOfflineSucc(false);
//				tr.setOfflineErrMessage("trade is rejected");
//			}
//			return tr;
        } catch (Exception ex) {
//			tr.setOfflineSucc(false);
//			tr.setOfflineErrMessage("result unknown");
//			return tr;
        }

    }

    /**
     * 交易返回结果处理
     * @param transfer
     * @param trade
     * @param card
     * @param dcdata
     */
    private void tradeEnd(Transfer transfer, Trade trade,
                         Card card, String dcdata) {
        try {

            Map<String, BelTLV> icDatas = new HashMap<String, BelTLV>();
            if(!TextUtils.isEmpty(dcdata))
            {
                byte[] datas = FormatUtils.hexStringToByteArray(dcdata);
                ByteArrayInputStream in = new ByteArrayInputStream(datas);

                while (true)
                {
                    BelTLV tlv = BelTLV.parse(in);
                    if (tlv == null)
                    {
                        break;
                    }
                    icDatas.put(tlv.getFlag(), tlv);
                }
            }

            // OnlineTradeResponse tradeRes = this._9onlineService(this.service,
            // /*action, */card, trade, Ac, pin, params);
            String resultACtype = "";
            // 默认为：不能联机执行（脱机拒绝）
            String authorizeCode = null;
            if (icDatas.containsKey("91")) {
                // 授权验证码为发卡行验证数据的最后2字节
                byte[] _datas = (byte[]) (icDatas.get("91").getValues());
                authorizeCode = new String(new byte[] {
                        _datas[_datas.length - 2],
                        _datas[_datas.length - 1] });
            }
            boolean externalAuthorize = true;
            if (card.getOption().isSupportBV() && icDatas.containsKey("91")) {
                // 卡片支持发卡行验证并且应答报文中包含发卡行验证数据，
                // 则进行发卡行认证
                byte[] _datas = (byte[]) (icDatas.get("91").getValues());
                externalAuthorize = this._10ExtenalAuthorize(transfer,
                        _datas);
            }

            if (authorizeCode != null) {
                if (externalAuthorize
                        && ("00".equals(authorizeCode)
                        || "10".equals(authorizeCode) || "11"
                        .equals(authorizeCode))) {
                    // 如果联机批准，并且外部认证成功
                    resultACtype = "TC";
                } else {
                    // 否则，脱机拒绝
                    resultACtype = "AAC";
                    if ("00".equals(authorizeCode)
                            || "10".equals(authorizeCode)
                            || "11".equals(authorizeCode)) {
                        // 如果联机交易授权批准，则需要根据交易类型进行交易冲正
                        //TODO
                        return;
                    }
                }
            }
            trade.setACtype(resultACtype);
            // 交易结束处理
            if (authorizeCode != null) {
                this._11EndTrade(transfer, trade, card, authorizeCode);
            }
        } catch (Exception ex) {

        } finally {
            cleanTransfer();
        }
    }

    private Transfer initTransfer(Intent intent) throws Exception
    {
        if(intent == null)
        {
            return null;
        }
        String action = intent.getAction();
        if(TextUtils.isEmpty(action))
        {
            return null;
        }
        Tag tag = null;
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }

        if (tag == null) {
            return null;
        }
        IsoDep iso = IsoDep.get(tag);
        if (iso == null)
        {
            return null;
        }
        cleanTransfer();
        Thread.sleep(500);
        Transfer transfer = new IsoTransfer(iso);
        if(transfer == null)
        {
            return null;
        }
        try {
            transfer.connect();
            return transfer;
        } catch (IOException e) {
            return null;
        }
    }

    public Card readRecordsCard(Intent intent, byte tradeType, BigDecimal amount) throws Exception {
        synchronized (this) {
            transfer = initTransfer(intent);
            if(transfer == null)
            {
                return null;
            }
            try {
                // 应用选择
                appSelected = this._1selectApp(transfer);
                trade = getTrade(tradeType, amount, appSelected);
                //读取记录
                List<Record> list = new ArrayList<>();
                for (int i = 1; i < 11; i++) {
                    Record record = this.gd.getTradeReadRecord(transfer, "0"
                            + Integer.toHexString(i).toUpperCase());
                    if (record == null) {
                        continue;
                    }
                    list.add(record);
                }
                // 应用初始化
                card = this._2InitApp(transfer, appSelected, trade);
                // 读取电子现金余额及上限
                if (card != null) {
                    card.setRecords(list);
                    try {
                        byte[] data = this.gd.getData(transfer, "9F79");
                        int fen = Integer.parseInt(
                                FormatUtils.byteArrayToHexString(data), 10);
                        BigDecimal bigdec = BigDecimal.valueOf(fen / 100.0d);
                        card.setEcashBalance(bigdec);
                        data = this.gd.getData(transfer, "9F77");
                        fen = Integer
                                .parseInt(FormatUtils.byteArrayToHexString(data), 10);
                        bigdec = BigDecimal.valueOf(fen / 100.0d);
                        card.setEcashBalanceLimit(bigdec);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new Exception("_trade unknown error:" + ex.getMessage(),
                        ex);
            } finally {
                cleanTransfer();
            }
            return card;
        }
    }
}
