
#ifndef __DUCKEYS_BLE_H__
#define __DUCKEYS_BLE_H__

#include "common/types.h"
#include "common/errors.h"
#include "duckeys_hub.h"

typedef struct DuckeysBLE_t
{
    DK_BOOL Enabled;

    DuckeysHub *hub;

    DK_BOOL is_connected;

    // uint8_t adv_config_done;

} DuckeysBLE;

////////////////////////////////////////////////////////////////////////////////

#endif
