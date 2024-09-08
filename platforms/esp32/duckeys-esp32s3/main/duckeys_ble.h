
#ifndef __DUCKEYS_BLE_H__
#define __DUCKEYS_BLE_H__

#include "common_types.h"

typedef struct type_DuckeysBLE
{

} DuckeysBLE;

// 初始化指定的 BLE (低功耗蓝牙) 模块
Error duckeys_ble_init(DuckeysBLE *self);

#endif
