
#ifndef __DUCKEYS_APP_H__
#define __DUCKEYS_APP_H__

#include "common/types.h"
#include "common/errors.h"

#include "duckeys_debug.h"
#include "duckeys_ble.h"
#include "duckeys_fifo.h"
#include "duckeys_led.h"
#include "duckeys_usb.h"

typedef struct
{

    DuckeysBLE ble;
    DuckeysDebug debug;
    DuckeysLED led;
    DuckeysFIFO fifo;
    DuckeysUSB usb;

} DuckeysApp;

// 初始化 app 的所有模块
Error duckeys_app_init(DuckeysApp *self);

#endif
