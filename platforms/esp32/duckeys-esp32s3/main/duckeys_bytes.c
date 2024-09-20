#include "common/bytes.h"

void ByteBufferReset(ByteBuffer *bb)
{
    bb->Len = 0;
    bb->Overflow = No;
}

// void ByteBufferCreate(ByteBuffer *bb, __uint16_t size)
// {
// }

// void ByteBufferDestroy(ByteBuffer *bb)
// {
// }

void ByteBufferWrite(ByteBuffer *bb, __uint8_t *src, __uint16_t size)
{
    for (__uint16_t i = 0; i < size; i++)
    {
        __uint8_t b = src[i];
        __uint16_t len = bb->Len;
        if (len < bb->Capacity)
        {
            bb->Data[len] = b;
            bb->Len = len + 1;
        }
        else
        {
            bb->Overflow = Yes;
            break;
        }
    }
}
