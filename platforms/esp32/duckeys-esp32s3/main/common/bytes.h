
#ifndef __COMMON_BYTES_H__
#define __COMMON_BYTES_H__

#include <stdio.h>
#include "types.h"

typedef struct
{

    __uint16_t Len;
    __uint16_t Capacity;
    __uint8_t *Data;
    BOOL   Overflow;

} ByteBuffer;

void ByteBufferReset(ByteBuffer *bb);
void ByteBufferCreate(ByteBuffer *bb, __uint16_t size);
void ByteBufferDestroy(ByteBuffer *bb);
void ByteBufferWrite(ByteBuffer *bb, __uint8_t *src, __uint16_t size);

#endif
