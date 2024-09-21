
#ifndef __COMMON_HEX_H__
#define __COMMON_HEX_H__

#include "bytes.h"

ByteBuffer9527 *HexParse(const char *str, ByteBuffer9527 *dst);

ByteBuffer9527 *HexStringify(const uint8_t *data, DK_LENGTH len, ByteBuffer9527 *dst);

#endif
