#include "common/bytes.h"

void ByteBuffer9527Reset(ByteBuffer9527 *bb)
{
    bb->Len = 0;
    bb->Overflow = No;
}

// void ByteBuffer9527Create(ByteBuffer9527 *bb, DK_LENGTH size)
// {
// }

// void ByteBuffer9527Destroy(ByteBuffer9527 *bb)
// {
// }

void ByteBuffer9527Write(ByteBuffer9527 *bb, const __uint8_t *src, DK_LENGTH size)
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
