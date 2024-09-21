
#ifndef __DUCKEYS_APP_H__
#define __DUCKEYS_APP_H__

#include "common/types.h"
#include "common/errors.h"

#include "duckeys_debug.h"
#include "duckeys_ble.h"
#include "duckeys_fifo.h"
#include "duckeys_led.h"
#include "duckeys_hub.h"
#include "duckeys_usb.h"

typedef struct DuckeysApp_t
{

    DuckeysBLE ble;
    DuckeysDebug debug;
    DuckeysLED led;
    DuckeysFIFO fifo;
    DuckeysHub hub;
    DuckeysUSB usb;

} DuckeysApp;

// 初始化 app 的所有模块
Error duckeys_app_init(DuckeysApp *self);

// 初始化指定的各个模块

Error duckeys_ble_init(DuckeysBLE *self, DuckeysApp *app);
Error duckeys_fifo_init(DuckeysFIFO *self, DuckeysApp *app);
Error duckeys_debug_init(DuckeysDebug *self, DuckeysApp *app);
Error duckeys_usb_init(DuckeysUSB *self, DuckeysApp *app);
Error duckeys_led_init(DuckeysLED *self, DuckeysApp *app);
Error duckeys_hub_init(DuckeysHub *self, DuckeysApp *app);

#endif
