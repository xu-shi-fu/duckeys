

#include "duckeys_debug.h"
#include "duckeys_led.h"
#include "duckeys_ble.h"
#include "duckeys_fifo.h"
#include "duckeys_usb.h"

#include "duckeys_app.h"

Error duckeys_app_init(DuckeysApp *self)
{
    Error err;

    err = duckeys_debug_init(&self->debug);
    if (err != Nil)
    {
        return err;
    }

    err = duckeys_fifo_init(&self->fifo);
    if (err != Nil)
    {
        return err;
    }

    err = duckeys_led_init(&self->led);
    if (err != Nil)
    {
        return err;
    }

    err = duckeys_ble_init(&self->ble);
    if (err != Nil)
    {
        return err;
    }

    err = duckeys_usb_init(&self->usb);
    if (err != Nil)
    {
        return err;
    }

    return Nil;
}
