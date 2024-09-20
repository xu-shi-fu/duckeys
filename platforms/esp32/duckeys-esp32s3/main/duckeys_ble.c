
#include <esp_bt.h>
#include <esp_bt_main.h>
#include <nvs_flash.h>
#include <memory.h>

#include "duckeys_ble.h"
#include "duckeys_app.h"
#include "common/hex.h"

enum
{
    IDX_SVC,

    IDX_CHAR_UP,
    IDX_CHAR_UP_VAL,
    IDX_CHAR_UP_CFG,

    IDX_CHAR_DOWN,
    IDX_CHAR_DOWN_VAL,
    IDX_CHAR_DOWN_CFG,

    HRS_IDX_NB,
};

////////////////////////////////////////////////////////////////////////////////
// define

#define LOG_EVENT_TAG "event .............................................. "

#define SAMPLE_DEVICE_NAME "DUCKEYS_M0"
#define SVC_INST_ID 0

#define ADV_CONFIG_FLAG (1 << 0)
#define SCAN_RSP_CONFIG_FLAG (1 << 1)

#define CHAR_DECLARATION_SIZE (sizeof(uint8_t))
#define GATTS_DEMO_CHAR_VAL_LEN_MAX 500

#define DUCKEYS_PROFILE_NUM 1
#define DUCKEYS_PROFILE_P0_ID 0

#define CONFIG_SET_RAW_ADV_DATA

#define DUCKEYS_SERVICE_UUID "03B80E5A-EDE8-4B33-A751-6CE34EC4C700"
#define DUCKEYS_CHAR_UP_UUID "e7ea0001-8e30-2e58-bdb6-0987991661e8"
#define DUCKEYS_CHAR_DOWN_UUID "e7ea0002-8e30-2e58-bdb6-0987991661e8"
#define DUCKEYS_CHAR_MIDI_UUID "7772E5DB-3868-4112-A1A9-F2669D106BF3"

////////////////////////////////////////////////////////////////////////////////
// struct

struct gatts_profile_inst
{
    esp_gatts_cb_t gatts_cb;
    uint16_t gatts_if;
    uint16_t app_id;
    uint16_t conn_id;
    uint16_t service_handle;
    esp_gatt_srvc_id_t service_id;
    uint16_t char_handle;
    esp_bt_uuid_t char_uuid;
    esp_gatt_perm_t perm;
    esp_gatt_char_prop_t property;
    uint16_t descr_handle;
    esp_bt_uuid_t descr_uuid;
};

static void duckeys_ble_gatts_event_handler_profile0(esp_gatts_cb_event_t event, esp_gatt_if_t gatts_if, esp_ble_gatts_cb_param_t *param);

static const char *duckeys_ble_stringify_gap_event(esp_gap_ble_cb_event_t event);

static const char *duckeys_ble_stringify_gatts_event(esp_gatts_cb_event_t event);

////////////////////////////////////////////////////////////////////////////////
// const

static const uint16_t primary_service_uuid = ESP_GATT_UUID_PRI_SERVICE;
static const uint16_t character_declaration_uuid = ESP_GATT_UUID_CHAR_DECLARE;
static const uint16_t character_client_config_uuid = ESP_GATT_UUID_CHAR_CLIENT_CONFIG;

// static const uint16_t GATTS_SERVICE_UUID_TEST = 0x2233;
static esp_bt_uuid_t duckeys_service_uuid;
static esp_bt_uuid_t duckeys_char_midi_uuid;
static esp_bt_uuid_t duckeys_char_down_uuid;
static esp_bt_uuid_t duckeys_char_up_uuid;

// static const uint8_t GATTS_CHAR_UUID_TEST_UP[16] = {
//     0xe7, 0xea, 0x00, 0x01, 0x8e, 0x30, 0x2e, 0x58, 0xbd, 0xb6, 0x09, 0x87, 0x99, 0x16, 0x61, 0xe8};
// static const uint8_t GATTS_CHAR_UUID_TEST_DOWN[16] = {
//     0xe7, 0xea, 0x00, 0x02, 0x8e, 0x30, 0x2e, 0x58, 0xbd, 0xb6, 0x09, 0x87, 0x99, 0x16, 0x61, 0xe8};

static const uint8_t char_prop_write = ESP_GATT_CHAR_PROP_BIT_WRITE;
static const uint8_t char_prop_read_write_notify = ESP_GATT_CHAR_PROP_BIT_WRITE | ESP_GATT_CHAR_PROP_BIT_READ | ESP_GATT_CHAR_PROP_BIT_NOTIFY;
static const uint8_t heart_measurement_ccc[2] = {0x00, 0x00};
static const uint8_t char_value[4] = {0x11, 0x22, 0x33, 0x44};

////////////////////////////////////////////////////////////////////////////////
// var

static uint8_t adv_config_done = 0;
static bool is_connected = false;
static uint16_t spp_conn_id = 0;
static esp_gatt_if_t spp_gatts_if = 0xff;

static uint16_t heart_rate_handle_table[HRS_IDX_NB];

static esp_ble_adv_params_t adv_params = {
    .adv_int_min = 0x20,
    .adv_int_max = 0x40,
    .adv_type = ADV_TYPE_IND,
    .own_addr_type = BLE_ADDR_TYPE_PUBLIC,
    .channel_map = ADV_CHNL_ALL,
    .adv_filter_policy = ADV_FILTER_ALLOW_SCAN_ANY_CON_ANY,
};

// raw_adv_data 表示生的广播数据，总长度不能大于 31 字节
static uint8_t raw_adv_data[] = {
    /* Flags */
    0x02, ESP_BLE_AD_TYPE_FLAG, 0x06,
    /* TX Power Level */
    0x02, ESP_BLE_AD_TYPE_TX_PWR, 0xEB,

    /* used as device version */
    0x03, ESP_BLE_AD_TYPE_16SRV_CMPL, 0xba, 0xbe,

    /* used as device class */
    0x05, ESP_BLE_AD_TYPE_32SRV_CMPL, 0xef, 0x01, 0x02, 0x03,

    /* Complete Local Name */
    0x0b, ESP_BLE_AD_TYPE_NAME_CMPL, 'D', 'u', 'c', 'k', 'e', 'y', 's', ' ', 'M', '0'};

// static uint8_t raw_scan_rsp_data[] = {};

// static esp_ble_adv_data_t adv_data = {};

// static esp_ble_adv_data_t scan_rsp_data = {};

/* Full Database Description - Used to add attributes into the database */
// { { auto_rsp } , { uuid_length, uuid_ptr, perm, max_length, length, value_ptr } }
static const esp_gatts_attr_db_t gatt_db[HRS_IDX_NB] =
    {
        // Service Declaration
        [IDX_SVC] =
            {{ESP_GATT_AUTO_RSP}, {ESP_UUID_LEN_16, (uint8_t *)&primary_service_uuid, ESP_GATT_PERM_READ, ESP_UUID_LEN_128, ESP_UUID_LEN_128, (uint8_t *)duckeys_service_uuid.uuid.uuid128}},

        /* Characteristic Declaration */
        [IDX_CHAR_UP] =
            {{ESP_GATT_AUTO_RSP}, {ESP_UUID_LEN_16, (uint8_t *)&character_declaration_uuid, ESP_GATT_PERM_READ, CHAR_DECLARATION_SIZE, CHAR_DECLARATION_SIZE, (uint8_t *)&char_prop_write}},

        /* Characteristic Value */
        [IDX_CHAR_UP_VAL] =
            {{ESP_GATT_AUTO_RSP}, {ESP_UUID_LEN_128, (uint8_t *)duckeys_char_up_uuid.uuid.uuid128, ESP_GATT_PERM_READ | ESP_GATT_PERM_WRITE, GATTS_DEMO_CHAR_VAL_LEN_MAX, sizeof(char_value), (uint8_t *)char_value}},

        /* Client Characteristic Configuration Descriptor */
        [IDX_CHAR_UP_CFG] =
            {{ESP_GATT_AUTO_RSP}, {ESP_UUID_LEN_16, (uint8_t *)&character_client_config_uuid, ESP_GATT_PERM_READ | ESP_GATT_PERM_WRITE, sizeof(uint16_t), sizeof(heart_measurement_ccc), (uint8_t *)heart_measurement_ccc}},

        /* Characteristic Declaration */
        [IDX_CHAR_DOWN] =
            {{ESP_GATT_AUTO_RSP}, {ESP_UUID_LEN_16, (uint8_t *)&character_declaration_uuid, ESP_GATT_PERM_READ, CHAR_DECLARATION_SIZE, CHAR_DECLARATION_SIZE, (uint8_t *)&char_prop_read_write_notify}},

        /* Characteristic Value */
        [IDX_CHAR_DOWN_VAL] =
            {{ESP_GATT_AUTO_RSP}, {ESP_UUID_LEN_128, (uint8_t *)duckeys_char_down_uuid.uuid.uuid128, ESP_GATT_PERM_READ | ESP_GATT_PERM_WRITE, GATTS_DEMO_CHAR_VAL_LEN_MAX, sizeof(char_value), (uint8_t *)char_value}},

        /* Client Characteristic Configuration Descriptor */
        [IDX_CHAR_DOWN_CFG] =
            {{ESP_GATT_AUTO_RSP}, {ESP_UUID_LEN_16, (uint8_t *)&character_client_config_uuid, ESP_GATT_PERM_READ | ESP_GATT_PERM_WRITE, sizeof(uint16_t), sizeof(heart_measurement_ccc), (uint8_t *)heart_measurement_ccc}},

};

static struct gatts_profile_inst heart_rate_profile_tab[DUCKEYS_PROFILE_NUM] = {
    [DUCKEYS_PROFILE_P0_ID] = {
        .gatts_cb = duckeys_ble_gatts_event_handler_profile0,
        .gatts_if = ESP_GATT_IF_NONE, /* Not get the gatt_if, so initial is ESP_GATT_IF_NONE */
    },

    // [PROFILE_B_APP_ID] = {
    //     .gatts_cb = gatts_event_p2_handler, .gatts_if = ESP_GATT_IF_NONE, /* Not get the gatt_if, so initial is ESP_GATT_IF_NONE */
    // },

};

////////////////////////////////////////////////////////////////////////////////
// functions

// BLE 模块的 GAP 处理函数
static void duckeys_ble_gap_event_handler(esp_gap_ble_cb_event_t event, esp_ble_gap_cb_param_t *param)
{
    const char *event_str = duckeys_ble_stringify_gap_event(event);
    ESP_LOGI(DUCKEYS_LOG_TAG, "%s  gap_event = %d(%s)", LOG_EVENT_TAG, event, event_str);

    switch (event)
    {

#ifdef CONFIG_SET_RAW_ADV_DATA
    case ESP_GAP_BLE_ADV_DATA_RAW_SET_COMPLETE_EVT:
        adv_config_done &= (~ADV_CONFIG_FLAG);
        if (adv_config_done == 0)
        {
            esp_ble_gap_start_advertising(&adv_params);
        }
        break;
    case ESP_GAP_BLE_SCAN_RSP_DATA_RAW_SET_COMPLETE_EVT:
        adv_config_done &= (~SCAN_RSP_CONFIG_FLAG);
        if (adv_config_done == 0)
        {
            esp_ble_gap_start_advertising(&adv_params);
        }
        break;
#else
    case ESP_GAP_BLE_ADV_DATA_SET_COMPLETE_EVT: // 广播数据设置完成事件标志
        adv_config_done &= (~ADV_CONFIG_FLAG);
        if (adv_config_done == 0)
        {
            esp_ble_gap_start_advertising(&adv_params); // 开始广播
        }
        break;
    case ESP_GAP_BLE_SCAN_RSP_DATA_SET_COMPLETE_EVT: // 广播扫描相应设置完成标志
        adv_config_done &= (~SCAN_RSP_CONFIG_FLAG);
        if (adv_config_done == 0)
        {
            esp_ble_gap_start_advertising(&adv_params);
        }
        break;
#endif

    case ESP_GAP_BLE_ADV_START_COMPLETE_EVT:
        /* advertising start complete event to indicate advertising start successfully or failed */
        if (param->adv_start_cmpl.status != ESP_BT_STATUS_SUCCESS)
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "advertising start failed");
        }
        else
        {
            ESP_LOGI(DUCKEYS_LOG_TAG, "advertising start successfully");
        }
        break;
    case ESP_GAP_BLE_ADV_STOP_COMPLETE_EVT:
        if (param->adv_stop_cmpl.status != ESP_BT_STATUS_SUCCESS)
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "Advertising stop failed");
        }
        else
        {
            ESP_LOGI(DUCKEYS_LOG_TAG, "Stop adv successfully");
        }
        break;
    case ESP_GAP_BLE_UPDATE_CONN_PARAMS_EVT:
        ESP_LOGI(DUCKEYS_LOG_TAG, "update connection params status = %d, min_int = %d, max_int = %d, conn_int = %d, latency = %d, timeout = %d",
                 param->update_conn_params.status,
                 param->update_conn_params.min_int,
                 param->update_conn_params.max_int,
                 param->update_conn_params.conn_int,
                 param->update_conn_params.latency,
                 param->update_conn_params.timeout);
        break;
    default:
        break;
    }
}

// BLE 模块的 GATT 处理函数
static void duckeys_ble_gatts_event_handler(esp_gatts_cb_event_t event, esp_gatt_if_t gatts_if, esp_ble_gatts_cb_param_t *param)
{
    const char *event_str = duckeys_ble_stringify_gatts_event(event);
    ESP_LOGI(DUCKEYS_LOG_TAG, "%s  gatts_event = %d(%s)", LOG_EVENT_TAG, event, event_str);

    /* If event is register event, store the gatts_if for each profile */
    if (event == ESP_GATTS_REG_EVT)
    {
        uint16_t app_id = param->reg.app_id;
        if (param->reg.status == ESP_GATT_OK && (app_id < DUCKEYS_PROFILE_NUM))
        {
            heart_rate_profile_tab[app_id].gatts_if = gatts_if;
        }
        else
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "reg app failed, app_id %04x, status %d",
                     param->reg.app_id,
                     param->reg.status);
            return;
        }
    }

    do
    {
        int idx;
        for (idx = 0; idx < DUCKEYS_PROFILE_NUM; idx++)
        {
            /* ESP_GATT_IF_NONE, not specify a certain gatt_if, need to call every profile cb function */
            if (gatts_if == ESP_GATT_IF_NONE || gatts_if == heart_rate_profile_tab[idx].gatts_if)
            {
                if (heart_rate_profile_tab[idx].gatts_cb)
                {
                    heart_rate_profile_tab[idx].gatts_cb(event, gatts_if, param);
                }
            }
        }
    } while (0);
}

static void duckeys_ble_gatts_event_handler_profile0(esp_gatts_cb_event_t event, esp_gatt_if_t gatts_if, esp_ble_gatts_cb_param_t *param)
{

    ESP_LOGI(DUCKEYS_LOG_TAG, "    handled by profile_0");

    switch (event)
    {
    case ESP_GATTS_REG_EVT:
    {

        ESP_LOGI(DUCKEYS_LOG_TAG, "REGISTER_APP_EVT, status %d, app_id %d\n", param->reg.status, param->reg.app_id);
        heart_rate_profile_tab[DUCKEYS_PROFILE_P0_ID].service_id.is_primary = true;
        heart_rate_profile_tab[DUCKEYS_PROFILE_P0_ID].service_id.id.inst_id = 0x00;
        heart_rate_profile_tab[DUCKEYS_PROFILE_P0_ID].service_id.id.uuid = duckeys_service_uuid;

        esp_err_t set_dev_name_ret = esp_ble_gap_set_device_name(SAMPLE_DEVICE_NAME);
        if (set_dev_name_ret)
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "set device name failed, error code = %x", set_dev_name_ret);
        }
#ifdef CONFIG_SET_RAW_ADV_DATA
        esp_err_t raw_adv_ret = esp_ble_gap_config_adv_data_raw(raw_adv_data, sizeof(raw_adv_data));
        if (raw_adv_ret)
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "config raw adv data failed, error code = %x ", raw_adv_ret);
        }
        adv_config_done |= ADV_CONFIG_FLAG;

        // esp_err_t raw_scan_ret = esp_ble_gap_config_scan_rsp_data_raw(raw_scan_rsp_data, sizeof(raw_scan_rsp_data));
        // if (raw_scan_ret)
        // {
        //     ESP_LOGE(DUCKEYS_LOG_TAG, "config raw scan rsp data failed, error code = %x", raw_scan_ret);
        // }
        // adv_config_done |= SCAN_RSP_CONFIG_FLAG;

#else
        // config adv data
        esp_err_t ret = esp_ble_gap_config_adv_data(&adv_data);
        if (ret)
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "config adv data failed, error code = %x", ret);
        }
        adv_config_done |= ADV_CONFIG_FLAG;
        // config scan response data
        ret = esp_ble_gap_config_adv_data(&scan_rsp_data);
        if (ret)
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "config scan response data failed, error code = %x", ret);
        }
        adv_config_done |= SCAN_RSP_CONFIG_FLAG;

#endif

        esp_err_t create_attr_ret = esp_ble_gatts_create_attr_tab(gatt_db, gatts_if, HRS_IDX_NB, SVC_INST_ID);
        if (create_attr_ret)
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "create attr table failed, error code = %x", create_attr_ret);
        }

        esp_ble_gatts_create_service(gatts_if, &heart_rate_profile_tab[DUCKEYS_PROFILE_P0_ID].service_id, HRS_IDX_NB);
        break;
    }

    case ESP_GATTS_CREAT_ATTR_TAB_EVT:
    {
        if (param->add_attr_tab.status != ESP_GATT_OK)
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "create attribute table failed, error code=0x%x", param->add_attr_tab.status);
        }
        else if (param->add_attr_tab.num_handle != HRS_IDX_NB)
        {
            ESP_LOGE(DUCKEYS_LOG_TAG, "create attribute table abnormally, num_handle (%d) \
                        doesn't equal to HRS_IDX_NB(%d)",
                     param->add_attr_tab.num_handle, HRS_IDX_NB);
        }
        else
        {
            ESP_LOGI(DUCKEYS_LOG_TAG, "create attribute table successfully, the number handle = %d", param->add_attr_tab.num_handle);
            memcpy(heart_rate_handle_table, param->add_attr_tab.handles, sizeof(heart_rate_handle_table));
            esp_ble_gatts_start_service(heart_rate_handle_table[IDX_SVC]);
        }
        break;
    }

    case ESP_GATTS_CONNECT_EVT:
    {
        spp_conn_id = param->connect.conn_id;
        spp_gatts_if = gatts_if;
        is_connected = true;

        //  esp_ble_gap_update_conn_params(&conn_params);
        esp_ble_gap_stop_advertising();
        break;
    }

    case ESP_GATTS_DISCONNECT_EVT:
    {
        is_connected = false;
        ESP_LOGI(DUCKEYS_LOG_TAG, "ESP_GATTS_DISCONNECT_EVT, esp_ble_gap_start_advertising");
        esp_ble_gap_start_advertising(&adv_params);
        break;
    }

    case ESP_GATTS_WRITE_EVT:
    {
        uint16_t data_len = param->write.len;
        uint8_t *data_buf = param->write.value;
        uint8_t buffer[128];
        if (data_len < sizeof(buffer))
        {
            memset(buffer, 0, sizeof(buffer));
            memcpy(buffer, data_buf, data_len);

            // blueduck_io_write_text_upstream((const char *)buffer, data_len);
            ESP_LOGI(DUCKEYS_LOG_TAG, "handle_ESP_GATTS_WRITE_EVT, data = %s", buffer);
        }
        break;
    }

    default:
        break;
    }
}

const esp_bt_uuid_t *duckeys_ble_parse_uuid_128(const char *str, esp_bt_uuid_t *dst)
{
    uint8_t buffer[16]; // uuid128 = 8bits * 16
    ByteBuffer bb;
    const uint16_t size = sizeof(buffer);

    memset(&bb, 0, sizeof(bb));
    memset(buffer, 0, size);
    bb.Capacity = size;
    bb.Data = buffer;
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
    return dst;
}

const char *duckeys_ble_stringify_gap_event(esp_gap_ble_cb_event_t event)
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

const char *duckeys_ble_stringify_gatts_event(esp_gatts_cb_event_t event)
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

// 初始化 BLE 模块
Error duckeys_ble_init(DuckeysBLE *self)
{
    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_ble_init - begin");

    duckeys_ble_parse_uuid_128(DUCKEYS_SERVICE_UUID, &duckeys_service_uuid);
    duckeys_ble_parse_uuid_128(DUCKEYS_CHAR_MIDI_UUID, &duckeys_char_midi_uuid);
    duckeys_ble_parse_uuid_128(DUCKEYS_CHAR_DOWN_UUID, &duckeys_char_down_uuid);
    duckeys_ble_parse_uuid_128(DUCKEYS_CHAR_UP_UUID, &duckeys_char_up_uuid);

    esp_err_t ret;
    esp_bt_controller_config_t bt_cfg = BT_CONTROLLER_INIT_CONFIG_DEFAULT();

    // 初始化 NVS.
    // Initialize NVS
    ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND)
    {
        ESP_ERROR_CHECK(nvs_flash_erase());
        ret = nvs_flash_init();
    }
    ESP_ERROR_CHECK(ret);

    ESP_ERROR_CHECK(esp_bt_controller_mem_release(ESP_BT_MODE_CLASSIC_BT));

    // 初始化蓝牙控制器
    ret = esp_bt_controller_init(&bt_cfg);
    if (ret)
    {
        ESP_LOGE(DUCKEYS_LOG_TAG, "%s enable controller failed: %s", __func__, esp_err_to_name(ret));
        return FormatError("error,exit");
    }

    // 激活蓝牙控制器
    ret = esp_bt_controller_enable(ESP_BT_MODE_BLE);
    if (ret)
    {
        ESP_LOGE(DUCKEYS_LOG_TAG, "%s enable controller failed: %s", __func__, esp_err_to_name(ret));
        return FormatError("error,exit");
    }

    ESP_LOGI(DUCKEYS_LOG_TAG, "%s init bluetooth", __func__);

    // 初始化蓝牙协议栈
    ret = esp_bluedroid_init();
    if (ret)
    {
        ESP_LOGE(DUCKEYS_LOG_TAG, "%s init bluetooth failed: %s", __func__, esp_err_to_name(ret));
        return FormatError("error,exit");
    }

    // 激活蓝牙协议栈
    ret = esp_bluedroid_enable();
    if (ret)
    {
        ESP_LOGE(DUCKEYS_LOG_TAG, "%s enable bluetooth failed: %s", __func__, esp_err_to_name(ret));
        return FormatError("error,exit");
    }

    // 注册 GATT 事件回调函数
    esp_ble_gatts_register_callback(duckeys_ble_gatts_event_handler);

    // 注册 GAP 事件回调函数
    esp_ble_gap_register_callback(duckeys_ble_gap_event_handler);

    // 注册 GATT 应用
    esp_ble_gatts_app_register(DUCKEYS_PROFILE_P0_ID);

    // duckeys_task_init();

    // 设置 MTU 参数
    esp_err_t local_mtu_ret = esp_ble_gatt_set_local_mtu(500);
    if (local_mtu_ret)
    {
        ESP_LOGE(DUCKEYS_LOG_TAG, "set local  MTU failed, error code = %x", local_mtu_ret);
    }

    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_ble_init - end");
    return Nil;
}
