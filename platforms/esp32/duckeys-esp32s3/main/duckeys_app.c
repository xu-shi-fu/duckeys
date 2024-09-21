

#include "duckeys_debug.h"
#include "duckeys_led.h"
#include "duckeys_ble.h"
#include "duckeys_fifo.h"
#include "duckeys_usb.h"

#include "duckeys_app.h"

#define ENABLE_MOD_USB No
#define ENABLE_MOD_BLE Yes
#define ENABLE_MOD_DEBUG No

Error duckeys_app_init(DuckeysApp *self)
{
    Error err;

    err = duckeys_fifo_init(&self->fifo, self);
    if (err != Nil)
    {
        return err;
    }

    err = duckeys_hub_init(&self->hub, self);
    if (err != Nil)
    {
        return err;
    }

    err = duckeys_led_init(&self->led, self);
    if (err != Nil)
    {
        return err;
    }

    if (ENABLE_MOD_BLE)
    {
        err = duckeys_ble_init(&self->ble, self);
        if (err != Nil)
        {
            return err;
        }
    }

    if (ENABLE_MOD_USB)
    {
        err = duckeys_usb_init(&self->usb, self);
        if (err != Nil)
        {
            return err;
        }
    }

    if (ENABLE_MOD_DEBUG)
    {
        err = duckeys_debug_init(&self->debug, self);
        if (err != Nil)
        {
            return err;
        }
    }

    return Nil;
}
