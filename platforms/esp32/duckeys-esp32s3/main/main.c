#include <stdio.h>
#include <memory.h>

#include "duckeys_app.h"

typedef struct
{

    DuckeysApp app;

    // DuckeysBLE ble;
    // DuckeysDebug debug;
    // DuckeysLED led;
    // DuckeysFIFO fifo;
    // DuckeysUSB usb;

} DuckeysAppImpl;

static DuckeysAppImpl theApp;

void app_main(void)
{
    ESP_LOGI(DUCKEYS_LOG_TAG, "app_main, begin");

    memset(&theApp, 0, sizeof(theApp));
    DuckeysAppImpl *impl = &theApp;
    DuckeysApp *app = &impl->app;

    Error err;
    err = duckeys_app_init(app);
    if (err != Nil)
    {
        ESP_LOGE(DUCKEYS_LOG_TAG, "app_main, error: %s", err->Message);
        return;
    }

    ESP_LOGI(DUCKEYS_LOG_TAG, "app_main, end");
}
