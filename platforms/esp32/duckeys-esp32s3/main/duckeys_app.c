

#include "duckeys_debug.h"
#include "duckeys_led.h"
#include "duckeys_ble.h"
#include "duckeys_fifo.h"
#include "duckeys_usb.h"

#include "duckeys_app.h"

#define ENABLE_MOD_BLE Yes
#define ENABLE_MOD_DEBUG No
#define ENABLE_MOD_HUB Yes
#define ENABLE_MOD_LED Yes
#define ENABLE_MOD_USB Yes

Error duckeys_app_init(DuckeysApp *self)
{
    Error err;

    self->ble.Enabled = ENABLE_MOD_BLE;
    self->debug.Enabled = ENABLE_MOD_DEBUG;
    self->hub.Enabled = ENABLE_MOD_HUB;
    self->led.Enabled = ENABLE_MOD_LED;
    self->usb.Enabled = ENABLE_MOD_USB;

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

    err = duckeys_ble_init(&self->ble, self);
    if (err != Nil)
    {
        return err;
    }

    err = duckeys_usb_init(&self->usb, self);
    if (err != Nil)
    {
        return err;
    }

    err = duckeys_debug_init(&self->debug, self);
    if (err != Nil)
    {
        return err;
    }

    return Nil;
}
