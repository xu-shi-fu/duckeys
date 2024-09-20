
#ifndef __DUCKEYS_FIFO_H__
#define __DUCKEYS_FIFO_H__

#include "common/types.h"
#include "common/errors.h"

typedef struct type_DuckeysFIFO
{

    // struct DuckeysApp *app;

} DuckeysFIFO;

// 初始化指定的 FIFO 模块
Error duckeys_fifo_init(DuckeysFIFO *self);

#endif
