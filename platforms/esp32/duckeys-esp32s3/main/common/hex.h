
#ifndef __COMMON_HEX_H__
#define __COMMON_HEX_H__

#include "bytes.h"

ByteBuffer *HexParse(DK_STRING str, ByteBuffer *dst);

ByteBuffer *HexStringify(const DK_BYTE *data, DK_LENGTH len, ByteBuffer *dst);

#endif
