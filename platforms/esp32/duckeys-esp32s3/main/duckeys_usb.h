
#ifndef __DUCKEYS_USB_H__
#define __DUCKEYS_USB_H__

#include "common/types.h"
#include "common/errors.h"
#include "duckeys_hub.h"

typedef struct DuckeysUSB_t
{
    DuckeysHub *hub;

    int debug_note;
    long debug_count;

} DuckeysUSB;

void duckeys_usb_debug_ontimer();

#endif
