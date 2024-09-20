
#ifndef __DUCKEYS_BLE_H__
#define __DUCKEYS_BLE_H__

#include <esp_gap_ble_api.h>
#include <esp_gatts_api.h>
#include <esp_gap_ble_api.h>
#include <esp_gatt_common_api.h>

#include "common/types.h"
#include "common/errors.h"

typedef struct
{

    BOOL is_connected;

    uint8_t adv_config_done;

} DuckeysBLE;

////////////////////////////////////////////////////////////////////////////////

// 初始化指定的 BLE (低功耗蓝牙) 模块
Error duckeys_ble_init(DuckeysBLE *self);

#endif
