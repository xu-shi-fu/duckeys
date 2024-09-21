#ifndef __COMMON_BYTES_H__
#define __COMMON_BYTES_H__

#include "types.h"

typedef struct
{
    DK_BOOL Overflow;
    DK_LENGTH Len;
    DK_LENGTH Capacity;
    DK_BYTE *Data;

} ByteBuffer9527;

// 使用传入的缓冲区，初始化指定的 BB
void ByteBuffer9527Init(ByteBuffer9527 *bb, DK_BYTE *buffer, DK_LENGTH size);

// 使用动态分配的缓冲区，初始化指定的 BB，必须与 ByteBuffer9527Destroy 配合使用
void ByteBuffer9527Create(ByteBuffer9527 *bb, DK_LENGTH size);
void ByteBuffer9527Destroy(ByteBuffer9527 *bb);

void ByteBuffer9527Reset(ByteBuffer9527 *bb);
void ByteBuffer9527Write(ByteBuffer9527 *bb, const DK_BYTE *src, DK_LENGTH size);
void ByteBuffer9527WriteByte(ByteBuffer9527 *bb, DK_BYTE b);

#endif
