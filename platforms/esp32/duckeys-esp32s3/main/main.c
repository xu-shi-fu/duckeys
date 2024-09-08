#include <stdio.h>
#include <memory.h>

#include "duckeys_ble.h"
#include "duckeys_led.h"
#include "duckeys_usb.h"
#include "duckeys_debug.h"
#include "duckeys_fifo.h"

typedef struct type_DuckeysApp
{

    DuckeysBLE ble;
    DuckeysDebug debug;
    DuckeysLED led;
    DuckeysFIFO fifo;
    DuckeysUSB usb;

} DuckeysApp;

static DuckeysApp theApp;

void app_main(void)
{
    DuckeysApp *app = &theApp;
    memset(app, 0, sizeof(theApp));

    Error err;
    err = duckeys_usb_init(&app->usb);
    err = duckeys_ble_init(&app->ble);
    err = duckeys_fifo_init(&app->fifo);

    if (err != Nil)
    {
        ESP_LOGE(DUCKEYS_LOG_TAG, "app_main, end");
    }
}
