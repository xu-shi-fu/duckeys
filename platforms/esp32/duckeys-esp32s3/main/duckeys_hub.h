
#ifndef __DUCKEYS_HUB_H__
#define __DUCKEYS_HUB_H__

#include "common/types.h"
#include "common/errors.h"

typedef struct DuckeysHub_t
{

    DK_BYTE upstream_buffer[256];
    DK_BYTE downstream_buffer[256];

} DuckeysHub;

int duckeys_hub_handle_down_string(DuckeysHub *self, DK_STRING str);

int duckeys_hub_handle_up_bytes(DuckeysHub *self, const DK_BYTE *data, DK_LENGTH len);

int duckeys_hub_write_up_string(DuckeysHub *self, DK_STRING str);

int duckeys_hub_write_down_bytes(DuckeysHub *self, const DK_BYTE *data, DK_LENGTH len);

#endif
