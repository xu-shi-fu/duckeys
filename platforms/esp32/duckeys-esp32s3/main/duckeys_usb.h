
#ifndef __DUCKEYS_USB_H__
#define __DUCKEYS_USB_H__

#include "common_types.h"

typedef struct type_DuckeysUSB
{

} DuckeysUSB;

// 初始化指定的 USB 模块
Error duckeys_usb_init(DuckeysUSB *self);

#endif
