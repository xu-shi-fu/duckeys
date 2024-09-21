
#include "duckeys_hub.h"
#include "duckeys_app.h"
#include "common/bytes.h"
#include "common/hex.h"

Error duckeys_fifo_init(DuckeysFIFO *self, DuckeysApp *app)
{
    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_fifo_init");

    return Nil;
}

int duckeys_hub_write_up_string(DuckeysHub *self, DK_STRING str)
{
    ByteBuffer9527 bb;
    ByteBuffer9527Init(&bb, self->upstream_buffer, sizeof(self->upstream_buffer));

    HexParse(str, &bb);

    if (bb.Overflow)
    {
        return 0;
    }
    return duckeys_hub_handle_up_bytes(self, bb.Data, bb.Len);
}

int duckeys_hub_write_down_bytes(DuckeysHub *self, const uint8_t *data, DK_LENGTH len)
{
    ByteBuffer9527 bb;
    ByteBuffer9527Init(&bb, self->downstream_buffer, sizeof(self->downstream_buffer));

    HexStringify(data, len, &bb);
    ByteBuffer9527WriteByte(&bb, 0);

    if (bb.Overflow)
    {
        return 0;
    }
    return duckeys_hub_handle_down_string(self, bb.Data);
}
