
#include "duckeys_libs.h"
#include "duckeys_app.h"

typedef struct
{

    DuckeysApp app;

} DuckeysAppImpl;

static DuckeysAppImpl theApp;

void app_main(void)
{
    ESP_LOGI(DUCKEYS_LOG_TAG, "app_main, begin");

    memset(&theApp, 0, sizeof(theApp));
    DuckeysAppImpl *impl = &theApp;
    DuckeysApp *app = &impl->app;

    app->ble.hub = &app->hub;
    app->usb.hub = &app->hub;

    Error err;
    err = duckeys_app_init(app);
    if (err != Nil)
    {
        ESP_LOGE(DUCKEYS_LOG_TAG, "app_main, error: %s", err->Message);
        return;
    }

    ESP_LOGI(DUCKEYS_LOG_TAG, "app_main, end");
}
