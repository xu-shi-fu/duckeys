
#include "duckeys_libs.h"
#include "duckeys_debug.h"
#include "duckeys_app.h"

void duckeys_debug_timer_task(void *arg)
{
    for (;;)
    {
        vTaskDelay(1000 / portTICK_PERIOD_MS);
        duckeys_usb_debug_ontimer();
    }
}

Error duckeys_debug_init(DuckeysDebug *self, DuckeysApp *app)
{
    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_debug_init");
    xTaskCreate(duckeys_debug_timer_task, "duckeys_debug_timer_task", 2048, NULL, 10, NULL);
    return Nil;
}
