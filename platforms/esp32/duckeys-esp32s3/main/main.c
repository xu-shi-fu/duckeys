
#include "duckeys_libs.h"
#include "duckeys_app.h"

typedef struct
{

    DuckeysApp app;

} DuckeysAppImpl;

static DuckeysAppImpl the_app_inst;

void app_main(void)
{
    ESP_LOGI(DUCKEYS_LOG_TAG, "app_main, begin");

    DuckeysAppImpl *impl = &the_app_inst;
    DuckeysApp *app = &impl->app;
    memset(app, 0, sizeof(DuckeysApp));

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
