package com.huawei.holosens.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {


    // 简单手机号规则，11位，1开头
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";

    /*
     * 移动: 2G号段(GSM网络)有139,138,137,136,135,134,159,158,152,151,150,
     * 3G号段(TD-SCDMA网络)有157,182,183,188,187 147,178是移动TD上网卡专用号段.
     * 联通: 2G号段(GSM网络)有130,131,132,155,156 3G号段(WCDMA网络)有186,185,176
     * 电信: 2G号段(CDMA网络)有133,153 3G号段(CDMA网络)有189,180,177
     */
    public static final String REGEX_MOBILE_YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([7]{1}[8]{1})|([4]{1}[7]{1}))[0-9]{8}$";
    public static final String REGEX_MOBILE_LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([7]{1}[6]{1})|([8]{1}[56]{1}))[0-9]{8}$";
    public static final String REGEX_MOBILE_DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([7]{1}[7]{1})|([8]{1}[109]{1}))[0-9]{8}$";


    /**
     * 中国移动：China Mobile
     *13[4-9],147,148,15[0-2,7-9],165,170[3,5,6],172,178,18[2-4,7-8],19[5,7,8]
     */
    public static final String REGEX_MOBILE_YD_2 = "^((13[4-9])|(14[7-8])|(15[0-2,7-9])|(165)|(178)|(18[2-4,7-8])|(19[5,7,8]))\\d{8}|(170[3,5,6])\\d{7}$";

    /**
     * 中国联通：China Unicom
     * 130,131,132,145,146,155,156,166,167,170[4,7,8,9],171,175,176,185,186,196
     */
    public static final String REGEX_MOBILE_LT_2 = "^((13[0-2])|(14[5,6])|(15[5-6])|(16[6-7])|(17[1,5,6])|(18[5,6])|(196))\\d{8}|(170[4,7-9])\\d{7}$";

    /**
     * 中国电信：China Telecom
     * 133,149,153,162,170[0,1,2],173,174[0-5],177,180,181,189,19[0,1,3,9]
     */
    public static final String REGEX_MOBILE_DX_2 = "^((133)|(149)|(153)|(162)|(17[3,7])|(18[0,1,9])|(19[0,1,3,9]))\\d{8}|((170[0-2])|(174[0-5]))\\d{7}$";

    /**
     * 中国广电：China Broadcasting Network
     * 192
     */
    public static final String REGEX_MOBILE_GD = "^((192))\\d{8}$";

    // 邮箱正则
    public static final String REGEX_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    // IP正则
    public static final String REGEX_IP = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    // 域名正则
    public static final String REGEX_DOMAIN = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";

    // 中文正则
    public static final String REGEX_CHINESE = "[\u4e00-\u9fa5]";

    // 数字正则
    public static final String REGEX_NUMBER = "^[0-9]+$";

    // 大写字母正则
    public static final String REGEX_UPPER_LETTER = "^[A-Z]+$";

    // 小写字母正则
    public static final String REGEX_LOWER_LETTER = "^[a-z]+$";

    // 特殊符号正则
    public static final String REGEX_SYMBOL = "[`~!@#$%^&*()-_=+\\|[{}];:'\",<.>/?]";


    /**
     * 判断是否匹配正则
     * */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 判断是否包含正则
     * */
    public static boolean find(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.compile(regex).matcher(input).find();
    }

    /**
     * 替换正则匹配的第一部分
     * */
    public static String getReplaceFirst(String regex, CharSequence input, String replacement) {
        if (input == null) return "";
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * 替换正则匹配的所有部分
     * */
    public static String getReplaceAll(String regex, CharSequence input, String replacement) {
        if (input == null) return "";
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }


    /**
     * 检查是否是中文
     * */
    public static boolean isChinese(CharSequence input) {
        return isMatch(REGEX_CHINESE, input);
    }

    /**
     * 检查是否是中文
     * */
    public static boolean isContainChinese(CharSequence input) {
        return find(REGEX_CHINESE, input);
    }

    /**
     * 检查是否是简单的手机号
     * */
    public static boolean isMobileSimple(CharSequence input) {
        return isMatch(REGEX_MOBILE_SIMPLE, input);
    }

    /**
     * 检查是否是准确的手机号
     * */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(REGEX_MOBILE_YD, input) || isMatch(REGEX_MOBILE_LT, input) || isMatch(REGEX_MOBILE_DX, input);
    }

    /**
     * 检查是否是手机号，目前用的就是这个判断，只判断11位
     * */
    public static boolean isMobile(CharSequence input) {
        return isMatch("^\\d{11}$", input);
    }

    public static boolean containsEmoji(String source) {
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /* 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {

        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 检查是否是邮箱
     * */
    public static boolean isEmail(CharSequence input) {
        return isMatch(REGEX_EMAIL, input);
    }

    /**
     * 检查是否是合法IP，只有CloudSEE用到了这个方法
     * */
    public static boolean isIP(CharSequence input) {
        return isMatch(REGEX_IP, input) && input.length() >= 7 && input.length() <= 15;
    }

    /**
     * 检查是否是域名，只有CloudSEE用到了这个方法
     * */
    public static boolean isDomain(CharSequence input) {
        return isMatch(REGEX_DOMAIN, input);
    }

    /**
     * 检查ip地址格式是否正确
     *
     * @param ipAddress
     * @return
     * @author
     */
    public static boolean checkIPAdress(String ipAddress) {
        return ipAddress
                .matches("^(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])$");
    }

    /**
     * 检查端口号格式是否正确
     *
     * @param portNum
     * @return
     */
    public static boolean checkPortNum(String portNum) {
        if (portNum.length() < 1 || portNum.length() > 5) {
            return false;
        }
        for (int i = 0; i < portNum.length(); i++) {
            char c = portNum.charAt(i);
            if (c > '9' || c < '0') {
                return false;
            }
        }
        return !(Integer.valueOf(portNum) <= 0
                && Integer.valueOf(portNum) > 65535);
    }


    /**
     * 验证用户昵称，1-20位，可以有中文
     * */
    public static boolean checkNickNameGBK(String str) {
        boolean flag = false;
        try {
            byte[] b = str.getBytes("GBK");
            str = new String(b, "GBK");
            Pattern pattern = Pattern
                    .compile("^[A-Za-z0-9_.()\\+\\-\\u4e00-\\u9fa5]{1,20}$");

            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches() && 20 >= str.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 校验是否为纯数字
     * @param text
     * @return
     */
    public static boolean checkNum(String text) {
        return !TextUtils.isEmpty(text) && isMatch(REGEX_NUMBER, text);
    }

    /**
     * 用户注册密码，登录密码的正则验证
     * @param username 因为用户密码不可与用户名相同，所以需要传用户名判断
     * @param userPwd
     * @return
     */
    public static boolean checkUserPwd(String username, String userPwd) {
        if (TextUtils.isEmpty(userPwd)) {
            return false;
        }
        if (TextUtils.equals(username, userPwd)) {
            return false;
        }
        if (TextUtils.equals(new StringBuffer(username).reverse().toString(), userPwd)) {
            return false;
        }

        //查询是否包含空格
        if (find("\\s+", userPwd)) {
            return false;
        }
        //2.0密码校验比较严格 需要校验大小写特殊字符数字四种中的三种
        int count = 0;
        //大写字母：
        String regex1 = "^.*(?=.*[A-Z]).*$";
        if (isMatch(regex1, userPwd)) {
            count++;
        }
        //小写字母：
        String regex2 = "^.*(?=.*[a-z]).*$";
        if (isMatch(regex2, userPwd)) {
            count++;
        }
        //数字：
        String regex3 = "^.*(?=.*\\d).*$";
        if (isMatch(regex3, userPwd)) {
            count++;
        }
        //特殊字符：
        String regex4 = "^.*(?=.*[`~!@#$%^&*()\\\\-_=+\\\\|[{}];:'\\\",<.>/?]).*$";
        if (isMatch(regex4, userPwd)) {
            count++;
        }
        if (count >= 3) {
            return true;
        } else {
            return false;
        }

//        String regex = "^(?![a-z]+$)(?![A-Z]+$)(?![0-9]+$)(?![^(a-zA-Z0-9)]+$).{8,}$";
        //2.0 需要校验大小写特殊字符数字四种中的三种
//        String regex = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,}$";

//        return isMatch(regex, userPwd);
    }

    /**
     * 验证设备昵称
     * 0-20位可以包含中文
     * */
    public static boolean checkDevNickName(String str) {
        boolean flag = false;
        try {
            byte[] b = str.getBytes("GBK");
            str = new String(b, "GBK");
            Pattern pattern = Pattern
                    .compile("^[A-Za-z0-9_.()\\+\\-\\u4e00-\\u9fa5]{0,20}$");

            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches() && 20 >= str.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 验证第三方设备昵称
     * 1-20位可以包含中文
     * */
    public static boolean checkThirdDevNickName(String str) {
        boolean flag = false;
        try {
            byte[] b = str.getBytes("UTF-8");
            str = new String(b, "UTF-8");
            Pattern pattern = Pattern
                    .compile("^[A-Za-z0-9_.()\\+\\-\\u4e00-\\u9fa5]{1,20}$");

            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches() && 20 >= str.getBytes().length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 验证设备用户名
     *
     * @param str
     * @return
     */
    public static boolean checkDeviceUsername(String str) {
        boolean flag = false;
        try {
            byte[] b = str.getBytes("UTF-8");
            str = new String(b, "UTF-8");
            Pattern pattern = Pattern.compile("^[A-Za-z0-9_.()\\+\\-]{1,16}$");
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // if(0 < str.length() && 16 > str.length()){
        // flag = true;
        // }

        return flag;

    }

    /**
     * 添加设备时验证密码长度可以为0，最长12位不能包含中文
     *
     * @param devPwd
     * @return
     */
    public static boolean checkAddDevPwd(String devPwd) {
        boolean right = false;
        try {
            byte[] byteArray = devPwd.getBytes("UTF-8");
            devPwd = new String(byteArray, "UTF-8");
//            Pattern pattern = Pattern.compile("[^\u4e00-\u9fa5]{0,12}$");
            Pattern pattern = Pattern.compile("^[A-Za-z0-9_.()\\+\\-]{0,12}$");
            Matcher matcher = pattern.matcher(devPwd);
            if (matcher.matches()) {
                right = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return right;
    }

    /**
     * 直接验证设备的密码，走云视通，最长12位不能包含中文
     *
     * @param devPwd
     * @return
     */
    public static boolean checkDevPwd(String devPwd) {
        boolean right = false;
        try {
            byte[] byteArray = devPwd.getBytes("UTF-8");
            devPwd = new String(byteArray, "UTF-8");
//            Pattern pattern = Pattern.compile("[^\u4e00-\u9fa5]{0,12}$");
            Pattern pattern = Pattern.compile("^[A-Za-z0-9_.()\\+\\-]{1,12}$");
            Matcher matcher = pattern.matcher(devPwd);
            if (matcher.matches()) {
                right = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return right;
    }


    /**
     * 检查是否是云视通号，包括1.0和2.0
     * */
    public static boolean checkYSTNum(String ystNum) {

        if(ystNum==null||ystNum.length()==0){
            return false;
        }
        boolean result;
        char c = ystNum.charAt(0);
        if (c <= '9' && c >= '0') {
            result = checkYSTNumOct(ystNum); // 2.0
            Log.e("checkYSTNum","ystNum="+ystNum+";OctRes="+result);
        } else {
            result = checkYSTNumOld(ystNum); // 1.0
            Log.e("checkYSTNum","ystNum="+ystNum+";OldRes="+result);
        }
        return result;
    }

    /**
     * 云视通1.0 验证云通号 最大是 2的31次方最大是2147483648
     *
     * @param ystNum
     * @return
     */
    public static boolean checkYSTNumOld(String ystNum) {
        boolean flag = true;
        try {
            int firstNumIndex;
            for (firstNumIndex = 0; firstNumIndex < ystNum.length();
                 firstNumIndex++) {
                char c = ystNum.charAt(firstNumIndex);
                if (c <= '9' && c >= '0') {
                    break;
                }
            }
            String group = ystNum.substring(0, firstNumIndex);
            String yst = ystNum.substring(firstNumIndex);
            for (int mm = 0; mm < group.length(); mm++) {
                char c = ystNum.charAt(mm);
                if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {

                } else {
                    flag = false;
                }
            }

            for (int i = 0; i < yst.length(); i++) {
                char c = yst.charAt(i);
                if ((c >= '0' && c <= '9')) {

                } else {
                    flag = false;
                }
            }
            int ystValue = "".equals(yst) ? 0 : Integer.parseInt(yst);
            if (firstNumIndex > 4 || firstNumIndex <= 0 || ystValue <= 0) {
                flag = false;
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        return flag;

    }

    /**
     * 云视通2.0 验证云通号 数字开头，字母数字组合
     *
     * @param ystNum
     * @return
     */
    public static boolean checkYSTNumOct(String ystNum) {
        boolean flag = false;
        try {
//            flag = Jni.octVerifyEid(ystNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;

    }

    /**
     * 已添加到设备列表的设备号码判断是否云视通2.0的设备(无需验证号码规则)
     * CloudSEE使用
     * @param ystNum
     * @return
     */
    public static boolean isOctDev(String ystNum){
        boolean result = false;
        char c = ystNum.charAt(0);
        if (c <= '9' && c >= '0') {
            result = true;
        }
        Log.e("isOctDev","ystNum="+ystNum+";isOctDev="+result);
        return result;
    }

    /**
     * 已添加到设备列表的设备号码判断是否云视通2.0的设备(无需验证号码规则)
     * CloudSEE使用
     * @param ystNum
     * @return
     */
    public static boolean isOctSoovviDev(String ystNum){
        boolean result = false;
        char c = ystNum.charAt(0);
        if (c <= '9' && c >= '0') {
            char c2 = ystNum.charAt(1);
            char c3 = ystNum.charAt(2);

            result = c2 == '2' && c3 == '4';

        }
        return result;
    }

    /**
     * 校验国标设备的用户名
     *
     * @param userName
     * @return
     */
    public static boolean checkDeviceAccount(String userName) {
        String regex1 = "^.*(?=.*[A-Z]).*$";
        String regex2 = "^.*(?=.*[a-z]).*$";
        String regex3 = "^.*(?=.*\\d).*$";
        String regex4 = "^.*(?=.*[-]).*$";
        boolean isMatch = true;
        for (int i = 0; i < userName.length(); i++) {
            char cr = userName.charAt(i);
            if (isMatch(regex1, String.valueOf(cr)) || isMatch(regex2, String.valueOf(cr))
                    || isMatch(regex3, String.valueOf(cr)) || isMatch(regex4, String.valueOf(cr))) {
                continue;
            } else {
                isMatch = false;
            }

        }
        return isMatch;
    }

    /**
     * 校验设备密码：数字，字母，特殊字符中的两种
     *
     * @param devicePwd
     * @return
     */
    public static boolean checkDevicePwd(String devicePwd) {
//2.0密码校验比较严格 需要校验大小写特殊字符数字四种中的三种
        int count = 0;
        //大写字母：
        String regex1 = "^.*(?=.*[A-Z]).*$";
        String regex2 = "^.*(?=.*[a-z]).*$";
//        if (isMatch(regex1, devicePwd)) {
//            count++;
//        }
//        if (isMatch(regex2, devicePwd)) {
//            count++;
//        }
        //数字：
//        String regex3 = "^.*(?=.*\\d).*$";
//        if (isMatch(regex3, devicePwd)) {
//            count++;
//        }
        //特殊字符：
        String regex4 = "^.*(?=.*[`~!@#$%^&*()\\\\-_=+\\\\|[{}];:'\\\",<.>/?]).*$";
//        if (isMatch(regex4, devicePwd)) {
//            count++;
//        }
//        if (count >= 3) {
//            return true;
//        } else {
//            return false;
//        }
        boolean isMatch = true;
        for (int i = 0; i < devicePwd.length(); i++) {
            char cr = devicePwd.charAt(i);
            if (isMatch(regex1, String.valueOf(cr)) || isMatch(regex2, String.valueOf(cr))
                    || isMatch(regex4, String.valueOf(cr))) {
                continue;
            } else {
                isMatch = false;
            }

        }
        return isMatch;
    }

    public static boolean checkDevicePwd2(String password) {
        int count = 0;
        //大写字母：
        String regex1 = "^.*(?=.*[A-Z]).*$";
        String regex2 = "^.*(?=.*[a-z]).*$";
        if (isMatch(regex1, password)) {
            count++;
        }
        if (isMatch(regex2, password)) {
            count++;
        }
        //数字：
        String regex3 = "^.*(?=.*\\d).*$";
        if (isMatch(regex3, password)) {
            count++;
        }
        //特殊字符：
//        String regex4 = "^.*(?=.*[`~!@#$%^&*()\\\\-_=+\\\\|[{}];:'\\\",<.>/?]).*$";
//        if (isMatch(regex4, password)) {
//            count++;
//        }
        if (count >= 2) {
            return true;
        } else {
            return false;
        }
    }
}
