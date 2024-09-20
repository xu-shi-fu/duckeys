
#ifndef __DUCKEYS_LED_H__
#define __DUCKEYS_LED_H__

#include "common/types.h"
#include "common/errors.h"

typedef struct type_DuckeysLED
{

    // struct DuckeysApp *app;

} DuckeysLED;

Error duckeys_led_init(DuckeysLED *self);

#endif
