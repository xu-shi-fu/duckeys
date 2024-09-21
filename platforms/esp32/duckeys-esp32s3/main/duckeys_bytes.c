#include "common/bytes.h"

void ByteBufferReset(ByteBuffer *bb)
{
    bb->Len = 0;
    bb->Overflow = No;
}

void ByteBufferInit(ByteBuffer *bb, DK_BYTE *buffer, DK_LENGTH size)
{
    bb->Capacity = size;
    bb->Overflow = No;
    bb->Data = buffer;
    bb->Len = 0;
}

// void ByteBufferCreate(ByteBuffer *bb, DK_LENGTH size)
// {
// }

// void ByteBufferDestroy(ByteBuffer *bb)
// {
// }

void ByteBufferWrite(ByteBuffer *bb, const DK_BYTE *src, DK_LENGTH size)
{
    for (DK_LENGTH i = 0; i < size; i++)
    {
        DK_BYTE b = src[i];
        DK_LENGTH len = bb->Len;
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

void ByteBufferWriteByte(ByteBuffer *bb, DK_BYTE b)
{
    ByteBufferWrite(bb, &b, sizeof(b));
}
