

#ifndef __DUCKEYS_BLE_HELPER_H__
#define __DUCKEYS_BLE_HELPER_H__

#include <esp_gap_ble_api.h>
#include <esp_gatt_common_api.h>
#include <esp_gatts_api.h>

#include "common/types.h"

DK_STRING duckeys_ble_stringify_gatts_event(esp_gatts_cb_event_t event);

DK_STRING duckeys_ble_stringify_gap_event(esp_gap_ble_cb_event_t event);

Error duckeys_ble_parse_uuid_128(DK_STRING str, esp_bt_uuid_t *dst);

#endif
