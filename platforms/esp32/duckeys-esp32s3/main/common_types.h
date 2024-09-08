
#ifndef __COMMON_TYPES_H__
#define __COMMON_TYPES_H__

#include <esp_log.h>

typedef struct type_Error
{
    const char *Message;
} type_Error_1;

typedef type_Error_1 *Error;

#define Nil 0

#define DUCKEYS_LOG_TAG "duckeys"

#endif
