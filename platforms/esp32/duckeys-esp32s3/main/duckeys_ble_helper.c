
#include "duckeys_libs.h"
#include "duckeys_ble.h"
#include "common/bytes.h"
#include "common/hex.h"
#include "duckeys_ble_helper.h"

Error duckeys_ble_parse_uuid_128(DK_STRING str, esp_bt_uuid_t *dst)
{
    uint8_t buffer[16]; // uuid128 = 8bits * 16
    ByteBuffer bb;
    const uint16_t size = sizeof(buffer);

    memset(&bb, 0, sizeof(bb));
    memset(buffer, 0, size);
    ByteBufferInit(&bb, buffer, size);
    HexParse(str, &bb);

    uint8_t i1, i2, v1, v2;
    for (i1 = 0, i2 = size - 1; i1 < i2; i1++, i2--)
    {
        v1 = buffer[i1];
        v2 = buffer[i2];
        buffer[i1] = v2;
        buffer[i2] = v1;
    }

    memcpy(dst->uuid.uuid128, buffer, size);
    dst->len = size;
    return Nil;
}

DK_STRING duckeys_ble_stringify_gap_event(esp_gap_ble_cb_event_t event)
{
    switch (event)
    {
    case ESP_GAP_BLE_ADV_DATA_SET_COMPLETE_EVT:
        return "ESP_GAP_BLE_ADV_DATA_SET_COMPLETE_EVT"; /*!< When advertising data set complete, the event comes */
    case ESP_GAP_BLE_SCAN_RSP_DATA_SET_COMPLETE_EVT:
        return "ESP_GAP_BLE_SCAN_RSP_DATA_SET_COMPLETE_EVT"; /*!< When scan response data set complete, the event comes */
    case ESP_GAP_BLE_SCAN_PARAM_SET_COMPLETE_EVT:
        return "ESP_GAP_BLE_SCAN_PARAM_SET_COMPLETE_EVT"; /*!< When scan parameters set complete, the event comes */
    case ESP_GAP_BLE_SCAN_RESULT_EVT:
        return "ESP_GAP_BLE_SCAN_RESULT_EVT"; /*!< When one scan result ready, the event comes each time */
    case ESP_GAP_BLE_ADV_DATA_RAW_SET_COMPLETE_EVT:
        return "ESP_GAP_BLE_ADV_DATA_RAW_SET_COMPLETE_EVT"; /*!< When raw advertising data set complete, the event comes */
    case ESP_GAP_BLE_SCAN_RSP_DATA_RAW_SET_COMPLETE_EVT:
        return "ESP_GAP_BLE_SCAN_RSP_DATA_RAW_SET_COMPLETE_EVT"; /*!< When raw scan response data set complete, the event comes */
    case ESP_GAP_BLE_ADV_START_COMPLETE_EVT:
        return "ESP_GAP_BLE_ADV_START_COMPLETE_EVT"; /*!< When start advertising complete, the event comes */
    case ESP_GAP_BLE_SCAN_START_COMPLETE_EVT:
        return "ESP_GAP_BLE_SCAN_START_COMPLETE_EVT"; /*!< When start scan complete, the event comes */
                                                      // BLE_INCLUDED
    case ESP_GAP_BLE_AUTH_CMPL_EVT:
        return "ESP_GAP_BLE_AUTH_CMPL_EVT"; /*!< Authentication complete indication. */
    case ESP_GAP_BLE_KEY_EVT:
        return ""; /*!< BLE  key event for peer device keys */
    case ESP_GAP_BLE_SEC_REQ_EVT:
        return ""; /*!< BLE  security request */
    case ESP_GAP_BLE_PASSKEY_NOTIF_EVT:
        return ""; /*!< passkey notification event */
    case ESP_GAP_BLE_PASSKEY_REQ_EVT:
        return ""; /*!< passkey request event */
    case ESP_GAP_BLE_OOB_REQ_EVT:
        return ""; /*!< OOB request event */
    case ESP_GAP_BLE_LOCAL_IR_EVT:
        return ""; /*!< BLE local IR (identity Root 128-bit random static value used to generate Long Term Key) event */
    case ESP_GAP_BLE_LOCAL_ER_EVT:
        return ""; /*!< BLE local ER (Encryption Root value used to generate identity resolving key) event */
    case ESP_GAP_BLE_NC_REQ_EVT:
        return ""; /*!< Numeric Comparison request event */
                   // BLE_42_FEATURE_SUPPORT
    case ESP_GAP_BLE_ADV_STOP_COMPLETE_EVT:
        return "ESP_GAP_BLE_ADV_STOP_COMPLETE_EVT"; /*!< When stop adv complete, the event comes */
    case ESP_GAP_BLE_SCAN_STOP_COMPLETE_EVT:
        return "ESP_GAP_BLE_SCAN_STOP_COMPLETE_EVT"; /*!< When stop scan complete, the event comes */
                                                     // BLE_INCLUDED
    case ESP_GAP_BLE_SET_STATIC_RAND_ADDR_EVT:
        return "ESP_GAP_BLE_SET_STATIC_RAND_ADDR_EVT"; /*!< When set the static rand address complete, the event comes */
    case ESP_GAP_BLE_UPDATE_CONN_PARAMS_EVT:
        return "ESP_GAP_BLE_UPDATE_CONN_PARAMS_EVT"; /*!< When update connection parameters complete, the event comes */
    case ESP_GAP_BLE_SET_PKT_LENGTH_COMPLETE_EVT:
        return "ESP_GAP_BLE_SET_PKT_LENGTH_COMPLETE_EVT"; /*!< When set pkt length complete, the event comes */
    case ESP_GAP_BLE_SET_LOCAL_PRIVACY_COMPLETE_EVT:
        return ""; /*!< When  Enable/disable privacy on the local device complete, the event comes */
    case ESP_GAP_BLE_REMOVE_BOND_DEV_COMPLETE_EVT:
        return ""; /*!< When remove the bond device complete, the event comes */
    case ESP_GAP_BLE_CLEAR_BOND_DEV_COMPLETE_EVT:
        return ""; /*!< When clear the bond device clear complete, the event comes */
    case ESP_GAP_BLE_GET_BOND_DEV_COMPLETE_EVT:
        return ""; /*!< When get the bond device list complete, the event comes */
    case ESP_GAP_BLE_READ_RSSI_COMPLETE_EVT:
        return ""; /*!< When read the rssi complete, the event comes */
    case ESP_GAP_BLE_UPDATE_WHITELIST_COMPLETE_EVT:
        return ""; /*!< When add or remove whitelist complete, the event comes */
                   // BLE_42_FEATURE_SUPPORT
    case ESP_GAP_BLE_UPDATE_DUPLICATE_EXCEPTIONAL_LIST_COMPLETE_EVT:
        return ""; /*!< When update duplicate exceptional list complete, the event comes */
                   // BLE_INCLUDED
    case ESP_GAP_BLE_SET_CHANNELS_EVT:
        return ""; /*!< When setting BLE channels complete, the event comes */
                   // BLE_50_FEATURE_SUPPORT
    case ESP_GAP_BLE_READ_PHY_COMPLETE_EVT:
        return ""; /*!< when reading phy complete, this event comes */
    case ESP_GAP_BLE_SET_PREFERRED_DEFAULT_PHY_COMPLETE_EVT:
        return ""; /*!< when preferred default phy complete, this event comes */
    case ESP_GAP_BLE_SET_PREFERRED_PHY_COMPLETE_EVT:
        return ""; /*!< when preferred phy complete , this event comes */
    case ESP_GAP_BLE_EXT_ADV_SET_RAND_ADDR_COMPLETE_EVT:
        return ""; /*!< when extended set random address complete, the event comes */
    case ESP_GAP_BLE_EXT_ADV_SET_PARAMS_COMPLETE_EVT:
        return ""; /*!< when extended advertising parameter complete, the event comes */
    case ESP_GAP_BLE_EXT_ADV_DATA_SET_COMPLETE_EVT:
        return ""; /*!< when extended advertising data complete, the event comes */
    case ESP_GAP_BLE_EXT_SCAN_RSP_DATA_SET_COMPLETE_EVT:
        return ""; /*!< when extended scan response data complete, the event comes */
    case ESP_GAP_BLE_EXT_ADV_START_COMPLETE_EVT:
        return ""; /*!< when extended advertising start complete, the event comes */
    case ESP_GAP_BLE_EXT_ADV_STOP_COMPLETE_EVT:
        return ""; /*!< when extended advertising stop complete, the event comes */
    case ESP_GAP_BLE_EXT_ADV_SET_REMOVE_COMPLETE_EVT:
        return ""; /*!< when extended advertising set remove complete, the event comes */
    case ESP_GAP_BLE_EXT_ADV_SET_CLEAR_COMPLETE_EVT:
        return ""; /*!< when extended advertising set clear complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_SET_PARAMS_COMPLETE_EVT:
        return ""; /*!< when periodic advertising parameter complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_DATA_SET_COMPLETE_EVT:
        return ""; /*!< when periodic advertising data complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_START_COMPLETE_EVT:
        return ""; /*!< when periodic advertising start complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_STOP_COMPLETE_EVT:
        return ""; /*!< when periodic advertising stop complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_CREATE_SYNC_COMPLETE_EVT:
        return ""; /*!< when periodic advertising create sync complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_SYNC_CANCEL_COMPLETE_EVT:
        return ""; /*!< when extended advertising sync cancel complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_SYNC_TERMINATE_COMPLETE_EVT:
        return ""; /*!< when extended advertising sync terminate complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_ADD_DEV_COMPLETE_EVT:
        return ""; /*!< when extended advertising add device complete , the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_REMOVE_DEV_COMPLETE_EVT:
        return ""; /*!< when extended advertising remove device complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_CLEAR_DEV_COMPLETE_EVT:
        return ""; /*!< when extended advertising clear device, the event comes */
    case ESP_GAP_BLE_SET_EXT_SCAN_PARAMS_COMPLETE_EVT:
        return ""; /*!< when extended scan parameter complete, the event comes */
    case ESP_GAP_BLE_EXT_SCAN_START_COMPLETE_EVT:
        return ""; /*!< when extended scan start complete, the event comes */
    case ESP_GAP_BLE_EXT_SCAN_STOP_COMPLETE_EVT:
        return ""; /*!< when extended scan stop complete, the event comes */
    case ESP_GAP_BLE_PREFER_EXT_CONN_PARAMS_SET_COMPLETE_EVT:
        return ""; /*!< when extended prefer connection parameter set complete, the event comes */
    case ESP_GAP_BLE_PHY_UPDATE_COMPLETE_EVT:
        return ""; /*!< when ble phy update complete, the event comes */
    case ESP_GAP_BLE_EXT_ADV_REPORT_EVT:
        return ""; /*!< when extended advertising report complete, the event comes */
    case ESP_GAP_BLE_SCAN_TIMEOUT_EVT:
        return ""; /*!< when scan timeout complete, the event comes */
    case ESP_GAP_BLE_ADV_TERMINATED_EVT:
        return ""; /*!< when advertising terminate data complete, the event comes */
    case ESP_GAP_BLE_SCAN_REQ_RECEIVED_EVT:
        return ""; /*!< when scan req received complete, the event comes */
    case ESP_GAP_BLE_CHANNEL_SELECT_ALGORITHM_EVT:
        return ""; /*!< when channel select algorithm complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_REPORT_EVT:
        return ""; /*!< when periodic report advertising complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_SYNC_LOST_EVT:
        return ""; /*!< when periodic advertising sync lost complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_SYNC_ESTAB_EVT:
        return ""; /*!< when periodic advertising sync establish complete, the event comes */
                   // BLE_INCLUDED
    case ESP_GAP_BLE_SC_OOB_REQ_EVT:
        return ""; /*!< Secure Connection OOB request event */
    case ESP_GAP_BLE_SC_CR_LOC_OOB_EVT:
        return ""; /*!< Secure Connection create OOB data complete event */
    case ESP_GAP_BLE_GET_DEV_NAME_COMPLETE_EVT:
        return ""; /*!< When getting BT device name complete, the event comes */
                   // BLE_FEAT_PERIODIC_ADV_SYNC_TRANSFER
    case ESP_GAP_BLE_PERIODIC_ADV_RECV_ENABLE_COMPLETE_EVT:
        return ""; /*!< when set periodic advertising receive enable complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_SYNC_TRANS_COMPLETE_EVT:
        return ""; /*!< when periodic advertising sync transfer complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_SET_INFO_TRANS_COMPLETE_EVT:
        return ""; /*!< when periodic advertising set info transfer complete, the event comes */
    case ESP_GAP_BLE_SET_PAST_PARAMS_COMPLETE_EVT:
        return ""; /*!< when set periodic advertising sync transfer params complete, the event comes */
    case ESP_GAP_BLE_PERIODIC_ADV_SYNC_TRANS_RECV_EVT:
        return ""; /*!< when periodic advertising sync transfer received, the event comes */
                   // DTM
    case ESP_GAP_BLE_DTM_TEST_UPDATE_EVT:
        return "ESP_GAP_BLE_DTM_TEST_UPDATE_EVT"; /*!< when direct test mode state changes, the event comes */
                                                  // BLE_INCLUDED
    case ESP_GAP_BLE_ADV_CLEAR_COMPLETE_EVT:
        return "ESP_GAP_BLE_ADV_CLEAR_COMPLETE_EVT"; /*!< When clear advertising complete, the event comes */
    case ESP_GAP_BLE_SET_RPA_TIMEOUT_COMPLETE_EVT:
        return "ESP_GAP_BLE_SET_RPA_TIMEOUT_COMPLETE_EVT"; /*!< When set the Resolvable Private Address (RPA) timeout completes, the event comes */
    case ESP_GAP_BLE_ADD_DEV_TO_RESOLVING_LIST_COMPLETE_EVT:
        return "ESP_GAP_BLE_ADD_DEV_TO_RESOLVING_LIST_COMPLETE_EVT"; /*!< when add a device to the resolving list completes, the event comes*/
    case ESP_GAP_BLE_VENDOR_CMD_COMPLETE_EVT:
        return "ESP_GAP_BLE_VENDOR_CMD_COMPLETE_EVT"; /*!< When vendor hci command complete, the event comes */
    case ESP_GAP_BLE_SET_PRIVACY_MODE_COMPLETE_EVT:
        return "ESP_GAP_BLE_SET_PRIVACY_MODE_COMPLETE_EVT"; /*!< When set privacy mode complete, the event comes */
    case ESP_GAP_BLE_EVT_MAX:                               /*!< when maximum advertising event complete, the event comes */
    default:
        break;
    }
    return "";
}

DK_STRING duckeys_ble_stringify_gatts_event(esp_gatts_cb_event_t event)
{
    switch (event)
    {
    case ESP_GATTS_REG_EVT:
        return "ESP_GATTS_REG_EVT"; /*!< When register application id, the event comes */
    case ESP_GATTS_READ_EVT:
        return "ESP_GATTS_READ_EVT"; /*!< When gatt client request read operation, the event comes */
    case ESP_GATTS_WRITE_EVT:
        return "ESP_GATTS_WRITE_EVT"; /*!< When gatt client request write operation, the event comes */
    case ESP_GATTS_EXEC_WRITE_EVT:
        return "ESP_GATTS_EXEC_WRITE_EVT"; /*!< When gatt client request execute write, the event comes */
    case ESP_GATTS_MTU_EVT:
        return "ESP_GATTS_MTU_EVT"; /*!< When set mtu complete, the event comes */
    case ESP_GATTS_CONF_EVT:
        return "ESP_GATTS_CONF_EVT"; /*!< When receive confirm, the event comes */
    case ESP_GATTS_UNREG_EVT:
        return "ESP_GATTS_UNREG_EVT"; /*!< When unregister application id, the event comes */
    case ESP_GATTS_CREATE_EVT:
        return "ESP_GATTS_CREATE_EVT"; /*!< When create service complete, the event comes */
    case ESP_GATTS_ADD_INCL_SRVC_EVT:
        return "ESP_GATTS_ADD_INCL_SRVC_EVT"; /*!< When add included service complete, the event comes */
    case ESP_GATTS_ADD_CHAR_EVT:
        return "ESP_GATTS_ADD_CHAR_EVT"; /*!< When add characteristic complete, the event comes */
    case ESP_GATTS_ADD_CHAR_DESCR_EVT:
        return "ESP_GATTS_ADD_CHAR_DESCR_EVT"; /*!< When add descriptor complete, the event comes */
    case ESP_GATTS_DELETE_EVT:
        return "ESP_GATTS_DELETE_EVT"; /*!< When delete service complete, the event comes */
    case ESP_GATTS_START_EVT:
        return "ESP_GATTS_START_EVT"; /*!< When start service complete, the event comes */
    case ESP_GATTS_STOP_EVT:
        return "ESP_GATTS_STOP_EVT"; /*!< When stop service complete, the event comes */
    case ESP_GATTS_CONNECT_EVT:
        return "ESP_GATTS_CONNECT_EVT"; /*!< When gatt client connect, the event comes */
    case ESP_GATTS_DISCONNECT_EVT:
        return "ESP_GATTS_DISCONNECT_EVT"; /*!< When gatt client disconnect, the event comes */
    case ESP_GATTS_OPEN_EVT:
        return "ESP_GATTS_OPEN_EVT"; /*!< When connect to peer, the event comes */
    case ESP_GATTS_CANCEL_OPEN_EVT:
        return "ESP_GATTS_CANCEL_OPEN_EVT"; /*!< When disconnect from peer, the event comes */
    case ESP_GATTS_CLOSE_EVT:
        return "ESP_GATTS_CLOSE_EVT"; /*!< When gatt server close, the event comes */
    case ESP_GATTS_LISTEN_EVT:
        return "ESP_GATTS_LISTEN_EVT"; /*!< When gatt listen to be connected the event comes */
    case ESP_GATTS_CONGEST_EVT:
        return "ESP_GATTS_CONGEST_EVT"; /*!< When congest happen, the event comes */
    /* following is extra event */
    case ESP_GATTS_RESPONSE_EVT:
        return "ESP_GATTS_RESPONSE_EVT"; /*!< When gatt send response complete, the event comes */
    case ESP_GATTS_CREAT_ATTR_TAB_EVT:
        return "ESP_GATTS_CREAT_ATTR_TAB_EVT"; /*!< When gatt create table complete, the event comes */
    case ESP_GATTS_SET_ATTR_VAL_EVT:
        return "ESP_GATTS_SET_ATTR_VAL_EVT"; /*!< When gatt set attr value complete, the event comes */
    case ESP_GATTS_SEND_SERVICE_CHANGE_EVT:
        return "ESP_GATTS_SEND_SERVICE_CHANGE_EVT"; /*!< When gatt send service change indication complete, the event comes */
    default:
        break;
    }
    return "";
}
