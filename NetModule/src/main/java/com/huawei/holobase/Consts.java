package com.huawei.holobase;


public class Consts {
    public static final String md5Salt = "1H8OoEUF";
    private static final String serverPathHeader = "https://";
    //private static final String serverPathIPPort = "119.3.210.177";        //内网
    private static final String serverPathIPPort = "58.56.25.110:19090";      //外网

//    public static String serverPath = serverPathHeader + serverPathIPPort;
    /* public static String serverPath = Utils.getApp().getResources().getString(R.string.server_path)*/

    /* public static String serverPath ="https://121.36.96.239";*/
    //    eums
    public static final String login = "/v2/eums/login";
    public static final String renewalToken = "/v2/eums/renewal_token";
    public static final String logout = "/v2/eums/logout";
    public static final String verifyCode = "/v2/eums/verification_code";
    public static final String register = "/v2/eums/register";
    public static final String forgetPwdw = "/v2/eums/forget_pwd";
    public static final String getAppVersion = "/v2/eums/app_last_version";
    public static final String getDownloadPath = "/v1/ums/get_download_url";
    public static final String getAccountInfo = "/v2/eums/get_own_user";
    public static final String modifyNickName = "/v2/eums/modify_own_nickname";
    public static final String modifyPassword = "/v2/eums/modify_pwd";
    public static final String modifyUserOrg = "/v2/eums/modify_user_org";
    public static final String addUserOrg = "/v2/eums/add_user_org";
    public static final String getTopUserOrg = "/v2/eums/get_top_user_org";
    public static final String getUserOrg = "/v2/eums/get_user_org";
    public static final String getUserOrgFilter = "/v2/eums/get_user_org_filter";
    public static final String inputUserInfo = "/v2/eums/input_user_info";
    public static final String getAdminTotal = "/v2/eums/get_admin_total";
    public static final String getUser = "/v2/eums/get_user";
    public static final String moveUserOrg = "/v2/eums/move_user_org";
    public static final String deleteEnterpriseUser = "/v2/eums/delete_enterprise_user";
    public static final String deleteUserOrg = "/v2/eums/del_user_org";
    public static final String getUserPower = "/v2/eums/get_user_power";
    public static final String getAllUserDevices = "/v2/eudms/devices/get_user_devices";
    public static final String addDevicesToServer = "/v2/eudms/devices/add_user_devices";

    public static final String queryUserOrg = "/v2/eums/query_user_org";
    public static final String selectUserOrg = "/v2/eums/select_user_org";
    public static final String checkUserExist = "/v2/eums/is_user_exist";
    public static final String getEnterpriseInfo = "/v2/eums/get_app_enterprise_info";
    public static final String queryAccountStatus = "/v2/eums/query_account_status";
    public static final String delDevOrgPower = "/v2/eums/del_device_org_power";
    public static final String delDevPower = "/v2/eums/del_device_power";
    public static final String reInviteUser = "/v2/eums/reinvite_user";
    public static final String getEnterpriseList = "/v2/eums/get_enterprise_list_app";
    public static final String queryOrgWholePath = "/v2/eums/query_whole_path";
    public static final String modifyUserInfo = "/v2/eums/modify_user_info";
    public static final String getInviteList = "/v2/eums/get_enterprise_invite_list";
    public static final String getInviteInfo = "/v2/eums/get_invite_enterprise_info";
    public static final String joinEnterprise = "/v2/eums/join_enterprise";
    public static final String refuseEnterprise = "/v2/eums/reject_enterprise";
    public static final String qrCodeLogin = "/v2/eums/qr_code_login";


    //    eudms
    public static final String addDevOrg = "/v2/eudms/devices/add_device_org";
    public static final String editDevOrg = "/v2/eudms/devices/edit_device_org";
    public static final String getDevOrg = "/v2/eudms/devices/get_device_org";
    public static final String delDevOrg = "/v2/eudms/devices/delete_device_org";
    public static final String getUperDevOrg = "/v2/eudms/devices/get_uper_device_org";
    public static final String addDev = "/v2/eudms/devices/add_gb28181_device";
    public static final String addHwDev = "/v2/eudms/devices/add_holo_device";
    public static final String getGB28181DevDetail = "/v2/eudms/devices/get_gb28181_device_detail";
    public static final String getHwDevDetail = "/v2/eudms/devices/get_holo_device_detail";
    public static final String editDevDetail = "/v2/eudms/devices/edit_device";
    public static final String deleteDev = "/v2/eudms/devices/delete_device";
    public static final String getGB28181NvrChannel = "/v2/eudms/devices/get_gb28181_nvr_channel";
    public static final String queryNvrChannel = "/v2/eudms/devices/query_channel_info_fuzzy";
    public static final String getHWNvrChannel = "/v2/eudms/devices/get_holo_nvr_channel";
    public static final String hwChannelInfo = "/v2/eudms/devices/get_holo_channel_info";
    public static final String getOrgByParentId = "/v2/eudms/devices/get_org_by_parent_id";
    public static final String moveDevOrg = "/v2/eudms/devices/move_device_org";
    public static final String moveDevs = "/v2/eudms/devices/move_devices";
    public static final String getChildOrg = "/v2/eudms/devices/get_child_org";
    public static final String editDevRole = "/v2/eudms/devices/edit_device_role";
    public static final String delSingleOperator = "/v2/eudms/devices/del_single_operator";
    public static final String delSingleDeviceOrg = "/v2/eudms/devices/del_single_device_org";
    public static final String queryDeviceAndOrg = "/v2/eudms/devices/query_device_and_org";
    public static final String queryDevWholePath = "/v2/eudms/devices/query_whole_path";
    public static final String delDeviceOrgPower = "/v2/eudms/devices/del_device_org_power";
    public static final String delDevicePower = "/v2/eudms/devices/del_device_power";
    public static final String editDeviceInfo = "/v2/eudms/devices/edit_gb28181_device_app";
    public static final String bindAssociate = "/v2/eudms/devices/bind_associate";
    public static final String releaseAssociate = "/v2/eudms/devices/release_associate";
    public static final String connectGB28181DeviceJvmp = "/v2/eudms/devices/get_gb28181_livestream";
    public static final String connectHWDeviceJvmp = "/v2/eudms/devices/get_holo_livestream";
    public static final String queryRecordList = "/v2/eudms/devices/query_record_list";
    public static String queryCloudRecordList = "/v2/eudms/devices/query_csg_record_list";
    public static final String requestVodStream = "/v2/eudms/devices/request_vod_stream";
    public static final String requestCloudVodStream = "/v2/eudms/devices/get_csps_recording_playback";
    public static final String requestLocalVodStream = "/v2/eudms/devices/get_gb28181_recording_playback";
    public static final String devSendCmd = "/v2/eudms/send_cmd/enterprise_id/device_id/channel_id/method_name";
    public static final String ptzDevSendCmd = "/v2/eudms/send_cmd/enterprise_id/device_id/channel_id/ptz/";
    public static final String operateCameraState = "/v2/eudms/devices/operate_camera_state";
    public static final String getDeviceAndOrgList = "/v2/eudms/devices/get_device_and_org_list";
    public static final String addSingleOperator = "/v2/eudms/devices/add_single_operator";
    public static final String addFavorite = "/v2/eudms/devices/add_favorite";
    public static final String delFavorite = "/v2/eudms/devices/del_favorite";
    public static final String getFavoriteList = "/v2/eudms/devices/get_favorite_list";
    public static final String getFavoriteStatus = "/v2/eudms/devices/get_favorite_status";
    public static final String getDeviceSip = "/v2/eudms/devices/get_device_sip";


    public static final String addView = "/v2/eudms/devices/add_view";
    public static final String modifyView = "/v2/eudms/devices/modify_view";
    public static final String deleteView = "/v2/eudms/devices/del_view";
    public static final String getViews = "/v2/eudms/devices/get_view_list";
    public static final String deleteViewChannel = "/v2/eudms/devices/del_view_channel";

    //ptz
    public static final String getPtzControlToken = "/v2/eudms/ptz/get_control_token";
    public static final String releasePtzControlToken = "/v2/eudms/ptz/release_control_token";


    //old

    public static final String addUser = "/v1/ums/add_user";
    public static final String getUserGroup = "/v1/ums/get_user_groups";
    public static final String getUsersByGroup = "/v1/ums/get_group_users";
    public static final String deleteGroup = "/v1/ums/del_group";
    public static final String deleteUser = "/v1/ums/del_user";
    public static final String modifyUser = "/v1/ums/modify_user";
    public static final String resetUserPwd = "/v1/ums/reset_user_password";
    public static final String userMove = "/v1/ums/move_user_group";
    public static final String getSearchUser = "/v1/ums/get_target_user";
    public static final String modifyGroup = "/v1/ums/modify_group";


    //    udms
    public static final String getDevGroup = "/v1/udms/get_group";
    public static final String getDevTree = "/v1/udms/get_group_and_device_tree";
    public static final String feedback = "/v2/feedback/upload";
    public static final String addDevGroup = "/v1/udms/add_group";
    public static final String deleteDevGroup = "/v1/udms/del_group";
    public static final String editDevGroup = "/v1/udms/modify_group";
    public static final String getDevsByGroup = "/v1/udms/get_devices";
    public static final String addDevice = "/v1/udms/add_device";
    public static final String searchDev = "/v1/udms/get_target_device";
    public static final String devMove = "/v1/udms/move_device_group";
    //    public static final String deleteDev = "/v1/udms/del_devices";
    public static final String getNrvChannels = "/v1/udms/get_nvr_channel";
    public static final String getDevInfo = "/v1/udms/get_device_info";
    public static final String isDevHtmlExit = "/v1/udms/is_device_html_exist";
    public static final String getDevType = "/v1/udms/get_device_type";
    public static final String getChannelInfo = "/v1/udms/get_channel_info";
    public static final String addDevices2User = "/v1/udms/add_user_devices";
    public static final String getUserDevices = "/v1/udms/get_user_device";
    public static final String getMts = "/v1/udms/do_connect_device";
    public static final String getMtsJvmp = "/v1/udms/do_connect_device_jvmp";
    public static final String getFavor = "/v1/udms/get_favorite_channels";
    public static final String addFavor = "/v1/udms/add_favorite";
    public static final String delFavor = "/v1/udms/del_favorite";
    //    public static final String addView = "/v1/udms/add_view";
    public static final String getChannelByid = "/v1/udms/get_view_channels";
    public static final String modifyViewChannels = "/v1/udms/modify_view_channels";
    //ptz
    //share
    public static final String getShareSendList = "/v1/udms/share/get_own_share";
    public static final String getShareReceiveList = "/v1/udms/share/get_others_share";
    public static final String checkShareUser = "/v1/udms/share/is_user_available";
    public static final String getShareTree = "/v1/udms/share/get_share_tree";
    public static final String addShare = "/v1/udms/share/add_share";
    public static final String modifyShare = "/v1/udms/share/modify_share";
    public static final String addSingleShare = "/v1/udms/share/single_channel_share";
    public static final String cancelShare = "/v1/udms/share/cancel_share";
    public static final String dealShare = "/v1/udms/share/deal_share";
    public static final String deleteShare = "/v1/udms/share/delete_share";
    public static final String getShareDetail = "/v1/udms/share/get_single_share_detail";
    public static final String checkShareExist = "/v1/udms/share/verify_share_exist";
    public static final String checkShareEnable = "/v1/udms/share/verify_user_can_share";
    public static final String getLimit = "/v1/udms/get_limit";

    //  push
    public static final String updateToken = "/v1/mns/upload_token";
    public static final String getAlarmList = "/v1/uac/%s/query_history";
    public static final String getAlarmUnreadCount = "/v1/uac/msg/get_unread_total";
    public static final String deleteAlarm = "/v1/uac/%s/delete";
    public static final String deleteAllAlarm = "/v1/uac/%s/delete_all";
    public static final String modifyAlarmStatus = "/v1/uac/%s/update_read_status";
    public static final String modifyAllAlarmStatus = "/v1/uac/%s/update_read_status_all";
    public static final String getAlarmInfo = "/v1/uac/%s/get_alarm_info";
    public static final String getAlarmPic = "/v1/uac/%s/get_alarm_pic_download_url";
    public static final String getPushSwitch = "/v1/mns/get_switch";
    public static final String modifyPushSwitch = "/v1/mns/modify_switch";

    //    信道
    public static final String sendCmd = "/v1/udms/send_cmd/device_id/channel_id";




    public static final String TAG = "bofangsdk";
    //todo sdk

    /* public static String serverPath ="https://124.70.27.149";
     public static  final String userId ="210666660220200728180907";
     public static  final String token ="20200221-2.qi8hSGH12QQZyNUIv/w7DcSpCiliLK3mVOoM+frsOCNI/FoaZIpihWtT+LELPgsCvbObuETUlRx+REPLACENNNNNN_REPLACENNNNNN_REPLACENNNNNNkEzU9Sj8hx63U+yKnK3ml1/dgCEnHu2m6lIf21LM2ZiAy2Q6J7xlfLLfPxeAvMks+XLUx6CfFJF+REPLACENNNNNN_REPLACENNNNNN_REPLACENNNNNNW+gEqXDNEohV5KS19fqFCNY4WhOrqKNRRQlz9Lfo";
   */
//    public static String serverPath = "https://124.70.54.227";
    public static String serverPath = "https://121.36.77.165";
//    public static final String userId = "124160825720200807211707";
    public static final String userId = "174367170720210126145700";
//    public static final String token =
//            "148814510820200807212129DELIMITER_DELIMITER_DELIMITER1597114610833DELIMITER_DELIMITER_DELIMITER20200221-2.h9WUUGmLSTZcvCAbf9ivWcgHyRNJUE9D3pDz3ey7m72tmf2i4WRB2HZZO/1RlrJKKzQ24+eYW8RJREPLACENNNNNN_REPLACENNNNNN_REPLACENNNNNNitFpLBWRD6l/3bGJCMkn8HP+wpcJUGssPxa2sHWQnIdZPe+Ry+XmMxdxLjIVFWaa1ylwamFzyX4RREPLACENNNNNN_REPLACENNNNNN_REPLACENNNNNNnQEmpJFLxZeW2UYmqqVPaufGzeJD53QblBkDweqll1EbPeB+iKpUCVFqp0C1sA==";

//    public static final String token = MySharedPreference.getString(TOKEN);
//    public static final String ak = "148814510820200807212129";
    public static final String ak = "178661947120210128195511";
//    public static final String sk = "148814510820200807212129d5d3f5372ee94e70bc6a3e429f7fde54";
    public static final String sk = "178661947120210128195511693dc92939ac43a9ac5ee834bfb701e7";

//    public static final String userId = "98101670120200804205859";
//    public static final String token =
//            "148814510820200807212129DELIMITER_DELIMITER_DELIMITER1597114610833DELIMITER_DELIMITER_DELIMITER20200221-2.h9WUUGmLSTZcvCAbf9ivWcgHyRNJUE9D3pDz3ey7m72tmf2i4WRB2HZZO/1RlrJKKzQ24+eYW8RJREPLACENNNNNN_REPLACENNNNNN_REPLACENNNNNNitFpLBWRD6l/3bGJCMkn8HP+wpcJUGssPxa2sHWQnIdZPe+Ry+XmMxdxLjIVFWaa1ylwamFzyX4RREPLACENNNNNN_REPLACENNNNNN_REPLACENNNNNNnQEmpJFLxZeW2UYmqqVPaufGzeJD53QblBkDweqll1EbPeB+iKpUCVFqp0C1sA==";
//    public static final String ak = "37462830920200801192924";
//    public static final String sk = "374628309202008011929244912baded2b2407c8d26aea6ae85aa74";

    //获取设备列表
    public static final String deviceList = "/v1/{user_id}/devices";
    public static final String channelList = "/v1/{user_id}/channels";

    public static final String GB_PLAYURL = "/v1/{user_id}/devices/channels/cloud-live/url";
    public static final String HOLO_PLAYURL = "/v1/{user_id}/devices/channels/p2p-connect/live";
    public static final String gainToken = "/v1/{user_id}/enterprises/access-token";
    public static final String GET_CLOUD_RECORDS = "/v1/{user_id}/devices/{device_id}/channels/{channel_id}/cloud-records";
    public static final String GET_CLOUD_VOD_URL = "/v1/{user_id}/devices/{device_id}/channels/{channel_id}/cloud-records/playback-url";
    public static final String GET_LOCAL_VOD_URL = "/v1/{user_id}/devices/{device_id}/channels/{channel_id}/device-records/playback-url";
    public static final String GET_LOCAL_RECORDS = "/v1/{user_id}/devices/{device_id}/channels/{channel_id}/device-records";

}
