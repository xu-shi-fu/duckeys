
#ifndef __DUCKEYS_USB_H__
#define __DUCKEYS_USB_H__

#include "common/types.h"
#include "common/errors.h"

typedef struct type_DuckeysUSB
{

    // struct DuckeysApp *app;

} DuckeysUSB;

// 初始化指定的 USB 模块
Error duckeys_usb_init(DuckeysUSB *self);

#endif
