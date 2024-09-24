
#include "duckeys_libs.h"
#include "duckeys_led.h"
#include "duckeys_app.h"

Error duckeys_led_init(DuckeysLED *self, DuckeysApp *app)
{
    if (!self->Enabled)
    {
        ESP_LOGW(DUCKEYS_LOG_TAG, "module_led: disabled");
        return Nil;
    }

    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_led_init - begin");

    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_led_init - end");
    return Nil;
}
