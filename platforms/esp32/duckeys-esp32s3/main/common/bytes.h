#ifndef __COMMON_BYTES_H__
#define __COMMON_BYTES_H__

#include "types.h"

typedef struct ByteBuffer_t
{

    DK_BOOL Overflow;
    DK_LENGTH Len;
    DK_LENGTH Capacity;
    DK_BYTE *Data;

} ByteBuffer;

////////////////////////////////////////////////////////////////////////////////

// 使用传入的缓冲区，初始化指定的 BB
void ByteBufferInit(ByteBuffer *bb, DK_BYTE *buffer, DK_LENGTH size);

// 使用动态分配的缓冲区，初始化指定的 BB，必须与 ByteBufferDestroy 配合使用
void ByteBufferCreate(ByteBuffer *bb, DK_LENGTH size);
void ByteBufferDestroy(ByteBuffer *bb);

void ByteBufferReset(ByteBuffer *bb);
void ByteBufferWrite(ByteBuffer *bb, const DK_BYTE *src, DK_LENGTH size);
void ByteBufferWriteByte(ByteBuffer *bb, DK_BYTE b);

#endif
