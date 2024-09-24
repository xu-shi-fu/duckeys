
#include "duckeys_libs.h"
#include "duckeys_hub.h"
#include "duckeys_app.h"
#include "common/bytes.h"
#include "common/hex.h"

Error duckeys_hub_init(DuckeysHub *self, DuckeysApp *app)
{
    if (!self->Enabled)
    {
        ESP_LOGW(DUCKEYS_LOG_TAG, "module_hub: disabled");
        return Nil;
    }

    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_hub_init - begin");
    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_hub_init - end");
    return Nil;
}

int duckeys_hub_write_up_string(DuckeysHub *self, DK_STRING str)
{
    ByteBuffer bb;
    ByteBufferInit(&bb, self->upstream_buffer, sizeof(self->upstream_buffer));
    HexParse(str, &bb);
    if (bb.Overflow)
    {
        return 0;
    }
    return duckeys_hub_handle_up_bytes(self, bb.Data, bb.Len);
}

int duckeys_hub_write_down_bytes(DuckeysHub *self, const DK_BYTE *data, DK_LENGTH len)
{
    ByteBuffer bb;
    ByteBufferInit(&bb, self->downstream_buffer, sizeof(self->downstream_buffer));

    HexStringify(data, len, &bb);
    ByteBufferWriteByte(&bb, 0);

    if (bb.Overflow)
    {
        return 0;
    }
    return duckeys_hub_handle_down_string(self, (char *)bb.Data);
}
